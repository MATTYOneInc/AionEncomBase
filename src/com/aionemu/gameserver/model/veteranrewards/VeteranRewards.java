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
package com.aionemu.gameserver.model.veteranrewards;

public class VeteranRewards {
	private int id;
	private String Player;
	private int type;
	private int item;
	private int count;
	private int kinah;
	private String Sender;
	private String Title;
	private String Message;

	public VeteranRewards(String Player, int type, int item, int count, int kinah, String Sender, String Title, String Message) {
		this.Player = Player;
		this.type = type;
		this.item = item;
		this.count = count;
		this.kinah = kinah;
		this.Sender = Sender;
		this.Title = Title;
		this.Message = Message;
	}

	public VeteranRewards(int id, String Player, int type, int item, int count, int kinah, String Sender, String Title, String Message) {
		this.id = id;
		this.Player = Player;
		this.type = type;
		this.item = item;
		this.count = count;
		this.kinah = kinah;
		this.Sender = Sender;
		this.Title = Title;
		this.Message = Message;
	}

	public int getId() {
		if (id != 0) {
			return id;
		}
		else {
			return -1;
		}
	}

	public String getPlayer() {
		return Player;
	}

	public int getType() {
		return type;
	}

	public int getItem() {
		return item;
	}

	public int getCount() {
		return count;
	}

	public int getKinah() {
		return kinah;
	}

	public String getSender() {
		return Sender;
	}

	public String getTitle() {
		return Title;
	}

	public String getMessage() {
		return Message;
	}
}