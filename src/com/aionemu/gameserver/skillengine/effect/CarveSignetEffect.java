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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.action.DamageType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

/**
 * @author ATracer
 * @Rework MATTY (ADev.Team)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarveSignetEffect")
public class CarveSignetEffect extends DamageEffect
{
	@XmlAttribute(required = true)
	protected int signetlvlstart;
	
	@XmlAttribute(required = true)
	protected int signetlvl;
	
	@XmlAttribute(required = true)
	protected int signetid;
	
	@XmlAttribute(required = true)
	protected String signet;
	
	@XmlAttribute(required = true)
	protected final float prob = 100;
	
	private int nextSignetLevel = 1;
	
	@Override
	public void applyEffect(Effect effect) {
		super.applyEffect(effect);
		if (Rnd.get(0, 100) > prob) {
			return;
		} if (signetlvl == 0) {
			signetlvl = 1;
		}
		Effect placedSignet = effect.getEffected().getEffectController().getAnormalEffect(signet);
		if (placedSignet != null) {
			placedSignet.endEffect();
		}
		nextSignetLevel = 1;
		if (placedSignet != null) {
			nextSignetLevel = placedSignet.getSkillId() - 8302 + 1;
			if (nextSignetLevel > signetlvl || nextSignetLevel > 5) {
				nextSignetLevel--;
			}
		} if (nextSignetLevel < signetlvlstart) {
			nextSignetLevel = signetlvlstart + 0;
		}
		effect.setCarvedSignet(nextSignetLevel);
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(8302 + nextSignetLevel);
		Effect newEffect = new Effect(effect.getEffector(), effect.getEffected(), template, effect.getCarvedSignet(), 0);
		newEffect.initialize();
		newEffect.applyEffect();
	}
	
	@Override
	public void calculate(Effect effect) {
		if (!super.calculate(effect, DamageType.PHYSICAL)) {
			return;
		}
	}
}