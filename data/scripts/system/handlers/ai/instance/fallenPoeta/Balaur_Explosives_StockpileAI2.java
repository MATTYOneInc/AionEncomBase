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
package ai.instance.fallenPoeta;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("Balaur_Explosives_Stockpile")
public class Balaur_Explosives_StockpileAI2 extends AggressiveNpcAI2
{
	private Future<?> attackBoostTask;
	
	@Override
	public void think() {
	}
	
	@Override
	protected void handleSpawned() {
		attackBoost();
		super.handleSpawned();
	}
	
	private void attackBoost() {
		attackBoostTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.targetCreature(Balaur_Explosives_StockpileAI2.this, getPosition().getWorldMapInstance().getNpc(243682)); //Lieutenant Anuhart.
				AI2Actions.useSkill(Balaur_Explosives_StockpileAI2.this, 0);
			}
		}, 3000, 8000);
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}