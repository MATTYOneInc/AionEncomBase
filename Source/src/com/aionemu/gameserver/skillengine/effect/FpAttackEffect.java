/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FpAttackEffect")
public class FpAttackEffect extends AbstractOverTimeEffect {

	@Override
	public void calculate(Effect effect) {
		// Only players have FP
		if (effect.getEffected() instanceof Player) {
			super.calculate(effect, null, null);
		}
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		Player effected = (Player) effect.getEffected();
		int maxFP = effected.getLifeStats().getMaxFp();
		int newValue = value;
		// Support for values in percentage
		if (percent) {
			newValue = (int) ((maxFP * value) / 100);
		}
		effected.getLifeStats().reduceFp(newValue);
	}
}