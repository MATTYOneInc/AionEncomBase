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

import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.MonsterHunt;
import javolution.util.FastMap;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterHuntData", propOrder = {"monster"})
@XmlSeeAlso({KillSpawnedData.class, MentorMonsterHuntData.class})
public class MonsterHuntData extends XMLQuest
{
	@XmlElement(name = "monster", required = true)
    protected List<Monster> monster;
	
    @XmlAttribute(name = "start_npc_ids", required = true)
    protected List<Integer> startNpcIds;
	
    @XmlAttribute(name = "end_npc_ids")
    protected List<Integer> endNpcIds;
	
    @XmlAttribute(name = "start_dialog_id")
    protected int startDialog;
	
    @XmlAttribute(name = "end_dialog_id")
    protected int endDialog;
	
    @XmlAttribute(name = "aggro_start_npcs")
    protected List<Integer> aggroNpcs;
	
    @XmlAttribute(name = "invasion_world")
    protected int invasionWorld;
	
	@Override
	public void register(QuestEngine questEngine) {
		FastMap<Monster, Set<Integer>> monsterNpcs = new FastMap<Monster, Set<Integer>>();
		for (Monster m : monster) {
			monsterNpcs.put(m, new HashSet<Integer>(m.getNpcIds()));
		}
		MonsterHunt template = new MonsterHunt(id, startNpcIds, endNpcIds, monsterNpcs, startDialog, endDialog, aggroNpcs, invasionWorld);
        questEngine.addQuestHandler(template);
	}
}