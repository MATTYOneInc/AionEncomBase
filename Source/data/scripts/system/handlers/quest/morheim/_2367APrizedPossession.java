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
public class _2367APrizedPossession extends QuestHandler {

	private final static int questId = 2367;
	
	int rewardIndex;
	
	public _2367APrizedPossession() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(204339).addOnQuestStart(questId);
		qe.registerQuestNpc(798080).addOnTalkEvent(questId);
		qe.registerQuestNpc(798079).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204339) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204339) { 
				if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 1003);
				}
				else if (dialog == QuestDialog.SELECT_ACTION_1012) {
					giveQuestItem(env, 182204147 , 1);
					return sendQuestDialog(env, 1012);
				}
				else if (dialog == QuestDialog.SELECT_ACTION_1097) {
					giveQuestItem(env, 182204147 , 1);
					return sendQuestDialog(env, 1097);
				}
				else if(dialog == QuestDialog.STEP_TO_10) {
					changeQuestStep(env, 0, 10, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
				else if(dialog == QuestDialog.STEP_TO_20) {
					rewardIndex = 1;
					changeQuestStep(env, 0, 20, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798079) {
				if (dialog == QuestDialog.USE_OBJECT) {
						return sendQuestDialog(env, 1352);
					}
				}
			else if(targetId == 798080) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 1693);
				}
			}
			return sendQuestEndDialog(env, rewardIndex);
		}
	 return false;
	}	
}