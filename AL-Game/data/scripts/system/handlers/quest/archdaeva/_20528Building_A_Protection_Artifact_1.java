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
/****/
public class _20528Building_A_Protection_Artifact_1 extends QuestHandler {

    public static final int questId = 20528;
	private final static int[] npcs = {806079, 806296, 703324, 731714, 731715};
    public _20528Building_A_Protection_Artifact_1() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } 
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestItem(182216088, questId); 
		qe.registerQuestNpc(244125).addOnKillEvent(questId);
        qe.registerQuestNpc(244126).addOnKillEvent(questId); 		
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q20528_A_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q20528_B_220110000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20527, true);
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
				case 806079: { // Feregran
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case STEP_TO_1:
						    giveQuestItem(env, 182216088, 1);
							return defaultCloseDialog(env, 0, 1); // 1
						case STEP_TO_3:
							return defaultCloseDialog(env, 2, 3); // 1
						default:
							break;
					}
				}
                break; 
				case 806296: { // Wejabobo
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
				case 703324: { // Anima's Treasure
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;
					}
				}
                break;
				case 806297: { // Awakened Wejabobo
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
							giveQuestItem(env, 182216089, 1);
							return defaultCloseDialog(env, 8, 9);
						}
						case SET_REWARD: {
							removeQuestItem(env, 182216088, 1);
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
				case 731714: { // Hidden Sky Island Teleporter
					switch (env.getDialog()) {
						case USE_OBJECT:
							if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						case STEP_TO_7: {
							changeQuestStep(env, 6, 7, false);
							TeleportService2.teleportTo(player, 220110000, 2230.3057f, 2310.2126f, 535.7577f, (byte) 91);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 731715: { // Norsvold Protection Artifact
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
						playQuestMovie(env, 877);
						removeQuestItem(env, 182216089, 1);
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
	}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806079) { // Feregran
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
					case 244125: {
						if (var1 < 9) {
							return defaultOnKillEvent(env, 244125, 0, 9, 1);
						}
						else if (var1 == 9) {
							if (var2 == 1) {
								qs.setQuestVar(5);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244125, 9, 10, 1);
							}
						}
						break;
					}
					case 244126: {
						if (var2 < 0) {
							return defaultOnKillEvent(env, 244126, 0, 0, 2);
						}
						else if (var2 == 0) {
							if (var1 == 10) {
								qs.setQuestVar(5);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, 244126, 0, 1, 2);
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
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q20528_A_220110000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					QuestService.addNewSpawn(220110000, 1, 244125, 2691.0403f, 647.167f, 270.70297f, (byte) 60); 
                    QuestService.addNewSpawn(220110000, 1, 244125, 2681.0925f, 632.02625f, 269.82822f, (byte) 44); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2671.9636f, 635.10345f, 268.55148f, (byte) 37); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2685.7607f, 654.4808f, 270.05008f, (byte) 52); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2683.1902f, 646.7986f, 269.64877f, (byte) 50); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2669.229f, 644.03455f, 267.3993f, (byte) 42); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2677.9429f, 654.6115f, 268.44983f, (byte) 60);
					QuestService.addNewSpawn(220110000, 1, 244125, 2671.857f, 651.171f, 267.34818f, (byte) 49); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2662.8188f, 634.545f, 267.96545f, (byte) 11); 
					QuestService.addNewSpawn(220110000, 1, 244125, 2683.5947f, 663.9745f, 268.92566f, (byte) 67); 
					QuestService.addNewSpawn(220110000, 1, 244126, 2684.0596f, 639.54694f, 269.78577f, (byte) 51); 
					return true;
				}
			} else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q20528_B_220110000")) {
				if (var == 7) {
					changeQuestStep(env, 7, 8, false);
					return true;
				}
			}
		}
		return false;
	}
}