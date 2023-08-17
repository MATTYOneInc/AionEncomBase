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
package quest.archives_of_eternity;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author (Encom)
/****/

public class _26806They_Dont_Make_Em_Like_They_Used_To extends QuestHandler
{
    private final static int questId = 26806;
	private final static int[] IDEternity01Mobs = {220306, 220309, 220312, 220315, 220318, 220324, 220327, 220330};
	private final static int[] IDEternity01Boss = {857450, 857452, 857454, 857456, 857458, 857459};
	
    public _26806They_Dont_Make_Em_Like_They_Used_To() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(806149).addOnTalkEvent(questId);
		for (int mob: IDEternity01Mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDEternity01Boss) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806149) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806149: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 806149) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
		    }
		}
		return false;
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 301540000) {
            if (qs == null || qs.canRepeat()) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					return true;
				}
            }
        }
        return false;
    }
	
	public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
				case 220306:
				case 220309:
				case 220312:
				case 220315:
				case 220318:
				case 220324:
				case 220327:
				case 220330:
				    if (qs.getQuestVarById(1) < 30) {
					    qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					    updateQuestStatus(env);
				    } if (qs.getQuestVarById(1) >= 30) {
					    updateQuestStatus(env);
				    }
				break;
				case 857450:
				case 857452:
				case 857454:
				case 857456:
				case 857458:
				case 857459:
			        if (qs.getQuestVarById(2) < 2) {
					    qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
					    updateQuestStatus(env);
				    } if (qs.getQuestVarById(2) >= 2) {
					    qs.setStatus(QuestStatus.REWARD);
					    updateQuestStatus(env);
				    }
				break;
            }
        }
        return false;
    }
}