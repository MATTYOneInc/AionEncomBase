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
package com.aionemu.gameserver.model.guide;

/**
 * @author xTz
 */
public class Guide {

	private int guide_id;
	private int player_id;
	private String title;

	public Guide(int guide_id, int player_id, String title) {
		this.guide_id = guide_id;
		this.player_id = player_id;
		this.title = title;
	}

	public int getGuideId() {
		return guide_id;
	}

	public int getPlayerId() {
		return player_id;
	}

	public String getTitle() {
		return title;
	}
}