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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.MentorMonsterHunt;

import javolution.util.FastMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MentorMonsterHuntData")
public class MentorMonsterHuntData extends MonsterHuntData {
	@XmlAttribute(name = "min_mente_level")
	protected int minMenteLevel = 1;

	@XmlAttribute(name = "max_mente_level")
	protected int maxMenteLevel = 999;

	public int getMinMenteLevel() {
		return minMenteLevel;
	}

	public int getMaxMenteLevel() {
		return maxMenteLevel;
	}

	@Override
	public void register(QuestEngine questEngine) {
		FastMap<Monster, Set<Integer>> monsterNpcs = new FastMap<Monster, Set<Integer>>();
		for (Monster m : monster) {
			monsterNpcs.put(m, new HashSet<Integer>(m.getNpcIds()));
		}
		MentorMonsterHunt template = new MentorMonsterHunt(id, startNpcIds, endNpcIds, monsterNpcs, minMenteLevel,
				maxMenteLevel);
		questEngine.addQuestHandler(template);
	}
}