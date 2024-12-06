/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Nephis and quest helper team
 */
public class _1371FlowersForIsson extends QuestHandler {

	private final static int questId = 1371;

	public _1371FlowersForIsson() {
		super(questId);
	}

	@Override
	public void register() {

		qe.registerQuestNpc(203949).addOnQuestStart(questId);
		qe.registerQuestNpc(203949).addOnTalkEvent(questId);
		qe.registerQuestNpc(730039).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		long itemCount = 0;
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203949) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 203949) {
				switch (env.getDialog()) {
					case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
						break;
					case CHECK_COLLECTED_ITEMS:
						if (var == 0)
							itemCount = player.getInventory().getItemCountByItemId(152000601);
						if (itemCount > 4) {
							return sendQuestDialog(env, 1353);
						}
						else {
							return sendQuestDialog(env, 1438);
						}
					case STEP_TO_1: {
						removeQuestItem(env, 152000601, 5);
						qs.setQuestVar(2);
						updateQuestStatus(env);
						return sendQuestSelectionDialog(env);
					}
				}
			}
			else if (targetId == 730039) {
				if (qs != null && qs.getStatus() == QuestStatus.START) {
					return useQuestObject(env, 2, 2, true, false); // reward
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203949) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
