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
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("kuhara_bomb")
public class KuharaBombAI2 extends AggressiveNpcAI2
{
	private Npc kuharaTheVolatile1;
	private Npc kuharaTheVolatile2;
	private AtomicBoolean isDestroyed = new AtomicBoolean(false);
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		this.setStateIfNot(AIState.FOLLOWING);
		kuharaTheVolatile1 = getPosition().getWorldMapInstance().getNpc(217311); //Kuhara The Volatile.
		kuharaTheVolatile2 = getPosition().getWorldMapInstance().getNpc(236298); //Kuhara The Volatile.
	}
	
	@Override
	protected void handleMoveArrived() {
		if (isDestroyed.compareAndSet(false, true)) {
			if (kuharaTheVolatile1 != null && !NpcActions.isAlreadyDead(kuharaTheVolatile1)) {
				SkillEngine.getInstance().getSkill(getOwner(), 19659, 60, kuharaTheVolatile1).useNoAnimationSkill();  //Bomb Explosion.
			} else if (kuharaTheVolatile2 != null && !NpcActions.isAlreadyDead(kuharaTheVolatile2)) {
				SkillEngine.getInstance().getSkill(getOwner(), 19659, 60, kuharaTheVolatile2).useNoAnimationSkill();  //Bomb Explosion.
			}
		}
	}
}