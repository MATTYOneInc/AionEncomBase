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
package ai.instance.trialsOfEternity;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.*;

/****/
/** Author (Encom)
/****/

@AIName("Dimension_Boss_Portal")
public class Boliag_TentaclesAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 246937:
			    spawn(246724, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0, 1018);
		    break;
			case 247024:
			    spawn(246724, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0, 991);
			break;
			case 247025:
			    spawn(246724, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0, 1016);
			break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
}