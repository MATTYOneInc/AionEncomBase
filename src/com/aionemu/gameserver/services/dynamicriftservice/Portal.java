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
package com.aionemu.gameserver.services.dynamicriftservice;

import com.aionemu.gameserver.model.dynamicrift.DynamicRiftLocation;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftStateType;

/**
 * @author Rinzler (Encom)
 */

public class Portal extends DynamicRift<DynamicRiftLocation>
{
	public Portal(DynamicRiftLocation dynamicRift) {
		super(dynamicRift);
	}
	
	@Override
	public void startDynamicRift() {
		getDynamicRiftLocation().setActiveDynamicRift(this);
		despawn();
		spawn(DynamicRiftStateType.OPEN);
	}
	
	@Override
	public void stopDynamicRift() {
		getDynamicRiftLocation().setActiveDynamicRift(null);
		despawn();
		spawn(DynamicRiftStateType.CLOSED);
	}
}