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
package quest.stigma_vision;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

public class _30317Group_Spirit_And_Stigma_Slots extends QuestHandler {

	private final static int questId = 30317;
	public _30317Group_Spirit_And_Stigma_Slots() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(799208).addOnQuestStart(questId);
		qe.registerQuestNpc(799208).addOnTalkEvent(questId);
		qe.registerQuestNpc(799506).addOnTalkEvent(questId);
		qe.registerQuestNpc(799322).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799208) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		}
		int var = qs.getQuestVarById(0);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799322: {
					switch (env.getDialog()) {
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case STEP_TO_1:
							QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 799506, player.getX(), player.getY(), player.getZ(), player.getHeading());
							qs.setQuestVarById(0, 1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
					}
				} case 799208:
					switch (env.getDialog()) {
						case START_DIALOG:
							long itemCount1 = player.getInventory().getItemCountByItemId(182209718);
							long itemCount2 = player.getInventory().getItemCountByItemId(182209719);
							if (var == 2) {
								if (itemCount1 > 0 && itemCount2 > 0) {
									removeQuestItem(env, 182209718, 1);
									removeQuestItem(env, 182209719, 1);
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1693);
								}
								else
									return sendQuestDialog(env, 10001);
							}
					}
				case 799506:
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 1)
								return sendQuestDialog(env, 1352);
						case STEP_TO_2:
							env.getVisibleObject().getController().delete();
							qs.setQuestVarById(0, 2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
					}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799208) {
				if (env.getDialogId() == 39)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}