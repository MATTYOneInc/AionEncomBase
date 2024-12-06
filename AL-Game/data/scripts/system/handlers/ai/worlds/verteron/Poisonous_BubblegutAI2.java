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
package ai.worlds.verteron;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("poisonous_bubblegut")
public class Poisonous_BubblegutAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned() {
  		protectionFluid();
		super.handleSpawned();
	}
	
	private void protectionFluid() {
   		SkillEngine.getInstance().getSkill(getOwner(), 16447, 1, getOwner()).useNoAnimationSkill(); //Spout Sticky Protection Fluid.
 	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
		    case 210318: //Poisonous Bubblegut.
			    spawn(203195, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Kato.
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
				        despawnNpc(203195); //Kato.
				    }
			    }, 60000);
			break;	
		}
		super.handleDied();
	}
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
}