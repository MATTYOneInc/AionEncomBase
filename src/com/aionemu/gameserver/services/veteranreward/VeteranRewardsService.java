/*

 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.veteranreward;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.VeteranRewardConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.VeteranRewardsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.veteranrewards.VeteranRewards;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemFactory;
import com.aionemu.gameserver.services.player.PlayerMailboxState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;

import javolution.util.FastSet;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeteranRewardsService {

	public enum RecipientType {

		ELYOS, ASMO, ALL, PLAYER;

		private boolean isAllowed(Race race) {
			switch (this) {
				case ELYOS:
					return race == Race.ELYOS;
				case ASMO:
					return race == Race.ASMODIANS;
				case ALL:
					return race == Race.ELYOS || race == Race.ASMODIANS;
				default:
					return false;
			}
		}
	}

	private static final Logger log = LoggerFactory.getLogger("VETERANREWARD_LOG");

	private Collection<VeteranRewards> veteran_rewards;

	private static final String VETERAN_REWARDS_LOOP_STATUS_BROADCAST_SCHEDULE = "0 * * ? * *";

	private VeteranRewardsService() {
		Init_VeteranRewardStatusLoop();
	}

	private void Init_VeteranRewardStatusLoop() {
		log.info("Veteran Reward System activated");

		CronService.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Init_VeteranRewards();
			}
		}, VETERAN_REWARDS_LOOP_STATUS_BROADCAST_SCHEDULE);
		log.info("Broadcasting Veteran Rewards status based on expression: " + VETERAN_REWARDS_LOOP_STATUS_BROADCAST_SCHEDULE);
	}

	private void Init_VeteranRewards() {
		if (veteran_rewards != null) {
			veteran_rewards.clear();
		}

		veteran_rewards = new FastSet<VeteranRewards>(getDAO().getVeteranReward()).shared();

		if (veteran_rewards.size() > 0) {
			if (VeteranRewardConfig.VETERANREWARDS_ENABLED_INFO_LOG) {
				log.info("Loaded " + veteran_rewards.size() + " Veteran Rewards.");
			}
			StartVeteranReward();
		}
	}

	private void StartVeteranReward() {
		if (VeteranRewardConfig.VETERANREWARDS_ENABLED_INFO_LOG) {
			log.info("Start Veteran Rewards.");
		}

		for (final VeteranRewards veteran_reward : veteran_rewards) {
			VerifyVeteranReward(veteran_reward.getId(), veteran_reward.getPlayer(), veteran_reward.getType(), veteran_reward.getItem(), veteran_reward.getCount(), veteran_reward.getKinah(), veteran_reward.getSender(), veteran_reward.getTitle(), veteran_reward.getMessage());
		}
	}

	private void VerifyVeteranReward(int id, String Player, int typeID, int itemID, int countID, int kinahID, String sender, String title, String message) {
		String recipient = null;
		recipient = Util.convertName(Player);

		int item = 0, count = 0, kinah = 0, mailtype = 0;
		String Sender = sender;
		String Title = title;
		String Message = message;

		item = itemID;
		count = countID;
		kinah = kinahID;
		mailtype = typeID;

		if (item <= 0) {
			item = 0;
		}

		if (count <= 0) {
			count = -1;
		}

		SendVeteranRewardMail(Sender, recipient, Title, Message, item, count, kinah, mailtype);

		if (item != 0) {
		} else if (kinah > 0) {
		}

		if (id > 1) {
			RecycleVeteranReward(id);
		}
	}

	public void VerifyVeteranReward(int id, String Player, int typeID, int itemID, int countID, int kinahID, String sender, String title, String message,
			RecipientType recipientType) {
		String recipient = null;

		recipient = Util.convertName(Player);

		int item = 0, count = 0, kinah = 0, mailtype = 0;
		String Sender = sender;
		String Title = title;
		String Message = message;

		item = itemID;
		count = countID;
		kinah = kinahID;
		mailtype = typeID;

		if (item <= 0) {
			item = 0;
		}

		if (count <= 0) {
			count = -1;
		}

		if (recipientType == RecipientType.PLAYER) {
			SendVeteranRewardMail(Sender, recipient, Title, Message, item, count, kinah, mailtype);
		} else {
			for (Player player : World.getInstance().getAllPlayers()) {
				if (recipientType.isAllowed(player.getCommonData().getRace())) {
					SendVeteranRewardMail(Sender, player.getName(), Title, Message, item, count, kinah, mailtype);
				}
			}
		}
	}

	private void RecycleVeteranReward(final int rewardId) {
		getDAO().delVeteranReward(rewardId);
	}

	private void SendVeteranRewardMail(String sender, String recipientName, String title, String message, int attachedItemObjId, int attachedItemCount,
			int attachedKinahCount, int mailtype) {
		if (attachedItemObjId != 0) {
			ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(attachedItemObjId);
			if (itemTemplate == null) {
				if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
					// log.error("[SenderName: " + sender + "] [RecipientName: " + recipientName + "] RETURN ITEM ID:" + itemTemplate + " ITEM COUNT "
					// + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " ITEM TEMPLATE IS MISSING ");
				}
				return;
			}
		}

		if (attachedItemCount == 0) {
			return;
		}

		if (recipientName.length() > 16) {
			if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
				// log.error("[SenderName: " + sender + "] [RecipientName: " + recipientName + "] ITEM RETURN" + attachedItemObjId + " ITEM COUNT "
				// + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " RECIPIENT NAME LENGTH > 16 ");
			}
			return;
		}

		if (sender.length() > 16) {
			if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
				// log.error("[SenderName: " + sender + "] [RecipientName: " + recipientName + "] ITEM RETURN" + attachedItemObjId + " ITEM COUNT "
				// + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " SENDER NAME LENGTH > 16 ");
			}
			return;
		}

		if (title.length() > 20) {
			title = title.substring(0, 20);
		}

		if (message.length() > 1000) {
			message = message.substring(0, 1000);
		}

		PlayerCommonData recipientCommonData = (DAOManager.getDAO(PlayerDAO.class)).loadPlayerCommonDataByName(recipientName);

		if (recipientCommonData == null) {
			if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
				log.error("[RecipientName: " + recipientName + "] NO SUCH CHARACTER NAME.");
			}
			return;
		}

		Player onlineRecipient = World.getInstance().findPlayer(recipientCommonData.getPlayerObjId());

		if (recipientCommonData.isOnline()) {
			if (!onlineRecipient.getMailbox().haveFreeSlots()) {
				if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
					// log.error("[SenderName: " + sender + "] [RecipientName: " + onlineRecipient.getName() + "] ITEM RETURN" + attachedItemObjId
					// + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " MAILBOX FULL ");
				}
				return;
			}
		} else {
			if (recipientCommonData.getMailboxLetters() >= 100) {
				if (VeteranRewardConfig.VETERANREWARDS_ENABLED_ERROR_LOG) {
					// log.error("[SenderName: " + sender + "] [RecipientName: " + recipientName + "] ITEM RETURN " + attachedItemObjId + " ITEM COUNT "
					// + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " MAILBOX FULL ");
				}
				return;
			}
			onlineRecipient = null;
		}

		Item attachedItem = null;
		int finalAttachedKinahCount = 0;
		int finalAttachedApCount = 0;
		int itemId = attachedItemObjId;
		long count = attachedItemCount;

		if (itemId != 0) {
			Item senderItem = ItemFactory.newItem(itemId, count);

			if (senderItem != null) {
				senderItem.setEquipped(false);
				senderItem.setEquipmentSlot(0);
				senderItem.setItemLocation(StorageType.MAILBOX.getId());
				attachedItem = senderItem;
			}
		}

		if (attachedKinahCount > 0) {
			finalAttachedKinahCount = attachedKinahCount;
		}		

		LetterType type;
		if (mailtype == 1) {
			type = LetterType.EXPRESS;
		} else if (mailtype == 2) {
			type = LetterType.BLACKCLOUD;
		} else {
			type = LetterType.NORMAL;
		}

		String finalSender = sender;

		Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
		Letter newLetter = new Letter(IDFactory.getInstance().nextId(), recipientCommonData.getPlayerObjId(), attachedItem, finalAttachedKinahCount, finalAttachedApCount, title, message, finalSender, time, true, type);

		if (!DAOManager.getDAO(MailDAO.class).storeLetter(time, newLetter)) {
			return;
		}

		if (attachedItem != null) {
			if (!DAOManager.getDAO(InventoryDAO.class).store(attachedItem, recipientCommonData.getPlayerObjId())) {
				return;
			}
		}

		if (onlineRecipient != null) {
			Mailbox recipientMailbox = onlineRecipient.getMailbox();
			recipientMailbox.putLetterToMailbox(newLetter);
			PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(onlineRecipient.getMailbox()));
			recipientMailbox.isMailListUpdateRequired = true;

			if (recipientMailbox.mailBoxState != 0) {
				boolean isPostman = (recipientMailbox.mailBoxState & PlayerMailboxState.EXPRESS) == PlayerMailboxState.EXPRESS;
				PacketSendUtility.sendPacket(onlineRecipient, new SM_MAIL_SERVICE(onlineRecipient, recipientMailbox.getLetters(), isPostman));
			}
			PacketSendUtility.sendPacket(onlineRecipient, SM_SYSTEM_MESSAGE.STR_POSTMAN_NOTIFY);
		}

		if (!recipientCommonData.isOnline()) {
			recipientCommonData.setMailboxLetters(recipientCommonData.getMailboxLetters() + 1);
			DAOManager.getDAO(MailDAO.class).updateOfflineMailCounter(recipientCommonData);
		}

		if (VeteranRewardConfig.VETERANREWARDS_ENABLED_INFO_LOG) {
			// log.info(String.format("Player: " + recipientName + " / " + "Item: " + attachedItemObjId + " / " + "Item Count: " + attachedItemCount + " / "
			// + "Kinah: " + attachedKinahCount + " / " + "Status: successfully."));
		}
	}

	private VeteranRewardsDAO getDAO() {
		return DAOManager.getDAO(VeteranRewardsDAO.class);
	}

	public static final VeteranRewardsService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final VeteranRewardsService instance = new VeteranRewardsService();
	}
}