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
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _3935Shoulder_The_Burden extends QuestHandler {

	private final static int questId = 3935;
	public _3935Shoulder_The_Burden() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203316, 203702, 203329, 203752, 203701};
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203701) {
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
				case 203316:
					switch (dialog) {
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
				break;
				case 203702:
					if (var == 1) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 1352);
							case STEP_TO_2:
								return defaultCloseDialog(env, 1, 2);
						}
					}
				break;
				case 203329:
					if (var == 2) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 1693);
							case STEP_TO_3:
								return defaultCloseDialog(env, 2, 3);
						}
					} if (var == 3) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 2034);
							case CHECK_COLLECTED_ITEMS:
								if (QuestService.collectItemCheck(env, true)) {
									changeQuestStep(env, 3, 4, false);
									return sendQuestDialog(env, 10000);
								} else
									return sendQuestDialog(env, 10001);
						}
					}
				break;
				case 203752:
					switch (dialog) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						} case SET_REWARD: {
							if (player.getInventory().getItemCountByItemId(186000080) >= 1) {
								removeQuestItem(env, 186000080, 1);
								return defaultCloseDialog(env, 4, 4, true, false, 0);
							} else {
								return sendQuestDialog(env, 2461);
							}
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				default:
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203701) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}