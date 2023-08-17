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
package ai.instance.tiamatStronghold;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("bladestorm")
public class BladeStormAI2 extends AggressiveNpcAI2
{
	private Future<?> stormBladeTask;
	
	@Override
	public void think() {
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		stormBlade();
		startLifeTask();
	}
	
	private void stormBlade() {
		stormBladeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.targetCreature(BladeStormAI2.this, getPosition().getWorldMapInstance().getNpc(219357)); //Adjudant Anuhart.
				AI2Actions.targetCreature(BladeStormAI2.this, getPosition().getWorldMapInstance().getNpc(247717)); //F4_Raid_Drakan_Boss_55_Ah.
				AI2Actions.useSkill(BladeStormAI2.this, 20748); //Storm Blade.
			}
		}, 3000, 8000);
	}
	
    private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(BladeStormAI2.this);
			}
		}, 10000);
	}
	
    @Override
	public boolean isMoveSupported() {
		return false;
	}
}