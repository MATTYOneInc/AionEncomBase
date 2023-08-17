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
package ai.instance.elementisForest;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author xTz
 */
@AIName("seed_hetgolem")
public class SeedHetgolemAI2 extends AggressiveNpcAI2 {

	@Override
	public void handleDied() {
		WorldPosition p = getPosition();
		if (p != null && p.getWorldMapInstance() != null) {
			spawn(282441, p.getX(), p.getY(), p.getZ(), p.getHeading());
			Npc npc = (Npc)spawn(282465, p.getX(), p.getY(), p.getZ(), p.getHeading());
			NpcActions.delete(npc);
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
		
	}
}