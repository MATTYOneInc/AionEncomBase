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

public class GeoDataConfig {

	/**
	 * Geodata enable
	 */
	@Property(key = "gameserver.geodata.enable", defaultValue = "false")
	public static boolean GEO_ENABLE;

	/**
	 * Enable canSee checks using geodata.
	 */
	@Property(key = "gameserver.geodata.cansee.enable", defaultValue = "true")
	public static boolean CANSEE_ENABLE;

	/**
	 * Enable Fear skill using geodata.
	 */
	@Property(key = "gameserver.geodata.fear.enable", defaultValue = "true")
	public static boolean FEAR_ENABLE;

	/**
	 * Enable Geo checks during npc movement (prevent flying mobs)
	 */
	@Property(key = "gameserver.geo.npc.move", defaultValue = "false")
	public static boolean GEO_NPC_MOVE;

	/**
	 * Enable npc checks aggro target visibility range (canSee)
	 */
	@Property(key = "gameserver.geo.npc.aggro", defaultValue = "false")
	public static boolean GEO_NPC_AGGRO;

	/**
	 * Enable geo materials using skills
	 */
	@Property(key = "gameserver.geo.materials.enable", defaultValue = "false")
	public static boolean GEO_MATERIALS_ENABLE;

	/**
	 * Show collision zone name and skill id
	 */
	@Property(key = "gameserver.geo.materials.showdetails", defaultValue = "false")
	public static boolean GEO_MATERIALS_SHOWDETAILS;

	/**
	 * Enable geo shields
	 */
	@Property(key = "gameserver.geo.shields.enable", defaultValue = "false")
	public static boolean GEO_SHIELDS_ENABLE;

	/**
	 * Enable geo doors
	 */
	@Property(key = "gameserver.geo.doors.enable", defaultValue = "false")
	public static boolean GEO_DOORS_ENABLE;

	/**
	 * Object factory for geodata primitives enabled
	 */
	@Property(key = "gameserver.geodata.objectfactory.enabled", defaultValue = "true")
	public static boolean GEO_OBJECT_FACTORY_ENABLE;

	/**
	 * If you use monon2 geo data for your server
	 */
	@Property(key = "gameserver.geodata.monon2.in.use", defaultValue = "fasle")
	public static boolean GEO_MONONO2_IN_USE;
}