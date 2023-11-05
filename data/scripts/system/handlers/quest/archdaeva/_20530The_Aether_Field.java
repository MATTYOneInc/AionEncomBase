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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author (Encom)
/****/

public class _20530The_Aether_Field extends QuestHandler
{
    public static final int questId = 20530;
	private final static int[] npcs = {204075, 204173, 204182, 798443, 806556, 703388, 703389, 703391, 703395};
	
    public _20530The_Aether_Field() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] norsvoldQuest = {20520, 20521, 20525, 20526, 20527, 20528, 20529};
		return defaultOnZoneMissionEndEvent(env, norsvoldQuest);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] norsvoldQuest = {20520, 20521, 20525, 20526, 20527, 20528, 20529};
		return defaultOnLvlUpEvent(env, norsvoldQuest, true);
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
            if (targetId == 204075) { //Balder.
                switch (env.getDialog()) {
					case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        } else if (var == 8) {
							return sendQuestDialog(env, 3398);
						} else if (var == 9) {
							return sendQuestDialog(env, 3739);
						}
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_3399: {
						if (var == 8) {
							return sendQuestDialog(env, 3399);
						}
					} case SELECT_ACTION_3740: {
						if (var == 9) {
							return sendQuestDialog(env, 3740);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					} case SELECT_REWARD: {
						playQuestMovie(env, 148);
						removeQuestItem(env, 182216166, 1);
						removeQuestItem(env, 182216168, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 8, 9, false, 10000, 10001);
					} case FINISH_DIALOG: {
						if (var == 9) {
							defaultCloseDialog(env, 9, 9);
						} else if (var == 8) {
							defaultCloseDialog(env, 8, 8);
						}
					}
                }
            } if (targetId == 204182) { //Heimdall.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 703388) { //Frosted Pandeanan.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 2) {
                            changeQuestStep(env, 2, 3, false);
							Npc npc = (Npc) env.getVisibleObject();
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 798443) { //Silke.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					} case SELECT_ACTION_2035: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_4: {
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 204173) { //Jeckrow.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 4) {
                            return sendQuestDialog(env, 2375);
                        }
					} case SELECT_ACTION_2376: {
						if (var == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case STEP_TO_5: {
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 806556) { //Jiharimaan.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					} case SELECT_ACTION_2717: {
					    if (var == 5) {
							return sendQuestDialog(env, 2717);
						}
					} case STEP_TO_6: {
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 703395) { //Cherry Brandy Bottle.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 6) {
                            changeQuestStep(env, 6, 7, false);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 703391) { //Magic Ward Of Diabolic Energy.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 7) {
							QuestService.addNewSpawn(120010000, 1, 703389, 1160.0125f, 1367.9454f, 209.66165f, (byte) 0, 2556);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 703389) { //Greatmage's Tiny Incense Burner.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 7) {
							giveQuestItem(env, 182216168, 1);
                            changeQuestStep(env, 7, 8, false);
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204075) { //Balder.
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