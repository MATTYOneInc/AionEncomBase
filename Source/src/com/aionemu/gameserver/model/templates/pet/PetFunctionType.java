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
package com.aionemu.gameserver.model.templates.pet;

/**
 * @author Rinzler
 * Formula: dataBitCount*2^5 OR id
 */
public enum PetFunctionType
{
	WAREHOUSE(0, true),
	FOOD(1, 64),
	DOPING(2, 256),
	LOOT(3, 8),
	APPEARANCE(1),
	NONE(4, true),
	CHEER(5), //need test
	MERCHAND(6), //need test
	BAG(-1),
	WING(-2);
	
	private short id;
	private boolean isPlayerFunc = false;
	
	PetFunctionType(int id, boolean isPlayerFunc) {
		this(id);
		this.isPlayerFunc = isPlayerFunc;
	}
	
	PetFunctionType(int id, int dataBitCount) {
        this(dataBitCount << 5 | id);
		this.isPlayerFunc = true;
	}
	
	PetFunctionType(int id) {
		this.id = (short) (id & 0xFFFF);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isPlayerFunction() {
		return isPlayerFunc;
	}
}