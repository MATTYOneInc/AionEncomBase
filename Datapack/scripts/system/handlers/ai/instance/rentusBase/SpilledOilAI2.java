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
package ai.instance.rentusBase;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("spilled_oil")
public class SpilledOilAI2 extends AggressiveNpcAI2
{
	private Future<?> attackOilSoakTask;
	
	@Override
	public void think() {
	}
	
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		attackOilSoak();
		startLifeTask();
    }
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(SpilledOilAI2.this);
			}
		}, 20000); //20 Secondes.
	}
	
	private void attackOilSoak() {
		attackOilSoakTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.targetCreature(SpilledOilAI2.this, getPosition().getWorldMapInstance().getNpc(217311)); //Kuhara The Volatile.
				AI2Actions.targetCreature(SpilledOilAI2.this, getPosition().getWorldMapInstance().getNpc(236298)); //Kuhara The Volatile.
				AI2Actions.useSkill(SpilledOilAI2.this, 19658); //Oil Soak.
			}
		}, 3000, 8000);
	}
	
	private void delete() {
		AI2Actions.deleteOwner(this);
	}
}