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

public class CleaningConfig {

	@Property(key = "gameserver.cleaning.enable", defaultValue = "false")
	public static boolean CLEANING_ENABLE;

	@Property(key = "gameserver.cleaning.period", defaultValue = "180")
	public static int CLEANING_PERIOD;

	@Property(key = "gameserver.cleaning.threads", defaultValue = "2")
	public static int CLEANING_THREADS;

	@Property(key = "gameserver.cleaning.limit", defaultValue = "5000")
	public static int CLEANING_LIMIT;

	@Property(key = "gameserver.abyss.cleaning.enable", defaultValue = "false")
	public static boolean ABYSS_CLEANING_ENABLE;

	@Property(key = "gameserver.abyss.cleaning.period", defaultValue = "180")
	public static int ABYSS_CLEANING_PERIOD;
}