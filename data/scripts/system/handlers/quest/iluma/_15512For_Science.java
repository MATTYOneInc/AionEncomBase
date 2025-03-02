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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _15512For_Science extends QuestHandler {

    private final static int questId = 15512;
    public _15512For_Science() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806093).addOnQuestStart(questId);
        qe.registerQuestNpc(806093).addOnTalkEvent(questId);
		qe.registerQuestNpc(241707).addOnKillEvent(questId);
		qe.registerQuestNpc(241708).addOnKillEvent(questId);
		qe.registerQuestNpc(241709).addOnKillEvent(questId);
		qe.registerQuestNpc(241710).addOnKillEvent(questId);
		qe.registerQuestNpc(241711).addOnKillEvent(questId);
		qe.registerQuestNpc(241712).addOnKillEvent(questId);
		qe.registerQuestNpc(241713).addOnKillEvent(questId);
		qe.registerQuestNpc(241714).addOnKillEvent(questId);
		qe.registerQuestNpc(241715).addOnKillEvent(questId);
		qe.registerQuestNpc(241716).addOnKillEvent(questId);
		qe.registerQuestNpc(241717).addOnKillEvent(questId);
		qe.registerQuestNpc(241718).addOnKillEvent(questId);
		qe.registerQuestNpc(242667).addOnKillEvent(questId);
		qe.registerQuestNpc(242671).addOnKillEvent(questId);
		qe.registerQuestNpc(242675).addOnKillEvent(questId);
		qe.registerQuestNpc(242679).addOnKillEvent(questId);
		qe.registerQuestNpc(242683).addOnKillEvent(questId);
		qe.registerQuestNpc(242687).addOnKillEvent(questId);
		qe.registerQuestNpc(242691).addOnKillEvent(questId);
		qe.registerQuestNpc(242695).addOnKillEvent(questId);
		qe.registerQuestNpc(242699).addOnKillEvent(questId);
		qe.registerQuestNpc(242703).addOnKillEvent(questId);
		qe.registerQuestNpc(242707).addOnKillEvent(questId);
		qe.registerQuestNpc(242711).addOnKillEvent(questId);
		qe.registerQuestNpc(242715).addOnKillEvent(questId);
		qe.registerQuestNpc(242719).addOnKillEvent(questId);
		qe.registerQuestNpc(242723).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806093) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806093) {
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
	
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
				case 241707:
				case 241708:
				case 241709:
				case 241710:
				case 241711:
				case 241712:
				case 241713:
				case 241714:
				case 241715:
				case 241716:
				case 241717:
				case 241718:
				case 242667:
				case 242671:
				case 242675:
				case 242679:
				case 242683:
				case 242687:
				case 242691:
				case 242695:
				case 242699:
				case 242703:
				case 242707:
				case 242711:
				case 242715:
				case 242719:
				case 242723:
                if (qs.getQuestVarById(1) < 30) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 30) {
					qs.setQuestVarById(0, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}