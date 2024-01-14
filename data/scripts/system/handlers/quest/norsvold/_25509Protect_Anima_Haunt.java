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
public class _25509Protect_Anima_Haunt extends QuestHandler {

    private final static int questId = 25509;
    public _25509Protect_Anima_Haunt() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806104).addOnQuestStart(questId);
        qe.registerQuestNpc(806104).addOnTalkEvent(questId);
		qe.registerQuestNpc(241535).addOnKillEvent(questId);
		qe.registerQuestNpc(241536).addOnKillEvent(questId);
		qe.registerQuestNpc(241537).addOnKillEvent(questId);
		qe.registerQuestNpc(241538).addOnKillEvent(questId);
		qe.registerQuestNpc(241539).addOnKillEvent(questId);
		qe.registerQuestNpc(241540).addOnKillEvent(questId);
		qe.registerQuestNpc(241541).addOnKillEvent(questId);
		qe.registerQuestNpc(241542).addOnKillEvent(questId);
		qe.registerQuestNpc(241543).addOnKillEvent(questId);
		qe.registerQuestNpc(241887).addOnKillEvent(questId);
		qe.registerQuestNpc(241891).addOnKillEvent(questId);
		qe.registerQuestNpc(241895).addOnKillEvent(questId);
		qe.registerQuestNpc(241899).addOnKillEvent(questId);
		qe.registerQuestNpc(241903).addOnKillEvent(questId);
		qe.registerQuestNpc(241907).addOnKillEvent(questId);
		qe.registerQuestNpc(241911).addOnKillEvent(questId);
		qe.registerQuestNpc(241915).addOnKillEvent(questId);
		qe.registerQuestNpc(241919).addOnKillEvent(questId);
		qe.registerQuestNpc(241923).addOnKillEvent(questId);
		qe.registerQuestNpc(241927).addOnKillEvent(questId);
		qe.registerQuestNpc(241931).addOnKillEvent(questId);
		qe.registerQuestNpc(241935).addOnKillEvent(questId);
		qe.registerQuestNpc(241939).addOnKillEvent(questId);
		qe.registerQuestNpc(241943).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806104) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806104) {
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
				case 241535:
				case 241536:
				case 241537:
				case 241538:
				case 241539:
				case 241540:
				case 241541:
				case 241542:
				case 241543:
				case 241887:
				case 241891:
				case 241895:
				case 241899:
				case 241903:
				case 241907:
				case 241911:
				case 241915:
				case 241919:
				case 241923:
				case 241927:
				case 241931:
				case 241935:
				case 241939:
				case 241943:
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