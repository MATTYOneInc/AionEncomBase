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
package com.aionemu.gameserver.questEngine.handlers;

/**
 * @author Rolandas
 *
 */
public enum HandlerResult {
	UNKNOWN, // allow other handlers to process
	SUCCESS, FAILED;

	public static HandlerResult fromBoolean(Boolean value) {
		if (value == null) {
			return HandlerResult.UNKNOWN;
		} else if (value) {
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}