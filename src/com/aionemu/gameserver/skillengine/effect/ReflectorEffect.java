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

import com.aionemu.gameserver.controllers.observer.AttackCalcObserver;
import com.aionemu.gameserver.controllers.observer.AttackShieldObserver;
import com.aionemu.gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReflectorEffect")
public class ReflectorEffect extends ShieldEffect
{
	@Override
	public void startEffect(final Effect effect) {
		int hit = hitvalue + hitdelta * effect.getSkillLevel();
		AttackShieldObserver asObserver = new AttackShieldObserver(hit, value, percent, false, effect, hitType, this.getType(), this.hitTypeProb, minradius, radius, null, 0, 0);
		effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
		effect.setAttackShieldObserver(asObserver, position);
	}
	
	@Override
	public void endEffect(Effect effect) {
		AttackCalcObserver acObserver = effect.getAttackShieldObserver(position);
		if (acObserver != null) {
			effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
		}
	}
	
	@Override
	public int getType() {
		return 1;
	}
}