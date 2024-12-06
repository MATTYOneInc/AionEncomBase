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

public class _15073Perennial_Struggles extends QuestHandler
{
    private final static int questId = 15073;
	
    public _15073Perennial_Struggles() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(804709).addOnQuestStart(questId);
        qe.registerQuestNpc(804709).addOnTalkEvent(questId);
		qe.registerQuestNpc(235892).addOnKillEvent(questId);
		qe.registerQuestNpc(235893).addOnKillEvent(questId);
		qe.registerQuestNpc(235888).addOnKillEvent(questId);
		qe.registerQuestNpc(235889).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 804709) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804709) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 2375);
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 5, 6, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804709) {
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
				int[] ettins = { 235892, 235893 };
				int[] lizards = { 235888, 235889 };
				int targetId = env.getTargetId();
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 235888:
					case 235889: {
						if (var1 < 4) {
							return defaultOnKillEvent(env, lizards, 0, 4, 1);
						}
						else if (var1 == 4) {
							if (var2 == 5) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, lizards, 4, 5, 1);
							}
						}
						break;
					}
					case 235892:
					case 235893: {
						if (var2 < 4) {
							return defaultOnKillEvent(env, ettins, 0, 4, 2);
						}
						else if (var2 == 4) {
							if (var1 == 5) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, ettins, 4, 5, 2);
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
}