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

public class EnchantsConfig
{
	@Property(key = "gameserver.socket.manastone", defaultValue = "50")
	public static float SOCKET_MANASTONE;
	@Property(key = "gameserver.enchant.item", defaultValue = "50")
	public static float ENCHANT_ITEM;
	@Property(key = "gameserver.enchant.plume", defaultValue = "50")
	public static float ENCHANT_PLUME;
	@Property(key = "gameserver.enchant.bracelet", defaultValue = "50")
	public static float ENCHANT_BRACELET;
	@Property(key = "gameserver.enchant.accessory", defaultValue = "50")
	public static float ENCHANT_ACCESSORY;
	@Property(key = "gameserver.enchant.stigma", defaultValue = "50")
	public static float ENCHANT_STIGMA;
	@Property(key = "gameserver.manastone.clean", defaultValue = "false")
	public static boolean CLEAN_STONE;
	@Property(key = "gameserver.enchant.cast.speed", defaultValue = "4000")
	public static int ENCHANT_SPEED;
	@Property(key = "gameserver.enchant.item.broke", defaultValue = "true")
	public static boolean ENABLE_ARCHDAEVA_ITEM_BROKE;
}