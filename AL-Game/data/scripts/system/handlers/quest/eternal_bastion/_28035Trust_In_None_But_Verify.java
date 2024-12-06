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
package quest.eternal_bastion;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _28035Trust_In_None_But_Verify extends QuestHandler 
{
	private final static int questId = 28035;
	
	public _28035Trust_In_None_But_Verify() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestItem(182213484, questId);
		qe.registerQuestNpc(801047).addOnTalkEvent(questId);
		qe.registerQuestNpc(800529).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0) {
				switch (env.getDialog()) {
					case ACCEPT_QUEST_SIMPLE:
						QuestService.startQuest(env);
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 801047: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case SELECT_ACTION_1353: {
							return sendQuestDialog(env, 1353);
						} case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
				} case 800529: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							removeQuestItem(env, 182213484, 1);
							changeQuestStep(env, 1, 1, true);
							return sendQuestDialog(env, 5);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 800529) { 
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return HandlerResult.fromBoolean(sendQuestDialog(env, 1011));
		}
		return HandlerResult.FAILED;
	}
}