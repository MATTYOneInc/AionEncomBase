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

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import javolution.util.FastMap;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("vritra_power_device")
public class GuardPostGeneratorAI2 extends NpcAI2
{
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();
	
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 22776, 1, getOwner()).useNoAnimationSkill();
				SkillEngine.getInstance().getSkill(getOwner(), 22781, 1, getOwner()).useNoAnimationSkill();
				SkillEngine.getInstance().getSkill(getOwner(), 22783, 1, getOwner()).useNoAnimationSkill();
			}
		}, 1000);
    }
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 230413: //Guard Post Generator.
				SpawnTemplate deviceBroken1 = SpawnEngine.addNewSingleTimeSpawn(210070000, 230417, 1513.6941f, 2400.3616f, 190.09221f, (byte) 0);
				deviceBroken1.setEntityId(951);
				objects.put(230417, SpawnEngine.spawnObject(deviceBroken1, 1));
				AI2Actions.deleteOwner(GuardPostGeneratorAI2.this);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(230417);
						SpawnTemplate deviceBroken2 = SpawnEngine.addNewSingleTimeSpawn(210070000, 230413, 1513.6941f, 2400.3616f, 190.09221f, (byte) 0);
						deviceBroken2.setEntityId(953);
						objects.put(230413, SpawnEngine.spawnObject(deviceBroken2, 1));
				    }
			    }, 300000); //5 Minutes.
			break;
			case 230416: //Guard Post Generator.
				SpawnTemplate deviceBroken3 = SpawnEngine.addNewSingleTimeSpawn(210070000, 230417, 1755.8412f, 1714.2434f, 199.66138f, (byte) 0);
				deviceBroken3.setEntityId(1192);
				objects.put(230417, SpawnEngine.spawnObject(deviceBroken3, 1));
				AI2Actions.deleteOwner(GuardPostGeneratorAI2.this);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(230417);
						SpawnTemplate deviceBroken4 = SpawnEngine.addNewSingleTimeSpawn(210070000, 230416, 1755.8412f, 1714.2434f, 199.66138f, (byte) 0);
						deviceBroken4.setEntityId(1191);
						objects.put(230416, SpawnEngine.spawnObject(deviceBroken4, 1));
				    }
			    }, 300000); //5 Minutes.
			break;
		}
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
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