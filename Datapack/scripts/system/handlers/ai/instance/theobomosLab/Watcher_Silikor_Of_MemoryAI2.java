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
package ai.instance.theobomosLab;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("watcher_silikor_of_memory")
public class Watcher_Silikor_Of_MemoryAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{50, 25, 10});
	}
	
	private void checkPercentage(int hpPercentage) {
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						sp(281054);
						sp(281053);
					break;
					case 25:
						sp(281054);
						sp(281053);
					break;
					case 10:
						sp(281054);
						sp(281053);
					break;
				}
			}
		}
	}
	
	private void sp(int npcId) {
		float direction = Rnd.get(0, 199) / 100f;
		int distance = Rnd.get(0, 2);
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (this.getNpcId()) {
			case 237248: //Watcher Silikor Of Memory.
				SkillEngine.getInstance().getSkill(getOwner(), 18481, 1, getOwner()).useSkill();
			break;
		}
	}
	
	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}
	
	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}
	
	@Override
	protected void handleDied() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(281054));
			deleteNpcs(instance.getNpcs(281053));
		}
		percents.clear();
		super.handleDied();
	}
}