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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.NameConfig;

public class NameRestrictionService {

	private static final String ENCODED_BAD_WORD = "----";
	private static String[] forbiddenSequences;
	private static String[] forbiddenByClient;

	public static boolean isValidName(String name) {
		return NameConfig.CHAR_NAME_PATTERN.matcher(name).matches();
	}

	public static boolean isForbiddenWord(String name) {
		return isForbiddenByClient(name) || isForbiddenBySequence(name);
	}

	private static boolean isForbiddenByClient(String name) {
		if (!NameConfig.NAME_FORBIDDEN_ENABLE || NameConfig.NAME_FORBIDDEN_CLIENT.equals("")) {
			return false;
		}
		if ((forbiddenByClient == null) || (forbiddenByClient.length == 0)) {
			forbiddenByClient = NameConfig.NAME_FORBIDDEN_CLIENT.split(",");
		}
		for (String s : forbiddenByClient) {
			if (name.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isForbiddenBySequence(String name) {
		if (NameConfig.NAME_SEQUENCE_FORBIDDEN.equals("")) {
			return false;
		}
		if (forbiddenSequences == null || forbiddenSequences.length == 0) {
			forbiddenSequences = NameConfig.NAME_SEQUENCE_FORBIDDEN.toLowerCase().split(",");
		}
		for (String s : forbiddenSequences) {
			if (name.toLowerCase().contains(s)) {
				return true;
			}
		}
		return false;
	}

	public static String filterMessage(String message) {
		for (String word : message.split(" ")) {
			if (isForbiddenWord(word)) {
				message.replace(word, ENCODED_BAD_WORD);
			}
		}
		return message;
	}
}