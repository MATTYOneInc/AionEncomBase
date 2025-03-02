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
package quest.ashunatal_dredgion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _28931Seizing_The_Ashunatal_Supplies extends QuestHandler {

	private static final int questId = 28931;
	public _28931Seizing_The_Ashunatal_Supplies() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(806261).addOnQuestStart(questId);
		qe.registerQuestNpc(806261).addOnTalkEvent(questId);
		qe.registerQuestNpc(806260).addOnTalkEvent(questId);
		qe.registerQuestNpc(703344).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806261) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		} else if (targetId == 703344) {
			if (dialog == QuestDialog.USE_OBJECT) {
				closeDialogWindow(env);
				return true;
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806260) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 0, 0, true, 5, 2716);
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806260) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}