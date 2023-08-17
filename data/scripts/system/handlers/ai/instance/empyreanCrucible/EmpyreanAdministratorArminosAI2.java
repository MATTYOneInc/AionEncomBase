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
package ai.instance.empyreanCrucible;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.services.NpcShoutsService;

/****/
/** Author (Encom)
/****/

@AIName("empadministratorarminos")
public class EmpyreanAdministratorArminosAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startEvent();
	}
	
	private void startEvent() {
		switch (getNpcId()) {
			case 217744: //Administrator Arminos. [3th Floor]
				sendMsg(1500247, getObjectId(), false, 8000);
				sendMsg(1500250, getObjectId(), false, 20000);
				sendMsg(1500251, getObjectId(), false, 60000);
			break;
			case 217749: //Administrator Arminos. [4th Floor]
				sendMsg(1500252, getObjectId(), false, 8000);
				sendMsg(1500253, getObjectId(), false, 16000);
				sendMsg(1400982, 0, false, 25000);
				sendMsg(1400988, 0, false, 27000);
				sendMsg(1400989, 0, false, 29000);
				sendMsg(1400990, 0, false, 31000);
				sendMsg(1401013, 0, false, 93000);
				sendMsg(1401014, 0, false, 113000);
				sendMsg(1401015, 0, false, 118000);
				sendMsg(1500255, getObjectId(), true, 118000);
			break;
		}
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}