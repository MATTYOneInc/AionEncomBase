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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author MrPoke remod By Nephis
 * @reworked vlog
 */
public class _2493BringingUpTayga extends QuestHandler {

	private final static int questId = 2493;
	public _2493BringingUpTayga() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204325).addOnQuestStart(questId);
		qe.registerOnLogOut(questId);
		qe.registerQuestNpc(204325).addOnTalkEvent(questId);
		qe.registerQuestNpc(204435).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204325) { // Ipoderr
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204435) { // Purra?
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				}
				else if (dialog == QuestDialog.SET_REWARD) {
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().scheduleRespawn();
					npc.getController().onDelete();
					changeQuestStep(env, 0, 0, true); // reward
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204325) { // Ipoderr
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}