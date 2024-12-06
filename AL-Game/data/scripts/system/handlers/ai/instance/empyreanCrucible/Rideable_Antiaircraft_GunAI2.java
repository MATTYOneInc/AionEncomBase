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

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("rideable_antiaircraft_gun")
public class Rideable_Antiaircraft_GunAI2 extends ActionItemNpcAI2
{
	private AtomicBoolean canUse = new AtomicBoolean(true);
	
	@Override
	protected void handleDialogStart(Player player) {
		super.handleDialogStart(player);
	}
	
	@Override
	protected void handleUseItemFinish(Player player) {
		if (canUse.compareAndSet(true, false)) {
			int morphSkill = getMorphSkill();
			SkillEngine.getInstance().getSkill(getOwner(), morphSkill >> 8, morphSkill & 0xFF, player).useNoAnimationSkill();
			AI2Actions.deleteOwner(this);
		}
	}
	
	private int getMorphSkill() {
		switch (getNpcId()) {
			case 701199: //Rideable Antiaircraft Gun.
				return 0x4E5133;
		}
		return 0;
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}