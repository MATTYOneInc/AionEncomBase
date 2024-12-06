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
public class _15513Thinning_Out_The_Herd extends QuestHandler {

    private final static int questId = 15513;
    public _15513Thinning_Out_The_Herd() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806093).addOnQuestStart(questId);
        qe.registerQuestNpc(806093).addOnTalkEvent(questId);
		qe.registerQuestNpc(241547).addOnTalkEvent(questId);
		qe.registerQuestNpc(241548).addOnTalkEvent(questId);
		qe.registerQuestNpc(241549).addOnKillEvent(questId);
		qe.registerQuestNpc(241550).addOnTalkEvent(questId);
		qe.registerQuestNpc(241551).addOnTalkEvent(questId);
		qe.registerQuestNpc(241552).addOnKillEvent(questId);
		qe.registerQuestNpc(243269).addOnKillEvent(questId);
		qe.registerQuestNpc(243270).addOnKillEvent(questId);
		qe.registerQuestNpc(243271).addOnKillEvent(questId);
		qe.registerQuestNpc(241947).addOnKillEvent(questId);
		qe.registerQuestNpc(241951).addOnKillEvent(questId);
		qe.registerQuestNpc(241955).addOnKillEvent(questId);
		qe.registerQuestNpc(241959).addOnKillEvent(questId);
		qe.registerQuestNpc(241963).addOnKillEvent(questId);
		qe.registerQuestNpc(241967).addOnKillEvent(questId);
		qe.registerQuestNpc(241971).addOnKillEvent(questId);
		qe.registerQuestNpc(241975).addOnKillEvent(questId);
		qe.registerQuestNpc(241979).addOnKillEvent(questId);
		qe.registerQuestNpc(241983).addOnKillEvent(questId);
		qe.registerQuestNpc(241987).addOnKillEvent(questId);
		qe.registerQuestNpc(241991).addOnKillEvent(questId);
		qe.registerQuestNpc(241995).addOnKillEvent(questId);
		qe.registerQuestNpc(241999).addOnKillEvent(questId);
		qe.registerQuestNpc(242003).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806093) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
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
				case 241547:
				case 241548:
				case 241549:
				case 241550:
				case 241551:
				case 241552:
				case 243269:
				case 243270:
				case 243271:
				case 241947:
				case 241951:
				case 241955:
				case 241959:
				case 241963:
				case 241967:
				case 241971:
				case 241975:
				case 241979:
				case 241983:
				case 241987:
				case 241991:
				case 241995:
				case 241999:
				case 242003:
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