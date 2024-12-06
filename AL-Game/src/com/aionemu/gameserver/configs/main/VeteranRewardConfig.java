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

public class VeteranRewardConfig {
	@Property(key = "gameserver.veteranreward.enabled", defaultValue = "false")
	public static boolean VETERANREWARDS_ENABLED;

	@Property(key = "gameserver.veteranreward.log_error", defaultValue = "true")
	public static boolean VETERANREWARDS_ENABLED_ERROR_LOG;

	@Property(key = "gameserver.veteranreward.log_info", defaultValue = "true")
	public static boolean VETERANREWARDS_ENABLED_INFO_LOG;
}