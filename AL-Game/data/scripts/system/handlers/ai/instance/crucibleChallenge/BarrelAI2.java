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
package ai.instance.crucibleChallenge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;

/****/
/** Author (Encom)
/****/

@AIName("barrel")
public class BarrelAI2 extends NpcAI2
{
	@Override
	protected void handleDied() {
		super.handleDied();
		int npcId = 0;
		switch (getNpcId()) {
			case 217840: //Meat Barrel.
				npcId = 217841; //Wafer Thin Meet.
			break;
			case 218560: //Aether Barrel.
				npcId = 218561; //Aether Lump.
			break;
		}
		spawn(npcId, 1298.4448f, 1728.3262f, 316.8472f, (byte) 63);
		AI2Actions.deleteOwner(this);
	}
}