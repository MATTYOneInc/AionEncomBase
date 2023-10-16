/*
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
package quest.ascension;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import java.util.ArrayList;
import java.util.List;

public class _2008Ascension extends QuestHandler
{
	private final static int questId = 2008;
	
	public _2008Ascension() {
		super(questId);
	}
	
	@Override
	public void register() {
		if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
			return;
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203550).addOnTalkEvent(questId);
		qe.registerQuestNpc(790003).addOnTalkEvent(questId);
		qe.registerQuestNpc(790002).addOnTalkEvent(questId);
		qe.registerQuestNpc(203546).addOnTalkEvent(questId);
		qe.registerQuestNpc(205020).addOnTalkEvent(questId);
		qe.registerQuestNpc(205040).addOnKillEvent(questId);
		qe.registerQuestNpc(205041).addOnAttackEvent(questId);
		qe.registerOnMovieEndQuest(152, questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnDie(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int instanceId = player.getInstanceId();
		List<Npc> mobs = new ArrayList<Npc>();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (targetId == 205040) {
			if (var >= 51 && var <= 53) {
				switch (var) {
					case 51:
						mobs.add((Npc) QuestService.addNewSpawn(320020000, instanceId, 205040, 305f, 279f, 206.5f, (byte) 0));
					break;
					case 52:
						mobs.add((Npc) QuestService.addNewSpawn(320020000, instanceId, 205040, 298f, 253f, 205.7f, (byte) 0));
					break;
					case 53:
						mobs.add((Npc) QuestService.addNewSpawn(320020000, instanceId, 205040, 306f, 251f, 206f, (byte) 0));
					break;
				}
				qs.setQuestVar(qs.getQuestVars().getQuestVars() + 1);
				updateQuestStatus(env);
				return true;
			} else if (var == 54) {
				qs.setQuestVar(5);
				updateQuestStatus(env);
				Npc mob = (Npc) QuestService.addNewSpawn(320020000, instanceId, 205041, 301f, 259f, 205.5f, (byte) 0);
				mob.getAggroList().addDamage(player, 1000);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onAttackEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 5) {
			return false;
		}
		int targetId = env.getTargetId();
		if (targetId != 205041) {
			return false;
		}
		Npc npc = (Npc) env.getVisibleObject();
		if (npc.getLifeStats().getCurrentHp() < npc.getLifeStats().getMaxHp() / 2) {
			playQuestMovie(env, 152);
			npc.getController().onDelete();
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVars().getQuestVars();
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203550) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
						else if (var == 6) {
							return sendQuestDialog(env, 2716);
						}
					case SELECT_ACTION_2376:
						if (var == 4) {
							playQuestMovie(env, 57);
							removeQuestItem(env, 182203009, 1);
							removeQuestItem(env, 182203010, 1);
							removeQuestItem(env, 182203011, 1);
							return false;
						}
					case STEP_TO_1:
						qs.setQuestVar(1);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 220010000, 585.2919f, 2416.9778f, 280.625f, (byte) 98);
						return true;
					case STEP_TO_5:
						if (var == 4) {
							qs.setQuestVar(99);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320020000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService2.teleportTo(player, 320020000, newInstance.getInstanceId(), 457.65f, 426.8f, 230.4f);
							return true;
						}
					case STEP_TO_6:
						if (var == 6) {
							PlayerClass playerClass = player.getCommonData().getPlayerClass();
							if (playerClass == PlayerClass.WARRIOR) {
								return sendQuestDialog(env, 3057);
							}
							else if (playerClass == PlayerClass.SCOUT) {
								return sendQuestDialog(env, 3398);
							}
							else if (playerClass == PlayerClass.MAGE) {
								return sendQuestDialog(env, 3739);
							}
							else if (playerClass == PlayerClass.PRIEST) {
								return sendQuestDialog(env, 4080);
							}
							else if (playerClass == PlayerClass.TECHNIST) {
								return sendQuestDialog(env, 3612);
							}
							else if (playerClass == PlayerClass.MUSE) {
								return sendQuestDialog(env, 3910);
							}
						}
					case STEP_TO_7:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.GLADIATOR);
						}
					case STEP_TO_8:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.TEMPLAR);
						}
					case STEP_TO_9:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.ASSASSIN);
						}
					case STEP_TO_10:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.RANGER);
						}
					case STEP_TO_11:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.SORCERER);
						}
					case STEP_TO_12:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.SPIRIT_MASTER);
						}
					case STEP_TO_13:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.CHANTER);
						}
					case STEP_TO_14:
						if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.CLERIC);
						}
					case STEP_TO_15:
					    if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.GUNSLINGER);
						}
					case STEP_TO_16:
	                    if (var == 6) {
							return setPlayerClass(env, qs, PlayerClass.SONGWEAVER);
						}
					case STEP_TO_17:
	                if (var == 6) {
						return setPlayerClass(env, qs, PlayerClass.AETHERTECH);
					}
				}
			} else if (targetId == 790003) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					case STEP_TO_2:
					if (var == 1) {
						if (player.getInventory().getItemCountByItemId(182203009) == 0)
							if (giveQuestItem(env, 182203009, 1)) {
								qs.setQuestVar(2);
						        updateQuestStatus(env);
						        TeleportService2.teleportTo(player, 220010000, 940.74475f, 2295.5305f, 265.65674f, (byte) 46);
							    return true;
						}
					}
				}
			} else if (targetId == 790002) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					case STEP_TO_3:
					if (var == 2) {
						if (player.getInventory().getItemCountByItemId(182203010) == 0) {
							if (!giveQuestItem(env, 182203010, 1)) {
								return true;
							}
						}
						qs.setQuestVar(3);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 220010000, 1105.3795f, 1707.9614f, 270.25f, (byte) 117);
						return true;
					}
				}
			} else if (targetId == 203546) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					case STEP_TO_4:
					if (var == 3) {
						if (player.getInventory().getItemCountByItemId(182203011) == 0) {
							if (!giveQuestItem(env, 182203011, 1)) {
								return true;
							}
						}
						qs.setQuestVar(4);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 220010000, 383.10248f, 1895.3093f, 327.625f, (byte) 59);
						return true;
					}
				}
			} else if (targetId == 205020) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 99) {
							player.setState(CreatureState.FLIGHT_TELEPORT);
							player.unsetState(CreatureState.ACTIVE);
							player.setFlightTeleportId(3001);
							PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 3001, 0));
							qs.setQuestVar(50);
							updateQuestStatus(env);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									qs.setQuestVar(51);
									updateQuestStatus(env);
									List<Npc> mobs = new ArrayList<Npc>();
									mobs.add((Npc) QuestService.addNewSpawn(320020000, instanceId, 205040, 294f, 277f, 207f, (byte) 0));
									for (Npc mob: mobs) {
										mob.getAggroList().addDamage(player, 1000);
									}
								}
							}, 43000);
							return true;
						}
						return false;
					default:
						return false;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203550) {
				player.getCommonData().setExp(126068, false);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var == 5 || (var >= 50 && var <= 55) || var == 99) {
				if (player.getWorldId() != 320020000) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				} else {
					PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 152) {
			return false;
		}
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 5) {
			return false;
		}
		int instanceId = player.getInstanceId();
		QuestService.addNewSpawn(320020000, instanceId, 203550, 301.92999f, 274.26001f, 205.7f, (byte) 0);
		qs.setQuestVar(6);
		updateQuestStatus(env);
		return true;
	}
	
	private boolean setPlayerClass(QuestEnv env, QuestState qs, PlayerClass playerClass) {
		Player player = env.getPlayer();
		ClassChangeService.setClass(player, playerClass);
		player.getController().upgradePlayer();
		TeleportService2.teleportTo(player, 220010000, 386.03476f, 1893.9309f, 327.62283f, (byte) 59);
		changeQuestStep(env, 6, 6, true);
		return true;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVars().getQuestVars();
		if (var == 5 || (var >= 51 && var <= 53)) {
			qs.setQuestVar(4);
			updateQuestStatus(env);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
		}
		return false;
	}
}