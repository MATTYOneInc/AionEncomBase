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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelayedFPAttackInstantEffect")
public class DelayedFPAttackInstantEffect extends EffectTemplate {

	@XmlAttribute
	protected int delay;
	@XmlAttribute
	protected boolean percent;

	@Override
	public void calculate(Effect effect) {
		if (!(effect.getEffected() instanceof Player)) {
			return;
		}
		if (!super.calculate(effect, null, null)) {
			return;
		}
		int maxFP = ((Player) effect.getEffected()).getLifeStats().getMaxFp();
		int newValue = (percent) ? (int) ((maxFP * value) / 100) : value;

		effect.setReserved2(newValue);
	}

	@Override
	public void applyEffect(final Effect effect) {
		final Player effected = (Player) effect.getEffected();
		final int newValue = effect.getReserved2();

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				effected.getLifeStats().reduceFp(newValue);
			}
		}, delay);
	}
}