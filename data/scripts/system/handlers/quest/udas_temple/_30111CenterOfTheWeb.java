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
package quest.udas_temple;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Cheatkiller
 *
 */
public class _30111CenterOfTheWeb extends QuestHandler {

	private final static int questId = 30111;
	public _30111CenterOfTheWeb() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(799335).addOnQuestStart(questId);
		qe.registerQuestNpc(799335).addOnTalkEvent(questId);
		qe.registerQuestNpc(215792).addOnKillEvent(questId);
        qe.registerOnEnterZone(ZoneName.get("SENSORYAREA_Q30011_300160000"), questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
		    if (targetId == 799335) { // Sulbanyer
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 1011);
				    case ASK_ACCEPTION: {
					    return sendQuestDialog(env, 4);
				    }
				    case ACCEPT_QUEST: {
					    return sendQuestStartDialog(env);
				    }
				    case REFUSE_QUEST: {
					   return closeDialogWindow(env);
                    }
				}
			}
		}
        if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 799335) // Sulbanyer
			return sendQuestEndDialog(env);
		}
		return false;  
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 215792, 1, true);
	}
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (zoneName.equals(ZoneName.get("SENSORYAREA_Q30011_300160000"))) {
                if (var == 0) {
                    changeQuestStep(env, 0, 1, false);
                    return true;
                }
            }
        }
        return false;
	}
}