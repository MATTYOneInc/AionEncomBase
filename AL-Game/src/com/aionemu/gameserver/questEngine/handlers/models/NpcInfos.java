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
package com.aionemu.gameserver.questEngine.handlers.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NpcInfos")
public class NpcInfos {
	@XmlAttribute(name = "npc_id", required = true)
	protected int npcId;

	@XmlAttribute(name = "var", required = true)
	protected int var;

	@XmlAttribute(name = "quest_dialog", required = true)
	protected int questDialog;

	@XmlAttribute(name = "close_dialog")
	protected int closeDialog;

	@XmlAttribute(name = "movie")
	protected int movie;

	public int getNpcId() {
		return npcId;
	}

	public int getVar() {
		return var;
	}

	public int getQuestDialog() {
		return questDialog;
	}

	public int getCloseDialog() {
		return closeDialog;
	}

	public int getMovie() {
		return movie;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}
}