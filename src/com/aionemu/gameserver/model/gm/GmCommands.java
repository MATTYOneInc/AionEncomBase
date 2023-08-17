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

public enum GmCommands
{
    GM_DIALOG_TELEPORTTO,
	GM_DIALOG_RECALL,
    GM_DIALOG,
    GM_DIALOG_POS,
	GM_DIALOG_MEMO,
	GM_DIALOG_BOOKMARK,
	GM_DIALOG_INVENTORY,
	GM_DIALOG_SKILL,
	GM_DIALOG_STATUS,
	GM_DIALOG_QUEST,
	GM_DIALOG_REFRESH,
	GM_DIALOG_WAREHOUSE,
	GM_DIALOG_MAIL,
	GM_POLL_DIALOG,
	GM_POLL_DIALOG_SUBMIT,
	GM_BOOKMARK_DIALOG,
	GM_BOOKMARK_DIALOG_ADD_BOOKMARK,
	GM_MEMO_DIALOG,
	GM_MEMO_DIALOG_ADD_MEMO,
	GM_DIALOG_CHECK_BOT1,
	GM_DIALOG_CHECK_BOT99,
	GM_INDICATOR_DIALOG_TOOLTIP_HOUSING_MODE,
	GM_DIALOG_CHARACTER,
	GM_DIALOG_OPTION,
	GM_DIALOG_BUILDER_CONTROL,
	GM_DIALOG_BUILDER_COMMAND;
	
	public static GmCommands getValue(String command) {
		for (GmCommands value : values()) {
			if (value.name().equals(command.toUpperCase())) {
				return value;
		    }
		}
		throw new IllegalArgumentException("Invalid GmCommands id: " + command);
	}
}