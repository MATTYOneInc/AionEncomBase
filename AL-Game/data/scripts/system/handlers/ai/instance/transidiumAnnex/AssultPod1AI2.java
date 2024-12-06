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
package ai.instance.transidiumAnnex;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("assult_pod_1")
public class AssultPod1AI2 extends AggressiveNpcAI2
{
	@Override
	public void think() {
	}
	
	private AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 5) {
				if (startedEvent.compareAndSet(false, true)) {
					SkillEngine.getInstance().getSkill(getOwner(), 19358, 60, getOwner()).useNoAnimationSkill();
					SkillEngine.getInstance().getSkill(getOwner(), 19922, 60, getOwner()).useNoAnimationSkill();
					AI2Actions.deleteOwner(AssultPod1AI2.this);
					spawn(297188, 379.51096f, 395.966f, 688.8357f, (byte) 78);
					spawn(297188, 394.50833f, 385.5321f, 688.8357f, (byte) 70);
					spawn(297188, 397.13196f, 401.5456f, 688.86523f, (byte) 75);
					//FXMon_Smoke.
					spawn(297352, 379.51096f, 395.966f, 688.8357f, (byte) 78);
					spawn(297352, 394.50833f, 385.5321f, 688.8357f, (byte) 70);
					spawn(297352, 397.13196f, 401.5456f, 688.86523f, (byte) 75);
					spawn(297306, 379.51096f, 377.18109f, 688.83929f, (byte) 78, 164); //Belus Advance Corridor Shield.
					ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
					    public void run() {
							spawn(297190, 376.9965f, 392.8957f, 688.8357f, (byte) 77);
				        }
			        }, 1000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
					    public void run() {
							spawn(297190, 390.98514f, 383.46262f, 688.8357f, (byte) 71);
				        }
			        }, 3000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
					    public void run() {
							spawn(297191, 394.29895f, 398.75403f, 688.86523f, (byte) 74);
							spawn(297190, 378.6231f, 376.32443f, 689.0103f, (byte) 57);
							spawn(297190, 371.39615f, 382.39874f, 688.95496f, (byte) 95);
				        }
			        }, 5000);
				}
			}
		}
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}