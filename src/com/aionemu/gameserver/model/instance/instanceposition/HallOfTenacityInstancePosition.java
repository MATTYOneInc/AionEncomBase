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
package com.aionemu.gameserver.model.instance.instanceposition;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * 
 * @author Ranastic
 *
 */
public class HallOfTenacityInstancePosition extends GenerealInstancePosition {
	
	@Override
	public void port(Player player, int zone, int position) {
		switch (position) {
		case 1:
			switch (zone) {
				case 0:
					teleport(player, 384.1f, 285.5f, 231.1f, (byte) 90);
				break;
				case 1:
					teleport(player, 385.0f, 226.6f, 231.1f, (byte) 35);
				break;
			}
		    break;
		}
	}
}