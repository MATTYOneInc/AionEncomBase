/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author MrPoke remod By Nephis
 * @reworked vlog
 */
public class _2430SecretInformation extends QuestHandler {

	private final static int questId = 2430;

	public _2430SecretInformation() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 204327, 204377, 798078, 798081, 798082, 204300 };
		qe.registerQuestNpc(204327).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204327) { // Sveinn
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					}
					case REFUSE_QUEST: {
						return sendQuestDialog(env, 1004);
					}
					case ACCEPT_QUEST: {
						return sendQuestDialog(env, 1003);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					case STEP_TO_1: {
						if (player.getInventory().getKinah() >= 500) {
							if (QuestService.startQuest(env)) {
								player.getInventory().decreaseKinah(500);
								changeQuestStep(env, 0, 1, false); // 1
								return sendQuestDialog(env, 1352);
							}
						}
						else {
							return sendQuestDialog(env, 1267);
						}
					}
					case STEP_TO_3: {
						if (player.getInventory().getKinah() >= 5000) {
							if (QuestService.startQuest(env)) {
								player.getInventory().decreaseKinah(5000);
								changeQuestStep(env, 0, 3, false); // 3
								return sendQuestDialog(env, 2034);
							}
						}
						else {
							return sendQuestDialog(env, 1267);
						}
					}
					case STEP_TO_7: {
						if (player.getInventory().getKinah() >= 50000) {
							if (QuestService.startQuest(env)) {
								player.getInventory().decreaseKinah(50000);
								changeQuestStep(env, 0, 7, false); // 7
								return sendQuestDialog(env, 3398);
							}
						}
						else {
							return sendQuestDialog(env, 1267);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204327: { // Sveinn
					switch (dialog) {
						case START_DIALOG: {
							switch (var) {
								case 1: {
									return sendQuestDialog(env, 1352);
								}
								case 3: {
									return sendQuestDialog(env, 2034);
								}
								case 7: {
									return sendQuestDialog(env, 3398);
								}
							}
						}
						case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2, 182204221, 1, 0, 0); // 2
						}
						case STEP_TO_4: {
							return defaultCloseDialog(env, 3, 4); // 4
						}
						case STEP_TO_8: {
							return defaultCloseDialog(env, 7, 8); // 8
						}
					}
					break;
				}
				case 204377: { // Grall
					switch (dialog) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SELECT_REWARD: {
							removeQuestItem(env, 182204221, 1);
							changeQuestStep(env, 2, 2, true); // reward 0
							return sendQuestDialog(env, 5);
						}
					}
					break;
				}
				case 798078: { // Hugorunerk
					switch (dialog) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						}
						case STEP_TO_5: {
							return defaultCloseDialog(env, 4, 5); // 5
						}
					}
					break;
				}
				case 798081: { // Nicoyerk
					switch (dialog) {
						case START_DIALOG: {
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						}
						case STEP_TO_6: {
							return defaultCloseDialog(env, 5, 6); // 6
						}
					}
					break;
				}
				case 798082: { // Bicorunerk
					switch (dialog) {
						case START_DIALOG: {
							if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						}
						case SELECT_REWARD: {
							changeQuestStep(env, 6, 6, true); // reward 1
							return sendQuestDialog(env, 6);
						}
					}
					break;
				}
				case 204300: { // Bolverk
					switch (dialog) {
						case START_DIALOG: {
							if (var == 8) {
								if (player.getInventory().getItemCountByItemId(182204222) > 0) {
									return sendQuestDialog(env, 3739);
								}
								else {
									return sendQuestDialog(env, 3825);
								}
							}
						}
						case SELECT_REWARD: {
							removeQuestItem(env, 182204222, 1);
							changeQuestStep(env, 8, 8, true); // reward 2
							return sendQuestDialog(env, 7);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204377: { // Grall
					if (var == 2) {
						return sendQuestEndDialog(env, 0);
					}
					break;
				}
				case 798082: { // Bicorunerk
					if (var == 6) {
						return sendQuestEndDialog(env, 1);
					}
					break;
				}
				case 204300: { // Bolverk
					if (var == 8) {
						return sendQuestEndDialog(env, 2);
					}
					break;
				}
			}
		}
		return false;
	}
}
