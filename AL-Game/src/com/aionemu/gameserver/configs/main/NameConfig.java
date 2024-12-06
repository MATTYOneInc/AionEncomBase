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
package com.aionemu.gameserver.configs.main;

import java.util.regex.Pattern;

import com.aionemu.commons.configuration.Property;

public class NameConfig {
	@Property(key = "gameserver.name.allow.custom", defaultValue = "false")
	public static boolean ALLOW_CUSTOM_NAMES;
	@Property(key = "gameserver.name.characterpattern", defaultValue = "[a-zA-Z]{2,16}")
	public static Pattern CHAR_NAME_PATTERN;
	@Property(key = "gameserver.name.forbidden.sequences", defaultValue = "")
	public static String NAME_SEQUENCE_FORBIDDEN;
	@Property(key = "gameserver.name.forbidden.enable.client", defaultValue = "true")
	public static boolean NAME_FORBIDDEN_ENABLE;
	@Property(key = "gameserver.name.forbidden.client", defaultValue = "")
	public static String NAME_FORBIDDEN_CLIENT;
	@Property(key = "gameserver.pet.name.change.enable", defaultValue = "true")
	public static boolean PET_NAME_CHANGE_ENABLE;
}