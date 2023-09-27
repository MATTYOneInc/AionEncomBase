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

public class PeriodicSaveConfig
{
	@Property(key = "gameserver.periodicsave.player.general", defaultValue = "900")
	public static int PLAYER_GENERAL;
	@Property(key = "gameserver.periodicsave.player.items", defaultValue = "900")
	public static int PLAYER_ITEMS;
	@Property(key = "gameserver.periodicsave.legion.items", defaultValue = "1200")
	public static int LEGION_ITEMS;
	@Property(key = "gameserver.periodicsave.broker", defaultValue = "1500")
	public static int BROKER;
	@Property(key = "gameserver.periodicsave.player.pets", defaultValue = "5")
	public static int PLAYER_PETS;
}