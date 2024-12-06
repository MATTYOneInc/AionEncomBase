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
package ai.worlds.tiamaranta_eye;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.model.gameobjects.Creature;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("Limb_Render")
public class Limb_RenderAI2 extends NpcAI2
{
	int attackCount;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
    public void handleAttack(Creature creature) {
		if (isAggred.compareAndSet(false, true)) {
			//A Limb Render is under attack. Defeat the player attacking the crystal.
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1401462);
		}
        attackCount++;
        if (attackCount == 195) {
            attackCount = 0;
			AI2Actions.useSkill(this, 20655); //Crystal Frgament.
			//A Limb Render has exploded.
            NpcShoutsService.getInstance().sendMsg(getOwner(), 1401463);
        }
		super.handleAttack(creature);
    }
	
	@Override
	protected void handleSpawned() {
  		switch (getNpcId()) {
			//Limb Render.
			case 283072:
			case 858016:
			    //A Limb Render has appeared. It explodes when destroyed, inflicting serious damage to those nearby.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1401461);
			break;
		}
		super.handleSpawned();
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
	
	@Override
	public int modifyOwnerDamage(int damage) {
		return 1;
	}
	
	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}