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
package com.aionemu.gameserver.utils.gametime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class GameTimeManager {
	private static final Logger log = LoggerFactory.getLogger(GameTimeManager.class);
	private static GameTime instance;
	private static GameTimeUpdater updater;
	private static boolean clockStarted = false;

	static {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		instance = new GameTime(dao.load("time"));
	}

	public static GameTime getGameTime() {
		return instance;
	}

	public static void startClock() {
		if (clockStarted) {
			throw new IllegalStateException("Clock is already started");
		}
		updater = new GameTimeUpdater(getGameTime());
		ThreadPoolManager.getInstance().scheduleAtFixedRate(updater, 0, 5000);
		clockStarted = true;
	}

	public static boolean saveTime() {
		return DAOManager.getDAO(ServerVariablesDAO.class).store("time", getGameTime().getTime());
	}

	public static void reloadTime(int time) {
		ThreadPoolManager.getInstance().purge();
		instance = new GameTime(time);
		clockStarted = false;
		startClock();
		log.info("Game time changed by admin and clock restarted...");
	}
}