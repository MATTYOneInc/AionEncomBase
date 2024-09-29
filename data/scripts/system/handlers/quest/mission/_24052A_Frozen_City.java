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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24052A_Frozen_City extends QuestHandler
{
    private final static int questId = 24052;
	
    public _24052A_Frozen_City() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestItem(182215378, questId);
        qe.registerQuestItem(182215379, questId);
        qe.registerQuestItem(182215380, questId);
        qe.registerQuestNpc(204753).addOnTalkEvent(questId);
        qe.registerQuestNpc(233864).addOnKillEvent(questId); //Parasitic Ice Spirit.
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24050, false);
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
            if (targetId == 204753) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    int[] questItems = {182215378, 182215379, 182215380};
                    return sendQuestEndDialog(env, questItems);
                }
            }
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (targetId == 204753) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 0) {
                        return sendQuestDialog(env, 1011);
                    }
                case SELECT_ACTION_1012:
                    playQuestMovie(env, 242);
                break;
                case STEP_TO_1:
                    if (var == 0) {
                        giveQuestItem(env, 182215378, 1);
                        giveQuestItem(env, 182215379, 1);
                        giveQuestItem(env, 182215380, 1);
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                    }
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        return defaultOnKillEvent(env, 233864, 4, true); //Parasitic Ice Spirit.
    }
	
	@Override
    public boolean onQuestTimerEndEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 4) {
                changeQuestStep(env, var, 0, false);
                return true;
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
        if (!player.isInsideZone(ZoneName.get("DF3_ITEMUSEAREA_Q2056"))) {
            return HandlerResult.FAILED;
        }
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 2000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                if (qs.getQuestVarById(0) == 1) {
                    playQuestMovie(env, 243);
                    removeQuestItem(env, id, 1);
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                } else if (qs.getQuestVarById(0) == 2) {
                    playQuestMovie(env, 244);
                    removeQuestItem(env, id, 1);
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                } else if (qs.getQuestVarById(0) == 3 && qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
                    removeQuestItem(env, id, 1);
                    playQuestMovie(env, 245);
                    QuestService.addNewSpawn(220040000, 1, 233864, (float) 2060, (float) 116, (float) 370, (byte) 36);  //Parasitic Ice Spirit.
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                }
            }
        }, 2000);
        return HandlerResult.SUCCESS;
    }
}