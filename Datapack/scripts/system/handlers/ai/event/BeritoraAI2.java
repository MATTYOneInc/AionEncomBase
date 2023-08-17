/*
 * This file is part of Encom.
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
package ai.event;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.world.WorldPosition;

/****/
/** Author (Encom)
/****/

@AIName("beritora")
public class BeritoraAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			spawn(832262, p.getX(), p.getY(), p.getZ(), (byte) 0); //Treasure Chest.
		}
		super.handleDied();
	}
}