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

/**
 * @author Rinzler (Encom)
 */
public class FFAConfig {

	//FFA Event
	@Property(key = "gameserver.pvp.mod.ffa.enabled", defaultValue = "true")
	public static boolean FFA_ENABLED;
	@Property(key = "gameserver.pvp.mod.ffa.spree.toll.quantity", defaultValue = "10")
	public static int FFA_SPREE_REWARD_TOLL_QUANTITY;
	@Property(key = "gameserver.pvp.mod.ffa.spree.reward.item", defaultValue = "166030005")
	public static int FFA_SPREE_REWARD_ITEM;

	//FFA Spree
	@Property(key = "gameserver.pvp.mod.ffa.spree1", defaultValue = " <is now on a Killing Spree> !")
	public static String FFA_SPREE_1;
	@Property(key = "gameserver.pvp.mod.ffa.spree2", defaultValue = " <is now on Rampage> !")
	public static String FFA_SPREE_2;
	@Property(key = "gameserver.pvp.mod.ffa.spree3", defaultValue = " <is now Dominating> !")
	public static String FFA_SPREE_3;
	@Property(key = "gameserver.pvp.mod.ffa.spree4", defaultValue = " <Unstoppable> !")
	public static String FFA_SPREE_4;
	@Property(key = "gameserver.pvp.mod.ffa.spree5", defaultValue = " <CHUUCHUU MUTHAFAKAAASS> !")
	public static String FFA_SPREE_5;
	@Property(key = "gameserver.pvp.mod.ffa.spree6", defaultValue = " <is now Getting Crazzyyy> !")
	public static String FFA_SPREE_6;
	@Property(key = "gameserver.pvp.mod.ffa.spree7", defaultValue = " <is now GODLIKE> !")
	public static String FFA_SPREE_7;
	@Property(key = "gameserver.pvp.mod.ffa.spree8", defaultValue = " <is now on WICKED SICKKKKKK> !")
	public static String FFA_SPREE_8;
	@Property(key = "gameserver.pvp.mod.ffa.spree9", defaultValue = " <Really knows how to kill players> !")
	public static String FFA_SPREE_9;
	@Property(key = "gameserver.pvp.mod.ffa.spree10", defaultValue = " <IS NOW A TRUE PVP FIGHTER> !")
	public static String FFA_SPREE_10;
}