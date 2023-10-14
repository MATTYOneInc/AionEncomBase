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
package com.aionemu.gameserver.services.towerofeternityservice;

import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityLocation;
import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityStateType;

/**
 * Created by Wnkrz on 22/08/2017.
 */

public class Tower extends TowerOfEternity<TowerOfEternityLocation> {
	public Tower(TowerOfEternityLocation towerOfEternity) {
		super(towerOfEternity);
	}

	@Override
	protected void startTowerOfEternity() {
		getTowerOfEternityLocation().setActiveTowerOfEternity(this);
		despawn();
		spawn(TowerOfEternityStateType.OPEN);
	}

	@Override
	protected void stopTowerOfEternity() {
		getTowerOfEternityLocation().setActiveTowerOfEternity(null);
		despawn();
		spawn(TowerOfEternityStateType.CLOSED);
	}
}