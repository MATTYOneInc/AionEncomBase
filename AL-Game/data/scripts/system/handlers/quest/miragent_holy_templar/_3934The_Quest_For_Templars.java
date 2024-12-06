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
public class _3934The_Quest_For_Templars extends QuestHandler {

	private final static int questId = 3934;
	public _3934The_Quest_For_Templars() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {798359, 798360, 798361, 798362, 798363, 798364, 798365, 798366, 203752, 203701};
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
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
				case 798359:
					switch (dialog) {
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
				break;
				case 798360:
					if (var == 1) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 1352);
							case STEP_TO_2:
								return defaultCloseDialog(env, 1, 2);
						}
					}
				break;
				case 798361:
					if (var == 2) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 1693);
							case STEP_TO_3:
								return defaultCloseDialog(env, 2, 3);
						}
					}
				break;
				case 798362:
					if (var == 3) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 2034);
							case STEP_TO_4:
								return defaultCloseDialog(env, 3, 4);
						}
					}
				break;
				case 798363:
					if (var == 4) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 2375);
							case STEP_TO_5:
								return defaultCloseDialog(env, 4, 5);
						}
					}
				break;
				case 798364:
					if (var == 5) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 2716);
							case STEP_TO_6:
								return defaultCloseDialog(env, 5, 6);
						}
					}
				break;
				case 798365:
					if (var == 6) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 3057);
							case STEP_TO_7:
								return defaultCloseDialog(env, 6, 7);
						}
					}
				break;
				case 798366:
					if (var == 7) {
						switch (dialog) {
							case START_DIALOG:
								return sendQuestDialog(env, 3398);
							case STEP_TO_8:
								return defaultCloseDialog(env, 7, 8);
						}
					}
				break;
				case 203752:
					switch (dialog) {
						case START_DIALOG: {
							if (var == 8) {
								return sendQuestDialog(env, 3739);
							}
						} case SET_REWARD: {
							if (player.getInventory().getItemCountByItemId(186000080) >= 1) {
								removeQuestItem(env, 186000080, 1);
								return defaultCloseDialog(env, 8, 8, true, false, 0);
							} else {
								return sendQuestDialog(env, 3825);
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