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
package ai.instance.aturamSkyFortress;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.services.NpcShoutsService;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("weaponh")
public class WeaponHAI2 extends AggressiveNpcAI2
{
	private boolean isHome = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		isHome = false;
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			//Abnormal object detected. Elimination beginning.
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1402787, 0);
			getPosition().getWorldMapInstance().getDoors().get(85).setOpen(true);
		}
	}
	
	@Override
	protected void handleBackHome() {
	    isHome = true;
		getPosition().getWorldMapInstance().getDoors().get(85).setOpen(false);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		getPosition().getWorldMapInstance().getDoors().get(85).setOpen(true);
		super.handleDied();
	}
}