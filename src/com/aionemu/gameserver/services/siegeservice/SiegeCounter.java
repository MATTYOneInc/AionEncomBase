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
package com.aionemu.gameserver.services.siegeservice;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SiegeCounter
{
	private final Map<SiegeRace, SiegeRaceCounter> siegeRaceCounters = Maps.newHashMap();
	
	public SiegeCounter() {
		siegeRaceCounters.put(SiegeRace.ELYOS, new SiegeRaceCounter(SiegeRace.ELYOS));
		siegeRaceCounters.put(SiegeRace.ASMODIANS, new SiegeRaceCounter(SiegeRace.ASMODIANS));
		siegeRaceCounters.put(SiegeRace.BALAUR, new SiegeRaceCounter(SiegeRace.BALAUR));
	}
	
	public void addDamage(Creature creature, int damage) {
		SiegeRace siegeRace;
		if (creature instanceof Player) {
			siegeRace = SiegeRace.getByRace(((Player) creature).getRace());
		} else if (creature instanceof SiegeNpc) {
			siegeRace = ((SiegeNpc) creature).getSiegeRace();
		} else {
			return;
		}
		siegeRaceCounters.get(siegeRace).addPoints(creature, damage);
	}
	
	public void addAbyssPoints(Player player, int ap) {
		SiegeRace sr = SiegeRace.getByRace(player.getRace());
		siegeRaceCounters.get(sr).addAbyssPoints(player, ap);
	}
	
	public SiegeRaceCounter getRaceCounter(SiegeRace race) {
		return siegeRaceCounters.get(race);
	}
	
	public void addRaceDamage(SiegeRace race, int damage) {
		getRaceCounter(race).addTotalDamage(damage);
	}
	
	public SiegeRaceCounter getWinnerRaceCounter() {
		List<SiegeRaceCounter> list = Lists.newArrayList(siegeRaceCounters.values());
		Collections.sort(list);
		return list.get(0);
	}
}