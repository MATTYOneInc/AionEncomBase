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

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24152Through_The_Looking_Glass extends QuestHandler {

    private final static int questId = 24152;
    public _24152Through_The_Looking_Glass() {
        super(questId);
    }
		
    @Override
    public void register() {
        qe.registerQuestNpc(204768).addOnQuestStart(questId); //Sleipnir
        qe.registerQuestNpc(204768).addOnTalkEvent(questId); //Sleipnir
        qe.registerQuestNpc(204739).addOnTalkEvent(questId); //Baugi
        qe.registerQuestNpc(802364).addOnTalkEvent(questId); //Dojer
        qe.addHandlerSideQuestDrop(questId, 213739, 182215461, 1, 100);
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204768) { //Sleipnir
            	switch (env.getDialog()) {
        			case START_DIALOG: {
        				return sendQuestDialog(env, 1011);
        			} case ASK_ACCEPTION: {
        				return sendQuestDialog(env, 4);
        			} case ACCEPT_QUEST: {
        				return sendQuestStartDialog(env);
        			} case REFUSE_QUEST: {
        				return sendQuestDialog(env, 1004);
        			}
				}
        	}
        } else if (qs == null || qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
            	case 204739: {	//Baugi
            		switch (env.getDialog()) {
            			case START_DIALOG: {
            				return sendQuestDialog(env, 1352);
            			} case STEP_TO_1: {
            				qs.setQuestVar(1);
            				updateQuestStatus(env);
            				return closeDialogWindow(env);
            			}
            		}
            	} case 802364: { //Dojer
            		switch (env.getDialog()) {
        				case START_DIALOG: {
        					return sendQuestDialog(env, 2375);
        				} case CHECK_COLLECTED_ITEMS_SIMPLE: {
        					if (player.getInventory().getItemCountByItemId(182215461) == 1) {
        						qs.setQuestVar(2);
        						qs.setStatus(QuestStatus.REWARD);
        						updateQuestStatus(env);
        						removeQuestItem(env, 182215461, 1);
        						return sendQuestDialog(env, 5);
        					} else {
        						return closeDialogWindow(env);
        					}
        				}
            		}
            	}
            }
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 802364) { //Dojer
            	if (env.getDialog() == QuestDialog.SELECT_REWARD) {
            		return sendQuestDialog(env, 5);
            	} else {
            		return sendQuestEndDialog(env);
            	}
            }
        }
        return false;
    }
}