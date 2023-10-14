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

import java.sql.Timestamp;
import java.util.Map;

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple
 */
public class SM_LEGION_INFO extends AionServerPacket {

	/** Legion information **/
	private Legion legion;

	/**
	 * This constructor will handle legion info
	 * 
	 * @param legion
	 */
	public SM_LEGION_INFO(Legion legion) {
		this.legion = legion;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeS(legion.getLegionName());
		writeC(legion.getLegionLevel());
		writeD(legion.getLegionRank());
		writeH(legion.getDeputyPermission());
		writeH(legion.getCenturionPermission());
		writeH(legion.getLegionaryPermission());
		writeH(legion.getVolunteerPermission());
		writeQ(legion.getContributionPoints());
		writeB(new byte[24]);
		writeS(legion.getLegionDescription());
		writeC(legion.getLegionJoinType());
		writeH(legion.getMinLevel());
		/** Get Announcements List From DB By Legion **/
		Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

		/** Show max 7 announcements **/
		int i = 0;
		for (Timestamp unixTime : announcementList.keySet()) {
			writeS(announcementList.get(unixTime));
			writeD((int) (unixTime.getTime() / 1000));
			i++;
			if (i >= 7) {
				break;
			}
		}
	}
}