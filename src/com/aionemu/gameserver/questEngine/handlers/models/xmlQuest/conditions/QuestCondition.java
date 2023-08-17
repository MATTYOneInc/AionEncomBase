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
package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.conditions;

import com.aionemu.gameserver.questEngine.model.ConditionOperation;
import com.aionemu.gameserver.questEngine.model.QuestEnv;

import javax.xml.bind.annotation.*;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestCondition")
@XmlSeeAlso({ NpcIdCondition.class, DialogIdCondition.class, PcInventoryCondition.class, QuestVarCondition.class,
	QuestStatusCondition.class })
public abstract class QuestCondition {

	@XmlAttribute(required = true)
	protected ConditionOperation op;

	/**
	 * Gets the value of the op property.
	 * 
	 * @return possible object is {@link ConditionOperation }
	 */
	public ConditionOperation getOp() {
		return op;
	}

	public abstract boolean doCheck(QuestEnv env);
}