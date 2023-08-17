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
package ai.instance.trialsOfEternity;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.skillengine.SkillEngine;

/****/
/** Author (Encom)
/****/

@AIName("IDEternity_03_Def_Boss_Energy")
public class Obelisk_Of_GaneshAI2 extends NpcAI2
{
	@Override
	public void think() {
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 17746, 60, getOwner()).useNoAnimationSkill();
	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 246421: //Obelisk Of Ganesh.
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(246421, 408.66196f, 1013.1304f, 711.93115f, (byte) 0, 177); //Obelisk Of Ganesh.
					}
				}, 120000);
			break;
			case 246422: //Obelisk Of Ganesh.
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(246422, 408.77426f, 1037.3873f, 711.90881f, (byte) 0, 179); //Obelisk Of Ganesh.
					}
				}, 120000);
			break;
			case 246423: //Obelisk Of Ganesh.
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(246423, 386.68903f, 1037.3842f, 711.95770f, (byte) 0, 181); //Obelisk Of Ganesh.
					}
				}, 120000);
			break;
			case 246424: //Obelisk Of Ganesh.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(246424, 386.68146f, 1013.2744f, 711.93091f, (byte) 0, 183); //Obelisk Of Ganesh.
					}
				}, 120000);
			break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}