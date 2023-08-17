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

package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Collect Bloodwing Meat and lure Vison (798214). Take Bloodwing Meat to Tityus (798191).
 * 
 * @author Balthazar
 * @reworked vlog
 */

public class _3092VisonTheDrakie extends QuestHandler {

	private final static int questId = 3092;

	public _3092VisonTheDrakie() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798191).addOnQuestStart(questId);
		qe.registerQuestNpc(798191).addOnTalkEvent(questId);
		qe.registerQuestNpc(798214).addOnTalkEvent(questId);
		qe.registerOnLogOut(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798191) { // Tityus
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798214: { // Vison
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 0) {
								long itemCount = player.getInventory().getItemCountByItemId(182208066);
								if (itemCount >= 25) {
									return sendQuestDialog(env, 1352);
								}
							}
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1); // 1
						}
					}
				}
				case 798191: {
					if (env.getDialog() == QuestDialog.START_DIALOG)
						return sendQuestDialog(env, 2375);
					if(env.getDialog() == QuestDialog.CHECK_COLLECTED_ITEMS) {
							return checkQuestItems(env, 1, 2, true, 5, 2716); // reward
					}
					if (env.getDialogId() == 1008)
						return defaultCloseDialog(env, 1, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798191) { 
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}