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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a container holding and serving all {@link NpcTemplate} instances.<br>
 * Briefly: Every {@link Npc} instance represents some class of NPCs among which each have the same id, name, items,
 * statistics. Data for such NPC class is defined in {@link NpcTemplate} and is uniquely identified by npc id.
 * 
 * @author Luno
 */
@XmlRootElement(name = "npc_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcData {

	@XmlElement(name = "npc_template")
	private List<NpcTemplate> npcs;

	/** A map containing all npc templates */
	private TIntObjectHashMap<NpcTemplate> npcData = new TIntObjectHashMap<NpcTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (NpcTemplate npc : npcs) {
			npcData.put(npc.getTemplateId(), npc);
		}
		npcs.clear();
		npcs = null;
	}

	public int size() {
		return npcData.size();
	}

	/**
	 * /** Returns an {@link NpcTemplate} object with given id.
	 * 
	 * @param id
	 *          id of NPC
	 * @return NpcTemplate object containing data about NPC with that id.
	 */
	public NpcTemplate getNpcTemplate(int id) {
		return npcData.get(id);
	}

	/**
	 * @return the npcData
	 */
	public TIntObjectHashMap<NpcTemplate> getNpcData() {
		return npcData;
	}
}