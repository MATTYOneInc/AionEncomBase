/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.services.minion;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.aionemu.gameserver.dao.PlayerPetsDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Minion;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.SkillUseAction;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;
import com.aionemu.gameserver.model.templates.pet.PetDopingBag;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.TimeUtil;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.common.legacy.LootRuleType;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.model.templates.minion.MinionTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Reworked by G-Robson26 /
 ****/

public class MinionService {

	private static final Logger log = LoggerFactory.getLogger(MinionService.class);
	MinionStatAddServices minionStatBuff = new MinionStatAddServices();

	private MinionService() {
	}

	public static MinionService getInstance() {
		return SingletonHolder.instance;
	}

	public static void addMinion(Player player, int minionId, String name, int expireTime, boolean isCombine, boolean combineSuccess) {
		int questId;
		MinionCommonData minionCommonData = player.getMinionList().addMinion(player, minionId, name, expireTime);
		if (minionCommonData != null) {
			if (isCombine) {
				if (combineSuccess) {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(1, 2, minionCommonData));
				} else {
					PacketSendUtility.sendPacket(player, new SM_MINIONS(1, 3, minionCommonData));
				}
			} else {
				PacketSendUtility.sendPacket(player, new SM_MINIONS(1, minionCommonData));
				if (expireTime > 0) {
					ExpireTimerTask.getInstance().addTask(minionCommonData, player);
				}
			}
		}
		if (player.getRace() == Race.ASMODIANS) {
			questId = 25545;
		} else {
			questId = 15545;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getQuestVars().getQuestVars() == 0) {
			qs.setStatus(QuestStatus.REWARD);
			qs.setQuestVar(1);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
			if (qs.getStatus() == QuestStatus.COMPLETE || qs.getStatus() == QuestStatus.REWARD) {
				player.getController().updateNearbyQuests();
			}
		}
	}

	private static boolean validateAdoption(Player player, ItemTemplate template, int minionId) {
		if (template == null || template.getActions() == null || template.getActions().getAdoptMinionAction() == null) {
			log.info("Null Action minionId: " + minionId);
			return false;
		}

		if (DataManager.MINION_DATA.getMinionTemplate(minionId) == null) {
			log.warn("Trying adopt minion without template. PetId:" + minionId);
			return false;
		}
		return true;
	}

	public void onPlayerLogin(Player player) {
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS( 11, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12, playerMinions));
		}
	}

	public void renameMinion(Player player, int minionObjtId, String name) {
		if (name.length() > 9) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404336));
			return;
		}
		for (MinionCommonData list : player.getMinionList().getMinions()) {
			if (list.getRealName().equalsIgnoreCase(name)) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404335));
				return;
			}
		}

		MinionCommonData minionCommonData = player.getMinionList().getMinion(minionObjtId);
		if (minionCommonData != null) {
			String OldName = minionCommonData.getRealName();
			minionCommonData.setName(name);
			DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionsName(minionCommonData, OldName);
			PacketSendUtility.broadcastPacket(player, new SM_MINIONS(3, minionCommonData), true);
		}
	}

	public void chargeSkillPoint(Player player, boolean isAuto) {
		int playerSkillPoint = player.getCommonData().getMinionPoint();
		long prices = (50000 - playerSkillPoint) * 20;
		player.getInventory().decreaseKinah(prices, player);
		player.getCommonData().setMinionPoint(50000);
		if (isAuto) {
			player.getCommonData().setMinionAutoCharge(true);
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(11, 0));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404327));
	}

	public void synthesisMinion(Player player, int slot1, int slot2, int slot3, int slot4) {
		FastMap<Integer, MinionTemplate> minionTemplate = new FastMap<>();
		String minionGrade = "D";
		boolean isKerub = false;
		boolean success = false;
		int minionId = 0;
		String minionName = "";

		if (player.getInventory().getKinah() < 500000) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300648)); //STR_FAMILIAR_GROWTH_MSG_NOGOLD
			return;
		}

		player.getInventory().decreaseKinah(50000);

		float unlucky = (float) (Math.random() * 100 + 1);

		if (unlucky <= 80) {
			success = true;
		}

		for (MinionCommonData list : player.getMinionList().getMinions()) {
			if (list.getObjectId() == slot1 || list.getObjectId() == slot2 || list.getObjectId() == slot3 || list.getObjectId() == slot4) {
				MinionTemplate mt = DataManager.MINION_DATA.getMinionTemplate(list.getMinionId());
				minionGrade = mt.getTierGrade();
				if (mt.isKerub()) {
					isKerub = true;
				}
				//Delete Used Minion From List
				player.getMinionList().deleteMinion(list.getObjectId());
				DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinions(player, list.getMinionId(), list.getRealName());
			}
		}

		if (success) {
			for (MinionTemplate template : DataManager.MINION_DATA.getMinionData().valueCollection()) {
				if (isKerub) {
					if (minionGrade.equals("D") && template.getTierGrade().equals("C") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("C") && template.getTierGrade().equals("B") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("B") && template.getTierGrade().equals("A") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("A")) {
						return;
					}
				} else {
					if (minionGrade.equals("D") && template.getTierGrade().equals("C") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("C") && template.getTierGrade().equals("B") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("B") && template.getTierGrade().equals("A") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("A")) {
						return;
					}
				}

			}
		} else {
			for (MinionTemplate template : DataManager.MINION_DATA.getMinionData().valueCollection()) {
				if (isKerub) {
					if (minionGrade.equals("D") && template.getTierGrade().equals("D") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("C") && template.getTierGrade().equals("C") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("B") && template.getTierGrade().equals("B") && template.getStarGrade() == 1 && template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("A")) {
						return;
					}
				} else {
					if (minionGrade.equals("D") && template.getTierGrade().equals("D") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("C") && template.getTierGrade().equals("C") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("B") && template.getTierGrade().equals("B") && template.getStarGrade() == 1 && !template.isKerub()) {
						minionTemplate.put(template.getId(), template);
					} else if (minionGrade.equals("A")) {
						return;
					}
				}
			}
		}


		int rnd = Rnd.get(1, minionTemplate.size());
		int i = 1;
		for (MinionTemplate mt : minionTemplate.values()) {
			if (i == rnd) {
				minionId = mt.getId();
				minionName = mt.getName() + getMinionUnkName();
				break;
			}
			i++;
		}

		addMinion(player, minionId, minionName, 0, true, success);
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, playerMinions));
		}
	}

	public void lockMinion(Player player, int minionObjId, int isLocked) {
		Minion minion = player.getMinion();
		if (minion != null) {
			DAOManager.getDAO(PlayerMinionsDAO.class).lockMinions(player, minionObjId, isLocked);
			minion.getCommonData().setLocked(isLocked);
			PacketSendUtility.broadcastPacket(player, new SM_MINIONS(4, minion), true);
		}
	}

	public void adoptMinion(Player player, Item item, String grade) {
		FastMap<Integer, MinionTemplate> minionTemplate = new FastMap<>();
		int minionId = 0;
		String minionName = "";

		for (MinionTemplate template : DataManager.MINION_DATA.getMinionData().valueCollection()) {
			if (template.getTierGrade().equalsIgnoreCase(grade) && template.getStarGrade() == 1) {
				minionTemplate.put(template.getId(), template);
			}
		}

		int rnd = Rnd.get(1, minionTemplate.size());
		int i = 1;
		for (MinionTemplate mt : minionTemplate.values()) {
			if (i == rnd) {
				minionId = mt.getId();
				minionName = "NEW_" + mt.getName() + getMinionUnkName();
				break;
			}
			i++;
		}


		if (!validateAdoption(player, item.getItemTemplate(), minionId)) {
			return;
		}

		int expireTime = item.getItemTemplate().getActions().getAdoptMinionAction().getExpireMinutes() != 0
				? (int) ((System.currentTimeMillis() / 1000) + item.getItemTemplate().getActions().getAdoptMinionAction().getExpireMinutes() * 60) : 0;

		addMinion(player, minionId, minionName, expireTime, false, false);
	}

	private String getMinionUnkName() {
		int daysx = Calendar.DAY_OF_MONTH;
		long uptime = System.currentTimeMillis();
		long days = TimeUnit.MILLISECONDS.toDays(uptime);
		uptime -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(uptime);
		uptime -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
		uptime -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);

		return daysx + "" + hours + "" + minutes + "" + seconds;
	}

	public void adoptMinion(Player player, Item item, int minionId) {

		String minionName = DataManager.MINION_DATA.getMinionTemplate(minionId).getName();
		minionName = "NEW_"+ minionName + getMinionUnkName();

		if (!validateAdoption(player, item.getItemTemplate(), minionId)) {
			return;
		}

		int expireTime = item.getItemTemplate().getActions().getAdoptMinionAction().getExpireMinutes() != 0
				? (int) ((System.currentTimeMillis() / 1000) + item.getItemTemplate().getActions().getAdoptMinionAction().getExpireMinutes() * 60) : 0;

		addMinion(player, minionId, minionName, expireTime, false, false);
	}

	public void deleteMinion(Player player, int minionObjtId, boolean materialuse) {
		for (MinionCommonData list : player.getMinionList().getMinions()) {
			if (list.getObjectId() == minionObjtId) {
				player.getMinionList().deleteMinion(list.getObjectId());
				DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinions(player, list.getMinionId(), list.getRealName());
				PacketSendUtility.broadcastPacket(player, new SM_MINIONS(2, list, materialuse), true);
				break;
			}
		}
	}

	public void summonMinion(Player player, int minionObjtId, boolean isManualSpawn) {
		MinionCommonData mcd = player.getMinionList().getMinion(minionObjtId);
		int minionId = mcd.getMinionId();

		if (player.getMinion() != null) {
			dismissMinion(player, isManualSpawn);
		}

		Minion minions = VisibleObjectSpawner.spawnMinion(player, minionObjtId);
		int skillId1 = DataManager.MINION_DATA.getMinionTemplate(minionId).getSkill1();
		int skillId2 = DataManager.MINION_DATA.getMinionTemplate(minionId).getSkill2();

		player.getSkillList().addSkillNoSave(player, skillId1, 1);
        if (skillId2 != 0) {
            player.getSkillList().addSkillNoSave(player, skillId2, 1);
        }

		if(minionStatBuff != null){
			minionStatBuff.endEffect(player);
		}

		minionStatBuff.applyEffect(player, minionId, mcd.getGrowthPoint());

		if (minions != null) {
			player.getMinionList().setLastUsedMinionsId(minionId);
		}
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(5, player.getMinion()));
	}

	public void dismissMinion(Player player, boolean isManualDespawn) {
		Minion minions = player.getMinion();
		if (minions != null) {
			if (isManualDespawn) {
				minions.getCommonData().setDespawnTime(new Timestamp(System.currentTimeMillis()));
			}
			int skillId1 = DataManager.MINION_DATA.getMinionTemplate(minions.getMinionId()).getSkill1();
			int skillId2 = DataManager.MINION_DATA.getMinionTemplate(minions.getMinionId()).getSkill2();

			if (skillId1 != 0) {
				player.getSkillList().removeSkill(skillId1);
			}

			if (skillId2 != 0) {
				player.getSkillList().removeSkill(skillId2);
			}

			if (minionStatBuff != null) {
				minionStatBuff.endEffect(player);
			}

			MinionDopingBag bag = player.getMinionList().getMinion(minions.getObjectId()).getDopingBag();
			if (bag != null && bag.isDirty()) {
				DAOManager.getDAO(PlayerMinionsDAO.class).saveDopingBag(player, minions.getCommonData(), bag);
			}

			player.getMinionList().setLastUsedMinionsId(0);
			player.setMinion(null);
			minions.getController().delete();

			PacketSendUtility.broadcastPacketAndReceive(player, new SM_MINIONS(6, minions));
		}
	}

	public void onPlayerLogout(Player player) {

	}

	public void growthMinion(Player player, int minionObjectId, List<Integer> material) {
		int growthPoint = 0;
		long growthCost = 0;
		String tierGrade = "";

		MinionCommonData playerMinion = player.getMinionList().getMinion(minionObjectId);
		tierGrade = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getTierGrade();

		for (MinionCommonData list : player.getMinionList().getMinions()) {
			for (int matObjt : material) {
				if (list.getObjectId() == matObjt) {
					int minionGrowth = 0;
					if (DataManager.MINION_DATA.getMinionTemplate(list.getMinionId()).getTierGrade().equalsIgnoreCase(tierGrade)) {
						minionGrowth = (DataManager.MINION_DATA.getMinionTemplate(list.getMinionId()).getGrowthPt() * 2);
					} else {
						minionGrowth = DataManager.MINION_DATA.getMinionTemplate(list.getMinionId()).getGrowthPt();
					}
					growthPoint += minionGrowth;
					growthCost += DataManager.MINION_DATA.getMinionTemplate(list.getMinionId()).getGrowthCost();
					player.getMinionList().deleteMinion(list.getObjectId());
					DAOManager.getDAO(PlayerMinionsDAO.class).removePlayerMinions(player, list.getMinionId(), list.getRealName());
				}
			}
		}

		if (growthPoint <= 0) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404345));//STR_FAMILIAR_GROWTH_MSG_NOTSELECT
			return;
		}

		if (player.getInventory().getKinah() < growthCost) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404346)); //STR_FAMILIAR_GROWTH_MSG_NOGOLD
			return;
		}

		int maxGrowth = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getMaxGrowthValue();

		if(growthPoint > maxGrowth){
			growthPoint = maxGrowth;
		}

		player.getInventory().decreaseKinah(growthCost);
		playerMinion.setGrowthPoint(playerMinion.getGrowthPoint() + growthPoint);
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionsGrowthPoint(playerMinion);
		PacketSendUtility.broadcastPacket(player, new SM_MINIONS(7, playerMinion), true);
		for (int matObjt : material) {
			deleteMinion(player, matObjt, true);
		}
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, playerMinions));
		}

	}

	public void evolutionMinion(Player player, int minionObjtId) {
		MinionCommonData playerMinion = player.getMinionList().getMinion(minionObjtId);
		String groupset = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getGroupSet();
		int oldMinionId = playerMinion.getMinionId();
		int minionStarGrade = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getStarGrade();
		int minionMaxGrowth = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getMaxGrowthValue();
		int evoItemId = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getEvolveItem();
		int evolveItemCount = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getEvolveItemNum();
		long evoCost = DataManager.MINION_DATA.getMinionTemplate(playerMinion.getMinionId()).getEvolveCost();

		if (minionStarGrade >= 4) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404349)); //STR_FAMILIAR_EVOLVE_MSG_NOEVOLVE
			return;
		}

		if (player.getInventory().getKinah() < evoCost) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404348)); //STR_FAMILIAR_EVOLVE_MSG_NOGOLD
			return;
		}

		if (player.getInventory().getItemCountByItemId(evoItemId) < evolveItemCount) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404347));//STR_FAMILIAR_EVOLVE_MSG_LACK_ITEM
			return;
		}

		player.getInventory().decreaseKinah(evoCost);
		player.getInventory().decreaseByItemId(190200000, evolveItemCount);

		int minionNewId = 0;
		for (MinionTemplate template : DataManager.MINION_DATA.getMinionData().valueCollection()) {
			if (template.getStarGrade() == minionStarGrade + 1 && template.getGroupSet().equalsIgnoreCase(groupset)) {
				minionNewId = template.getId();
				break;
			}
		}

		playerMinion.setMinionId(minionNewId);
		DAOManager.getDAO(PlayerMinionsDAO.class).evolutionMinion(player, oldMinionId, minionNewId, playerMinion);

		playerMinion.setGrowthPoint(playerMinion.getGrowthPoint() - minionMaxGrowth);
		DAOManager.getDAO(PlayerMinionsDAO.class).updateMinionsGrowthPoint(playerMinion);


		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404350, playerMinion.getName(), minionStarGrade + 1));//STR_FAMILIAR_EVOLVE_MSG_LACK_ITEM

		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(0, playerMinions));
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(1, 1, playerMinion));
		PacketSendUtility.broadcastPacket(player, new SM_MINIONS(7, playerMinion), true);
	}

	public void useFunction(Player player) {
		player.getCommonData().setMinionFunctionTime(new Timestamp(TimeUtil.getAddDaysTimestamp(30)));
		Collection<MinionCommonData> playerMinions = player.getMinionList().getMinions();
		if (playerMinions != null && playerMinions.size() > 0) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(9, playerMinions));
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12, playerMinions));
		}
	}

	public void setFunction(Player player) {

	}

	public void relocateDoping(Player player, int minionObj, int targetSlot, int destinationSlot) {
		MinionCommonData mcd = player.getMinionList().getMinion(minionObj);
		if (mcd == null || mcd.getDopingBag() == null) {
			return;
		}
		int[] scrollBag = mcd.getDopingBag().getScrollsUsed();
		int targetItem = scrollBag[targetSlot - 2];
		if (destinationSlot - 2 > scrollBag.length - 1) {
			mcd.getDopingBag().setItem(targetItem, destinationSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0, 1, targetItem, destinationSlot));
			mcd.getDopingBag().setItem(0, targetSlot);
		} else {
			mcd.getDopingBag().setItem(scrollBag[destinationSlot - 2], targetSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0, 1, scrollBag[destinationSlot - 2], targetSlot));
			mcd.getDopingBag().setItem(targetItem, destinationSlot);
			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0, 1, targetItem, destinationSlot));
		}
	}

	public void autoFunction(Player player, final boolean activate) {
		if (activate) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12, 0, false));
		} else {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(12, 0, true));
		}
	}

	public void useDoping(final Player player, int minionObj, int functId, int itemId, int slot) {
		MinionCommonData mcd = player.getMinionList().getMinion(minionObj);

		if (mcd == null || mcd.getDopingBag() == null) {
			mcd = player.getMinion().getCommonData();
			if(mcd == null){
				log.warn("Player trying use minion function without minion : " + player.getName());
				return;
			}
		}


		if (functId == 0) {
			mcd.getDopingBag().setItem(itemId, slot);
		} else if (functId == 1) {
			mcd.getDopingBag().setItem(0, slot);
		} else if (functId == 3) {
			List<Item> items = player.getInventory().getItemsByItemId(itemId);
			for (; ; ) {
				Item useItem = items.get(0);
				ItemActions itemActions = useItem.getItemTemplate().getActions();
				ItemUseLimits limit = new ItemUseLimits();
				int useDelay = player.getItemCooldown(useItem.getItemTemplate()) / 3;
				if (useDelay < 3000) {
					useDelay = 3000;
				}
				limit.setDelayId(useItem.getItemTemplate().getUseLimits().getDelayId());
				limit.setDelayTime(useDelay);
				if (player.isItemUseDisabled(limit)) {
					final int useAction = functId;
					final int useItemId = itemId;
					final int useSlot = slot;
					final MinionCommonData finalMcd = mcd;
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							PacketSendUtility.sendPacket(player, new SM_MINIONS(8, finalMcd,0, useAction, useItemId, useSlot));
						}
					}, useDelay);
					return;
				}
				if (!RestrictionsManager.canUseItem(player, useItem) || player.isProtectionActive()) {
					player.addItemCoolDown(limit.getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
					break;
				}
				for (AbstractItemAction itemAction : itemActions.getItemActions()) {
					if (itemAction instanceof SkillUseAction) {
						PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), player.getObjectId(), useItem.getObjectId(), useItem.getItemId(), 0, 1, 1, 1, 0, 15360), true);
						SkillEngine.getInstance().applyEffectDirectly(((SkillUseAction) itemAction).getSkillid(), player, player, 0);
						player.addItemCoolDown(limit.getDelayId(), System.currentTimeMillis() + player.getItemCooldown(useItem.getItemTemplate()), player.getItemCooldown(useItem.getItemTemplate()) / 1000);
						if (useItem.getItemTemplate().getMaxStackCount() != 1 && useItem.getItemTemplate().getActivationCount() != 1000){
							player.getInventory().decreaseByItemId(useItem.getItemId(), 1);
						}
					} else {
						log.warn("Minion attempt to use item without SkillUseAction");
					}
				}
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_MINIONS(8, mcd, 0, functId, itemId, slot));
		itemId = mcd.getDopingBag().getFoodItem();
		long totalDopes = player.getInventory().getItemCountByItemId(itemId);
		itemId = mcd.getDopingBag().getDrinkItem();
		totalDopes += player.getInventory().getItemCountByItemId(itemId);
		int[] scrollBag = mcd.getDopingBag().getScrollsUsed();
		for (int i = 0; i < scrollBag.length; i++) {
			if (scrollBag[i] != 0) {
				totalDopes += player.getInventory().getItemCountByItemId(scrollBag[i]);
			}
		}
		if (totalDopes == 0) {
			mcd.setIsBuffing(false);
		}
	}

	public void activateLoot(final Player player, int minionObj, final boolean activate) {
		if (activate) {
			if (player.isInTeam()) {
				LootRuleType lootType = player.getLootGroupRules().getLootRule();
				if (lootType == LootRuleType.FREEFORALL) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE03);
					return;
				}
			}
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LOOTING_PET_MESSAGE01);
		}
		MinionCommonData mcd = player.getMinionList().getMinion(minionObj);
		mcd.setIsLooting(activate);
		PacketSendUtility.sendPacket(player, new SM_MINIONS(mcd, activate));
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final MinionService instance = new MinionService();
	}
}