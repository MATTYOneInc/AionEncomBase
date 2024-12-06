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
import com.aionemu.gameserver.model.base.BaseLocation;

public abstract class BaseDAO implements DAO {
	public abstract boolean loadBaseLocations(Map<Integer, BaseLocation> locations);

	public abstract boolean updateBaseLocation(BaseLocation location);

	public void updateLocation(final BaseLocation location) {
		updateBaseLocation(location);
	}

	@Override
	public String getClassName() {
		return BaseDAO.class.getName();
	}
}