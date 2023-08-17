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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class HTMLConfig
{
	@Property(key = "gameserver.html.welcome.enable", defaultValue = "false")
	public static boolean ENABLE_HTML_WELCOME;
	@Property(key = "gameserver.html.guides.enable", defaultValue = "true")
	public static boolean ENABLE_GUIDES;
	@Property(key = "gameserver.html.root", defaultValue = "./data/static_data/HTML/")
	public static String HTML_ROOT;
	@Property(key = "gameserver.html.cache.file", defaultValue = "./cache/html.cache")
	public static String HTML_CACHE_FILE;
	@Property(key = "gameserver.html.encoding", defaultValue = "UTF-8")
	public static String HTML_ENCODING;
}