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
package quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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
/**
 * @author pralinka
 */
public class _25023Sprouting_Developments extends QuestHandler {

	private static final int questId = 25023;
	public _25023Sprouting_Developments() {
		super(questId);
	}
	
	@Override
	public void register() {
        qe.registerQuestItem(182215711, questId);
		qe.registerQuestNpc(804909).addOnQuestStart(questId);
		qe.registerQuestNpc(804909).addOnTalkEvent(questId);
        qe.registerQuestNpc(804910).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804909) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 804909) {
				switch (dialog) {
					case START_DIALOG: {
						if (var == 0)
							return sendQuestDialog(env, 1011);
						if (var == 1)
							return sendQuestDialog(env, 1352);
					}
					case CHECK_COLLECTED_ITEMS:
						if (QuestService.collectItemCheck(env, true)) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10000);
						}
						else {
							return sendQuestDialog(env, 10001);
						}
					case STEP_TO_2:
						if (var == 1) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							giveQuestItem(env, 182215711, 1);
							return closeDialogWindow(env);
						}
						return false;
					default:
						break;
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804910) {
				return sendQuestEndDialog(env);
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
        if (player.isInsideZone(ZoneName.get("DF5_ITEMUSEAREA_Q25023"))) {
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				QuestService.addNewSpawn(220080000, player.getInstanceId(), 805161, player.getX() + 2, player.getY() + 2, player.getZ() + 1, (byte) 0);
				removeQuestItem(env, 182215711, 1);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			    }
		    }, 3000);
            }
        }
		return HandlerResult.SUCCESS;
	}
}