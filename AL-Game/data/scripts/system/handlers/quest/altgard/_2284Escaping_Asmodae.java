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
package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

public class _2284Escaping_Asmodae extends QuestHandler {

	private final static int questId = 2284;
	public _2284Escaping_Asmodae() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203645).addOnQuestStart(questId);
		qe.registerQuestNpc(798040).addOnTalkEvent(questId);
		qe.registerQuestNpc(798041).addOnTalkEvent(questId);
		qe.registerQuestNpc(798034).addOnTalkEvent(questId);
		qe.registerQuestNpc(203645).addOnTalkEvent(questId);
		qe.registerOnLogOut(questId);
		qe.registerAddOnLostTargetEvent(questId);
		qe.registerAddOnReachTargetEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203645) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798040) {
					if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 1352);
					} else if (dialog == QuestDialog.STEP_TO_2) {
						if (env.getVisibleObject() instanceof Npc) {
							targetId = ((Npc) env.getVisibleObject()).getNpcId();
							Npc npc = (Npc) env.getVisibleObject();
							npc.getController().onDelete();
							QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 798041, 2553.9f, 916.9f, 311.8f, (byte) 82);
						    return defaultCloseDialog(env, 0, 1);
						}
					}
				} else if (targetId == 798041) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1693);
				} else if (dialog == QuestDialog.STEP_TO_3) {
					return defaultStartFollowEvent(env, (Npc) env.getVisibleObject(), 798034, 1, 2);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798034) { 
			  if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				changeQuestStep(env, 2, 1, false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onNpcReachTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 2, true);
	}
	
	@Override
	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 1, false);
	}
}