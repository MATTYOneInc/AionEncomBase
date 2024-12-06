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

/****/
/** Author (Encom)
/****/

public class _20526Special_Secret_Mission extends QuestHandler
{
    public static final int questId = 20526;
	private final static int[] npcs = {806079, 806394, 806296, 806297, 703318, 703319, 703320};
	
    public _20526Special_Secret_Mission() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182216086, questId); 
		qe.registerOnEnterZoneMissionEnd(questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20525, true);
    }
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 220120000) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 1) {
                    changeQuestStep(env, 1, 2, false);
				}
			}
		} else if (player.getWorldId() == 220110000) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 4) {
					playQuestMovie(env, 876);
					changeQuestStep(env, 4, 5, false);
				}
			}
		}
        return false;
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
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806079) { //Feregran.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806394) { //Barota.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case SELECT_ACTION_1695: {
						if (var == 2) {
							return sendQuestDialog(env, 1695);
						}
					} case STEP_TO_3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806296) { //Wejabobo
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 3) {
                            return sendQuestDialog(env, 2291);
                        }
					} case SELECT_ACTION_2292: {
						if (var == 3) {
							return sendQuestDialog(env, 2292);
						}
					} case STEP_TO_4: {
						giveQuestItem(env, 182216086, 1);
                        changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
                }
            } 
			if (targetId == 806297) { // Awakened Wejabobo
				switch (env.getDialog()) {
					case START_DIALOG:
					    if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
						else if (var == 7) {
							return sendQuestDialog(env, 3398);
						}
						else if (var == 9) {
							return sendQuestDialog(env, 4080);
						}
						else if (var == 11) {
						return sendQuestDialog(env, 6841);
						}
					case STEP_TO_6: {
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return defaultCloseDialog(env, 5, 6); // 6
					}	
					case STEP_TO_8: {
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return defaultCloseDialog(env, 7, 8); // 8
					}	
					case STEP_TO_10: {
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return defaultCloseDialog(env, 9, 10); // 10
					}	
					case SET_REWARD: 
						removeQuestItem(env, 182216086, 1);
						removeQuestItem(env, 164002348, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return defaultCloseDialog(env, 11, 12); // 12
					default:
						break;
					}
				}
			if (targetId == 703318) { // Strange Aether Piece
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 6) {
                            return sendQuestDialog(env, 2717);
                        }
					} case STEP_TO_6: {
                        changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 703319) { // Mysterious Aether Piece
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 8) {
                            return sendQuestDialog(env, 3400);
                        }
					} case STEP_TO_8: {
                        changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 703320) { // Suspicious Aether Piece
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 10) {
                            return sendQuestDialog(env, 4081);
                        }
					} case STEP_TO_10: {
                        changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
                }
            } 
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806079) { //Feregran.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
}