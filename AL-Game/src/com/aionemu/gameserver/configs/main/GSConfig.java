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

import java.util.Calendar;

import com.aionemu.commons.configuration.Property;

public class GSConfig {
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int SERVER_COUNTRY_CODE;
	@Property(key = "gameserver.name", defaultValue = "Aion")
	public static String SERVER_NAME;
	@Property(key = "gameserver.players.max.level", defaultValue = "83")
	public static int PLAYER_MAX_LEVEL;
	@Property(key = "gameserver.lang", defaultValue = "en")
	public static String LANG;
	@Property(key = "gameserver.timezone", defaultValue = "")
	public static String TIME_ZONE_ID = Calendar.getInstance().getTimeZone().getID();
	@Property(key = "gameserver.chatserver.enable", defaultValue = "false")
	public static boolean ENABLE_CHAT_SERVER;
	@Property(key = "gameserver.revisiondisplay.enable", defaultValue = "false")
	public static boolean SERVER_MOTD_DISPLAYREV;
	@Property(key = "gameserver.character.creation.mode", defaultValue = "0")
	public static int CHARACTER_CREATION_MODE;
	@Property(key = "gameserver.character.limit.count", defaultValue = "8")
	public static int CHARACTER_LIMIT_COUNT;
	@Property(key = "gameserver.character.faction.limitation.mode", defaultValue = "0")
	public static int CHARACTER_FACTION_LIMITATION_MODE;
	@Property(key = "gameserver.ratio.limitation.enable", defaultValue = "false")
	public static boolean ENABLE_RATIO_LIMITATION;
	@Property(key = "gameserver.ratio.min.value", defaultValue = "60")
	public static int RATIO_MIN_VALUE;
	@Property(key = "gameserver.ratio.min.required.level", defaultValue = "10")
	public static int RATIO_MIN_REQUIRED_LEVEL;
	@Property(key = "gameserver.ratio.min.characters_count", defaultValue = "50")
	public static int RATIO_MIN_CHARACTERS_COUNT;
	@Property(key = "gameserver.ratio.high_player_count.disabling", defaultValue = "500")
	public static int RATIO_HIGH_PLAYER_COUNT_DISABLING;
	@Property(key = "gameserver.abyssranking.small.cache", defaultValue = "false")
	public static boolean ABYSSRANKING_SMALL_CACHE;
	@Property(key = "gameserver.character.reentry.time", defaultValue = "20")
	public static int CHARACTER_REENTRY_TIME;
	@Property(key = "gameserver.master.server.enable", defaultValue = "false")
	public static boolean MASTER_SERVER;

	@Property(key = "gameserver.yaadminpanel.server.enable", defaultValue = "false")
	public static boolean SERVER_YAADMINPANEL_SWITCH_ON;

	/**
	 * Memory Optimization Configs
	 */
	@Property(key = "gameserver.gc.enable", defaultValue = "true")
	public static boolean ENABLE_MEMORY_GC;
	@Property(key = "gameserver.gc.optimization.time", defaultValue = "5")
	public static int GC_OPTIMIZATION_TIME;
}