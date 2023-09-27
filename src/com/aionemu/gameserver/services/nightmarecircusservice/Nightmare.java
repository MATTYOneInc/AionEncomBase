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
package com.aionemu.gameserver.services.nightmarecircusservice;

import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusLocation;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusStateType;

/**
 * @author Rinzler (Encom)
 */

public class Nightmare extends CircusInstance<NightmareCircusLocation>
{
	public Nightmare(NightmareCircusLocation nightmareCircus) {
		super(nightmareCircus);
	}
	
	@Override
	public void startNightmareCircus() {
		getNightmareCircusLocation().setActiveNightmareCircus(this);
		despawn();
		spawn(NightmareCircusStateType.OPEN);
	}
	
	@Override
	public void stopNightmareCircus() {
		getNightmareCircusLocation().setActiveNightmareCircus(null);
		despawn();
		spawn(NightmareCircusStateType.CLOSED);
	}
}