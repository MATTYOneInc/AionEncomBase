/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _1527Rotten_Rotrons extends QuestHandler {

	private final static int questId = 1527;
	public _1527Rotten_Rotrons() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(204555).addOnQuestStart(questId); //Mirborante.
		qe.registerQuestNpc(204555).addOnTalkEvent(questId); //Mirborante.
		qe.registerQuestNpc(204562).addOnTalkEvent(questId); //Sirilis.
		qe.registerQuestNpc(205229).addOnTalkEvent(questId); //Raninia.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204555) { //Mirborante.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } 
                if (env.getDialog() == QuestDialog.SELECT_ACTION_1012) {
                    return sendQuestDialog(env, 1012);
                }
                if (env.getDialog() == QuestDialog.ACCEPT_QUEST_SIMPLE) {
                    return sendQuestStartDialog(env);
                }
                if (env.getDialog() == QuestDialog.REFUSE_QUEST_SIMPLE) {
				    return closeDialogWindow(env);
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
            if (targetId == 204562) { //Sirilis.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				}
                else if (env.getDialog() == QuestDialog.SELECT_ACTION_1353) {
					return sendQuestDialog(env, 1353);
				}
                else if (env.getDialog() == QuestDialog.SELECT_ACTION_1354) {
					return sendQuestDialog(env, 1354);
				}
                else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
            if (targetId == 205229) { //Raninia.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
			}
		}
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205229) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}