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
package quest.inggison;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 */
public class _11077AWeaponOfWorth extends QuestHandler {

	private final static int questId = 11077;

	public _11077AWeaponOfWorth() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798926).addOnQuestStart(questId); // Outremus
		qe.registerQuestNpc(798926).addOnTalkEvent(questId); // Outremus
		qe.registerQuestNpc(799028).addOnTalkEvent(questId); // Brontes
		qe.registerQuestNpc(798918).addOnTalkEvent(questId); // Pilipides
		qe.registerQuestNpc(798903).addOnTalkEvent(questId); // Drenia
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798926) {
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 1011);
					default: {
						if (!giveQuestItem(env, 182214016, 1))
							updateQuestStatus(env);
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799028: // Brontes
				{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1353);
						}
						case SELECT_ACTION_1353:{
							return sendQuestDialog(env, 1353);
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						}
					}
				}
				case 798918: // Pilipides
				{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						}
						case SELECT_ACTION_1694: {
							return sendQuestDialog(env, 1694);
						}
						case STEP_TO_2:{
							return defaultCloseDialog(env, 1, 2);
						}
					}
				}
				case 798903: // Drenia
				{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_REWARD: {
							return defaultCloseDialog(env, 2, 3, true, true);
						}
					}
				}
			}
		}
			else if (qs.getStatus() == QuestStatus.REWARD) {
				if (targetId == 798903) // Drenia
				{
					switch (env.getDialogId()) {
						case 1009: {
							return sendQuestDialog(env, 5);
						}
						default:
							return sendQuestEndDialog(env);
					}
				}
			}
		return false;
	}
}
