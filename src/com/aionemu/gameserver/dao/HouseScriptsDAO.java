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

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;

public abstract class HouseScriptsDAO implements DAO {

	@Override
	public final String getClassName() {
		return HouseScriptsDAO.class.getName();
	}

	public abstract PlayerScripts getPlayerScripts(int paramInt);

	public abstract void addScript(int paramInt1, int paramInt2, String paramString);

	public abstract void updateScript(int paramInt1, int paramInt2, String paramString);

	public abstract void deleteScript(int paramInt1, int paramInt2);
}