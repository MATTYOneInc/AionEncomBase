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
package com.aionemu.gameserver.skillengine.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.FlyingRestriction;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelfFlyingCondition")
public class SelfFlyingCondition extends Condition {

	@XmlAttribute(required = true)
	protected FlyingRestriction restriction;

	@Override
	public boolean validate(Skill env) {
		if (env.getEffector() == null) {
			return false;
		}
		switch (restriction) {
		case FLY:
			return env.getEffector().isFlying();
		case GROUND:
			return !env.getEffector().isFlying();
		}
		return true;
	}

	@Override
	public boolean validate(Effect effect) {
		if (effect.getEffector() == null) {
			return false;
		}
		switch (restriction) {
		case FLY:
			return effect.getEffector().isFlying();
		case GROUND:
			return !effect.getEffector().isFlying();
		}
		return true;
	}
}