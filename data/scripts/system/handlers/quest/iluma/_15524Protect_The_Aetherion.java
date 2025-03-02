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
public class _15524Protect_The_Aetherion extends QuestHandler {

    private final static int questId = 15524;
    public _15524Protect_The_Aetherion() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806097).addOnQuestStart(questId);
        qe.registerQuestNpc(806097).addOnTalkEvent(questId);
		qe.registerQuestNpc(241767).addOnKillEvent(questId);
		qe.registerQuestNpc(241768).addOnKillEvent(questId);
		qe.registerQuestNpc(241769).addOnKillEvent(questId);
		qe.registerQuestNpc(241770).addOnKillEvent(questId);
		qe.registerQuestNpc(241771).addOnKillEvent(questId);
		qe.registerQuestNpc(241772).addOnKillEvent(questId);
		qe.registerQuestNpc(242927).addOnKillEvent(questId);
		qe.registerQuestNpc(242931).addOnKillEvent(questId);
		qe.registerQuestNpc(242935).addOnKillEvent(questId);
		qe.registerQuestNpc(242939).addOnKillEvent(questId);
		qe.registerQuestNpc(242943).addOnKillEvent(questId);
		qe.registerQuestNpc(242947).addOnKillEvent(questId);
		qe.registerQuestNpc(242951).addOnKillEvent(questId);
		qe.registerQuestNpc(242955).addOnKillEvent(questId);
		qe.registerQuestNpc(242959).addOnKillEvent(questId);
		qe.registerQuestNpc(242963).addOnKillEvent(questId);
		qe.registerQuestNpc(242967).addOnKillEvent(questId);
		qe.registerQuestNpc(242971).addOnKillEvent(questId);
		qe.registerQuestNpc(242975).addOnKillEvent(questId);
		qe.registerQuestNpc(242979).addOnKillEvent(questId);
		qe.registerQuestNpc(242983).addOnKillEvent(questId);
		qe.registerQuestNpc(242987).addOnKillEvent(questId);
		qe.registerQuestNpc(242991).addOnKillEvent(questId);
		qe.registerQuestNpc(242995).addOnKillEvent(questId);
		qe.registerQuestNpc(242999).addOnKillEvent(questId);
		qe.registerQuestNpc(243003).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806097) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806097) {
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
				case 241767:
				case 241768:
				case 241769:
				case 241770:
				case 241771:
				case 241772:
				case 242927:
				case 242931:
				case 242935:
				case 242939:
				case 242943:
				case 242947:
				case 242951:
				case 242955:
				case 242959:
				case 242963:
				case 242967:
				case 242971:
				case 242975:
				case 242979:
				case 242983:
				case 242987:
				case 242991:
				case 242995:
				case 242999:
				case 243003:
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