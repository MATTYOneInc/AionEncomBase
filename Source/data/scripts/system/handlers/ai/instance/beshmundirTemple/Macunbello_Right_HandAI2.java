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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.commons.network.util.ThreadPoolManager;

/****/
/** Author (Encom)
/****/

@AIName("MacunbelloRightHand")
public class Macunbello_Right_HandAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startLifeTask();
		this.setStateIfNot(AIState.FOLLOWING);
	}
	
	@Override
	protected void handleMoveArrived() {
		AI2Actions.targetCreature(Macunbello_Right_HandAI2.this, getPosition().getWorldMapInstance().getNpc(216245)); //Macunbello.
		AI2Actions.useSkill(Macunbello_Right_HandAI2.this, 19049); //Devour Soul.
	}
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(Macunbello_Right_HandAI2.this);
			}
		}, 33000);
	}
}