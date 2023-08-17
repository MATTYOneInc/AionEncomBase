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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.drop.NpcDrop;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author MrPoke
 *
 */
@XmlRootElement(name = "npc_drops")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "npcDropData", propOrder = { "npcDrop" })
public class NpcDropData {

	@XmlElement(name = "npc_drop")
	protected List<NpcDrop> npcDrop;

	/**
	 * @return the npcDrop
	 */
	public List<NpcDrop> getNpcDrop() {
		return npcDrop;
	}

	/**
	 * @param npcDrop the npcDrop to set
	 */
	public void setNpcDrop(List<NpcDrop> npcDrop) {
		this.npcDrop = npcDrop;
	}

	public int size() {
		return npcDrop.size();
	}
}