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
package com.aionemu.gameserver.model.autogroup;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldMapInstance;

public interface AutoInstanceHandler
{
	public abstract void initsialize(int instanceMaskId);
	public abstract void onInstanceCreate(WorldMapInstance instance);
	public abstract AGQuestion addPlayer(Player player, SearchInstance searchInstance);
	public abstract void onEnterInstance(Player player);
	public abstract void onLeaveInstance(Player player);
	public abstract void onPressEnter(Player player);
	public abstract void unregister(Player player);
	public abstract void clear();
}