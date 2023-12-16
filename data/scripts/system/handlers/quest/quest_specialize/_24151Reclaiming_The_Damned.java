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
package quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24151Reclaiming_The_Damned extends QuestHandler {

    private final static int questId = 24151;
    private final static int[] mob_ids = {213044, 213045, 214092, 214093};
	
    public _24151Reclaiming_The_Damned() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(204715).addOnQuestStart(questId); //Grundt
        qe.registerQuestNpc(204715).addOnTalkEvent(questId); //Grundt
        qe.registerQuestNpc(204801).addOnTalkEvent(questId); //Gigrite
        for (int mob_id: mob_ids) {
            qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
        }
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204715) { //Grundt
            	switch (env.getDialog()) {
        			case START_DIALOG: {
        				return sendQuestDialog(env, 1011);
        			} case ASK_ACCEPTION: {
        				return sendQuestDialog(env, 4);
        			} case ACCEPT_QUEST: {
        				QuestService.startQuest(env);
                        qs.setQuestVarById(5, 1);
                        updateQuestStatus(env);
                        return closeDialogWindow(env);
        			} case REFUSE_QUEST: {
        				return sendQuestDialog(env, 1004);
        			}
            	}
            }
        } else if (qs.getStatus() == QuestStatus.START) {
        	int var = qs.getQuestVarById(0);
            if (targetId == 204801) { //Gigrite
                switch (env.getDialog()) {
                    case START_DIALOG: {
                    	if (var == 5) {
                    		return sendQuestDialog(env, 2375);
                    	}
                    	return sendQuestDialog(env, 1352);
                    } case STEP_TO_1: {
                        qs.setQuestVar(0);
                        updateQuestStatus(env);
                        return closeDialogWindow(env);
                    } case SELECT_REWARD: {
                    	qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
						return sendQuestDialog(env, 5);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204801) { //Gigrite
            	if (env.getDialog() == QuestDialog.SELECT_REWARD) {
            		return sendQuestDialog(env, 5);
            	} else {
            		return sendQuestEndDialog(env);
            	}
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (var < 5) {
            return defaultOnKillEvent(env, mob_ids, var, var + 1);
        }
        return false;
    }
}