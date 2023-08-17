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
package ai.instance.danuarReliquary;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("venge_full_orb")
public class Venge_Full_OrbAI2 extends AggressiveNpcAI2
{
	private Future<?> task;
	
    @Override
    protected void handleSpawned() {
  	    super.handleSpawned();
		final int skill;
		switch (getNpcId()) {
			case 284443: //Sorcerer Queen Modor.
				skill = 21178;
		    break;
			default:
				skill = 0;
		} if (skill == 0) {
			return;
		}
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.useSkill(Venge_Full_OrbAI2.this, skill);
			}
		},0, 2000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(Venge_Full_OrbAI2.this);
			}
		}, 1000);
	}
	
	@Override
	public void handleDespawned() {
		task.cancel(true);
		super.handleDespawned();
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}