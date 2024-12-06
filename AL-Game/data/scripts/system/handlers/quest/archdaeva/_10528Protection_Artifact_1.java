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
package quest.archdaeva;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author (Encom)
/**  @author Phantom_KNA
/****/
public class _10528Protection_Artifact_1 extends QuestHandler {

    public static final int questId = 10528;
	private final static int[] npcs = {806075, 806291, 806292, 703316, 731708, 731709};
    public _10528Protection_Artifact_1() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } 
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestItem(182216076, questId); 
		qe.registerQuestNpc(244109).addOnKillEvent(questId);
        qe.registerQuestNpc(244110).addOnKillEvent(questId); 		
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q10528_A_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q10528_B_210100000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10527, true);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806075: { // Wedas
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case STEP_TO_1:
						    giveQuestItem(env, 182216076, 1);
							return defaultCloseDialog(env, 0, 1); // 1
						case STEP_TO_3:
							return defaultCloseDialog(env, 2, 3); // 1
						default:
							break;
					}
				}
                break;
				case 806291: { // Dezabo
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case STEP_TO_2:
							return defaultCloseDialog(env, 1, 2); // 2
						default:
							break;
					}
				}
                break;
				case 703316: { // Barteon's Treasure Chest
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;
					}
				}
                break;
				case 806292: { // Awakened Wizabo
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
							else if (var == 8) {
								return sendQuestDialog(env, 3739);
							}
							else if (var == 11) {
								return sendQuestDialog(env, 6841);
							}
						case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 5, 6, false, 10000, 10001); 
						}
						case STEP_TO_9: {
							giveQuestItem(env, 182216093, 1);
							return defaultCloseDialog(env, 8, 9);
						}
						case SET_REWARD: {
							removeQuestItem(env, 182216076, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							changeQuestStep(env, 11, 12, true);
						    return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
                break;
				case 731708: { // Teleport
					switch (env.getDialog()) {
						case USE_OBJECT:
							if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						case STEP_TO_7: {
							changeQuestStep(env, 6, 7, false);
							TeleportService2.teleportTo(player, 210100000, 2497.7742f, 643.23785f, 458.36703f, (byte) 75);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 731709: { // Artefact
					switch (env.getDialog()) {
						case USE_OBJECT: {
						if (var == 9) {
							return sendQuestDialog(env, 3739);
						} else if (var == 10) {
							return sendQuestDialog(env, 4080);
						}
					} case SELECT_ACTION_3740: {
						if (var == 9) {
							return sendQuestDialog(env, 3740);
						}
					} case SELECT_ACTION_4081: {
						if (var == 10) {
							return sendQuestDialog(env, 4081);
						}
					} case STEP_TO_9: {
                        changeQuestStep(env, 9, 10, false);
						return closeDialogWindow(env);
					} case STEP_TO_10: {
						playQuestMovie(env, 1005);
						removeQuestItem(env, 182216093, 1);
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
	}
    else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806075) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 4) {
				int targetId = env.getTargetId();
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 244109: {
						if (var1 < 9) {
							return defaultOnKillEvent(env, 244109, 0, 9, 1);
						}
						else if (var1 == 9) {
							if (var2 == 1) {
								qs.setQuestVar(5);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244109, 9, 10, 1);
							}
						}
						break;
					}
					case 244110: {
						if (var2 < 0) {
							return defaultOnKillEvent(env, 244110, 0, 0, 2);
						}
						else if (var2 == 0) {
							if (var1 == 10) {
								qs.setQuestVar(5);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244110, 0, 1, 2);
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q10528_A_210100000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					QuestService.addNewSpawn(210100000, 1, 244109, 2889.9578f, 2380.9097f, 236.62764f, (byte) 74);
                    QuestService.addNewSpawn(210100000, 1, 244109, 2906.218f, 2378.6492f, 235.3418f, (byte) 65);
                    QuestService.addNewSpawn(210100000, 1, 244109, 2891.9438f, 2361.2007f, 233.62851f, (byte) 35);
                    QuestService.addNewSpawn(210100000, 1, 244109, 2914.7168f, 2370.3203f, 236.04294f, (byte) 70);
                    QuestService.addNewSpawn(210100000, 1, 244109, 2874.361f, 2380.2175f, 237.0f, (byte) 94);
					QuestService.addNewSpawn(210100000, 1, 244109, 2890.476f, 2352.9487f, 233.9343f, (byte) 90);
					QuestService.addNewSpawn(210100000, 1, 244109, 2868.4194f, 2341.2292f, 237.02196f, (byte) 16);
					QuestService.addNewSpawn(210100000, 1, 244109, 2873.2576f, 2352.1912f, 237.20416f, (byte) 112);
					QuestService.addNewSpawn(210100000, 1, 244109, 2851.1619f, 2370.3933f, 237.10155f, (byte) 110);
					QuestService.addNewSpawn(210100000, 1, 244109, 2881.8997f, 2341.9282f, 235.0179f, (byte) 110);
					QuestService.addNewSpawn(210100000, 1, 244110, 2865.538f, 2363.0034f, 237.20416f, (byte) 111);
					return true;
				}
			} else if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q10528_B_210100000")) {
				if (var == 7) {
					changeQuestStep(env, 7, 8, false);
					return true;
				}
			}
		}
		return false;
	}
}