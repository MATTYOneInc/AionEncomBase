/*

 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.QuestsData;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.DropNpc;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFaction;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.legacy.LootRuleType;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.quest.*;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOT_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.models.WorkOrdersData;
import com.aionemu.gameserver.questEngine.handlers.models.XMLQuest;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.craft.CraftSkillUpdateService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.reward.BonusService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;

public final class QuestService
{
	static QuestsData questsData = DataManager.QUEST_DATA;
	private static final Logger log = LoggerFactory.getLogger(QuestService.class);
	private static Multimap<Integer, QuestDrop> questDrop = ArrayListMultimap.create();
	
	public static boolean finishQuest(QuestEnv env) {
		return finishQuest(env, 0);
	}
	
	public static boolean finishQuest(QuestEnv env, int reward) {
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(id);
		Rewards rewards = new Rewards();
		Rewards extendedRewards = new Rewards();
		if (qs == null || qs.getStatus() != QuestStatus.REWARD) {
			return false;
		}
		QuestTemplate template = questsData.getQuestById(id);
		if (template.getCategory() == QuestCategory.MISSION && qs.getCompleteCount() != 0) {
			return false;
		}
		List<QuestItems> questItems = new ArrayList<QuestItems>();
		if (!template.getExtendedRewards().isEmpty()) {
			if (qs.getCompleteCount() == template.getMaxRepeatCount() - 1) {
				questItems.addAll(getRewardItems(env, template, true, reward));
				extendedRewards = template.getExtendedRewards().get(0);
			}
		} if (!template.getRewards().isEmpty() || !template.getBonus().isEmpty()) {
			questItems.addAll(getRewardItems(env, template, false, reward));
			rewards = template.getRewards().get(reward);
		} if (ItemService.addQuestItems(player, questItems)) {
			giveReward(env, rewards);
			giveReward(env, extendedRewards);
			if (template.getCategory() == QuestCategory.CHALLENGE_TASK) {
				ChallengeTaskService.getInstance().onChallengeQuestFinish(player, id);
			}
			return setFinishingState(env, template, reward);
		} if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_FULL_INVENTORY);
			return false;
		}
		return false;
	}
	
	private static List<QuestItems> getRewardItems(QuestEnv env, QuestTemplate template, boolean extended, int reward) {
		Player player = env.getPlayer();
		int id = env.getQuestId();
		List<QuestItems> questItems = new ArrayList<QuestItems>();
		Rewards rewards;
		if (extended) {
			rewards = template.getExtendedRewards().get(0);
		} else {
			rewards = template.getRewards().get(reward);
		}
		questItems.addAll(rewards.getRewardItem());
		int dialogId = env.getDialogId();
		if (dialogId != 23 && dialogId != 0 && !extended) {
			QuestState qs = player.getQuestStateList().getQuestState(id);
			boolean isLastRepeat = qs.getCompleteCount() == template.getMaxRepeatCount() - 1 && template.getMaxRepeatCount() < 255;
			if (isLastRepeat && template.isUseSingleClassReward() || template.isUseRepeatedClassReward()) {
				QuestItems classRewardItem = null;
				PlayerClass playerClass = player.getCommonData().getPlayerClass();
				int selRewIndex = dialogId - 8;
				switch (playerClass) {
					case ASSASSIN: {
						classRewardItem = getQuestItemsbyClass(id, template.getAssassinSelectableReward(), selRewIndex);
						break;
					} case CHANTER: {
						classRewardItem = getQuestItemsbyClass(id, template.getChanterSelectableReward(), selRewIndex);
						break;
					} case CLERIC: {
						classRewardItem = getQuestItemsbyClass(id, template.getPriestSelectableReward(), selRewIndex);
						break;
					} case GLADIATOR: {
						classRewardItem = getQuestItemsbyClass(id, template.getFighterSelectableReward(), selRewIndex);
						break;
					} case RANGER: {
						classRewardItem = getQuestItemsbyClass(id, template.getRangerSelectableReward(), selRewIndex);
						break;
					} case SORCERER: {
						classRewardItem = getQuestItemsbyClass(id, template.getWizardSelectableReward(), selRewIndex);
						break;
					} case SPIRIT_MASTER: {
						classRewardItem = getQuestItemsbyClass(id, template.getElementalistSelectableReward(), selRewIndex);
						break;
					} case TEMPLAR: {
						classRewardItem = getQuestItemsbyClass(id, template.getKnightSelectableReward(), selRewIndex);
						break;
					} case GUNSLINGER: {
						classRewardItem = getQuestItemsbyClass(id, template.getGunslingerSelectableReward(), selRewIndex);
						break;
					} case SONGWEAVER: {
						classRewardItem = getQuestItemsbyClass(id, template.getSongweaverSelectableReward(), selRewIndex);
						break;
					} case AETHERTECH: {
						classRewardItem = getQuestItemsbyClass(id, template.getAethertechSelectableReward(), selRewIndex);
						break;
					}
					default:
						break;
				} if (classRewardItem != null) {
					questItems.add(classRewardItem);
				}
			} else {
				QuestItems selectebleRewardItem = null;
				if (dialogId - 8 >= 0 && dialogId - 8 < rewards.getSelectableRewardItem().size()) {
					selectebleRewardItem = rewards.getSelectableRewardItem().get(dialogId - 8);
				} if (selectebleRewardItem != null) {
					questItems.add(selectebleRewardItem);
				}
			}
		} else if (dialogId == 23 && dialogId != 0 && !extended) {
			QuestState qs = player.getQuestStateList().getQuestState(id);
			boolean isLastRepeat = qs.getCompleteCount() == template.getMaxRepeatCount() - 1 && template.getMaxRepeatCount() < 255;
			if (isLastRepeat && template.isUseSingleClassReward() || template.isUseRepeatedClassReward()) {
				QuestItems classRewardItem = null;
				PlayerClass playerClass = player.getCommonData().getPlayerClass();
				int selRewIndex = env.getExtendedRewardIndex() - 8;
				switch (playerClass) {
					case ASSASSIN: {
						classRewardItem = getQuestItemsbyClass(id, template.getAssassinSelectableReward(), selRewIndex);
						break;
					} case CHANTER: {
						classRewardItem = getQuestItemsbyClass(id, template.getChanterSelectableReward(), selRewIndex);
						break;
					} case CLERIC: {
						classRewardItem = getQuestItemsbyClass(id, template.getPriestSelectableReward(), selRewIndex);
						break;
					} case GLADIATOR: {
						classRewardItem = getQuestItemsbyClass(id, template.getFighterSelectableReward(), selRewIndex);
						break;
					} case RANGER: {
						classRewardItem = getQuestItemsbyClass(id, template.getRangerSelectableReward(), selRewIndex);
						break;
					} case SORCERER: {
						classRewardItem = getQuestItemsbyClass(id, template.getWizardSelectableReward(), selRewIndex);
						break;
					} case SPIRIT_MASTER: {
						classRewardItem = getQuestItemsbyClass(id, template.getElementalistSelectableReward(), selRewIndex);
						break;
					} case TEMPLAR: {
						classRewardItem = getQuestItemsbyClass(id, template.getKnightSelectableReward(), selRewIndex);
						break;
					} case AETHERTECH: {
						classRewardItem = getQuestItemsbyClass(id, template.getAethertechSelectableReward(), selRewIndex);
						break;
					} case GUNSLINGER: {
						classRewardItem = getQuestItemsbyClass(id, template.getGunslingerSelectableReward(), selRewIndex);
						break;
					} case SONGWEAVER: {
						classRewardItem = getQuestItemsbyClass(id, template.getSongweaverSelectableReward(), selRewIndex);
						break;
					}
					default:
						break;
				} if (classRewardItem != null) {
					questItems.add(classRewardItem);
				}
			}
		} else if (dialogId == 23 && extended && !rewards.getSelectableRewardItem().isEmpty()) {
			QuestItems selectebleRewardItem = null;
			int index = env.getExtendedRewardIndex();
			if (index - 8 >= 0 && index - 8 < rewards.getSelectableRewardItem().size()) {
				selectebleRewardItem = rewards.getSelectableRewardItem().get(index - 8);
			} else if ((index - 1) >= 0 && (index - 1) < rewards.getSelectableRewardItem().size()) {
				selectebleRewardItem = rewards.getSelectableRewardItem().get(index - 1);
			} if (selectebleRewardItem != null) {
				questItems.add(selectebleRewardItem);
			}
		} if (!template.getBonus().isEmpty()) {
			QuestBonuses bonus = template.getBonus().get(0);
			// Handler can add additional bonuses on repeat (for event quests no data)
			HandlerResult result = QuestEngine.getInstance().onBonusApplyEvent(env, bonus.getType(), questItems);
			if (result != HandlerResult.FAILED) {
				QuestItems additional = BonusService.getInstance().getQuestBonus(player, template);
				if (additional != null) {
                    questItems.add(additional);
                }
			}
		}
		return questItems;
	}
	
	private static void giveReward(QuestEnv env, Rewards rewards) {
		Player player = env.getPlayer();
		if (rewards.getGold() != null) {
			player.getInventory().increaseKinah((long) (player.getRates().getQuestKinahRate() * rewards.getGold()), ItemUpdateType.INC_KINAH_QUEST);
		} if (rewards.getExp() != null) {
			NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(env.getTargetId());
			player.getCommonData().addExp(rewards.getExp(), RewardType.QUEST);
		}
		//Aura Of Growth
		if (rewards.getExpBoost() != null) {
			player.getCommonData().addAuraOfGrowth(1060000 * rewards.getExpBoost());
		}
		//CP Reward 5.3
		if (rewards.getCP() != null) {
			//To Do...
		}
		//Abyss Landing 4.9.1
		if (rewards.getAbyssOp() != null) {
			AbyssLandingService.getInstance().AnnounceToPoints(player, null, null, rewards.getAbyssOp(), LandingPointsEnum.QUEST);
			if (player.getRace() == Race.ASMODIANS) {
				AbyssLandingService.getInstance().updateHarbingerLanding(rewards.getAbyssOp(), LandingPointsEnum.QUEST, true);
			} if (player.getRace() == Race.ELYOS) {
				AbyssLandingService.getInstance().updateRedemptionLanding(rewards.getAbyssOp(), LandingPointsEnum.QUEST, true);
			}
		}
		//Now player can win "Dp" if finish quest.
		if (rewards.getDp() != null) {
			player.getCommonData().addDp(rewards.getDp());
		} if (rewards.getTitle() != null) {
			player.getTitleList().addTitle(rewards.getTitle(), true, 0);
		} if (rewards.getAp() != null) { //Abyss Points
			AbyssPointsService.addAp(player, (int) (player.getRates().getQuestApRate() * rewards.getAp()));
		} if (rewards.getGp() != null) { //Glory Points
			AbyssPointsService.addGp(player, (int) (player.getRates().getQuestGpRate() * rewards.getGp()));
		} if (rewards.getExtendInventory() != null) {
			if (rewards.getExtendInventory() == 1) {
				CubeExpandService.expand(player, false);
			} else if (rewards.getExtendInventory() == 2) {
				WarehouseService.expand(player);
			}
		}
		//Send for: "Aura Of Growth & Berdin's Favor & Abyss Favor"
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
	}
	
	private static boolean setFinishingState(QuestEnv env, QuestTemplate template, int reward) {
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(id);
		QuestWorkItems qwi = questsData.getQuestById(id).getQuestWorkItems();
		if (qwi != null) {
			long count = 0;
			for (QuestItems qi : qwi.getQuestWorkItem()) {
				if (qi != null) {
					count = player.getInventory().getItemCountByItemId(qi.getItemId());
					if (count > 0) {
						if (!player.getInventory().decreaseByItemId(qi.getItemId(), count)) {
							return false;
						}
					}
				}
			}
		}
		qs.setStatus(QuestStatus.COMPLETE);
		qs.setQuestVar(0);
		qs.setReward(reward);
		qs.setCompleteCount(qs.getCompleteCount() + 1);
		if (template.getRepeatCycle() != null && player.getAccessLevel() == 0 || template.getQuestCoolTime() > 0) {
			qs.setNextRepeatTime(countNextRepeatTime(player, template));
		} else if (template.isTimeBased() && player.getAccessLevel() > 0) {
			PacketSendUtility.sendMessage(player, "You're GM! So system won't apply countNextRepeatTime()");
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		player.getController().updateZone();
		player.getController().updateNearbyQuests();
		QuestEngine.getInstance().onLvlUp(env);
		if (template.getNpcFactionId() != 0) {
			player.getNpcFactions().completeQuest(template);
		}
		return true;
	}
	
	private static QuestItems getQuestItemsbyClass(int id, List<QuestItems> classSelRew, int selRewIndex) {
		if (selRewIndex >= 0 && selRewIndex < classSelRew.size()) {
			return classSelRew.get(selRewIndex);
		}
		return null;
	}
	
	private static Timestamp countNextRepeatTime(Player player, QuestTemplate template) {
		int questCooltime = template.getQuestCoolTime();
		DateTime now = DateTime.now();
		DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 9, 0, 0);
		if (template.isDaily()) {
			if (now.isAfter(repeatDate)) {
				repeatDate = repeatDate.plusHours(24);
			}
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400855, "9"));
		} else if (template.getQuestCoolTime() > 0) {
			repeatDate = repeatDate.plusSeconds(template.getQuestCoolTime());
			//This quest can be re-attempted in %DURATIONDAY0s.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402676, + questCooltime));
		} else {
			int daysToAdd = 7;
			int startDay = 7;
			for (QuestRepeatCycle weekDay : template.getRepeatCycle()) {
				int diff = weekDay.getDay() - repeatDate.getDayOfWeek();
				if (diff > 0 && diff < daysToAdd) {
					daysToAdd = diff;
				} if (startDay > weekDay.getDay()) {
					startDay = weekDay.getDay();
				}
			} if (startDay == daysToAdd) {
				daysToAdd = 7;
			} else if (daysToAdd == 7 && startDay < 7) {
				daysToAdd = 7 - repeatDate.getDayOfWeek() + startDay;
			}
			repeatDate = repeatDate.plusDays(daysToAdd);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400857, new DescriptionId(1800667), "9"));
		}
		return new Timestamp(repeatDate.getMillis());
	}
	
	public static boolean checkStartConditions(QuestEnv env, boolean warn) {
		return checkStartConditionsImpl(env, warn);
	}
	
	private static boolean checkStartConditionsImpl(QuestEnv env, boolean warn) {
		Player player = env.getPlayer();
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		if (template == null) {
            return false;
        } if (template.getRacePermitted() != null) {
            if (template.getRacePermitted() != player.getRace() && template.getRacePermitted() != Race.PC_ALL) {
                return false;
            }
        }
		int levelDiff = template.getMinlevelPermitted() - player.getLevel();
		if (levelDiff > 2 && template.getMinlevelPermitted() != 999) {
			return false;
		} if (warn && levelDiff > 0 && (template.getMinlevelPermitted() != 999)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_MIN_LEVEL(Integer.toString(template.getMinlevelPermitted())));
			return false;
		} if (template.getMaxlevelPermitted() != 0 && player.getLevel() > template.getMaxlevelPermitted()) {
			if (warn) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_MAX_LEVEL(Integer.toString(template.getMaxlevelPermitted())));
			}
			return false;
		} if (!template.getClassPermitted().isEmpty() && !template.getClassPermitted().contains(player.getCommonData().getPlayerClass())) {
			if (warn) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_CLASS);
			}
			return false;
		} if (template.getGenderPermitted() != null && template.getGenderPermitted() != player.getGender()) {
			if (warn) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_GENDER);
			}
			return false;
		} if (template.getRequiredRank() != 0) {
            if (player.getAbyssRank().getRank().getId() < template.getRequiredRank()) {
                if (warn) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_MIN_RANK(AbyssRankEnum.getRankById(template.getRequiredRank()).getDescriptionId()));
                }
                return false;
            }
        } if (template.getTitleId() != 0) {
			if (!player.getTitleList().contains(template.getTitleId())) {
                if (warn) {
					//You can only receive this quest when you have the %0 title.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300588, template.getTitleId()));
				}
				return false;
            }
        } if (((template.isMaster()) && (!CraftSkillUpdateService.canLearnMoreMasterCraftingSkill(player))) || ((template.isExpert()) && (!CraftSkillUpdateService.canLearnMoreExpertCraftingSkill(player)))) {
            return false;
        }
		int fulfilledStartConditions = 0;
		if (!template.getXMLStartConditions().isEmpty()) {
			for (XMLStartCondition startCondition : template.getXMLStartConditions()) {
				if (startCondition.check(player, warn)) {
					fulfilledStartConditions++;
				}
			} if (fulfilledStartConditions < 1) {
				return false;
			}
		} if (warn && !inventoryItemCheck(env, warn)) {
			return false;
		} if (template.getCombineSkill() != null) {
			List<Integer> skills = new ArrayList<Integer>();
			if (template.getCombineSkill() == -1) {
				skills.add(30002);
				skills.add(30003);
				skills.add(40001);
				skills.add(40002);
				skills.add(40003);
				skills.add(40004);
				skills.add(40007);
				skills.add(40008);
				skills.add(40010);
			} else {
				skills.add(template.getCombineSkill());
			}
			boolean result = false;
			for (int skillId : skills) {
				PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
				if (skill != null && skill.getSkillLevel() >= template.getCombineSkillPoint()) {
				   if (template.getCategory().equals(QuestCategory.TASK) && skill.getSkillLevel() - 40 > template.getCombineSkillPoint())
					  continue;
					result = true;
					break;
				}
			} if (!result) {
				return false;
			}
		} if (warn && template.getNpcFactionId() != 0 && !template.isTimeBased()) {
            if (!player.getNpcFactions().canStartQuest(template)) {
                return false;
            }
        }
		QuestState qs = player.getQuestStateList().getQuestState(template.getId());
		if (qs != null && qs.getStatus() != QuestStatus.NONE) {
			if (!qs.canRepeat()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean startQuest(QuestEnv env, QuestStatus status) {
		return startQuest(env, status, env.getDialogId() != 0);
	}
	
	public static boolean startQuest(QuestEnv env, QuestStatus status, boolean warn) {
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestStateList qsl = player.getQuestStateList();
		QuestState qs = qsl.getQuestState(id);
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		//TO DO LATER
		/*if (template.getTargetType() == QuestTargetType.FORCE || template.getTargetType() == QuestTargetType.UNION) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UNION_YOU_ARE_NOT_UNION_MEMBER);
			return true;
		}*/
		if (template.getNpcFactionId() != 0) {
			NpcFaction faction = player.getNpcFactions().getNpcFactionById(template.getNpcFactionId());
			if (!faction.isActive() || faction.getQuestId() != env.getQuestId()) {
				return false;
			}
		} if (!checkStartConditions(env, warn)) {
			return false;
		} if (player.getLevel() < template.getMinlevelPermitted() && template.getMinlevelPermitted() != 999) {
			return false;
		} if (!template.isNoCount() && !checkQuestListSize(qsl)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300622, template.getName()));
			return false;
		} if (qs != null) {
			if (!qs.canRepeat()) {
				return false;
			}
			qs.setStatus(status);
		} else {
			player.getQuestStateList().addQuest(id, new QuestState(id, status, 0, 0, null, 0, null));
		} if (template.getNpcFactionId() != 0 && !template.isTimeBased()) {
			if (!player.getNpcFactions().canStartQuest(template)) {
				return false;
			}
			player.getNpcFactions().startQuest(template);
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(id, status.value(), 0));
		player.getController().updateZone();
		player.getController().updateNearbyQuests();
		return true;
	}
	
	public static boolean startQuest(QuestEnv env) {
		return startQuest(env, QuestStatus.START, env.getDialogId() != 0);
	}
	
	public static void startMission(QuestEnv env, QuestStatus status) {
		Player player = env.getPlayer();
		int questId = env.getQuestId();
		if (player.getQuestStateList().getQuestState(questId) != null) {
			return;
		} else {
			player.getQuestStateList().addQuest(questId, new QuestState(questId, status, 0, 0, null, 0, null));
		}
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, status.value(), 0));
	}
	
	public static boolean checkMissionStatConditions(QuestEnv env) {
		Player player = env.getPlayer();
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		if (template == null) {
			return false;
		} if (template.getRacePermitted() != null && template.getRacePermitted() != player.getRace()) {
			return false;
		} if (template.getClassPermitted().size() != 0 && !template.getClassPermitted().contains(player.getCommonData().getPlayerClass())) {
			return false;
		} if (template.getGenderPermitted() != null && template.getGenderPermitted() != player.getGender()) {
			return false;
		} if (template.getCombineSkill() != null) {
			List<Integer> skills = new ArrayList<Integer>();
			if (template.getCombineSkill() == -1) {
				skills.add(30002);
				skills.add(30003);
				skills.add(40001);
				skills.add(40002);
				skills.add(40003);
				skills.add(40004);
				skills.add(40007);
				skills.add(40008);
				skills.add(40010);
			} else {
				skills.add(template.getCombineSkill());
			}
			boolean result = false;
			for (int skillId : skills) {
				PlayerSkillEntry skill = player.getSkillList().getSkillEntry(skillId);
				if (skill != null && skill.getSkillLevel() >= template.getCombineSkillPoint()
					&& skill.getSkillLevel() - 40 <= template.getCombineSkillPoint()) {
					result = true;
					break;
				}
			} if (!result) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean startEventQuest(QuestEnv env, QuestStatus questStatus) {
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		if (template.getCategory() != QuestCategory.EVENT) {
			return false;
		}
		int id = env.getQuestId();
		Player player = env.getPlayer();
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(id, questStatus, 0));
		if ((player.getLevel() < template.getMinlevelPermitted()) && (template.getMinlevelPermitted() != 999)) {
			return false;
		} if (template.getMaxlevelPermitted() != 0 && player.getLevel() > template.getMaxlevelPermitted()) {
			return false;
		} if (template.getRacePermitted() != null) {
            if (template.getRacePermitted() != player.getRace() && template.getRacePermitted() != Race.PC_ALL) {
                return false;
            }
        } if (!template.getClassPermitted().isEmpty()) {
            if (!template.getClassPermitted().contains(player.getCommonData().getPlayerClass())) {
                return false;
            }
        } if (template.getGenderPermitted() != null) {
            if (template.getGenderPermitted() != player.getGender()) {
                return false;
            }
        }
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if (qs == null) {
			qs = new QuestState(template.getId(), questStatus, 0, 0, null, 0, null);
			player.getQuestStateList().addQuest(id, qs);
		} else {
			if (template.getMaxRepeatCount() >= qs.getCompleteCount()) {
				qs.setStatus(questStatus);
				qs.setQuestVar(0);
			}
		}
		player.getController().updateZone();
		player.getController().updateNearbyQuests();
		return true;
	}
	
	private static boolean checkQuestListSize(QuestStateList qsl) {
		return (qsl.getNormalQuestListSize() + 1) <= CustomConfig.BASIC_QUEST_SIZE_LIMIT;
	}
	
	public boolean completeQuest(QuestEnv env) {
		Player player = env.getPlayer();
		int id = env.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
		qs.setStatus(QuestStatus.REWARD);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		player.getController().updateZone();
		player.getController().updateNearbyQuests();
		return true;
	}
	
	public static boolean collectItemCheck(QuestEnv env, boolean removeItem) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null && removeItem) {
			return false;
		}
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		CollectItems collectItems = template.getCollectItems();
		if (collectItems == null) {
			InventoryItems inventoryItems = template.getInventoryItems();
			if (inventoryItems == null) {
				return true;
			} for (InventoryItem inventoryItem : inventoryItems.getInventoryItem()) {
				int itemId = inventoryItem.getItemId();
				if (player.getInventory().getItemCountByItemId(itemId) < 1) {
					return false;
				}
			} if (removeItem) {
				for (InventoryItem inventoryItem : inventoryItems.getInventoryItem()) {
					player.getInventory().decreaseByItemId(inventoryItem.getItemId(), 1);
				}
			}
			return true;
		} for (CollectItem collectItem : collectItems.getCollectItem()) {
			int itemId = collectItem.getItemId();
			long count = itemId == ItemId.KINAH.value() ? player.getInventory().getKinah() : player.getInventory().getItemCountByItemId(itemId);
			if (collectItem.getCount() > count) {
				return false;
			}
		} if (removeItem) {
			for (CollectItem collectItem : collectItems.getCollectItem()) {
				if (collectItem.getItemId() == 182400001) {
					player.getInventory().decreaseKinah(collectItem.getCount());
				} else {
					player.getInventory().decreaseByItemId(collectItem.getItemId(), collectItem.getCount());
				}
			}
		}
		return true;
	}
	
	public static boolean inventoryItemCheck(QuestEnv env, boolean showWarning) {
		Player player = env.getPlayer();
		QuestTemplate template = questsData.getQuestById(env.getQuestId());
		InventoryItems inventoryItems = template.getInventoryItems();
		if (inventoryItems == null) {
			return true;
		}
		int requiredItemNameId = 0;
		for (InventoryItem inventoryItem : inventoryItems.getInventoryItem()) {
			Item item = player.getInventory().getFirstItemByItemId(inventoryItem.getItemId());
			if (item == null) {
				requiredItemNameId = DataManager.ITEM_DATA.getItemTemplate(inventoryItem.getItemId()).getNameId();
				break;
			}
		} if (requiredItemNameId != 0 && showWarning) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_QUEST_ACQUIRE_ERROR_INVENTORY_ITEM(new DescriptionId(requiredItemNameId)));
		}
		return requiredItemNameId == 0;
	}
	
	public static VisibleObject spawnQuestNpc(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading) {
		return SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(worldId, templateId, x, y, z, heading), instanceId);
	}
	
	public static VisibleObject addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading) {
		return SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(worldId, templateId, x, y, z, heading), instanceId);
	}
	
	public static void addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, int timeInMin) {
		final Npc npc = (Npc) spawnQuestNpc(worldId, instanceId, templateId, x, y, z, (byte) 0);
		if (!npc.getPosition().isInstanceMap()) {
            despawnQuestNpc(npc, timeInMin);
        }
	}
	
	private static void despawnQuestNpc(final Npc npc, int timeInMin) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (npc != null && !npc.getLifeStats().isAlreadyDead()) {
                    npc.getController().onDelete();
                }
            }
        }, 60000 * timeInMin);
    }
	
	public static int getQuestDrop(Set<DropItem> dropItems, int index, Npc npc, Collection<Player> players, Player player) {
		Collection<QuestDrop> drops = getQuestDrop(npc.getNpcId());
		if (drops.isEmpty()) {
			return index;
		}
		DropNpc dropNpc = DropRegistrationService.getInstance().getDropRegistrationMap().get(npc.getObjectId());
		for (QuestDrop drop : drops) {
			if (Rnd.get() * 100 > drop.getChance()) {
				continue;
			} if (players != null && player.isInGroup2()) {
				List<Player> pls = new ArrayList<Player>();
				if (drop.isDropEachMemberGroup()) {
					for (Player member : players) {
						if (isQuestDrop(member, drop)) {
							pls.add(member);
							dropItems.add(regQuestDropItem(drop, index++, member.getObjectId()));
						}
					}
				} else {
					for (Player member : players) {
						if (isQuestDrop(member, drop)) {
							pls.add(member);
							break;
						}
					}
				} 
				if (pls.size() > 0) {
					if (!drop.isDropEachMemberGroup()) {
						dropItems.add(regQuestDropItem(drop, index++, 0));
					}
					for (Player p : pls) {
						dropNpc.setPlayerObjectId(p.getObjectId());
						if (player.getPlayerGroup2().getLootGroupRules().getLootRule() != LootRuleType.FREEFORALL) {
							PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npc.getObjectId(), 0));
						}
					}
					pls.clear();
				}
			} else if (players != null && player.isInAlliance2()) {
				List<Player> pls = new ArrayList<Player>();
				if (drop.isDropEachMemberAlliance()) {
					for (Player member : players) {
						if (isQuestDrop(member, drop)) {
							pls.add(member);
							dropItems.add(regQuestDropItem(drop, index++, member.getObjectId()));
						}
					}
				} else {
					for (Player member : players) {
						if (isQuestDrop(member, drop)) {
							pls.add(member);
							break;
						}
					}
				} 
				if (pls.size() > 0) {
					if (!drop.isDropEachMemberAlliance()) {
						dropItems.add(regQuestDropItem(drop, index++, 0));
					}
					for (Player p : pls) {
						dropNpc.setPlayerObjectId(p.getObjectId());
						if (player.getPlayerAlliance2().getLootGroupRules().getLootRule() != LootRuleType.FREEFORALL) {
							PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npc.getObjectId(), 0));
						}
					}
					pls.clear();
				}
			} else {
				if (isQuestDrop(player, drop)) {
					dropItems.add(regQuestDropItem(drop, index++, player.getObjectId()));
				}
			}
		}
		return index;
	}
	
	private static DropItem regQuestDropItem(QuestDrop drop, int index, Integer winner) {
		DropItem item = new DropItem(new Drop(drop.getItemId(), 1, 1, drop.getChance(), false, false));
		item.setPlayerObjId(winner);
		item.setIndex(index);
		item.setCount(1);
		return item;
	}
	
	private static boolean isQuestDrop(Player player, QuestDrop drop) {
		int questId = drop.getQuestId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (drop.getCollectingStep() != 0) {
            if (drop.getCollectingStep() != qs.getQuestVarById(0)) {
                return false;
            }
        }
		QuestTemplate qt = DataManager.QUEST_DATA.getQuestById(questId);
		if (player.isInAlliance2()) {
			if (!qt.getTargetType().equals(QuestTargetType.UNION)) { //League.
				return false;
			}
		} if (qt.getMentorType() == QuestMentorType.MENTE) {
			if (!player.isInGroup2()) {
                return false;
            }
			PlayerGroup group = player.getPlayerGroup2();
			boolean found = false;
			for (Player member : group.getMembers()) {
				if (member.isMentor() && MathUtil.getDistance(player, member) < GroupConfig.GROUP_MAX_DISTANCE) {
					found = true;
					break;
				}
			} if (!found) {
                return false;
            }
		} if (drop instanceof HandlerSideDrop) {
            return ((HandlerSideDrop) drop).getNeededAmount() > player.getInventory().getItemCountByItemId(drop.getItemId());
        }
		CollectItems collectItems = questsData.getQuestById(questId).getCollectItems();
		if (collectItems == null) {
            return true;
        } for (CollectItem collectItem : collectItems.getCollectItem()) {
            int collectItemId = collectItem.getItemId();
            long count = player.getInventory().getItemCountByItemId(collectItemId);
            if (collectItem.getCount() > count) {
                return true;
            }
        }
		return false;
	}
	
	public static boolean checkLevelRequirement(int questId, int playerLevel) {
		return playerLevel >= questsData.getQuestById(questId).getMinlevelPermitted();
	}
	
	public static int getLevelRequirement(int questId, int playerLevel) {
        QuestTemplate template = questsData.getQuestById(questId);
        if (template == null) {
            return 999;
        } if (questsData.getQuestById(questId).getMinlevelPermitted() == 999) {
            return 0;
        }
        return questsData.getQuestById(questId).getMinlevelPermitted() - playerLevel;
    }
	
	/*
	public static boolean bountyReward(Player player, int questId) {
    	QuestTemplate template = questsData.getQuestById(questId);
        if (template == null) {
            return false;
        } if (!template.isBountyReward()) {
            return false;
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        } if (qs.getStatus() == QuestStatus.COMPLETE || qs.getStatus() == QuestStatus.LOCKED) {
            return false;
        }
        QuestEnv env = new QuestEnv(player, player, questId, QuestDialog.AUTO_REWARD.id());
        finishQuest(env);
		player.getController().updateZone();
        player.getController().updateNearbyQuests();
        return true;
    }*/
	
	public static boolean questTimerStart(QuestEnv env, int timeInSeconds) {
		final Player player = env.getPlayer();
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				QuestEngine.getInstance().onQuestTimerEnd(new QuestEnv(null, player, 0, 0));
			}
		}, timeInSeconds * 1000);
		player.getController().addTask(TaskId.QUEST_TIMER, task);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(env.getQuestId(), timeInSeconds));
		return true;
	}
	
	public static boolean invisibleTimerStart(QuestEnv env, int timeInSeconds) {
		final Player player = env.getPlayer();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				QuestEngine.getInstance().onInvisibleTimerEnd(new QuestEnv(null, player, 0, 0));
			}
		}, timeInSeconds * 1000);
		return true;
	}
	
	public static boolean questTimerEnd(QuestEnv env) {
		final Player player = env.getPlayer();
		player.getController().cancelTask(TaskId.QUEST_TIMER);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(env.getQuestId(), 0));
		return true;
	}
	
	public static boolean abandonQuest(Player player, int questId) {
		QuestTemplate template = questsData.getQuestById(questId);
		if (template == null) {
            return false;
        } if (template.isCannotGiveup()) {
            return false;
        }
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
            return false;
        } if (qs.getStatus() == QuestStatus.COMPLETE || qs.getStatus() == QuestStatus.LOCKED) {
			AuditLogger.info(player, "Cancel from completed quest. quest Id: " + questId);
			return false;
		} if (template.getNpcFactionId() != 0) {
            player.getNpcFactions().abortQuest(template);
        }
		qs.setStatus(QuestStatus.NONE);
		qs.setQuestVar(0);
		QuestWorkItems qwi = template.getQuestWorkItems();
		if (qwi != null) {
			long count = 0;
			for (QuestItems qi : qwi.getQuestWorkItem()) {
				if (qi != null) {
					count = player.getInventory().getItemCountByItemId(qi.getItemId());
					if (count > 0) {
                        player.getInventory().decreaseByItemId(qi.getItemId(), count);
                    }
				}
			}
		} if (template.getCategory() == QuestCategory.TASK) {
			WorkOrdersData wod = null;
			for (XMLQuest xmlQuest : DataManager.XML_QUESTS.getQuest()) {
				if (xmlQuest.getId() == questId) {
					if (xmlQuest instanceof WorkOrdersData) {
						wod = (WorkOrdersData) xmlQuest;
						break;
					}
				}
			} if (wod != null) {
				player.getRecipeList().deleteRecipe(player, wod.getRecipeId());
			}
		} if (player.getController().getTask(TaskId.QUEST_TIMER) != null) {
            questTimerEnd(new QuestEnv(null, player, questId, 0));
        }
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId));
		player.getController().updateZone();
		player.getController().updateNearbyQuests();
		return true;
	}
	
	public static Collection<QuestDrop> getQuestDrop(int npcId) {
		if (questDrop.containsKey(npcId)) {
			return questDrop.get(npcId);
		}
		return Collections.<QuestDrop> emptyList();
	}
	
	public static void addQuestDrop(int npcId, QuestDrop drop) {
		if (!questDrop.containsKey(npcId)) {
			questDrop.put(npcId, drop);
		} else {
			questDrop.get(npcId).add(drop);
		}
	}
	
	public static List<Player> getEachDropMembersGroup(PlayerGroup group, int npcId, int questId) {
		List<Player> players = new ArrayList<Player>();
		for (QuestDrop qd : getQuestDrop(npcId)) {
			if (qd.isDropEachMemberGroup()) {
				for (Player player : group.getMembers()) {
					QuestState qstel = player.getQuestStateList().getQuestState(questId);
					if (qstel != null && qstel.getStatus() == QuestStatus.START) {
						players.add(player);
					}
				}
				break;
			}
		}
		return players;
	}
	
	public static List<Player> getEachDropMembersAlliance(PlayerAlliance alliance, int npcId, int questId) {
		List<Player> players = new ArrayList<Player>();
		for (QuestDrop qd : getQuestDrop(npcId)) {
			if (qd.isDropEachMemberGroup()) {
				for (Player player : alliance.getMembers()) {
					QuestState qstel = player.getQuestStateList().getQuestState(questId);
					if (qstel != null && qstel.getStatus() == QuestStatus.START) {
						players.add(player);
					}
				}
				break;
			}
		}
		return players;
	}
}