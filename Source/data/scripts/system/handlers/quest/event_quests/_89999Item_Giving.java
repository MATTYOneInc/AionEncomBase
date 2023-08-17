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
package quest.event_quests;

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _89999Item_Giving extends QuestHandler
{
	private final static int questId = 89999;
	
	public _89999Item_Giving() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(799702).addOnTalkEvent(questId); //Laylin.
		qe.registerQuestNpc(799703).addOnTalkEvent(questId); //Ronya.
		qe.registerQuestNpc(798414).addOnTalkEvent(questId); //Brios.
		qe.registerQuestNpc(798416).addOnTalkEvent(questId); //Bothen.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		int itemId = 0;
		int targetId = env.getVisibleObject().getObjectId();
		Player player = env.getPlayer();
		if (env.getTargetId() == 799703 || env.getTargetId() == 799702) {
			itemId = EventsConfig.EVENT_GIVE_JUICE;
		} else if (env.getTargetId() == 798416 || env.getTargetId() == 798414) {
			itemId = EventsConfig.EVENT_GIVE_CAKE;
		} if (itemId == 0) {
			return false;
		} switch (env.getDialog()) {
			case USE_OBJECT: {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1011, 0));
				return true;
			} case SELECT_ACTION_1012: {
				Storage inventory = player.getInventory();
				if (inventory.getItemCountByItemId(itemId) > 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1097, 0));
					return true;
				} else {
					if (giveQuestItem(env, itemId, 1)) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1012, 0));
					}
					return true;
				}
			}
		}
		return false;
	}
}