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
package quest.ishalgen;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

public class _2106VanarsFlattery extends QuestHandler {

	private final static int questId = 2106;
	public _2106VanarsFlattery() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203502).addOnQuestStart(questId);
		qe.registerQuestNpc(203502).addOnTalkEvent(questId);
		qe.registerQuestNpc(203517).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(getQuestId());
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 203502) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
						 return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						if (giveQuestItem(env, 182203106, 1)) {
							return sendQuestStartDialog(env);
						}
					} case REFUSE_QUEST:{
						return sendQuestDialog(env, 1004);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203502) {
				switch (env.getDialog()) {
					case START_DIALOG:{
						return sendQuestDialog(env, 1003);
					} case STEP_TO_1:
						TeleportService2.teleportTo(player, 220010000, 576.0000f, 2538.0000f, 272.0000f, (byte) 9);
						changeQuestStep(env, 0, 1, true);
						return closeDialogWindow(env);
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203517) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}