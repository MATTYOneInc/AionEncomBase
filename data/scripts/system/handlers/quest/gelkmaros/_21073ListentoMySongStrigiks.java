/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.gelkmaros;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author HellBoy
 * @reworked vlog
 */
public class _21073ListentoMySongStrigiks extends QuestHandler {

	private final static int questId = 21073;

	public _21073ListentoMySongStrigiks() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 799407, 799408 };
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(799407).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799407) { // Skilving
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 799408) { // Svasuth
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1); // 1
				}
			}
			else if (targetId == 799407) { // Skilving
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 1) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
					changeQuestStep(env, 1, 1, true); // reward
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799407) { // Skilving
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
