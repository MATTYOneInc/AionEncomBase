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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_CHAT_WINDOW extends AionServerPacket
{
	private Player target;
	private boolean isGroup;
	
	public SM_CHAT_WINDOW(Player target, boolean isGroup) {
		this.target = target;
		this.isGroup = isGroup;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		if (target == null) {
			return;
		}
		if (isGroup) {
			if (target.isInGroup2()) {
				writeC(2);
				writeS(target.getName());
				PlayerGroup group = target.getPlayerGroup2();
				writeD(group.getTeamId());
				writeS(group.getLeader().getName());
				Collection<Player> members = group.getMembers();
				for (Player groupMember : members) {
					writeC(groupMember.getLevel());
				}
				for (int i = group.size(); i < 6; i++) {
					writeC(0);
				}
				for (Player groupMember : members) {
					writeC(groupMember.getPlayerClass().getClassId());
				}
				for (int i = group.size(); i < 6; i++) {
					writeC(0);
				}
			} else if (target.isInAlliance2()) {
				writeC(2);
				writeS(target.getName());
				PlayerAlliance alliance = target.getPlayerAlliance2();
				writeD(alliance.getTeamId());
				writeS(alliance.getLeader().getName());
				Collection<Player> members = alliance.getMembers();
				for (Player groupMember : members) {
					writeC(groupMember.getLevel());
				}
				for (int i = alliance.size(); i < 24; i++) {
					writeC(0);
				}
				for (Player groupMember : members) {
					writeC(groupMember.getPlayerClass().getClassId());
				}
				for (int i = alliance.size(); i < 24; i++) {
					writeC(0);
				}
			} else {
				writeC(4);
				writeS(target.getName());
				writeD(0);
				writeC(target.getPlayerClass().getClassId());
				writeC(target.getLevel());
				writeC(0);
			}
		} else {
			writeC(1);
			writeS(target.getName());
			writeS(target.getLegion() != null ? target.getLegion().getLegionName() : "");
			writeC(target.getLevel());
			writeH(target.getPlayerClass().getClassId());
			writeS(target.getCommonData().getNote());
			writeD(1);
		}
	}
}