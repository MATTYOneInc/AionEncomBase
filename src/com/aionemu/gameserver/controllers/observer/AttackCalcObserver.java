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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;

import java.util.List;

/**
 * @author ATracer
 */
public class AttackCalcObserver {

	/**
	 * @param status
	 * @return false
	 */
	public boolean checkStatus(AttackStatus status) {
		return false;
	}

	/**
	 * @param attackList
	 * @param effect 
	 * @return value
	 */
	public void checkShield(List<AttackResult> attackList, Effect effect, Creature attacker) {

	}

	/**
	 * @param status
	 * @return
	 */
	public boolean checkAttackerStatus(AttackStatus status) {
		return false;
	}
	
	/**
	 * @param status
	 * @param isSkill
	 * @return
	 */
	public AttackerCriticalStatus checkAttackerCriticalStatus(AttackStatus status, boolean isSkill) {
		return new AttackerCriticalStatus(false);
	}

	/**
	 * @param isSkill
	 * @return physical damage multiplier
	 */
	public float getBasePhysicalDamageMultiplier(boolean isSkill) {
		return 1f;
	}

	/**
	 * @return magic damage multiplier
	 */
	public float getBaseMagicalDamageMultiplier() {
		return 1f;
	}
}