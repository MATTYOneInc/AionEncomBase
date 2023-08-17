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
package com.aionemu.gameserver.services;

/**
 * @author KorLightning (Encom)
 */

public class ColorChat
{
	/**
	 * @param message
	 * @param color
	 * @return
	 */
	public static String colorChat(String message, String color) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		int start = 0;
		for (char ch : message.toCharArray()) {
			if (index % 3 == 0) {
				if (start % 2 == 0) {
					if (start > 0) {
						sb.append(";" + color + "][color:");
					} else {
						sb.append("[color:");
					}
				} else if (start % 2 == 1) {
					if (index < message.length()) {
						sb.append(";" + color + "][color:");
					}
				}
				start++;
			}
			sb.append(String.valueOf(ch));
			index++;
		} if (start % 2 == 1) {
			sb.append(";" + color + "]");
		} if (sb.lastIndexOf("[color:") > sb.lastIndexOf(";" + color + "]")) {
			sb.append(";" + color + "]");
		}
		return sb.toString();
	}
}