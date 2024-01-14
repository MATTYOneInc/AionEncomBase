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
public class _15518Protect_Ariel_Rest extends QuestHandler {

    private final static int questId = 15518;
    public _15518Protect_Ariel_Rest() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806095).addOnQuestStart(questId);
        qe.registerQuestNpc(806095).addOnTalkEvent(questId);
		qe.registerQuestNpc(241743).addOnKillEvent(questId);
		qe.registerQuestNpc(241744).addOnKillEvent(questId);
		qe.registerQuestNpc(241745).addOnKillEvent(questId);
		qe.registerQuestNpc(241746).addOnKillEvent(questId);
		qe.registerQuestNpc(241747).addOnKillEvent(questId);
		qe.registerQuestNpc(241748).addOnKillEvent(questId);
		qe.registerQuestNpc(241749).addOnKillEvent(questId);
		qe.registerQuestNpc(241750).addOnKillEvent(questId);
		qe.registerQuestNpc(241751).addOnKillEvent(questId);
		qe.registerQuestNpc(242807).addOnKillEvent(questId);
		qe.registerQuestNpc(242811).addOnKillEvent(questId);
		qe.registerQuestNpc(242815).addOnKillEvent(questId);
		qe.registerQuestNpc(242819).addOnKillEvent(questId);
		qe.registerQuestNpc(242823).addOnKillEvent(questId);
		qe.registerQuestNpc(242827).addOnKillEvent(questId);
		qe.registerQuestNpc(242831).addOnKillEvent(questId);
		qe.registerQuestNpc(242835).addOnKillEvent(questId);
		qe.registerQuestNpc(242839).addOnKillEvent(questId);
		qe.registerQuestNpc(242843).addOnKillEvent(questId);
		qe.registerQuestNpc(242847).addOnKillEvent(questId);
		qe.registerQuestNpc(242851).addOnKillEvent(questId);
		qe.registerQuestNpc(242855).addOnKillEvent(questId);
		qe.registerQuestNpc(242859).addOnKillEvent(questId);
		qe.registerQuestNpc(242863).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806095) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806095) {
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
				case 241743:
				case 241744:
				case 241745:
				case 241746:
				case 241747:
				case 241748:
				case 241749:
				case 241750:
				case 241751:
				case 242807:
				case 242811:
				case 242815:
				case 242819:
				case 242823:
				case 242827:
				case 242831:
				case 242835:
				case 242839:
				case 242843:
				case 242847:
				case 242851:
				case 242855:
				case 242859:
				case 242863:
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