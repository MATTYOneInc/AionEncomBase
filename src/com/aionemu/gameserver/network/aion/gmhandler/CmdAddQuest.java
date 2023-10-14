/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.network.aion.gmhandler;

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.FinishedQuestCond;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Kill3r
 */
public class CmdAddQuest extends AbstractGMHandler {

	public CmdAddQuest(Player admin, String params) {
		super(admin, params);
		run();
	}

	public void run() {
		try {
			int id = Integer.parseInt(params);
			QuestEnv env = new QuestEnv(admin, target, id, 0);

			if (QuestService.startQuest(env)) {
				PacketSendUtility.sendMessage(admin, "Quest started.");
			} else {
				QuestTemplate template = DataManager.QUEST_DATA.getQuestById(id);
				List<XMLStartCondition> preconditions = template.getXMLStartConditions();
				if (preconditions != null && preconditions.size() > 0) {
					for (XMLStartCondition condition : preconditions) {
						List<FinishedQuestCond> finisheds = condition.getFinishedPreconditions();
						if (finisheds != null && finisheds.size() > 0) {
							for (FinishedQuestCond fcondition : finisheds) {
								QuestState qs1 = admin.getQuestStateList().getQuestState(fcondition.getQuestId());
								if (qs1 == null || qs1.getStatus() != QuestStatus.COMPLETE) {
									PacketSendUtility.sendMessage(admin,
											"You have to finish " + fcondition.getQuestId() + " first!");
								}
							}
						}
					}
				}
				PacketSendUtility.sendMessage(admin, "Quest not started. Some preconditions failed");
			}
		} catch (NumberFormatException e) {
			PacketSendUtility.sendMessage(admin, "Quest Id Not Found!");
		}
	}
}