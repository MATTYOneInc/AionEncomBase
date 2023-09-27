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

import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author kecimis
 */
public class AbnormalStateCondition extends Condition {

	@XmlAttribute(required = true)
	protected AbnormalState value;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aionemu.gameserver.skillengine.condition.Condition#validate(com.aionemu.gameserver.skillengine.model.Skill)
	 */
	@Override
	public boolean validate(Skill env) {
		if (env.getFirstTarget() != null) {
			return (env.getFirstTarget().getEffectController().isAbnormalSet(value));
		}
		return false;
	}

	@Override
	public boolean validate(Effect effect) {
		if (effect.getEffected() != null) {
			return (effect.getEffected().getEffectController().isAbnormalSet(value));
		}
		return false;
	}
}