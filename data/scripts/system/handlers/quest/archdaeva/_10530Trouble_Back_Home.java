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
public class _10530Trouble_Back_Home extends QuestHandler {

    public static final int questId = 10530;
	private final static int[] npcs = {203725, 203752, 203852, 798440, 806555, 703386, 703387, 703390, 703394};
    public _10530Trouble_Back_Home() {
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
		int[] ilumaQuest = {10520, 10521, 10525, 10526, 10527, 10528, 10529};
		return defaultOnZoneMissionEndEvent(env, ilumaQuest);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] ilumaQuest = {10520, 10521, 10525, 10526, 10527, 10528, 10529};
		return defaultOnLvlUpEvent(env, ilumaQuest, true);
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
            if (targetId == 203752) { //Jucleas.
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
						playQuestMovie(env, 118);
						removeQuestItem(env, 182216164, 1);
						removeQuestItem(env, 182216167, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestEndDialog(env);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 8, 9, true, 10000, 10001);
					}
                }
            } if (targetId == 203725) { //Leah.
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
			} if (targetId == 703386) { //Frosted Ellianan.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 2) {
                            changeQuestStep(env, 2, 3, false);
							Npc npc = (Npc) env.getVisibleObject();
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 798440) { //Letia.
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
			} if (targetId == 203852) { //Ludina.
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
            } if (targetId == 806555) { //Damation.
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
			} if (targetId == 703394) { //Omblic Brandy Bottle.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 6) {
                            changeQuestStep(env, 6, 7, false);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 703390) { //Magic Ward Of Dark Energy.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 7) {
							QuestService.addNewSpawn(110010000, 1, 703387, 1540.9883f, 1457.1543f, 572.86218f, (byte) 0, 2050);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 703387) { //Greatmage's Small Incense Burner.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 7) {
							giveQuestItem(env, 182216167, 1);
                            changeQuestStep(env, 7, 8, false);
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203752) { //Jucleas.
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