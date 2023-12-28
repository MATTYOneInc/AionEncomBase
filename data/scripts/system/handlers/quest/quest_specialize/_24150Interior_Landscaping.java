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
public class _24150Interior_Landscaping extends QuestHandler {

    private final static int questId = 24150;
    public _24150Interior_Landscaping() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(204702).addOnQuestStart(questId); //Nerita
        qe.registerQuestNpc(204702).addOnTalkEvent(questId); //Nerita
        qe.registerQuestNpc(204733).addOnTalkEvent(questId); //Bestla
        qe.registerQuestNpc(204734).addOnTalkEvent(questId); //Horu
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204702) { //Nerita
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
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
            	case 204733: { //Bestla
            		switch (env.getDialog()) {
            			case START_DIALOG: {
           					return sendQuestDialog(env, 1352);
            			} case STEP_TO_1: {
            				giveQuestItem(env, 182215460, 1);
            				qs.setQuestVar(1);
            				updateQuestStatus(env);
            				return closeDialogWindow(env);
            			}
            		}
            	} case 204734: { //Horu
            		switch (env.getDialog()) {
        				case START_DIALOG: {
        					return sendQuestDialog(env, 1693);
        				} case STEP_TO_2: {
        					qs.setQuestVar(2);
                            updateQuestStatus(env);
        					return closeDialogWindow(env);
        				}
        			}
            	} case 204702: { //Nerita
            		switch (env.getDialog()) {
    					case START_DIALOG: {
    						return sendQuestDialog(env, 2375);
    					} case SELECT_REWARD: {
    						qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
    						return sendQuestDialog(env, 5);
    					}
            		}
            	}
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204702) { //Nerita
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