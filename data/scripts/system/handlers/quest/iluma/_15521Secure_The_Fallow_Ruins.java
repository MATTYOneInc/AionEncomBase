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
public class _15521Secure_The_Fallow_Ruins extends QuestHandler {

    private final static int questId = 15521;
    public _15521Secure_The_Fallow_Ruins() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806096).addOnQuestStart(questId);
        qe.registerQuestNpc(806096).addOnTalkEvent(questId);
		qe.registerQuestNpc(241755).addOnKillEvent(questId);
		qe.registerQuestNpc(241756).addOnKillEvent(questId);
		qe.registerQuestNpc(241757).addOnKillEvent(questId);
		qe.registerQuestNpc(241759).addOnKillEvent(questId);
		qe.registerQuestNpc(241758).addOnKillEvent(questId);
		qe.registerQuestNpc(241760).addOnKillEvent(questId);
		qe.registerQuestNpc(241761).addOnKillEvent(questId);
		qe.registerQuestNpc(241762).addOnKillEvent(questId);
		qe.registerQuestNpc(241763).addOnKillEvent(questId);
		qe.registerQuestNpc(242867).addOnKillEvent(questId);
		qe.registerQuestNpc(242871).addOnKillEvent(questId);
		qe.registerQuestNpc(242875).addOnKillEvent(questId);
		qe.registerQuestNpc(242879).addOnKillEvent(questId);
		qe.registerQuestNpc(242883).addOnKillEvent(questId);
		qe.registerQuestNpc(242887).addOnKillEvent(questId);
		qe.registerQuestNpc(242891).addOnKillEvent(questId);
		qe.registerQuestNpc(242895).addOnKillEvent(questId);
		qe.registerQuestNpc(242899).addOnKillEvent(questId);
		qe.registerQuestNpc(242903).addOnKillEvent(questId);
		qe.registerQuestNpc(242907).addOnKillEvent(questId);
		qe.registerQuestNpc(242911).addOnKillEvent(questId);
		qe.registerQuestNpc(242915).addOnKillEvent(questId);
		qe.registerQuestNpc(242919).addOnKillEvent(questId);
		qe.registerQuestNpc(242923).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806096) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806096) {
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
				case 241755:
				case 241756:
				case 241757:
				case 241759:
				case 241758:
				case 241760:
				case 241761:
				case 241762:
				case 241763:
				case 242867:
				case 242871:
				case 242875:
				case 242879:
				case 242883:
				case 242887:
				case 242891:
				case 242895:
				case 242899:
				case 242903:
				case 242907:
				case 242911:
				case 242915:
				case 242919:
				case 242923:
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