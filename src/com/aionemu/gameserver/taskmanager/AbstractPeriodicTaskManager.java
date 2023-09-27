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
package com.aionemu.gameserver.taskmanager;

import com.aionemu.commons.taskmanager.AbstractLockManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.GameServer.StartupHook;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lord_rex and MrPoke based on l2j-free engines. This can be used for periodic calls.
 */
public abstract class AbstractPeriodicTaskManager extends AbstractLockManager implements Runnable, StartupHook {

	protected static final Logger log = LoggerFactory.getLogger(AbstractPeriodicTaskManager.class);

	private final int period;

	public AbstractPeriodicTaskManager(int period) {
		this.period = period;

		GameServer.addStartupHook(this);

		log.info(getClass().getSimpleName() + ": Initialized.");
	}

	@Override
	public final void onStartup() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000 + Rnd.get(period), Rnd.get(period - 5, period + 5));
	}

	@Override
	public abstract void run();
}