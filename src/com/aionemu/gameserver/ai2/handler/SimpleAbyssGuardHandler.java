/*

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
package com.aionemu.gameserver.ai2.handler;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.geo.GeoService;

public class SimpleAbyssGuardHandler
{
	public static void onCreatureMoved(NpcAI2 npcAI, Creature creature) {
		checkAggro(npcAI, creature);
	}
	
	public static void onCreatureSee(NpcAI2 npcAI, Creature creature) {
		checkAggro(npcAI, creature);
	}
	
	protected static void checkAggro(NpcAI2 ai, Creature creature) {
		if (!(creature instanceof Npc)) {
			CreatureEventHandler.checkAggro(ai, creature);
			return;
		}
		Npc owner = ai.getOwner();
		if (creature.getLifeStats().isAlreadyDead() || !owner.canSee(creature)) {
			return;
		}
		Npc npc = ((Npc) creature);
		if (npc.getNpcType() != NpcType.ATTACKABLE && npc.getNpcType() != NpcType.AGGRESSIVE || npc.getLevel() < 2) {
			return;
		}
		if (creature.getTarget() != null) {
			return;
		}
		if (!owner.getActiveRegion().isMapRegionActive()) {
			return;
		}
		if (!ai.isInState(AIState.FIGHT) && (MathUtil.isIn3dRange(owner, creature, owner.getObjectTemplate().getAggroRange()))) {
			if (GeoService.getInstance().canSee(owner, creature)) {
				if (!ai.isInState(AIState.RETURNING))
				ai.getOwner().getMoveController().storeStep();
				ai.onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
			}
		}
	}
}