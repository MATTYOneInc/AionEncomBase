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
package quest.brusthonin;

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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4082Gathering_The_Herb_Pouches extends QuestHandler
{
	private final static int questId = 4082;
	
	public _4082Gathering_The_Herb_Pouches() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(205190).addOnQuestStart(questId);
		qe.registerQuestNpc(205190).addOnTalkEvent(questId);
		qe.registerQuestNpc(700430).addOnTalkEvent(questId);
		qe.registerQuestNpc(700431).addOnTalkEvent(questId);
		qe.registerQuestNpc(700432).addOnTalkEvent(questId);
		qe.registerQuestItem(182209058, questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		int targetId = 0;
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (targetId == 205190) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else if (env.getDialogId() == 1002) {
					if (giveQuestItem(env, 182209058, 1)) {
						return sendQuestStartDialog(env);
					}
					return true;
				} else {
					return sendQuestStartDialog(env);
				}
			} if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				} else if (env.getDialog() == QuestDialog.CHECK_COLLECTED_ITEMS) {
					if (QuestService.collectItemCheck(env, true)) {
						removeQuestItem(env, 182209058, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					} else {
						return sendQuestDialog(env, 2716);
					}
				} else {
					return sendQuestEndDialog(env);
				}
			} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestEndDialog(env);
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 700430:
				case 700431:
				case 700432: {
					if (qs.getQuestVarById(0) == 0 && env.getDialog() == QuestDialog.USE_OBJECT) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		if (id != 182209058 || qs == null) {
			return HandlerResult.UNKNOWN;
		} if (qs.getQuestVarById(0) != 0) {
			return HandlerResult.FAILED;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}