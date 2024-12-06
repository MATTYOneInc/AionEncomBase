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
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/****/
/** Author (Encom)
/****/

@AIName("divineartifact")
public class DivineArtifactAI2 extends AggressiveNpcAI2
{
	private boolean cooldown = false;
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (!cooldown) {
			AI2Actions.useSkill(this, 18915);
			setCD();
		}
	}
	
	private void setCD() {
		cooldown = true;
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				cooldown = false;
			}
		}, 1000);
	}
}