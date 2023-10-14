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
package com.aionemu.gameserver.model;

/**
 * Chat types that are supported by Aion.
 *
 * @author SoulKeeper, Imaginary
 */
public enum ChatType {
	NORMAL(0x00), // Normal chat (White)
	SHOUT(0x03), // Shout chat (Orange)
	WHISPER(0x04), // Whisper chat (Green)
	GROUP(0x05), // Group chat (Blue)
	ALLIANCE(0x06), // Alliance chat (Aqua)
	GROUP_LEADER(0x07), // Group Leader chat (???)
	LEAGUE(0x08), // League chat (Dark Blue)
	LEAGUE_ALERT(0x09), // League chat (Orange)
	LEGION(0x0A), // Legion chat (Green)
	SHOUT2(0x0C), // Shout chat (Orange) visible in "All" chat thumbnail only !
	COMMAND(0x1A), // Command chat (Yellow)
	COALITION(0x1B), // Command chat (Blue)
	COALITION_ALERT(0x1C), // Coalition chat (Orange)
	ANNOUNCE(0x37), // Announce.

	CH1(0x0E), CH2(0x0F), CH3(0x10), CH4(0x11), CH5(0x12), CH6(0x13), CH7(0x14), CH8(0x15), CH9(0x16), CH10(0x17),

	/**
	 * Global chat types
	 */
	GOLDEN_YELLOW(0x20, true), // Same with 0x21 System message (Dark Yellow), most commonly used, no "center"
								// equivalent.

	YELLOW(0x22, true), // System message (Yellow), visible in "All" chat thumbnail only !
	WHITE(0x23, true), // System message (White), visible in "All" chat thumbnail only !
	BRIGHT_YELLOW(0x25, true), // System message (Light Yellow), visible in "All" chat thumbnail only !
	WHITE_CENTER(0x26, true), // Periodic Notice (White && Box on screen center)
	YELLOW_CENTER(0x27, true), // Periodic Announcement(Yellow && Box on screen center)
	BRIGHT_YELLOW_CENTER(0x28, true); // System Notice (Light Yellow && Box on screen center)

	private final int intValue;
	private boolean sysMsg;

	/**
	 * Constructor client chat type integer representation
	 */
	private ChatType(int intValue) {
		this(intValue, false);
	}

	/**
	 * Converts ChatType value to integer representation
	 *
	 * @return chat type in client
	 */
	public int toInteger() {
		return intValue;
	}

	/**
	 * Returns ChatType by it's integer representation
	 *
	 * @param integerValue integer value of chat type
	 * @return ChatType
	 * @throws IllegalArgumentException if can't find suitable chat type
	 */
	public static ChatType getChatTypeByInt(int integerValue) throws IllegalArgumentException {
		for (ChatType ct : ChatType.values()) {
			if (ct.toInteger() == integerValue) {
				return ct;
			}
		}

		throw new IllegalArgumentException("Unsupported chat type: " + integerValue);
	}

	private ChatType(int intValue, boolean sysMsg) {
		this.intValue = intValue;
		this.sysMsg = sysMsg;
	}

	/**
	 * @return true if this is one of system message ( all races can read chat )
	 */
	public boolean isSysMsg() {
		return sysMsg;
	}
}