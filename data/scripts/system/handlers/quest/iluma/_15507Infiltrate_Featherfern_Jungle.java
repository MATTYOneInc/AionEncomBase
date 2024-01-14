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
public class _15507Infiltrate_Featherfern_Jungle extends QuestHandler {

    private final static int questId = 15507;
    public _15507Infiltrate_Featherfern_Jungle() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806091).addOnQuestStart(questId);
        qe.registerQuestNpc(806091).addOnTalkEvent(questId);
		qe.registerQuestNpc(240395).addOnKillEvent(questId);
		qe.registerQuestNpc(240396).addOnKillEvent(questId);
		qe.registerQuestNpc(240397).addOnKillEvent(questId);
		qe.registerQuestNpc(240398).addOnKillEvent(questId);
		qe.registerQuestNpc(240399).addOnKillEvent(questId);
		qe.registerQuestNpc(240400).addOnKillEvent(questId);
        qe.registerQuestNpc(241827).addOnKillEvent(questId);
		qe.registerQuestNpc(241831).addOnKillEvent(questId);
		qe.registerQuestNpc(241835).addOnKillEvent(questId);
		qe.registerQuestNpc(241839).addOnKillEvent(questId);
		qe.registerQuestNpc(241843).addOnKillEvent(questId);
		qe.registerQuestNpc(241847).addOnKillEvent(questId);
		qe.registerQuestNpc(241851).addOnKillEvent(questId);
		qe.registerQuestNpc(241855).addOnKillEvent(questId);
		qe.registerQuestNpc(241859).addOnKillEvent(questId);
		qe.registerQuestNpc(241863).addOnKillEvent(questId);
		qe.registerQuestNpc(241883).addOnKillEvent(questId);
		qe.registerQuestNpc(243266).addOnKillEvent(questId);
		qe.registerQuestNpc(243267).addOnKillEvent(questId);
		qe.registerQuestNpc(243268).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806091) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806091) {
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
				case 240395:
				case 240396:
				case 240397:
				case 240398:
				case 240399:
				case 240400:
        		case 241827:
				case 241831:
				case 241835:
				case 241839:
				case 241843:
				case 241847:
				case 241851:
				case 241855:
				case 241859:
				case 241863:
				case 241883:
				case 243266:
				case 243267:
				case 243268:
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