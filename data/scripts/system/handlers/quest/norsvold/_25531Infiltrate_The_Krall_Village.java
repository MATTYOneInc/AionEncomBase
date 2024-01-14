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
package quest.norsvold;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _25531Infiltrate_The_Krall_Village extends QuestHandler {

    private final static int questId = 25531;
    public _25531Infiltrate_The_Krall_Village() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806111).addOnQuestStart(questId);
        qe.registerQuestNpc(806111).addOnTalkEvent(questId);
		qe.registerQuestNpc(241254).addOnKillEvent(questId);
		qe.registerQuestNpc(241255).addOnKillEvent(questId);
		qe.registerQuestNpc(241764).addOnKillEvent(questId);
		qe.registerQuestNpc(241765).addOnKillEvent(questId);
		qe.registerQuestNpc(241766).addOnKillEvent(questId);
		qe.registerQuestNpc(241800).addOnKillEvent(questId);
		qe.registerQuestNpc(241801).addOnKillEvent(questId);
		qe.registerQuestNpc(241802).addOnKillEvent(questId);
		qe.registerQuestNpc(243127).addOnKillEvent(questId);
		qe.registerQuestNpc(243131).addOnKillEvent(questId);
		qe.registerQuestNpc(243135).addOnKillEvent(questId);
		qe.registerQuestNpc(243139).addOnKillEvent(questId);
		qe.registerQuestNpc(243143).addOnKillEvent(questId);
		qe.registerQuestNpc(243147).addOnKillEvent(questId);
		qe.registerQuestNpc(243151).addOnKillEvent(questId);
		qe.registerQuestNpc(243155).addOnKillEvent(questId);
		qe.registerQuestNpc(243159).addOnKillEvent(questId);
		qe.registerQuestNpc(243163).addOnKillEvent(questId);
		qe.registerQuestNpc(243167).addOnKillEvent(questId);
		qe.registerQuestNpc(243171).addOnKillEvent(questId);
		qe.registerQuestNpc(243175).addOnKillEvent(questId);
		qe.registerQuestNpc(243179).addOnKillEvent(questId);
		qe.registerQuestNpc(243183).addOnKillEvent(questId);
		qe.registerQuestNpc(243187).addOnKillEvent(questId);
		qe.registerQuestNpc(243191).addOnKillEvent(questId);
		qe.registerQuestNpc(243195).addOnKillEvent(questId);
		qe.registerQuestNpc(243199).addOnKillEvent(questId);
		qe.registerQuestNpc(243203).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806111) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806111) {
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
				case 241254:
				case 241255:
				case 241764:
				case 241765:
				case 241766:
				case 241800:
				case 241801:
				case 241802:
				case 243127:
				case 243131:
				case 243135:
				case 243139:
				case 243143:
				case 243147:
				case 243151:
				case 243155:
				case 243159:
				case 243163:
				case 243167:
				case 243171:
				case 243175:
				case 243179:
				case 243183:
				case 243187:
				case 243191:
				case 243195:
				case 243199:
				case 243203:
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