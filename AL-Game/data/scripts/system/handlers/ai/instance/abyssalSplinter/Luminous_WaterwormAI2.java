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
package ai.instance.abyssalSplinter;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

/****/
/** Author (Encom)
/****/

@AIName("Luminous_Waterworm")
public class Luminous_WaterwormAI2 extends AggressiveNpcAI2
{
	@Override
	public void think() {
	}
	
    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                AI2Actions.targetCreature(Luminous_WaterwormAI2.this, getPosition().getWorldMapInstance().getNpc(216951)); //Pazuzu.
				AI2Actions.targetCreature(Luminous_WaterwormAI2.this, getPosition().getWorldMapInstance().getNpc(219554)); //Unstable Pazuzu.
                AI2Actions.useSkill(Luminous_WaterwormAI2.this, 19291); //Replenishment.
            }
        }, 3000);
    }
	
	@Override
	protected void handleDied() {
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}