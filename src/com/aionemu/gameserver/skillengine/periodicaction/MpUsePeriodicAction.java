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
package com.aionemu.gameserver.skillengine.periodicaction;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author antness
 */
public class MpUsePeriodicAction extends PeriodicAction {

	@XmlAttribute(name = "value")
	protected int value;

	@Override
	public void act(Effect effect) {
		Creature effected = effect.getEffected();
		int maxMp = effected.getGameStats().getMaxMp().getCurrent();
		int requiredMp = (int) (maxMp * (value / 100f));
		if (effected.getLifeStats().getCurrentMp() < requiredMp) {
			effect.endEffect();
			return;
		}
		effected.getLifeStats().reduceMp(requiredMp);
	}
}