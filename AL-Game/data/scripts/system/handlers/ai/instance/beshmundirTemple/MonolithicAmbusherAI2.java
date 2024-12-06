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
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;

/****/
/** Author (Encom)
/****/

@AIName("monolithicambusher")
public class MonolithicAmbusherAI2 extends AggressiveNpcAI2
{
	private boolean hasHelped;
	
	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		hasHelped = false;
	}
	
	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		if (!hasHelped) {
			hasHelped = true;
			help(creature);
		}
	}
	
	private void help(Creature creature) {
		for (VisibleObject object : getKnownList().getKnownObjects().values()) {
			if (object instanceof Npc && isInRange(object, 60)) {
				Npc npc = (Npc) object;
				if (!npc.getLifeStats().isAlreadyDead() && npc.getNpcId() == 216215 && (int) npc.getSpawn().getY() == (int) getSpawnTemplate().getY()) {
					npc.getAi2().onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
				}
			}
		}
	}
}