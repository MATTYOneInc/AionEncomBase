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
package com.aionemu.gameserver.model.team2.common.events;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TeamEvent;
import com.aionemu.gameserver.model.team2.TeamMember;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

public abstract class PlayerLeavedEvent<TM extends TeamMember<Player>, T extends TemporaryPlayerTeam<TM>> implements Predicate<TM>, TeamEvent
{
    public static enum LeaveReson {
        BAN,
        LEAVE,
        LEAVE_TIMEOUT,
        DISBAND;
    }
	
    protected final T team;
    protected final Player leavedPlayer;
    protected final LeaveReson reason;
    protected final TM leavedTeamMember;
    protected final String banPersonName;
	
    public PlayerLeavedEvent(T alliance, Player player) {
        this(alliance, player, LeaveReson.LEAVE);
    }
	
    public PlayerLeavedEvent(T alliance, Player player, LeaveReson reason) {
        this(alliance, player, reason, StringUtils.EMPTY);
    }
	
    public PlayerLeavedEvent(T team, Player player, LeaveReson reason, String banPersonName) {
        this.team = team;
        this.leavedPlayer = player;
        this.reason = reason;
        this.leavedTeamMember = team.getMember(player.getObjectId());
        this.banPersonName = banPersonName;
    }
	
    @Override
    public boolean checkCondition() {
        return team.hasMember(leavedPlayer.getObjectId());
    }
}