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
package ai.instance.draupnirCave;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author (Encom)
/****/

@AIName("akhal_phantasm")
public class Akhal_PhantasmAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startEvent();
	}
	
	private void startEvent() {
		switch (getNpcId()) {
			case 237276: //Akhal's Phantasm.
				//You shame us, Bakarma! Fallen to a flock of weak-fleshed Daevas? Your honor is in shambles.
				sendMsg(1403082, getObjectId(), false, 2000);
				//The Draupnir defense flame at Beritra's Oracle Chamber has been activated. If you approach the flame area, prepare to burn.
				sendMsg(1403084, getObjectId(), false, 6000);
				//Seeing all enemies slain, the Balaur exits defensive stance.
				sendMsg(1403085, getObjectId(), false, 10000);
				//Hath our fated plans fallen prey to sabatoge? Impede the intruders, Bakarma. Our Lord depends upon it!
				sendMsg(1403067, getObjectId(), false, 14000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						AI2Actions.deleteOwner(Akhal_PhantasmAI2.this);
				    }
			    }, 18000);
			break;
		}
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}