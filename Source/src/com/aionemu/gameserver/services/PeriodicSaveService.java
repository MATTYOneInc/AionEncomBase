/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
public class PeriodicSaveService {

	private static final Logger log = LoggerFactory.getLogger(PeriodicSaveService.class);

	private Future<?> legionWhUpdateTask;

	public static final PeriodicSaveService getInstance() {
		return SingletonHolder.instance;
	}

	private PeriodicSaveService() {

		int DELAY_LEGION_ITEM = PeriodicSaveConfig.LEGION_ITEMS * 1000;

		legionWhUpdateTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new LegionWhUpdateTask(), DELAY_LEGION_ITEM, DELAY_LEGION_ITEM);
	}

	private class LegionWhUpdateTask implements Runnable {

		@Override
		public void run() {
			log.info("Legion WH update task started.");
			long startTime = System.currentTimeMillis();
			Iterator<Legion> legionsIterator = LegionService.getInstance().getCachedLegionIterator();
			int legionWhUpdated = 0;
			while (legionsIterator.hasNext()) {
				Legion legion = legionsIterator.next();
				FastList<Item> allItems = legion.getLegionWarehouse().getItemsWithKinah();
				allItems.addAll(legion.getLegionWarehouse().getDeletedItems());
				try {
					/**
					 * 1. save items first
					 */
					DAOManager.getDAO(InventoryDAO.class).store(allItems, null, null, legion.getLegionId());

					/**
					 * 2. save item stones
					 */
					DAOManager.getDAO(ItemStoneListDAO.class).save(allItems);
				}
				catch (Exception ex) {
					log.error("Exception during periodic saving of legion WH", ex);
				}
				legionWhUpdated++;
			}
			long workTime = System.currentTimeMillis() - startTime;
			log.info("Legion WH update: " + workTime + " ms, legions: " + legionWhUpdated + ".");
		}
	}

	/**
	 * Save data on shutdown
	 */
	public void onShutdown() {
		log.info("Starting data save on shutdown.");
		// save legion warehouse
		legionWhUpdateTask.cancel(false);
		new LegionWhUpdateTask().run();
		log.info("Data successfully saved.");
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final PeriodicSaveService instance = new PeriodicSaveService();
	}
}