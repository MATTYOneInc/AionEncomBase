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
public class _24153The_Efficacy_Of_Fire extends QuestHandler {

    private final static int questId = 24153;
    private final static int[] mob_ids = {213730, 213788, 213789, 213790, 213791};
	
    public _24153The_Efficacy_Of_Fire() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(204787).addOnQuestStart(questId); //Delris
        qe.registerQuestNpc(204787).addOnTalkEvent(questId); //Chieftain Akagitan
        qe.registerQuestNpc(204784).addOnTalkEvent(questId); //Delris
        qe.registerQuestItem(182215462, questId);
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
            if (targetId == 204787) { //Chieftain Akagitan
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
        		case 204784: { //Delris
        			switch (env.getDialog()) {
        				case START_DIALOG: {
        					return sendQuestDialog(env, 1352);
        				} case STEP_TO_2: {
        					giveQuestItem(env, 182215462, 1); 
        					qs.setQuestVar(0);
        					updateQuestStatus(env);
        					return closeDialogWindow(env);
        				}
        			}
        		} case 204787: { //Chieftain Akagitan
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
            if (targetId == 204787) { //Chieftain Akagitan
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
		int targetId = env.getTargetId();
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
        int var3 = qs.getQuestVarById(3);
        int var4 = qs.getQuestVarById(4);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (targetId == 213730 && var == 0 && var < 1) { //Glaciont The Hardy
            qs.setQuestVarById(0, 1);
            updateQuestStatus(env);
        } else if (targetId == 213788 && var1 == 0 && var1 < 1) { //Frostfist
            qs.setQuestVarById(1, 1);
            updateQuestStatus(env);
        } else if (targetId == 213789 && var2 == 0 && var2 < 1) { //Iceback
            qs.setQuestVarById(2, 1);
            updateQuestStatus(env);
        } else if (targetId == 213790 && var3 == 0 && var3 < 1) { //Chillblow
            qs.setQuestVarById(3, 1);
            updateQuestStatus(env);
        } else if (targetId == 213791 && var4 == 0 && var4 < 1) { //Snowfury
            qs.setQuestVarById(4, 1);
            updateQuestStatus(env);
        }
        return false;
    }
}