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
package quest.oriel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18807Blessed_Be_Thy_Hame extends QuestHandler
{
	private static final int questId = 18807;
	
	public _18807Blessed_Be_Thy_Hame() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(830194).addOnQuestStart(questId);
		qe.registerQuestNpc(830194).addOnTalkEvent(questId);
		qe.registerQuestNpc(730524).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 830194) {
				switch (dialog) {
					case START_DIALOG:
						playQuestMovie(env, 803);
						return sendQuestDialog(env, 1011);
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730524: {
					switch (dialog) {
						case START_DIALOG:
							return sendQuestDialog(env, 1352);
						case SELECT_ACTION_1353:
							return sendQuestDialog(env, 1353);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				} case 830194: {
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD:
							changeQuestStep(env, 1, 1, true);
							return sendQuestDialog(env, 5);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 830194) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}