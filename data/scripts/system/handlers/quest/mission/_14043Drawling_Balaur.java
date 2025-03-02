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
package quest.mission;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14043Drawling_Balaur extends QuestHandler {

    private final static int questId = 14043;
    public _14043Drawling_Balaur() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        qe.registerQuestItem(182215351, questId);
        qe.registerQuestNpc(278532).addOnTalkEvent(questId);
        qe.registerQuestNpc(798026).addOnTalkEvent(questId);
        qe.registerQuestNpc(798025).addOnTalkEvent(questId);
        qe.registerQuestNpc(279019).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14040, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278532) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else if (env.getDialogId() == 1009) {
                    return sendQuestDialog(env, 5);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
            return false;
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (targetId == 278532) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 0) {
                        return sendQuestDialog(env, 1011);
                    }
                case STEP_TO_1:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                    return false;
            }
        } else if (targetId == 798026) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 1) {
                        return sendQuestDialog(env, 1352);
                    } else if (var == 4) {
                        return sendQuestDialog(env, 2375);
                    } else if (var == 6 || var == 8) {
                        return sendQuestDialog(env, 3057);
                    }
                case STEP_TO_5:
                    if (var == 4) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        removeQuestItem(env, 182215352, 1);
                        giveQuestItem(env, 182215351, 1);
						return closeDialogWindow(env);
                    }
                    return false;
                case STEP_TO_7:
                   if (var == 6 || var == 8) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                    return false;
                case STEP_TO_2:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                case STEP_TO_11:
                    if (var == 1 && player.getInventory().tryDecreaseKinah(20000)) {
						giveQuestItem(env, 182215351, 1);
                        qs.setQuestVarById(0, var + 6);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    } else {
                        return sendQuestDialog(env, 1355);
                    }
                case STEP_TO_12:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                    return false;
            }
        } else if (targetId == 798025) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 2) {
                        return sendQuestDialog(env, 1693);
                    }
                case STEP_TO_3:
                    if (var == 2) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                    return false;
            }
        } else if (targetId == 279019) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 3) {
                        return sendQuestDialog(env, 2034);
                    }
                    return false;
                case STEP_TO_4:
                    if (var == 3) {
                        giveQuestItem(env, 182215352, 1);
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
						return closeDialogWindow(env);
                    }
                    return false;
            }
        }
        return false;
    }
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                removeQuestItem(env, 182215351, 1);
                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
				updateQuestStatus(env);
			    }
		    }, 3000);
        } 
        return HandlerResult.SUCCESS;
    }
}