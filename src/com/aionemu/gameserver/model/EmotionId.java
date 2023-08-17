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
package com.aionemu.gameserver.model;

public enum EmotionId
{
	NONE(0),
	LAUGH(1),
	ANGRY(2),
	SAD(3),
	POINT(5),
	YES(6),
	NO(7),
	VICTORY(8),
	CLAP(11),
	SIGH(12),
	SURPRISE(13),
	COMFORT(14),
	THANK(15),
	BEG(16),
	BLUSH(17),
	SMILE(28),
	SALUTE(29),
	PANIC(30),
	SORRY(31),
	THINK(33),
	DISLIKE(34),
	STAND(128),
	CASH_GOOD_DAY_FULL(133),
	CASH_U_AND_ME_FULL(134);
	
	private int id;
	
	private EmotionId(int id) {
		this.id = id;
	}
	
	public int id() {
		return id;
	}
}