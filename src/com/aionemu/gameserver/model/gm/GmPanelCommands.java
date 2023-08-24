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
package com.aionemu.gameserver.model.gm;

public enum GmPanelCommands {
  REMOVE_SKILL_DELAY_ALL,
  ITEMCOOLTIME,
  CLEARUSERCOOLT,
  SET_MAKEUP_BONUS,
  SET_VITALPOINT,
  SET_DISABLE_ITEMUSE_GAUGE,
  PARTYRECALL,
  ATTRBONUS,
  TELEPORTTO,
  RESURRECT,
  INVISIBLE,
  VISIBLE,
  LEVELDOWN,
  LEVELUP,
  CHANGECLASS,
  CLASSUP,
  DELETECQUEST,
  ADDQUEST,
  ENDQUEST,
  SETINVENTORYGROWTH,
  SKILLPOINT,
  COMBINESKILL,
  ADDSKILL,
  DELETESKILL,
  GIVETITLE,
  ENCHANT100,
  FREEFLY,
  TELEPORT_TO_NAMED,
  WISH,
  WISHID,
  DELETE_ITEMS,
  BOOKMARK_ADD,
  SEARCH;
  
  public static GmPanelCommands getValue(String command) {
    for (GmPanelCommands value : values()) {
      if (value.name().equals(command.toUpperCase()))
        return value; 
    } 
    throw new IllegalArgumentException("Invalid GmPanelCommands id: " + command);
  }
}
