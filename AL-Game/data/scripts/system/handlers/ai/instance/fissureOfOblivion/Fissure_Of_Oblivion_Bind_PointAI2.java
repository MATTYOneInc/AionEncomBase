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
package ai.instance.fissureOfOblivion;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;

/****/
/** Author (Encom)
/****/

@AIName("Fissure_Of_Oblivion_Bind_Point")
public class Fissure_Of_Oblivion_Bind_PointAI2 extends NpcAI2
{
	@Override
    protected void handleCreatureSee(Creature creature) {
        checkDistance(this, creature);
    }
	
    @Override
    protected void handleCreatureMoved(Creature creature) {
        checkDistance(this, creature);
    }
	
	private void checkDistance(NpcAI2 ai, Creature creature) {
        if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
        	if (MathUtil.isIn3dRange(getOwner(), creature, 10)) {
        		FissureOfOblivionBindPoint();
        	}
        }
    }
	
	private void FissureOfOblivionBindPoint() {
		AI2Actions.deleteOwner(Fissure_Of_Oblivion_Bind_PointAI2.this);
		spawn(281446, 404.32239f, 513.28625f, 342.31073f, (byte) 0);
		spawn(730845, 404.32239f, 513.28625f, 342.31073f, (byte) 0, 56);
    }
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}