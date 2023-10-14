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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class DatabaseCleaningService {

	private Logger log = LoggerFactory.getLogger(DatabaseCleaningService.class);
	private PlayerDAO dao = (PlayerDAO) DAOManager.getDAO(PlayerDAO.class);

	private final int SECURITY_MINIMUM_PERIOD = 30;

	private final int WORKER_CHECK_TIME = 10000;

	private static DatabaseCleaningService instance = new DatabaseCleaningService();
	private List<Worker> workers;
	private long startTime;

	private DatabaseCleaningService() {
		if (CleaningConfig.CLEANING_ENABLE) {
			runCleaning();
		}
	}

	private void runCleaning() {
		log.info("DatabaseCleaningService: Executing database cleaning");
		startTime = System.currentTimeMillis();

		int periodInDays = CleaningConfig.CLEANING_PERIOD;

		if (periodInDays > SECURITY_MINIMUM_PERIOD) {
			delegateToThreads(CleaningConfig.CLEANING_THREADS, dao.getPlayersToDelete(periodInDays, CleaningConfig.CLEANING_LIMIT));
			monitoringProcess();
		}
		else {
			log.warn("The configured days for database cleaning is to low. For security reasons the service will only execute with periods over 30 days!");
		}
	}

	private void monitoringProcess() {
		while (!allWorkersReady())
		try {
			Thread.sleep(WORKER_CHECK_TIME);
			log.info("DatabaseCleaningService: Until now " + currentlyDeletedChars() + " chars deleted in " + (System.currentTimeMillis() - startTime) / 1000L + " seconds!");
		}
		catch (InterruptedException ex) {
			log.error("DatabaseCleaningService: Got Interrupted!");
		}
	}

	private boolean allWorkersReady() {
		for (Worker w : workers) {
			if (!w._READY) {
				return false;
			}
		}
		return true;
	}

	private int currentlyDeletedChars() {
		int deletedChars = 0;
		for (Worker w : workers) {
			deletedChars += w.deletedChars;
		}
		return deletedChars;
	}

	private void delegateToThreads(int numberOfThreads, List<Integer> idsToDelegate) {
		workers = new ArrayList<Worker>();
		log.info("DatabaseCleaningService: Executing deletion over " + numberOfThreads + " longrunning threads");

		int itr = 0;
		for (int workerNo = 0; itr < idsToDelegate.size(); workerNo %= numberOfThreads) {
			if (workerNo >= workers.size()) {
				workers.add(new Worker());
			}
			workers.get(workerNo).ids.add(idsToDelegate.get(itr));

			itr++;
			workerNo++;
		}

		for (Worker w : workers) {
			ThreadPoolManager.getInstance().executeLongRunning(w);
		}
	}

	public static DatabaseCleaningService getInstance() {
		return instance;
	}

	private class Worker implements Runnable {

		private List<Integer> ids = new ArrayList<Integer>();
		private int deletedChars = 0;
		private boolean _READY = false;

		private Worker() {
		}

		public void run() {
			for (int id : ids) {
				PlayerService.deletePlayerFromDB(id);
				deletedChars += 1;
			}
			_READY = true;
		}
	}
}