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
public class _15509Protect_The_Nephilim_Graveyard extends QuestHandler {

    private final static int questId = 15509;
    public _15509Protect_The_Nephilim_Graveyard() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806092).addOnQuestStart(questId);
        qe.registerQuestNpc(806092).addOnTalkEvent(questId);
		qe.registerQuestNpc(241695).addOnKillEvent(questId);
		qe.registerQuestNpc(241696).addOnKillEvent(questId);
		qe.registerQuestNpc(241697).addOnKillEvent(questId);
		qe.registerQuestNpc(241698).addOnKillEvent(questId);
		qe.registerQuestNpc(241699).addOnKillEvent(questId);
		qe.registerQuestNpc(241700).addOnKillEvent(questId);
		qe.registerQuestNpc(241701).addOnKillEvent(questId);
		qe.registerQuestNpc(241702).addOnKillEvent(questId);
		qe.registerQuestNpc(241703).addOnKillEvent(questId);
		qe.registerQuestNpc(242607).addOnKillEvent(questId);
		qe.registerQuestNpc(242611).addOnKillEvent(questId);
		qe.registerQuestNpc(242615).addOnKillEvent(questId);
		qe.registerQuestNpc(242619).addOnKillEvent(questId);
		qe.registerQuestNpc(242623).addOnKillEvent(questId);
		qe.registerQuestNpc(242627).addOnKillEvent(questId);
		qe.registerQuestNpc(242631).addOnKillEvent(questId);
		qe.registerQuestNpc(242635).addOnKillEvent(questId);
		qe.registerQuestNpc(242639).addOnKillEvent(questId);
		qe.registerQuestNpc(242643).addOnKillEvent(questId);
		qe.registerQuestNpc(242647).addOnKillEvent(questId);
		qe.registerQuestNpc(242651).addOnKillEvent(questId);
		qe.registerQuestNpc(242655).addOnKillEvent(questId);
		qe.registerQuestNpc(242659).addOnKillEvent(questId);
		qe.registerQuestNpc(242663).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806092) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806092) {
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
				case 241695:
				case 241696:
				case 241697:
				case 241698:
				case 241699:
				case 241700:
				case 241701:
				case 241702:
				case 241703:
				case 242607:
				case 242611:
				case 242615:
				case 242619:
				case 242623:
				case 242627:
			    case 242631:
				case 242635:
				case 242639:
				case 242643:
				case 242647:
				case 242651:
				case 242655:
				case 242659:
				case 242663:
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