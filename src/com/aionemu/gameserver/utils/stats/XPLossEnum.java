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
package com.aionemu.gameserver.utils.stats;

public enum XPLossEnum
{
	LEVEL_6(6, 1.0),
	LEVEL_10(10, 1.0),
	LEVEL_20(20, 1.0),
	LEVEL_30(30, 1.0),
	LEVEL_40(40, 1.0),
	LEVEL_50(50, 1.0),
	LEVEL_60(60, 1.0),
	//XP-LossEnum 5.0
	LEVEL_70(70, 0.25),
	LEVEL_75(75, 0.25),
	LEVEL_80(80, 0.25),
	LEVEL_83(83, 0.25);	
	
	private int level;
	private double param;
	
	private XPLossEnum(int level, double param) {
		this.level = level;
		this.param = param;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getParam() {
		return param;
	}
	
	public static long getExpLoss(int level, long expNeed) {
		if (level < 11) { //5.0
			return 0;
		} for (XPLossEnum xpLossEnum : values()) {
			if (level <= xpLossEnum.getLevel()) {
				return Math.round(expNeed / 100 * xpLossEnum.getParam());
			}
		}
		return 0;
	}
}