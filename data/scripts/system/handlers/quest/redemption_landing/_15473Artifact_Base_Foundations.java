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
package quest.redemption_landing;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15473Artifact_Base_Foundations extends QuestHandler
{
    private final static int questId = 15473;
	
    public _15473Artifact_Base_Foundations() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(805798).addOnQuestStart(questId);
        qe.registerQuestNpc(805798).addOnTalkEvent(questId);
		qe.registerQuestNpc(883226).addOnKillEvent(questId);
		qe.registerQuestNpc(883238).addOnKillEvent(questId);
		qe.registerQuestNpc(883250).addOnKillEvent(questId);
		qe.registerQuestNpc(883262).addOnKillEvent(questId);
		qe.registerQuestNpc(883052).addOnKillEvent(questId);
		qe.registerQuestNpc(883058).addOnKillEvent(questId);
		qe.registerQuestNpc(883064).addOnKillEvent(questId);
		qe.registerQuestNpc(883070).addOnKillEvent(questId);
		qe.registerQuestNpc(883228).addOnKillEvent(questId);
		qe.registerQuestNpc(883240).addOnKillEvent(questId);
		qe.registerQuestNpc(883252).addOnKillEvent(questId);
		qe.registerQuestNpc(883264).addOnKillEvent(questId);
		qe.registerQuestNpc(883054).addOnKillEvent(questId);
		qe.registerQuestNpc(883060).addOnKillEvent(questId);
		qe.registerQuestNpc(883066).addOnKillEvent(questId);
		qe.registerQuestNpc(883072).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 805798) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 805798) {
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
			if (targetId == 805798) {
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
            switch (env.getTargetId()) {
				case 883226:
				case 883238:
				case 883250:
				case 883262:
				case 883052:
				case 883058:
				case 883064:
				case 883070:
				case 883228:
				case 883240:
				case 883252:
				case 883264:
				case 883054:
				case 883060:
				case 883066:
				case 883072:
                if (qs.getQuestVarById(1) < 1) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 1) {
					qs.setQuestVarById(0, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}