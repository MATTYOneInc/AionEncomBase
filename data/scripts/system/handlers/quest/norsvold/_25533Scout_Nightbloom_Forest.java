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
public class _25533Scout_Nightbloom_Forest extends QuestHandler {

    private final static int questId = 25533;
    public _25533Scout_Nightbloom_Forest() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806112).addOnQuestStart(questId);
        qe.registerQuestNpc(806112).addOnTalkEvent(questId);
		qe.registerQuestNpc(241646).addOnKillEvent(questId);
		qe.registerQuestNpc(241647).addOnKillEvent(questId);
		qe.registerQuestNpc(241648).addOnKillEvent(questId);
		qe.registerQuestNpc(241649).addOnKillEvent(questId);
		qe.registerQuestNpc(241650).addOnKillEvent(questId);
		qe.registerQuestNpc(241651).addOnKillEvent(questId);
		qe.registerQuestNpc(242487).addOnKillEvent(questId);
		qe.registerQuestNpc(242491).addOnKillEvent(questId);
		qe.registerQuestNpc(242495).addOnKillEvent(questId);
		qe.registerQuestNpc(242499).addOnKillEvent(questId);
		qe.registerQuestNpc(242503).addOnKillEvent(questId);
		qe.registerQuestNpc(242507).addOnKillEvent(questId);
		qe.registerQuestNpc(242511).addOnKillEvent(questId);
		qe.registerQuestNpc(242515).addOnKillEvent(questId);
		qe.registerQuestNpc(242519).addOnKillEvent(questId);
		qe.registerQuestNpc(242523).addOnKillEvent(questId);
		qe.registerQuestNpc(242527).addOnKillEvent(questId);
		qe.registerQuestNpc(242531).addOnKillEvent(questId);
		qe.registerQuestNpc(242535).addOnKillEvent(questId);
		qe.registerQuestNpc(242539).addOnKillEvent(questId);
		qe.registerQuestNpc(242543).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806112) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806112) {
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
				case 241646:
				case 241647:
				case 241648:
				case 241649:
				case 241650:
				case 241651:
				case 242487:
				case 242491:
				case 242495:
				case 242499:
				case 242503:
				case 242507:
				case 242511:
				case 242515:
				case 242519:
				case 242523:
				case 242527:
				case 242531:
				case 242535:
				case 242539:
				case 242543:
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