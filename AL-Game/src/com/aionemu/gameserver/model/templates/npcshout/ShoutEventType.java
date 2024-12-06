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
package com.aionemu.gameserver.model.templates.npcshout;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ShoutEventType")
@XmlEnum
public enum ShoutEventType {
	IDLE, ATTACKED, ATTACK_BEGIN, ATTACK_END, ATTACK_K, SUMMON_ATTACK, CASTING, CAST_K, DIED, HELP, HELPCALL,
	WALK_WAYPOINT, START, WAKEUP, SLEEP, RESET_HATE, UNK_ACC, WALK_DIRECTION, STATUP, SWITCH_TARGET, SEE, PLAYER_MAGIC,
	PLAYER_SNARE, PLAYER_DEBUFF, PLAYER_SKILL, PLAYER_SLAVE, PLAYER_BLOW, PLAYER_PULL, PLAYER_PROVOKE, PLAYER_CAST,
	GOD_HELP, LEAVE, BEFORE_DESPAWN, ATTACK_DEADLY, WIN, ENEMY_DIED, ENTER_BATTLE, LEAVE_BATTLE, DEFORM_SKILL,
	ATTACK_HITPOINT;

	public String value() {
		return name();
	}

	public static ShoutEventType fromValue(String v) {
		return valueOf(v);
	}
}