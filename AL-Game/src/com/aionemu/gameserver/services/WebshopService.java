package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.dao.RewardServiceDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.rewards.RewardEntryItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

public class WebshopService {
	private static final Logger log = LoggerFactory.getLogger(WebshopService.class);

	private WebshopService() {
		this.load();
	}

	public static final WebshopService getInstance() {
		return SingletonHolder.instance;
	}

	private void load() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player pl) {
						FastList<RewardEntryItem> liste = DAOManager.getDAO(RewardServiceDAO.class)
								.getAvailable(pl.getObjectId());
						if (liste.isEmpty()) {
							return;
						} else {
							int i = 0;
							for (RewardEntryItem item : liste) {
								if (pl.getInventory().isFull()) {
									PacketSendUtility.sendPacket(pl, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
									return;
								} else {
									if (DAOManager.getDAO(RewardServiceDAO.class).setUpdate(item.unique)) {
										if (ItemService.addItem(pl, item.id, item.count) != 0) {
											DAOManager.getDAO(RewardServiceDAO.class).setUpdateDown(item.unique);
										} else {
											i++;
										}
									}
								}
							}
						}
					}
				});
			}
		}, 5 * 1000, 5 * 1000);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final WebshopService instance = new WebshopService();
	}
}