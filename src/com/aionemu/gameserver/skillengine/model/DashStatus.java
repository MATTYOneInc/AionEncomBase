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
package com.aionemu.gameserver.skillengine.model;

public enum DashStatus {

	NONE(0),
	RANDOMMOVELOC(1),
	DASH(2),
	BACKDASH(3),
	MOVEBEHIND(4),
	RIDERMOVELOC(6);
	
	private int id;
	
	private DashStatus(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}