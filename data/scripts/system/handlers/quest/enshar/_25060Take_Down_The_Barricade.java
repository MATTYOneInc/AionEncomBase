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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25060Take_Down_The_Barricade extends QuestHandler
{
    private final static int questId = 25060;
	
    public _25060Take_Down_The_Barricade() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(804730).addOnQuestStart(questId);
        qe.registerQuestNpc(804730).addOnTalkEvent(questId);
		qe.registerQuestNpc(220033).addOnKillEvent(questId);
		qe.registerQuestNpc(220034).addOnKillEvent(questId);
		qe.registerQuestNpc(220035).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 804730) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804730) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 1) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 1, 2, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804730) {
				if (env.getDialogId() == 1352) {
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
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 0) {
                int targetId = env.getTargetId();
                int var1 = qs.getQuestVarById(1);
                int var2 = qs.getQuestVarById(2);
				int var3 = qs.getQuestVarById(3);
                switch (targetId) {
                    case 220033:
                        if (var1 < 0) {
                            return defaultOnKillEvent(env, 220033, 0, 0, 1);
                        } else if (var1 == 0) {
                            if (var2 == 1 && var3 == 1) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 220033, 0, 1, 1);
                            }
                        }
                    break;
                    case 220034:
                        if (var2 < 0) {
                            return defaultOnKillEvent(env, 220034, 0, 0, 2);
                        } else if (var2 == 0) {
                            if (var1 == 1 && var3 == 1) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 220034, 0, 1, 2);
                            }
                        }
                    break;
					case 220035:
                        if (var3 < 0) {
                            return defaultOnKillEvent(env, 220035, 0, 0, 3);
                        } else if (var3 == 0) {
                            if (var1 == 1 && var2 == 1) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 220035, 0, 1, 3);
                            }
                        }
                    break;
                }
            }
        }
        return false;
    }
}