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
package com.aionemu.gameserver.skillengine.model;

public enum SkillMoveType {

	RESIST(0),
	DEFAULT(16),
	PULL(50), // OLD 18 NEW 50 (5.6)
	OPENAERIAL(20),
	KNOCKBACK(28),
	MOVEBEHIND(48),
	STAGGER(112), //5.1
	STUMBLE(16), //5.1
	NEWPULL(54); //5.1
	
	private int id;
	
	private SkillMoveType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}