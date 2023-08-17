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

import com.aionemu.gameserver.controllers.attack.AttackStatus;

public class AttackerCriticalStatusObserver extends AttackCalcObserver {

	protected AttackerCriticalStatus acStatus = null;
	protected AttackStatus status;

	public AttackerCriticalStatusObserver(AttackStatus status, int count, int value, boolean isPercent) {
		this.status = status;
		this.acStatus = new AttackerCriticalStatus(count, value, isPercent); 
	}

	public int getCount() {
		return acStatus.getCount();
	}

	public void decreaseCount() {
		acStatus.setCount((acStatus.getCount()-1));
	}
}