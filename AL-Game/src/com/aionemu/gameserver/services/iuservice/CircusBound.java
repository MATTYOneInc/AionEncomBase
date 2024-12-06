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
package com.aionemu.gameserver.services.iuservice;

import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.iu.IuStateType;

/**
 * @author Rinzler (Encom)
 */

public class CircusBound extends Iu<IuLocation> {
	public CircusBound(IuLocation iu) {
		super(iu);
	}

	@Override
	public void startConcert() {
		getIuLocation().setActiveIu(this);
		despawn();
		spawn(IuStateType.OPEN);
	}

	@Override
	public void stopConcert() {
		getIuLocation().setActiveIu(null);
		despawn();
		spawn(IuStateType.CLOSED);
	}
}