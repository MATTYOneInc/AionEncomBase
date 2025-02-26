/*
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
package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Mr. Poke
 * @modified by Gigi
 * @reworked vlog
 */

public class _2223AMythicalMonster extends QuestHandler {

	private final static int questId = 2223;
	public _2223AMythicalMonster() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203616).addOnQuestStart(questId);
		qe.registerQuestNpc(203616).addOnTalkEvent(questId);
		qe.registerQuestNpc(700134).addOnTalkEvent(questId);
		qe.registerQuestNpc(211621).addOnKillEvent(questId);
		qe.registerOnMovieEndQuest(67, questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {

		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203616) { // Gefion
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 203620: { // Lamir
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 1) {
								if (player.getInventory().getItemCountByItemId(182203217) == 1) {
									return sendQuestDialog(env, 1694);
								}
								else {
									giveQuestItem(env, 182203217, 1);
									return sendQuestDialog(env, 1779);
								}
							}
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1, 182203217, 1, 0, 0); // 1
						}
					}
					break;
				}
				case 700134: { // Old Incense Burner
					if (env.getDialog() == QuestDialog.USE_OBJECT) {
						if (player.getInventory().getItemCountByItemId(182203217) == 1) {
							return useQuestObject(env, 1, 1, false, 0, 0, 0, 182203217, 1, 67, true);
						}
					}
					break;
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203616) { // Gefion
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 211621, 1, true); // reward
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 67) {
			QuestService.addNewSpawn(220030000, 1, 211621, (float) 1547.1047, (float) 894.2969, (float) 248.019, (byte) 85);
			return true;
		}
		return false;
	}
}