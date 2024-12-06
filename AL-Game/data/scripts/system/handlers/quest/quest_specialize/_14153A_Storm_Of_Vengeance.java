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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _14153A_Storm_Of_Vengeance extends QuestHandler {

    private final static int questId = 14153;
    public _14153A_Storm_Of_Vengeance() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnMovieEndQuest(193, questId);
        qe.registerQuestItem(182215459, questId);
        qe.registerQuestNpc(204504).addOnQuestStart(questId);
        qe.registerQuestNpc(204505).addOnTalkEvent(questId);
        qe.registerQuestNpc(204533).addOnTalkEvent(questId);
        qe.registerQuestNpc(204535).addOnTalkEvent(questId);
        qe.registerQuestNpc(700282).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204504) { //Sofne
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (targetId == 204505) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 0) {
                        return sendQuestDialog(env, 1011);
                    }
                case STEP_TO_1:
                    if (var == 0) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
					default:
					break;
            }
        } else if (targetId == 204533) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 1) {
                        return sendQuestDialog(env, 1352);
                    } else if (var == 3) {
                        return sendQuestDialog(env, 2034);
                    }
                case STEP_TO_2:
                    if (var == 1) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                case STEP_TO_4:
                    if (var == 3) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
					default:
					break;
            }
        } else if (targetId == 204535) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 4) {
                        return sendQuestDialog(env, 2375);
                    }
                case STEP_TO_5:
                    if (var == 4) {
                        if (!giveQuestItem(env, 182215459, 1)) {
                            return true;
                        }
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
                    return false;
					default:
					break;
                }
            } 
            else if (targetId == 700282 && var == 2) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return playQuestMovie(env, 193);
                }
            }
        }
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204505) { //Sofne.
                    return sendQuestEndDialog(env);
                }
            }
        return false;
    }
	
    @Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        if (movieId != 193) {
            return false;
        }
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 2) {
            return false;
        }
        qs.setQuestVar(3);
        updateQuestStatus(env);
        SkillEngine.getInstance().applyEffectDirectly(10217, player, player, 15000);
        return true;
    }
	
    @Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();
        if (id != 182215459) {
            return HandlerResult.UNKNOWN;
        } if (!player.isInsideZone(ZoneName.get("LF3_ITEMUSEAREA_Q1059"))) {
            return HandlerResult.UNKNOWN;
        }
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.COMPLETE) {
            return HandlerResult.UNKNOWN;
        }
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                playQuestMovie(env, 192);
                removeQuestItem(env, 182215459, 1);
                qs.setQuestVarById(0, 5);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
        }, 3000);
        return HandlerResult.SUCCESS;
    }
}