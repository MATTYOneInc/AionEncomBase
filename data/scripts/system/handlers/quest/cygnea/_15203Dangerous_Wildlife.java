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
package quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15203Dangerous_Wildlife extends QuestHandler {

    private final static int questId = 15203;
    public _15203Dangerous_Wildlife() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(804704).addOnQuestStart(questId);
        qe.registerQuestNpc(804704).addOnTalkEvent(questId);
		qe.registerQuestNpc(235829).addOnKillEvent(questId);
		qe.registerQuestNpc(235831).addOnKillEvent(questId);
		qe.registerQuestNpc(235851).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 804704) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804704) {
				if (env.getDialogId() == 1352) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
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
                    case 235829:
                        if (var1 < 4) {
                            return defaultOnKillEvent(env, 235829, 0, 4, 1);
                        } else if (var1 == 4) {
                            if (var2 == 3 && var3 == 2) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 235829, 4, 5, 1);
                            }
                        }
                    break;
                    case 235831:
                        if (var2 < 2) {
                            return defaultOnKillEvent(env, 235831, 0, 2, 2);
                        } else if (var2 == 2) {
                            if (var1 == 5 && var3 == 2) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 235831, 2, 3, 2);
                            }
                        }
                    break;
					case 235851:
                        if (var3 < 1) {
                            return defaultOnKillEvent(env, 235851, 0, 1, 3);
                        } else if (var3 == 1) {
                            if (var1 == 5 && var2 == 3) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, 235851, 1, 2, 3);
                            }
                        }
                    break;
                }
            }
        }
        return false;
    }
}