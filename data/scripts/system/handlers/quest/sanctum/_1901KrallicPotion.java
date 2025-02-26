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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.sanctum;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author edynamic90
 */
public class _1901KrallicPotion extends QuestHandler {

	private final static int questId = 1901;
	public _1901KrallicPotion() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203830).addOnQuestStart(questId);
		qe.registerQuestNpc(203830).addOnTalkEvent(questId);
		qe.registerQuestNpc(798026).addOnTalkEvent(questId);
		qe.registerQuestNpc(798025).addOnTalkEvent(questId);
		qe.registerQuestNpc(203131).addOnTalkEvent(questId);
		qe.registerQuestNpc(798003).addOnTalkEvent(questId);
		qe.registerQuestNpc(203864).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
		if (targetId == 203830) { // Fuchsia 
			if (env.getDialog() == QuestDialog.START_DIALOG)
				return sendQuestDialog(env, 1011);
			else
				return sendQuestStartDialog(env);
            }
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 798026:// Kunberunerk
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1352);
							else if (var == 5)
								return sendQuestDialog(env, 3057);
						case SELECT_ACTION_1438:
							Storage inventory = player.getInventory();
							if (inventory.tryDecreaseKinah(10000)) {
								return sendQuestDialog(env, 1438);
							}
							else
								return sendQuestDialog(env, 1523);
						case STEP_TO_1:// buy potion 10000
                            qs.setQuestVarById(0, 5);
			                qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						case STEP_TO_2:// no kinah
					        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						case STEP_TO_7:
					        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
			                qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
						case 798025:// Mapireck
						switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 1)
								return sendQuestDialog(env, 1693);
							else if (var == 4)
								return sendQuestDialog(env, 2716);
						case STEP_TO_3:
					        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						case STEP_TO_6:
							removeQuestItem(env, 182206000, 1);
					        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
                        return closeDialogWindow(env);
						}
						case 203131:// Maniparas
						switch (env.getDialog()) {
							case START_DIALOG:
								return sendQuestDialog(env, 2034);
							case STEP_TO_4:
					            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
								updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
						case 798003:// Gaphyrk
						switch (env.getDialog()) {
							case START_DIALOG:
								return sendQuestDialog(env, 2375);
							case STEP_TO_5:
								giveQuestItem(env, 182206000, 1);
					            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
								updateQuestStatus(env);
                                return closeDialogWindow(env);
                        }
                 }
            }
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203864) // Marmeia
			return sendQuestEndDialog(env);
		}
		return false;
	}
}