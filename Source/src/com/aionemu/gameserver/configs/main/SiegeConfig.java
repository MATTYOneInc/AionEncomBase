/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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

public class SiegeConfig
{
	@Property(key = "gameserver.siege.enable", defaultValue = "true")
	public static boolean SIEGE_ENABLED;
	
	@Property(key = "gameserver.siege.medal.rate", defaultValue = "1")
	public static int SIEGE_MEDAL_RATE;
	
	@Property(key = "gameserver.siege.shield.enable", defaultValue = "true")
	public static boolean SIEGE_SHIELD_ENABLED;
	
    @Property(key="gameserver.siege.assault.enable", defaultValue="false")
    public static boolean BALAUR_AUTO_ASSAULT;
	
    @Property(key="gameserver.siege.assault.rate", defaultValue="1")
    public static float BALAUR_ASSAULT_RATE;
	
	@Property(key = "gameserver.auto.siege.race", defaultValue = "false")
	public static boolean SIEGE_AUTO_RACE;
	
	@Property(key = "gameserver.auto.siege.id", defaultValue = "")
	public static String SIEGE_AUTO_LOCID;
}