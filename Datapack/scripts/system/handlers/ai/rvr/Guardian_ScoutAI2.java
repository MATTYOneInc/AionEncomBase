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

@AIName("guardian_scout")
public class Guardian_ScoutAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 240971: //Guardian Warrior.
			case 240972: //Guardian Mage.
			case 240973: //Guardian Scout.
			case 240974: //Guardian Marksman.
				startLifeTask();
			break;
        }
		//Victory is a certainty for us Elyos, yet you continue your futile struggles.
		sendMsg(1501537, getObjectId(), false, 5000);
		//You're no fools. Haven't you yet realized that this isn't a battle you can win ?
		sendMsg(1501538, getObjectId(), false, 8000);
		//Are there really this many Asmodians ignorant of their fate… ?
		//Then I'll teach you here and now. That today is your last day alive!
		sendMsg(1501539, getObjectId(), false, 11000);
		//Lord Ariel! Please show your power to that Asmodian!
		sendMsg(1501541, getObjectId(), false, 14000);
	}
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(Guardian_ScoutAI2.this);
			}
		}, 300000); //5 Minutes.
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}