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
package com.aionemu.gameserver.model.templates.teleport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author orz
 */
@XmlRootElement(name = "teleporter_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TeleporterTemplate {

	@XmlAttribute(name = "npc_ids")
	private List<Integer> npcIds;

	@XmlAttribute(name = "teleportId", required = true)
	private int teleportId = 0;

	@XmlElement(name = "locations")
	private TeleLocIdData teleLocIdData;

	/**
	 * @return the npcId
	 */
	public List<Integer> getNpcIds() {
		return npcIds;
	}

	/**
	 * @return the name of npc
	 */
	public boolean containNpc(int npcId) {
		return npcIds.contains(npcId);
	}

	/**
	 * @return the teleportId
	 */
	public int getTeleportId() {
		return teleportId;
	}

	/**
	 * @return the teleLocIdData
	 */
	public TeleLocIdData getTeleLocIdData() {
		return teleLocIdData;
	}
}