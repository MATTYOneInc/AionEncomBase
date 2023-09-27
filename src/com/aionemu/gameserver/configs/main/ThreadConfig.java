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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class ThreadConfig
{
	@Property(key = "gameserver.thread.basepoolsize", defaultValue = "1")
	public static int BASE_THREAD_POOL_SIZE;
	@Property(key = "gameserver.thread.threadpercore", defaultValue = "4")
	public static int EXTRA_THREAD_PER_CORE;
	@Property(key = "gameserver.thread.runtime", defaultValue = "5000")
	public static long MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING;
	@Property(key = "gameserver.thread.usepriority", defaultValue = "false")
	public static boolean USE_PRIORITIES;
	public static int THREAD_POOL_SIZE;
	
	public static void load() {
		final int extraThreadPerCore = EXTRA_THREAD_PER_CORE;
		THREAD_POOL_SIZE = (BASE_THREAD_POOL_SIZE + extraThreadPerCore) * Runtime.getRuntime().availableProcessors();
	}
}