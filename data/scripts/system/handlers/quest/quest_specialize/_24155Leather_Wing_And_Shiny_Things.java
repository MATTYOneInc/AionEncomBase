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
public class _24155Leather_Wing_And_Shiny_Things extends QuestHandler {

    private final static int questId = 24155;
	
    public _24155Leather_Wing_And_Shiny_Things() {
        super(questId);
    }
	
    @Override
    public void register() {
    	qe.registerQuestItem(182204318, questId);
		qe.registerQuestNpc(204701).addOnQuestStart(questId); //Hod
    	qe.registerQuestNpc(204701).addOnTalkEvent(questId); //Hod
    	qe.registerQuestNpc(204785).addOnTalkEvent(questId); //Gwendolin
        qe.registerQuestNpc(700290).addOnKillEvent(questId); //Field Suppressor
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204701) { //Hod
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
        	switch (targetId) {
                case 204785: { //Gwendolin
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            return sendQuestDialog(env, 1352);
                        } case STEP_TO_2: {
                        	qs.setQuestVar(0);
        					updateQuestStatus(env);
                            return closeDialogWindow(env);
                        }
                    }
                } case 204701: { //Hod
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                        	return sendQuestDialog(env, 2375);
                        } case SELECT_REWARD: {
                        	qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
    						return sendQuestDialog(env, 5);
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204701) { //Hod
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
        } if (var < 3) {
            return defaultOnKillEvent(env, 700290, var, var + 1);
        }
        return false;
    }
}