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

public class AdvCustomConfig {
	@Property(key = "gameserver.cube.size", defaultValue = "0")
	public static int CUBE_SIZE;

	@Property(key = "gameserver.gameshop.limit", defaultValue = "false")
	public static boolean GAMESHOP_LIMIT;

	@Property(key = "gameserver.gameshop.category", defaultValue = "0")
	public static byte GAMESHOP_CATEGORY;

	@Property(key = "gameserver.gameshop.limit.time", defaultValue = "60")
	public static long GAMESHOP_LIMIT_TIME;

	@Property(key = "gameserver.craft.delaytime,rate", defaultValue = "2")
	public static Integer CRAFT_DELAYTIME_RATE;
}