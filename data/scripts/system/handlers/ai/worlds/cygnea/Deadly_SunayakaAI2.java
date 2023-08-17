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
package ai.worlds.cygnea;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("deadly_sunayaka")
public class Deadly_SunayakaAI2 extends AggressiveNpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			simmeringRage();
		}
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
  		rageOfTheDragonLords();
	}
	
 	private void simmeringRage() {
   		SkillEngine.getInstance().getSkill(getOwner(), 20651, 1, getOwner()).useNoAnimationSkill(); //Simmering Rage.
   		getOwner().getEffectController().removeEffect(8763);
 	}
	
 	private void rageOfTheDragonLords() {
   		SkillEngine.getInstance().getSkill(getOwner(), 8763, 1, getOwner()).useNoAnimationSkill(); //Rage of the Dragon Lords
 	}
}