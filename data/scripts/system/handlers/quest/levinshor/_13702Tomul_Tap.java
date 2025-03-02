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
package quest.levinshor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _13702Tomul_Tap extends QuestHandler {

    private final static int questId = 13702;
    public _13702Tomul_Tap() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(802350).addOnQuestStart(questId);
        qe.registerQuestNpc(802350).addOnTalkEvent(questId);
		qe.registerQuestNpc(802352).addOnTalkEvent(questId);
		qe.registerQuestNpc(235346).addOnKillEvent(questId);
		qe.registerQuestNpc(235347).addOnKillEvent(questId);
		qe.registerQuestNpc(235348).addOnKillEvent(questId);
		qe.registerQuestNpc(235349).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 802350) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 802352) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 4) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 4, 5, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 802352) {
				if (env.getDialogId() == 1009) {
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
                case 235346:
                case 235347:
				case 235348:
				case 235349:
                if (qs.getQuestVarById(0) < 4) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }
}