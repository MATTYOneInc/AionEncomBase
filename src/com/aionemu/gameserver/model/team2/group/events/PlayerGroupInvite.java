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
package com.aionemu.gameserver.model.team2.group.events;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class PlayerGroupInvite extends RequestResponseHandler
{
    private final Player inviter;
    private final Player invited;
	
    public PlayerGroupInvite(Player inviter, Player invited) {
        super(inviter);
        this.inviter = inviter;
        this.invited = invited;
    }
	
    @Override
    public void acceptRequest(Creature requester, Player responder) {
        if (PlayerGroupService.canInvite(inviter, invited)) {
            //You have invited %0 to join your group.
			PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_INVITED_HIM(invited.getName()));
            PlayerGroup group = inviter.getPlayerGroup2();
            if (group != null) {
                PlayerGroupService.addPlayer(group, invited);
            } else {
                PlayerGroupService.createGroup(inviter, invited, TeamType.GROUP);
            }
        }
    }
	
    @Override
    public void denyRequest(Creature requester, Player responder) {
        //%0 has declined your invitation.
		PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_PARTY_HE_REJECT_INVITATION(responder.getName()));
    }
}