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
package com.aionemu.gameserver.model.drop;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

@XmlRootElement(name = "npc_drop")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "npcDrop", propOrder = {
    "dropGroup"
})
public class NpcDrop implements DropCalculator {

    @XmlElement(name = "drop_group")
	protected List<DropGroup> dropGroup;
    @XmlAttribute(name = "npc_id", required = true)
	protected int npcId;

	public List<DropGroup> getDropGroup() {
		if (dropGroup == null) {
			return Collections.emptyList();
		}
		return this.dropGroup;
	}

	/**
	 * Gets the value of the npcId property.
	 */
	public int getNpcId() {
		return npcId;
	}

	@Override
	public int dropCalculator(Set<DropItem> result, int index, float dropModifier, Race race, Collection<Player> groupMembers) {
		if (dropGroup == null || dropGroup.isEmpty()) {
			return index;
		}
		for (DropGroup dg : dropGroup) {
			if (dg.getRace() == Race.PC_ALL || dg.getRace() == race) {
				index = dg.dropCalculator(result, index, dropModifier, race, groupMembers);
			}
		}
		return index;
	}
}