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
package quest.adma_fall;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author (Encom)
/****/

public class _28990Fallen_Doorways extends QuestHandler
{
    private final static int questId = 28990;
	private final static int[] IDF6AdmaZombieSN67Ae = {220417}; //악령�?� 저주를 받�?� 지투른.
	private final static int[] IDF6AdmaPrincessSN67Ae = {220418}; //악령�?� 저주를 받�?� 카르미웬.
	private final static int[] IDF6AdmaEvilSpirit67Ah = {220427}; //아티팩트를 지배하는 악령.
	
    public _28990Fallen_Doorways() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806079).addOnQuestStart(questId); //Feregran.
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); //Feregran.
		qe.registerQuestNpc(806216).addOnTalkEvent(questId); //Petur.
		for (int mob: IDF6AdmaZombieSN67Ae) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDF6AdmaPrincessSN67Ae) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: IDF6AdmaEvilSpirit67Ah) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806079) { //Feregran.
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806216) { //Petur.
                if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
                } if (dialog == QuestDialog.STEP_TO_1) {
					qs.setQuestVarById(0, 1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806079) { //Feregran.
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 3) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 2, 3, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806079) { //Feregran.
				if (env.getDialogId() == 1352) {
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
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (var == 1) {
			if (targetId == 220417) { //악령�?� 저주를 받�?� 지투른.
				qs.setQuestVarById(1, 1);
			} else if (targetId == 220418) { //악령�?� 저주를 받�?� 카르미웬.
				qs.setQuestVarById(2, 1);
			}
			updateQuestStatus(env);
			if (qs.getQuestVarById(1) == 1 && qs.getQuestVarById(2) == 1) {
				changeQuestStep(env, 1, 2, false);
			}
		} else if (var == 2) {
            if (targetId == 220427) { //아티팩트를 지배하는 악령.
                qs.setStatus(QuestStatus.REWARD);
				changeQuestStep(env, 2, 3, false);
				updateQuestStatus(env);
            }
        }
        return false;
    }
}