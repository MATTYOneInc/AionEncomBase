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
public class _15501Expedition_To_Asmodae extends QuestHandler {

    private final static int questId = 15501;
    public _15501Expedition_To_Asmodae() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806089).addOnQuestStart(questId);
        qe.registerQuestNpc(806089).addOnTalkEvent(questId);
		qe.registerQuestNpc(240369).addOnKillEvent(questId);
		qe.registerQuestNpc(240370).addOnKillEvent(questId);
		qe.registerQuestNpc(240371).addOnKillEvent(questId);
		qe.registerQuestNpc(240372).addOnKillEvent(questId);
		qe.registerQuestNpc(240373).addOnKillEvent(questId);
		qe.registerQuestNpc(240374).addOnKillEvent(questId);
		qe.registerQuestNpc(240375).addOnKillEvent(questId);
		qe.registerQuestNpc(240376).addOnKillEvent(questId);
		qe.registerQuestNpc(240377).addOnKillEvent(questId);
		qe.registerQuestNpc(240378).addOnKillEvent(questId);
		qe.registerQuestNpc(241177).addOnKillEvent(questId);
		qe.registerQuestNpc(241178).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806089) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806089) {
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
                case 240369:
				case 240370:
				case 240371:
				case 240372:
				case 240373:
				case 240374:
				case 240375:
				case 240376:
				case 240377:
				case 240378:
				case 241177:
				case 241178:
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