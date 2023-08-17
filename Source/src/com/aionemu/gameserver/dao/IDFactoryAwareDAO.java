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

import com.aionemu.commons.database.dao.DAO;

/**
 * This interface is generic one for all DAO classes that are generating their id's using
 * {@link com.aionemu.gameserver.utils.idfactory.IDFactory}
 * 
 * @author SoulKeeper
 */
public interface IDFactoryAwareDAO extends DAO {

	/**
	 * Returns array of all id's that are used by this DAO
	 * 
	 * @return array of used id's
	 */
	public int[] getUsedIDs();
}