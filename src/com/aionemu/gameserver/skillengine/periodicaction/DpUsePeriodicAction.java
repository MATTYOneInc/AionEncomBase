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

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;

 /**
 * @Rework MATTY (ADev.Team)
 */

public class DpUsePeriodicAction extends PeriodicAction
{
	@XmlAttribute(name = "value")
	protected int value;
	
	@Override
	public void act(final Effect effect) {
		final Player effector = (Player) effect.getEffector();
		int currentDp = effector.getCommonData().getDp();
		if (currentDp <= 0 || currentDp < value) {
			effect.endEffect();
			return;
		}
		effector.getCommonData().setDp(currentDp - value);
	}
}