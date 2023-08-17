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
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;


/**
 * @author Cheatkiller
 *
 */
public class _2486AStrangeCreature extends QuestHandler {

	private final static int questId = 2486;
	
	
	public _2486AStrangeCreature() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
		qe.registerQuestNpc(204208).addOnTalkEvent(questId);
		qe.registerQuestNpc(204092).addOnTalkEvent(questId);
		qe.registerQuestNpc(798063).addOnTalkEvent(questId);
		qe.registerQuestNpc(204342).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204053) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204208) { 
				if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 1352);
				}
			  else if (dialog == QuestDialog.STEP_TO_1) {
			  	return defaultCloseDialog(env, 0, 1);
			}
		}
			else if (targetId == 204092) { 
				if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 1693);
				}
			  else if (dialog == QuestDialog.STEP_TO_2) {
			  	return defaultCloseDialog(env, 1, 2);
			}
		}
			else if (targetId == 798063) { 
				if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 2034);
				}
			  else if (dialog == QuestDialog.STEP_TO_3) {
			  	qs.setQuestVar(3);
			  	return defaultCloseDialog(env, 3, 3, true, false);
			}
		}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204342) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}