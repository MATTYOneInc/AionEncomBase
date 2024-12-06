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
package quest.dark_poeta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Kortana (Encom)
/****/

public class _3502Nereus_Need_You extends QuestHandler
{
	private final static int questId = 3502;
	private final static int[] mobs = {214894, 214895, 214896, 214897, 214904};
	
	public _3502Nereus_Need_You() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(204656).addOnQuestStart(questId);
		qe.registerQuestNpc(204656).addOnTalkEvent(questId);
		qe.registerQuestNpc(730192).addOnTalkEvent(questId);
		for (int mob: mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204656) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
					    return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 730192) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204656) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			int var3 = qs.getQuestVarById(3);
			switch (targetId) {
				case 214894:
					if (var == 1) {
						return defaultOnKillEvent(env, 214894, 1, 2, 0);
					}
				break;
				case 214895:
					if (var == 2 && var1 != 1) {
						defaultOnKillEvent(env, 214895, 0, 1, 1);
						if (var2 == 1 && var3 == 1) {
							return true;
						}
						return true;
					}
				break;
				case 214896:
					if (var == 2 && var2 != 1) {
						defaultOnKillEvent(env, 214896, 0, 1, 2);
						if (var1 == 1 && var3 == 1) {
							return true;
						}
						return true;
					}
				break;
				case 214897:
					if (var == 2 && var3 != 1) {
						defaultOnKillEvent(env, 214897, 0, 1, 3);
						if (var1 == 1 && var2 == 1) {
							return true;
						}
						return true;
					}
				break;
				case 214904:
					if (var == 2 && var1 == 1 && var2 == 1 && var3 == 1) {
						return defaultOnKillEvent(env, 214904, 2, true);
					}
				break;
			}
		}
		return false;
	}
}