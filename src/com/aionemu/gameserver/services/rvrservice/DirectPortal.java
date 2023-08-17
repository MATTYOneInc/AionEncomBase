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
package com.aionemu.gameserver.services.rvrservice;

import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.rvr.RvrStateType;

/**
 * @author Rinzler (Encom)
 */

public class DirectPortal extends Rvrlf3df3<RvrLocation>
{
	public DirectPortal(RvrLocation rvr) {
		super(rvr);
	}
	
	@Override
	public void startRvr() {
		getRvrLocation().setActiveRvr(this);
		despawn();
		spawn(RvrStateType.RVR);
	}
	
	@Override
	public void stopRvr() {
		getRvrLocation().setActiveRvr(null);
		despawn();
		spawn(RvrStateType.PEACE);
	}
}