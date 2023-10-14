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
package com.aionemu.gameserver.dao;

import java.util.Map;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.outpost.OutpostLocation;

/**
 * Created by Wnkrz on 27/08/2017.
 */

public abstract class OutpostDAO implements DAO {
	public abstract boolean loadOutposLocations(Map<Integer, OutpostLocation> locations);

	public abstract boolean updateOutpostLocation(OutpostLocation location);

	public void updateLocation(final OutpostLocation location) {
		updateOutpostLocation(location);
	}

	@Override
	public String getClassName() {
		return OutpostDAO.class.getName();
	}
}