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
package ai.rvr;

import com.aionemu.commons.network.util.ThreadPoolManager;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.services.NpcShoutsService;

/****/
/** Author (Encom)
/****/

@AIName("archon_scout")
public class Archon_ScoutAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 240887: //Archon Warrior.
			case 240888: //Archon Mage.
			case 240889: //Archon Scout.
			case 240890: //Archon Marksman.
				startLifeTask();
			break;
        }
		//Wretches!! Your resistance shall be futile.
		sendMsg(1501534, getObjectId(), false, 5000);
		//Let's show these cowardly Elyos the might of the Asmodians!
		sendMsg(1501535, getObjectId(), false, 8000);
		//Don't give up! The will of Empyrean Lord Azphel is with us.
		sendMsg(1501536, getObjectId(), false, 11000);
		//Empyrean Lord Azphel! Please give me strength.
		sendMsg(1501540, getObjectId(), false, 14000);
	}
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(Archon_ScoutAI2.this);
			}
		}, 300000); //5 Minutes.
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}