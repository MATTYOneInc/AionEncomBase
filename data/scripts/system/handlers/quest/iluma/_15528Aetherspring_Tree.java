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
public class _15528Aetherspring_Tree extends QuestHandler {

    private final static int questId = 15528;
    public _15528Aetherspring_Tree() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806098).addOnQuestStart(questId);
        qe.registerQuestNpc(806098).addOnTalkEvent(questId);
		qe.registerQuestNpc(241610).addOnKillEvent(questId);
		qe.registerQuestNpc(241611).addOnKillEvent(questId);
		qe.registerQuestNpc(241612).addOnKillEvent(questId);
		qe.registerQuestNpc(241613).addOnKillEvent(questId);
		qe.registerQuestNpc(241614).addOnKillEvent(questId);
		qe.registerQuestNpc(241615).addOnKillEvent(questId);
		qe.registerQuestNpc(241616).addOnKillEvent(questId);
		qe.registerQuestNpc(241617).addOnKillEvent(questId);
		qe.registerQuestNpc(241618).addOnKillEvent(questId);
		qe.registerQuestNpc(242287).addOnKillEvent(questId);
		qe.registerQuestNpc(242291).addOnKillEvent(questId);
		qe.registerQuestNpc(242295).addOnKillEvent(questId);
		qe.registerQuestNpc(242299).addOnKillEvent(questId);
		qe.registerQuestNpc(242303).addOnKillEvent(questId);
		qe.registerQuestNpc(242307).addOnKillEvent(questId);
		qe.registerQuestNpc(242311).addOnKillEvent(questId);
		qe.registerQuestNpc(242315).addOnKillEvent(questId);
		qe.registerQuestNpc(242319).addOnKillEvent(questId);
		qe.registerQuestNpc(242323).addOnKillEvent(questId);
		qe.registerQuestNpc(242327).addOnKillEvent(questId);
		qe.registerQuestNpc(242331).addOnKillEvent(questId);
		qe.registerQuestNpc(242335).addOnKillEvent(questId);
		qe.registerQuestNpc(242339).addOnKillEvent(questId);
		qe.registerQuestNpc(242343).addOnKillEvent(questId);
		qe.registerQuestNpc(243281).addOnKillEvent(questId);
		qe.registerQuestNpc(243282).addOnKillEvent(questId);
		qe.registerQuestNpc(243283).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806098) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806098) {
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
				case 241610:
				case 241611:
				case 241612:
				case 241613:
				case 241614:
				case 241615:
				case 241616:
				case 241617:
				case 241618:
				case 242287:
				case 242291:
				case 242295:
				case 242299:
				case 242303:
				case 242307:
				case 242311:
				case 242315:
				case 242319:
				case 242323:
				case 242327:
				case 242331:
				case 242335:
				case 242339:
				case 242343:
				case 243281:
				case 243282:
				case 243283:
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