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

import com.aionemu.gameserver.questEngine.handlers.models.XMLQuest;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quest_scripts")
public class XMLQuests {

	@XmlElements({ @XmlElement(name = "report_to", type = com.aionemu.gameserver.questEngine.handlers.models.ReportToData.class),
		@XmlElement(name = "monster_hunt", type = com.aionemu.gameserver.questEngine.handlers.models.MonsterHuntData.class),
		@XmlElement(name = "xml_quest", type = com.aionemu.gameserver.questEngine.handlers.models.XmlQuestData.class),
		@XmlElement(name = "item_collecting", type = com.aionemu.gameserver.questEngine.handlers.models.ItemCollectingData.class),
		@XmlElement(name = "relic_rewards", type = com.aionemu.gameserver.questEngine.handlers.models.RelicRewardsData.class),
		@XmlElement(name = "crafting_rewards", type = com.aionemu.gameserver.questEngine.handlers.models.CraftingRewardsData.class),
		@XmlElement(name = "report_to_many", type = com.aionemu.gameserver.questEngine.handlers.models.ReportToManyData.class),
		@XmlElement(name = "kill_in_world", type = com.aionemu.gameserver.questEngine.handlers.models.KillInWorldData.class),
		@XmlElement(name = "skill_use", type = com.aionemu.gameserver.questEngine.handlers.models.SkillUseData.class),
		@XmlElement(name = "kill_spawned", type = com.aionemu.gameserver.questEngine.handlers.models.KillSpawnedData.class),
		@XmlElement(name = "mentor_monster_hunt", type = com.aionemu.gameserver.questEngine.handlers.models.MentorMonsterHuntData.class),
		@XmlElement(name = "fountain_rewards", type = com.aionemu.gameserver.questEngine.handlers.models.FountainRewardsData.class),
		@XmlElement(name = "item_order", type = com.aionemu.gameserver.questEngine.handlers.models.ItemOrdersData.class),
		@XmlElement(name = "work_order", type = com.aionemu.gameserver.questEngine.handlers.models.WorkOrdersData.class) })
	protected List<XMLQuest> data;

	/**
	 * @return the data
	 */
	public List<XMLQuest> getQuest() {
		return data;
	}

	/**
	 * @param data
	 *          the data to set
	 */
	public void setData(List<XMLQuest> data) {
		this.data = data;
	}
}