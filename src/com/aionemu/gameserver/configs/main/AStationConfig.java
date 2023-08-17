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

/**
 * @author Ranastic
 */

public class AStationConfig
{
	@Property(key = "a.station.server.id", defaultValue = "2")
    public static int A_STATION_SERVER_ID;
    @Property(key = "a.station.max.level", defaultValue = "83")
    public static int A_STATION_MAX_LEVEL;
    @Property(key = "a.station.enable", defaultValue = "true")
    public static boolean A_STATION_ENABLE;
}