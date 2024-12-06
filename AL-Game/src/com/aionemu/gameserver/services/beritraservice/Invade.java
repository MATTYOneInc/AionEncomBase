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
package com.aionemu.gameserver.services.beritraservice;

import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.beritra.BeritraStateType;

/**
 * @author Rinzler (Encom)
 */

public class Invade extends BeritraInvasion<BeritraLocation> {
	public Invade(BeritraLocation beritra) {
		super(beritra);
	}

	@Override
	public void startBeritraInvasion() {
		getBeritraLocation().setActiveBeritra(this);
		despawn();
		spawn(BeritraStateType.INVASION);
	}

	@Override
	public void stopBeritraInvasion() {
		getBeritraLocation().setActiveBeritra(null);
		despawn();
		spawn(BeritraStateType.PEACE);
	}
}