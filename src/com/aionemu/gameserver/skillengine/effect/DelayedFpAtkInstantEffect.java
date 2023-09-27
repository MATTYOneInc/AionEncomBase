/*

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
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAttribute;

public class DelayedFpAtkInstantEffect extends EffectTemplate {

	@XmlAttribute
	protected int delay;

	@XmlAttribute
	protected boolean percent;

	public void calculate(Effect effect) {
		if ((effect.getEffected() instanceof Player)) {
			super.calculate(effect, null, null);
		}
	}

	public void applyEffect(final Effect effect) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			public void run() {
				if (effect.getEffector().isEnemy(effect.getEffected())) {
					DelayedFpAtkInstantEffect.this.calculateAndApplyDamage(effect);
				}
			}
		}, delay);
	}

	private void calculateAndApplyDamage(Effect effect) {
		if (!(effect.getEffected() instanceof Player)) {
			return;
		}
		int valueWithDelta = value + delta * effect.getSkillLevel();
		Player player = (Player) effect.getEffected();
		int maxFP = player.getLifeStats().getMaxFp();

		int newValue = valueWithDelta;

		if (percent) {
			newValue = maxFP * valueWithDelta / 100;
		}
		player.getLifeStats().reduceFp(newValue);
	}
}