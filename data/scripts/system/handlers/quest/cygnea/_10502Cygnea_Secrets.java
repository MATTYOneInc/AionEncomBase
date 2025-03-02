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
package quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _10502Cygnea_Secrets extends QuestHandler {

    public static final int questId = 10502;
    public _10502Cygnea_Secrets() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {804701, 804702, 731537, 702669};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215601, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_ITEMUSEAREA_Q10502"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10501, true);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final Npc npc = (Npc) env.getVisibleObject();
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804701) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 804702) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        } else if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        } else if (var == 3) {
                            return sendQuestDialog(env, 2034);
                        } else if (var == 5) {
                            return sendQuestDialog(env, 2716);
                        }
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					} case STEP_TO_4: {
						giveQuestItem(env, 182215601, 1);
                        changeQuestStep(env, 3, 4, false);
                        return closeDialogWindow(env);
					} case STEP_TO_6: {
						removeQuestItem(env, 182215602, 1);
                        changeQuestStep(env, 5, 6, false);
                        return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 2, 3, false, 10000, 10001);
					}
                }
            } if (targetId == 702669) { //Shining Lump Of Soil.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 731537) { //Ancient Fossil.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 6) {
                            return sendQuestDialog(env, 3057);
                        }
					} case SELECT_ACTION_3058: {
						if (var == 6) {
							return sendQuestDialog(env, 3058);
						}
					} case SET_REWARD: {
						giveQuestItem(env, 182215635, 1);
						qs.setQuestVar(7);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804701) {
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
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
		if (player.isInsideZone(ZoneName.get("LF5_ITEMUSEAREA_Q10502"))) {
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
                removeQuestItem(env, 182215601, 1);
				giveQuestItem(env, 182215602, 1);
                changeQuestStep(env, 4, 5, false);
                updateQuestStatus(env);  
			    }
		    }, 3000);
            }
        }
        return HandlerResult.SUCCESS;
    }
}