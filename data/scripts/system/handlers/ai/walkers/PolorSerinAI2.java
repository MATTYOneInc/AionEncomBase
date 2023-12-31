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
package ai.walkers;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.MoveEventHandler;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.utils.MathUtil;
import org.apache.commons.lang.ArrayUtils;

/****/
/** Author (Encom)
/****/

@AIName("polorserin")
public class PolorSerinAI2 extends WalkGeneralRunnerAI2
{
	static final int[] stopAdults = { 203129, 203132 };
	
	@Override
	protected void handleMoveArrived() {
		boolean adultsNear = false;
		for (VisibleObject object : getOwner().getKnownList().getKnownObjects().values()) {
			if (object instanceof Npc) {
				Npc npc = (Npc)object;
				if (!ArrayUtils.contains(stopAdults, npc.getNpcId()))
					continue;
				if (MathUtil.isIn3dRange(npc, getOwner(), getOwner().getAggroRange())) {
					adultsNear = true;
					break;
				}
			}
		} if (adultsNear) {
			MoveEventHandler.onMoveArrived(this);
			getOwner().unsetState(CreatureState.WEAPON_EQUIPPED);
		} else {
			super.handleMoveArrived();
			getOwner().setState(CreatureState.WEAPON_EQUIPPED);
		}
	}
}