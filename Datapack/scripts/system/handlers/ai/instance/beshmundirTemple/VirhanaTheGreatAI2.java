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
package ai.instance.beshmundirTemple;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/****/
/** Author (Encom)
/****/

@AIName("virhana")
public class VirhanaTheGreatAI2 extends AggressiveNpcAI2
{
	private int count;
	private boolean isStart;
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (!isStart){
			isStart = true;
			scheduleRage();
		}
	}
	
	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		isStart = false;
	}
	
	private void scheduleRage() {
		if (isAlreadyDead() || !isStart) {
			return;
		}
		AI2Actions.useSkill(this, 19121); //Seal Of Reflection.
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				startRage();
			}
		}, 70000);
	}
	
	private void startRage() {
		if (isAlreadyDead() || !isStart) {
			return;
		} if (count < 12) {
			AI2Actions.useSkill(this, 18897); //Earthly Retribution.
			count++;
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startRage();
				}
			}, 10000);
		} else {
			count = 0;
			scheduleRage();
		}
	}
}