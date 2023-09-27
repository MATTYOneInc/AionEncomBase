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
package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.conditions;

import com.aionemu.gameserver.questEngine.model.ConditionUnionType;
import com.aionemu.gameserver.questEngine.model.QuestEnv;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestConditions", propOrder = { "conditions" })
public class QuestConditions {

	@XmlElements({ @XmlElement(name = "quest_status", type = QuestStatusCondition.class),
		@XmlElement(name = "npc_id", type = NpcIdCondition.class),
		@XmlElement(name = "pc_inventory", type = PcInventoryCondition.class),
		@XmlElement(name = "quest_var", type = QuestVarCondition.class),
		@XmlElement(name = "dialog_id", type = DialogIdCondition.class) })
	protected List<QuestCondition> conditions;
	@XmlAttribute(required = true)
	protected ConditionUnionType operate;

	public boolean checkConditionOfSet(QuestEnv env) {
		boolean inCondition = (operate == ConditionUnionType.AND);
		for (QuestCondition cond : conditions) {
			boolean bCond = cond.doCheck(env);
			switch (operate) {
				case AND:
					if (!bCond) {
						return false;
					}
					inCondition = inCondition && bCond;
					break;
				case OR:
					if (bCond) {
						return true;
					}
					inCondition = inCondition || bCond;
				break;
			}
		}
		return inCondition;
	}
}