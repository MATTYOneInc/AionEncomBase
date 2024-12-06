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
package ai.instance.secretMunitionsFactory;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.world.WorldPosition;

/****/
/** Author Rinzler, Ranastic (Encom)
/****/

@AIName("Mechaturerk")
public class MechaturerkAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}
	
	@Override
	protected void handleDied() {
		spawnMechaturerkCore(245185);
		spawn(703381, 138.86005f, 253.14404f, 191.8727f, (byte) 0); //Mechaturerk’s Footlocker.
		spawn(703382, 138.84244f, 249.96141f, 191.8727f, (byte) 0); //Mechaturerk’s Core.
		spawn(703383, 138.83214f, 246.4382f, 191.8727f, (byte) 0); //Destruction Golem’s Footlocker.
		spawn(833998, 152.87827f, 268.53104f, 191.8727f, (byte) 106); //Corridor To Atreia.
		spawn(834167, 149.93068f, 255.50876f, 191.8727f, (byte) 6); //Jay.
		super.handleDied();
	}
	
	private void spawnMechaturerkCore(int npcId) {
		rndSpawnInRange(npcId, Rnd.get(1, 5));
		rndSpawnInRange(npcId, Rnd.get(1, 5));
		rndSpawnInRange(npcId, Rnd.get(1, 5));
		rndSpawnInRange(npcId, Rnd.get(1, 5));
		rndSpawnInRange(npcId, Rnd.get(1, 5));
		rndSpawnInRange(npcId, Rnd.get(1, 5));
	}
	
	private Npc rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		return (Npc) spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), (byte) 0);
	}
}