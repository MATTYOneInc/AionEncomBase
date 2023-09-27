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
package com.aionemu.gameserver.model.ingameshop;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.ingameshop.InGameShopProperty;
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.dao.InGameShopDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.templates.mail.MailMessage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_PREMIUM_CONTROL;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author KID
 */
public class InGameShopEn {

	private static InGameShopEn instance = new InGameShopEn();
	private final Logger log = LoggerFactory.getLogger("INGAMESHOP_LOG");
	private FastMap<Byte, List<IGItem>> items;
	private InGameShopDAO dao;
	private InGameShopProperty iGProperty;
	private int lastRequestId = 0;
	private FastList<IGRequest> activeRequests;
	private static Map<Integer, Long> lastUsage = new FastMap<Integer, Long>();

	public static InGameShopEn getInstance() {
		return instance;
	}

	public InGameShopEn() {
		if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
			log.info("InGameShop is disabled.");
			return;
		}
		iGProperty = InGameShopProperty.load();
		dao = DAOManager.getDAO(InGameShopDAO.class);
		items = FastMap.newInstance();
		activeRequests = FastList.newInstance();
		items = dao.loadInGameShopItems();
		log.info("Loaded with " + items.size() + " items.");
	}

	public InGameShopProperty getIGSProperty() {
		return iGProperty;
	}

	public void reload() {
		if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
			log.info("InGameShop is disabled.");
			return;
		}
		iGProperty.clear();
		iGProperty = InGameShopProperty.load();
		items = DAOManager.getDAO(InGameShopDAO.class).loadInGameShopItems();
		log.info("Loaded with " + items.size() + " items.");
	}

	public IGItem getIGItem(int id) {
		for (byte key : items.keySet()) {
			for (IGItem item : items.get(key)) {
				if (item.getObjectId() == id) {
					return item;
				}
			}
		}
		return null;
	}

	public Collection<IGItem> getItems(byte category) {
		if (!items.containsKey(category)) {
			return Collections.emptyList();
		}
		return items.get(category);
	}

	public FastList<Integer> getTopSales(int subCategory, byte category) {
		byte max = 6;
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>(new DescFilter());
		if (!items.containsKey(category)) {
			return FastList.newInstance();
		}
		for (IGItem item : items.get(category))
			if (item.getSalesRanking() != 0 && (subCategory == 2 || item.getSubCategory() == subCategory)) {
				map.put(item.getSalesRanking(), item.getObjectId());
			}
		FastList<Integer> top = FastList.newInstance();
		byte cnt = 0;
		for (Iterator<Integer> i = map.values().iterator(); i.hasNext();) {
			int objId = i.next();
			if (cnt > max) {
				break;
			}
			top.add(objId);
			cnt++;
		}

		map.clear();
		return top;
	}

	public int getMaxList(byte subCategoryId, byte category) {
		int id = 0;
		if (!items.containsKey(category)) {
			return id;
		}
		for (IGItem item : items.get(category)) {
			if (item.getSubCategory() == subCategoryId) {
				if (item.getList() > id) {
					id = item.getList();
				}
			}
		}

		return id;
	}

	public void acceptRequest(Player player, int itemObjId) {
		if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
			return;
		}

		IGItem item = getInstance().getIGItem(itemObjId);
		if (AdvCustomConfig.GAMESHOP_LIMIT) {
			if (item.getCategory() == AdvCustomConfig.GAMESHOP_CATEGORY) {
				if (lastUsage.containsKey(player.getObjectId())) {
					if ((System.currentTimeMillis() - lastUsage.get(player.getObjectId())) < AdvCustomConfig.GAMESHOP_LIMIT_TIME * 60 * 1000) {
						PacketSendUtility.sendMessage(player, "?????????????,??????????:" + (int) ((AdvCustomConfig.GAMESHOP_LIMIT_TIME * 60 * 1000 - (System.currentTimeMillis() - lastUsage.get(player.getObjectId()))) / 1000) + " ?");
						return;
					}
				}
			}
		}
		lastRequestId++;
		IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), itemObjId);
		request.accountId = player.getClientConnection().getAccount().getId();
		if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, item.getItemPrice())))
			activeRequests.add(request);
		if (AdvCustomConfig.GAMESHOP_LIMIT) {
			if (item.getCategory() == AdvCustomConfig.GAMESHOP_CATEGORY) {
				lastUsage.put(player.getObjectId(), new Long(System.currentTimeMillis()));
			}
		}
	}

	public void sendRequest(Player player, String receiver, String message, int itemObjId) {
		if (receiver.equalsIgnoreCase(player.getName())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_CANNOT_GIVE_TO_ME);
			return;
		}

		if (!InGameShopConfig.ALLOW_GIFTS) {
			PacketSendUtility.sendMessage(player, "Gifts are disabled.");
			return;
		}

		if (!DAOManager.getDAO(PlayerDAO.class).isNameUsed(receiver)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_NO_USER_TO_GIFT);
			return;
		}

		PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(receiver);
		if (recipientCommonData.getMailboxLetters() >= 100) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MAIL_MSG_RECIPIENT_MAILBOX_FULL(recipientCommonData.getName()));
			return;
		}

		if (!InGameShopConfig.ENABLE_GIFT_OTHER_RACE && !player.isGM()) {
			if (player.getRace() != recipientCommonData.getRace()) {
				PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(MailMessage.MAIL_IS_ONE_RACE_ONLY));
				return;
			}
        }
		IGItem item = getIGItem(itemObjId);
		lastRequestId++;
		IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), receiver, message, itemObjId);
		request.accountId = player.getClientConnection().getAccount().getId();
		if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, item.getItemPrice()))) {
			activeRequests.add(request);
		}
	}

	public void addToll(Player player, long cnt) {
		if (InGameShopConfig.ENABLE_IN_GAME_SHOP) {
			lastRequestId++;
			IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), 0);
			request.accountId = player.getClientConnection().getAccount().getId();
			if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, cnt * -1))) {
				activeRequests.add(request);
			}
		}
		else {
			PacketSendUtility.sendMessage(player, "You can't add toll if ingameshop is disabled!");
		}
	}

	public void finishRequest(int requestId, int result, long toll, long luna) {
		for (IGRequest request : activeRequests)
			if (request.requestId == requestId) {
				Player player = World.getInstance().findPlayer(request.playerId);
				if (player != null) {
					if (result == 1) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
					}
					else if (result == 2) {
						IGItem item = getIGItem(request.itemObjId);
						if (item == null) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
							log.error("player " + player.getName() + " requested " + request.itemObjId + " that was not exists in list.");
							return;
						}
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_INGAMESHOP_NOT_ENOUGH_CASH("Toll"));
						PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
					}
					else if (result == 3) {
						IGItem item = getIGItem(request.itemObjId);
						if (item == null) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
							log.error("player " + player.getName() + " requested " + request.itemObjId + " that was not exists in list.");
							return;
						}

						if (request.gift) {
							SystemMailService.getInstance().sendMail(player.getName(), request.receiver, "In Game Shop", request.message, item.getItemId(), item.getItemCount(), 0L, 0L, LetterType.BLACKCLOUD);
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_GIFT_SUCCESS);
							player.getClientConnection().getAccount().setToll(toll);
							player.getClientConnection().getAccount().setLuna(luna);
						}
						else {
							ItemService.addItem(player, item.getItemId(), item.getItemCount());
							player.getClientConnection().getAccount().setToll(toll);
							player.getClientConnection().getAccount().setLuna(luna);
						}

						item.increaseSales();
						dao.increaseSales(item.getObjectId(), item.getSalesRanking());
						PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
					}
					else if (result == 4) {
						player.getClientConnection().getAccount().setToll(toll);
						PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
					}
				}
				activeRequests.remove(request);
				break;
			}
	}

	class DescFilter implements Comparator<Object> {

		DescFilter() {
		}

		public int compare(Object o1, Object o2) {
			Integer i1 = (Integer) o1;
			Integer i2 = (Integer) o2;
			return -i1.compareTo(i2);
		}
	}
}