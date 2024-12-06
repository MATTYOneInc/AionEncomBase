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
package ai;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ai.BombTemplate;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author (Encom)
/****/

@AIName("bomb")
public class BombAi2 extends AggressiveNpcAI2
{
	private BombTemplate template;
	
	@Override
	protected void handleSpawned() {
		bombSkill();
	}
	
	private void bombSkill() {
		template = DataManager.AI_DATA.getAiTemplate().get(getNpcId()).getBombs().getBombTemplate();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				useSkill(template.getSkillId());
			}
		}, template.getCd());
	}
	
	private void useSkill(int skill) {
		AI2Actions.targetSelf(this);
		AI2Actions.useSkill(this, skill);
		int duration = DataManager.SKILL_DATA.getSkillTemplate(skill).getDuration();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(BombAi2.this);
			}
		}, duration != 0 ? duration + 1000 : 0);
	}
}