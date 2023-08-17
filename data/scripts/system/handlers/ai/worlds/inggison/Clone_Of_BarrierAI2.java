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
package ai.worlds.inggison;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;

/****/
/** Author (Encom)
/****/

@AIName("omega_clone")
public class Clone_Of_BarrierAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied() {
		for (VisibleObject object: getKnownList().getKnownObjects().values()) {
			if (object instanceof Npc) {
				Npc npc = (Npc) object;
				if (npc.getNpcId() == 216516) { //Omega.
					npc.getEffectController().removeEffect(18671); //Magic Ward.
					break;
				}
			}
		}
		super.handleDied();
	}
}