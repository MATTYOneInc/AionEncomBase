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
package com.aionemu.gameserver.skillengine.effect.modifier;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetClassDamageModifier")
public class TargetClassDamageModifier extends ActionModifier {

	@XmlAttribute(name = "class")
	private PlayerClass skillTargetClass;

	@Override
	public int analyze(Effect effect) {
		Creature effected = effect.getEffected();
		if (effected instanceof Player) {
			Player player = (Player) effected;
			if (player.getPlayerClass() == skillTargetClass) {
				return value + effect.getSkillLevel() * delta;
			}
		}
		return 0;
	}

	@Override
	public boolean check(Effect effect) {
		Creature effected = effect.getEffected();
		if (effected instanceof Player) {
			Player player = (Player) effected;
			return player.getPlayerClass() == skillTargetClass;
		}
		return false;
	}
}