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

public class RankingConfig {
	@Property(key = "gameserver.top.ranking.update.setting", defaultValue = "true")
	public static boolean TOP_RANKING_UPDATE_SETTING;

	@Property(key = "gameserver.top.ranking.update.hour", defaultValue = "0 0 */2 ? * *")
	public static String TOP_RANKING_UPDATE_RULE;

	@Property(key = "gameserver.top.ranking.update.minute", defaultValue = "10")
	public static int TOP_RANKING_UPDATE_RULE2;

	@Property(key = "gameserver.top.ranking.max.offline.days", defaultValue = "0")
	public static int TOP_RANKING_MAX_OFFLINE_DAYS;
}