/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _20504Tiamats_Shadow extends QuestHandler
{
    public static final int questId = 20504;
	private final static int[] mobs = {219943, 219944, 219945, 219946, 219947, 219948};
	
    public _20504Tiamats_Shadow() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {804730, 804742, 804731};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(219949).addOnKillEvent(questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20503, true);
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
            if (targetId == 804730) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        } else if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					} case STEP_TO_3: {
                        changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 804742) {
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
						Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
                }
			} if (targetId == 804731) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 6) {
                            return sendQuestDialog(env, 3057);
                        }
					} case SELECT_ACTION_3058: {
						if (var == 6) {
							return sendQuestDialog(env, 3058);
						}
					} case SET_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804730) {
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
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
		final Npc npc = (Npc) env.getVisibleObject();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			int targetId = env.getTargetId();
            if (var == 1) {
				int[] mobs1 = {219943, 219944, 219945};
				switch (targetId) {
                    case 219943: //Beritra Defense Scaleblade.
					case 219944: //Beritra Defense Talonscout.
                    case 219945: { //Beritra Defense Wyrmtongue.
                        if (var1 >= 0 && var1 < 4) {
							return defaultOnKillEvent(env, mobs1, var1, var1 + 1, 1);
						} else if (var1 == 4) {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return true;
						}
                        break;
                    }
				}
            } else if (var == 3) {
				int[] mobs2 = {219946, 219947, 219948};
				switch (targetId) {
                    case 219946: //Vengeful Aetheric Guard Dominator.
					case 219947: //Vengeful Aetheric Guard Swiftshank.
                    case 219948: { //Vengeful Aetheric Guard Seersage.
                        if (var2 >= 0 && var2 < 4) {
							return defaultOnKillEvent(env, mobs2, var2, var2 + 1, 2);
						} else if (var2 == 4) {
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return true;
						}
                        break;
                    }
				}
            } else if (var == 4) {
				switch (targetId) {
                    case 219949: { //Cursed Gilgamesh.
						QuestService.addNewSpawn(220080000, 1, 804742, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						qs.setQuestVar(5);
						updateQuestStatus(env);
						return true;
					}
                }
			}
		}
		return false;
    }
}