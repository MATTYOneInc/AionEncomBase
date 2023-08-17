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
package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.events;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.models.Monster;
import com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperations;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnKillEvent", propOrder = { "monster", "complite" })
public class OnKillEvent extends QuestEvent {

	@XmlElement(name = "monster")
	protected List<Monster> monster;
	protected QuestOperations complite;

	public List<Monster> getMonsters() {
		if (monster == null) {
			monster = new ArrayList<Monster>();
		}
		return this.monster;
	}

	public boolean operate(QuestEnv env) {
		if (monster == null || !(env.getVisibleObject() instanceof Npc)) {
			return false;
        }
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null) {
			return false;
        }
		Npc npc = (Npc) env.getVisibleObject();
		for (Monster m : monster) {
			if (m.getNpcIds().contains(npc.getNpcId())) {
				int var = qs.getQuestVarById(m.getVar());
				if (var >= (m.getStartVar() == null ? 0 : m.getStartVar()) && var < m.getEndVar()) {
					qs.setQuestVarById(m.getVar(), var + 1);
					PacketSendUtility.sendPacket(env.getPlayer(), new SM_QUEST_ACTION(env.getQuestId(), qs.getStatus(), qs
						.getQuestVars().getQuestVars()));
				}
			}
		}

		if (complite != null) {
			for (Monster m : monster) {
				if (qs.getQuestVarById(m.getVar()) != qs.getQuestVarById(m.getVar())) {
					return false;
				}
			}
			complite.operate(env);
		}
		return false;
	}
}