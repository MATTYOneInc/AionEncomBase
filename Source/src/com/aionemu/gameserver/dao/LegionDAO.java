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
package com.aionemu.gameserver.dao;

import com.aionemu.gameserver.model.team.legion.*;
import javolution.util.FastList;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.TreeMap;

public abstract class LegionDAO implements IDFactoryAwareDAO
{
	public abstract boolean isNameUsed(String name);
	public abstract boolean saveNewLegion(Legion legion);
	public abstract void storeLegion(Legion legion);
	public abstract Legion loadLegion(String legionName);
	public abstract Legion loadLegion(int legionId);
	public abstract void deleteLegion(int legionId);
	public abstract TreeMap<Timestamp, String> loadAnnouncementList(int legionId);
	public abstract boolean saveNewAnnouncement(int legionId, Timestamp currentTime, String message);

	@Override
	public final String getClassName() {
		return LegionDAO.class.getName();
	}

	public abstract void storeLegionEmblem(int legionId, LegionEmblem legionEmblem);
	public abstract void removeAnnouncement(int legionId, Timestamp key);
	public abstract LegionEmblem loadLegionEmblem(int legionId);
	public abstract LegionWarehouse loadLegionStorage(Legion legion);
	public abstract void loadLegionHistory(Legion legion);
	public abstract boolean saveNewLegionHistory(int legionId, LegionHistory legionHistory);
	public abstract void updateLegionDescription(Legion legion);
	public abstract void storeLegionJoinRequest(LegionJoinRequest legionJoinRequest);
	public abstract FastList<LegionJoinRequest> loadLegionJoinRequests(int legionId);
	public abstract void deleteLegionJoinRequest(int legionId, int playerId);
	public abstract void deleteLegionJoinRequest(LegionJoinRequest legionJoinRequest);
	public abstract Collection<Integer> getLegionIdsWithTerritories();
}