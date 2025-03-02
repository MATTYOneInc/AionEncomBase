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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24154Better_Than_Last_Time extends QuestHandler {

    private final static int questId = 24154;
    public _24154Better_Than_Last_Time() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerOnDie(questId);
        qe.registerOnLogOut(questId);
        qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215463, questId);
        qe.registerOnMovieEndQuest(249, questId);
        qe.registerOnMovieEndQuest(250, questId);
    	qe.registerQuestNpc(204774).addOnQuestStart(questId); //Tristran
        qe.registerQuestNpc(204774).addOnTalkEvent(questId); //Tristran
        qe.registerQuestNpc(204809).addOnTalkEvent(questId); //Stua
        qe.registerQuestNpc(700349).addOnKillEvent(questId); //Research Center Power Generator
		qe.registerQuestNpc(700359).addOnTalkEvent(questId); //Secret Port Entrance
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204774) { //Tristran
            	switch (env.getDialog()) {
        			case START_DIALOG: {
        				return sendQuestDialog(env, 4762);
        			} case ASK_ACCEPTION: {
        				return sendQuestDialog(env, 4);
        			} case ACCEPT_QUEST: {
        				playQuestMovie(env, 249);
        				return sendQuestStartDialog(env);
        			} case REFUSE_QUEST: {
        				return sendQuestDialog(env, 1004);
        			}
            	}
            }
        }
        if (qs == null) {
		    return false;
		} 
        else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
            	case 204809: { //Stua
            		switch (env.getDialog()) {
            			case START_DIALOG: {
            				return sendQuestDialog(env, 1352);
            			 }   
                         case SELECT_ACTION_1353: {
                            giveQuestItem(env, 182215463, 1);
            				giveQuestItem(env, 185000006, 1);  
            				return sendQuestDialog(env, 1353);
                         } 
                         case STEP_TO_2: {
            				qs.setQuestVar(2);
            				updateQuestStatus(env);
            				SkillEngine.getInstance().applyEffectDirectly(267, player, player, (350 * 1000));
            				return closeDialogWindow(env);
            			}
            		}
            	} case 700359: { //Secret Port Entrance
                    int var = qs.getQuestVarById(0); 
            		if (env.getDialog() == QuestDialog.USE_OBJECT && var == 2) {
            			return playQuestMovie(env, 250);
            		}
            	}
            }
        } 
        else if (qs.getStatus() == QuestStatus.REWARD) {
        	if (targetId == 204774) { // Tristran
    			if (env.getDialog() == QuestDialog.USE_OBJECT) {
    				return sendQuestDialog(env, 10002);
    			} else {
    				return sendQuestEndDialog(env);
    			}
        	}
        }
        return false;
    }
	
    @Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
       	if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (movieId == 249) {
               changeQuestStep(env, 0, 1, false);
            }
    		if (movieId == 250) {
    			TeleportService2.teleportTo(player, 220040000, 2452f, 2471f, 673f, (byte) 28);
                changeQuestStep(env, 2, 3, false);
                return true;
    		}
        }
		return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        return defaultOnKillEvent(env, 700349, 3, 4); //Research Center Power Generator
    }
	
    @Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        if (item.getItemId() != 182215463) {
            return HandlerResult.UNKNOWN;
        } if (player.isInsideZone(ZoneName.get("DF3_ITEMUSEAREA_Q2058"))) {
              player.getEffectController().removeEffect(267); 
            return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true, 251));
        }
        return HandlerResult.FAILED;
    }
    
    @Override
    public boolean onDieEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                qs.setQuestVar(1);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }
	
    @Override
    public boolean onLogOutEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                qs.setQuestVar(1);
                updateQuestStatus(env);
            }
        }
        return false;
    }
	
    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (player.getWorldId() != 320110000) {
                int var = qs.getQuestVarById(0);
                if (var == 3) {
                    qs.setQuestVar(1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1,DataManager.QUEST_DATA.getQuestById(questId).getName()));
                    return true;
                }
            }
        }
        return false;
    }
}