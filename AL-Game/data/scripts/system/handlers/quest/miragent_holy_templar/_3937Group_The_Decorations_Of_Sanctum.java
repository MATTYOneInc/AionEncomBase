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
package quest.miragent_holy_templar;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _3937Group_The_Decorations_Of_Sanctum extends QuestHandler {

	private final static int questId = 3937;
	public _3937Group_The_Decorations_Of_Sanctum() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203708).addOnQuestStart(questId);
		qe.registerQuestNpc(203708).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || (qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)) {
			if (targetId == 203708) {
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		} if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203708:
					switch (dialog) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 1)
								return sendQuestDialog(env, 1352);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
						case CHECK_COLLECTED_ITEMS:
							long itemCount1 = player.getInventory().getItemCountByItemId(182206095);
							if (itemCount1 >= 1) {
								removeQuestItem(env, 182206095, 1);
								changeQuestStep(env, 1, 1, true);
								return sendQuestDialog(env, 5);
							}
							else
								return sendQuestDialog(env, 10001);
					}
					break;
				default:
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203708)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}