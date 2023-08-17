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
package com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations;

import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.QuestService;

import javax.xml.bind.annotation.*;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollectItemQuestOperation", propOrder = { "_true", "_false" })
public class CollectItemQuestOperation extends QuestOperation {

	@XmlElement(name = "true", required = true)
	protected QuestOperations _true;
	@XmlElement(name = "false", required = true)
	protected QuestOperations _false;
	@XmlAttribute
	protected Boolean removeItems;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.aionemu.gameserver.questEngine.handlers.models.xmlQuest.operations.QuestOperation#doOperate(com.aionemu.gameserver
	 * .questEngine.model.QuestEnv)
	 */
	@Override
	public void doOperate(QuestEnv env) {
		if (QuestService.collectItemCheck(env, removeItems == null ? true : false)) {
			_true.operate(env);
		}
		else {
			_false.operate(env);
		}
	}
}