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
package com.aionemu.gameserver.model.instance;

public enum InstanceScoreType {
	PREPARING(1 * 1024 * 1024), START_PROGRESS(2 * 1024 * 1024), END_PROGRESS(3 * 1024 * 1024);

	private int id;

	private InstanceScoreType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isPreparing() {
		return id == 1048576;
	}

	public boolean isStartProgress() {
		return id == 2097152;
	}

	public boolean isEndProgress() {
		return id == 3145728;
	}
}