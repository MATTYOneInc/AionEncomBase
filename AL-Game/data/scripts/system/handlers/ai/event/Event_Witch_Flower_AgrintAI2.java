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

import com.aionemu.gameserver.ai2.*;

/****/
/** Author (Encom)
/****/

@AIName("Event_Witch_Flower_Agrint")
public class Event_Witch_Flower_AgrintAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 248365: //마녀�?� 나무 I.
			case 248366: //마녀�?� 나무 II.
				spawn(835678, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
			break;
			case 248367: //마녀�?� 나무 I.
			case 248368: //마녀�?� 나무 II.
				spawn(835679, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
			break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
	
	@Override
	public int modifyOwnerDamage(int damage) {
		return 1;
	}
	
	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}