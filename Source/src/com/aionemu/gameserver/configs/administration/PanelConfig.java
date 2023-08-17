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
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

/**
 * @author Hoo
 *
 */
public class PanelConfig {
	
	@Property(key = "gameserver.administration.skilpanel", defaultValue = "3")
    public static int SKILL_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.changeclasspanel", defaultValue = "3")
    public static int CHANGECLASS_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.delquestpanel", defaultValue = "3")
    public static int DELQUEST_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.endquestpanel", defaultValue = "3")
    public static int ENDQUEST_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.givetitlepanel", defaultValue = "3")
    public static int GIVETITLE_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.startquestpanel", defaultValue = "3")
    public static int STARTQUEST_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.wishitempanel", defaultValue = "3")
    public static int WISHITEM_PANEL_LEVEL;
	
	@Property(key = "gameserver.administration.wishitemidpanel", defaultValue = "3")
    public static int WISHITEMID_PANEL_LEVEL;
}