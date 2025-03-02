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
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _2345Orashunerk_Special_Order extends QuestHandler {

	private final static int questId = 2345;
	int rewardIndex;
	public _2345Orashunerk_Special_Order() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(798084).addOnQuestStart(questId);
		qe.registerQuestNpc(798084).addOnTalkEvent(questId);
		qe.registerQuestNpc(700238).addOnTalkEvent(questId);
		qe.registerQuestNpc(204304).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798084) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 798084) {
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					} else if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
				} else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
				  return checkQuestItems(env, 0, 1, false, 10000, 10001);
				} else if (dialog == QuestDialog.SELECT_ACTION_1353) {
					return sendQuestDialog(env, 1353);
				} else if (dialog == QuestDialog.SELECT_ACTION_1438) {
					return sendQuestDialog(env, 1438);
				} else if (dialog == QuestDialog.STEP_TO_10) {
					giveQuestItem(env, 182204137, 1);
					changeQuestStep(env, 1, 10, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				} else if (dialog == QuestDialog.STEP_TO_20) {
					giveQuestItem(env, 182204138, 1);
					changeQuestStep(env, 1, 20, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			} else if (targetId == 700238 && player.getInventory().getItemCountByItemId(182204136) < 3) {
				return true;
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204304) {
				if (dialog == QuestDialog.START_DIALOG) {
					if (qs.getQuestVarById(0) == 10) {
						removeQuestItem(env, 182204137, 1);
						return sendQuestDialog(env, 1693);
					} else if (qs.getQuestVarById(0) == 20) {
						rewardIndex = 1;
						removeQuestItem(env, 182204138, 1);
						return sendQuestDialog(env, 2034);
					}
				}
				return sendQuestEndDialog(env, rewardIndex);
			}
		}
		return false;
	}
}