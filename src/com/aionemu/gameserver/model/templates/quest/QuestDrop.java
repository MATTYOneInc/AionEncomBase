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
package com.aionemu.gameserver.model.templates.quest;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestDrop")
public class QuestDrop
{
	@XmlAttribute(name = "npc_id")
	protected Integer npcId;
	@XmlAttribute(name = "item_id")
	protected Integer itemId;
	@XmlAttribute
	protected Integer chance;
	@XmlAttribute(name = "drop_each_member")
	protected int dropEachMember = 0;
	@XmlAttribute(name = "collecting_step")
	protected int collecting_step = 0;
	
	@XmlTransient
	protected Integer questId;
	
	public Integer getNpcId() {
		return npcId;
	}
	
	public Integer getItemId() {
		return itemId;
	}
	
	public int getChance() {
		if (chance == null) {
			return 100;
		}
		return chance;
	}
	
	public boolean isDropEachMemberGroup() {
		return dropEachMember == 1;
	}
	
	public boolean isDropEachMemberAlliance() {
		return dropEachMember == 2;
	}
	
	public Integer getQuestId() {
		return questId;
	}
	
	public int getCollectingStep() {
		return collecting_step;
	}
	
	public void setQuestId(Integer questId) {
		this.questId = questId;
	}
}