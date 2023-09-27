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
package com.aionemu.gameserver.model.team2.league.events;

import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.events.AlwaysTrueTeamEvent;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.model.team2.league.events.LeagueLeftEvent.LeaveReson;
import com.google.common.base.Predicate;

public class LeagueDisbandEvent extends AlwaysTrueTeamEvent implements Predicate<PlayerAlliance>
{
    private final League league;
	
    public LeagueDisbandEvent(League league) {
        this.league = league;
    }
	
    @Override
    public void handleEvent() {
        league.applyOnMembers(this);
    }
	
    @Override
    public boolean apply(PlayerAlliance alliance) {
        league.onEvent(new LeagueLeftEvent(league, alliance, LeaveReson.DISBAND));
        return true;
    }
}