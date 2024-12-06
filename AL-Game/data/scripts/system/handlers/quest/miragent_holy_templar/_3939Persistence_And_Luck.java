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
public class _3939Persistence_And_Luck extends QuestHandler {

	private final static int questId = 3939;
	public _3939Persistence_And_Luck() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = { 203701, 203780, 203781, 203752, 700537 };
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
			if (targetId == 203701) { //Lavirintos.
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
				case 203780: { //Cornelius.
					switch (dialog) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1011);
							if (var == 2)
								return sendQuestDialog(env, 1693);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
						case CHECK_COLLECTED_ITEMS:
							return checkQuestItems(env, 2, 3, false, 10000, 10001, 182206099, 1);
						case FINISH_DIALOG:
							return defaultCloseDialog(env, var, var);
					}
					break;
				} case 203781: { //Sabotes.
					switch (dialog) {
						case START_DIALOG:
							if (var == 1)
								return sendQuestDialog(env, 1352);
						case SELECT_ACTION_1354: {
							if (var == 1 && player.getInventory().tryDecreaseKinah(3400000)) {
								return defaultCloseDialog(env, 1, 2, 122001274, 1, 0, 0);
							} else {
								return sendQuestDialog(env, 1438);
							}
						} case FINISH_DIALOG:
							return defaultCloseDialog(env, 1, 1);
					}
					break;
				} case 700537:
					if (dialog == QuestDialog.USE_OBJECT && var == 2) {
						return useQuestObject(env, 2, 2, false, 0);
					}
				break;
				case 203752: { // Jucleas.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						} case SET_REWARD: {
							if (player.getInventory().getItemCountByItemId(186000080) >= 1) {
								removeQuestItem(env, 186000080, 1);
								return defaultCloseDialog(env, 3, 3, true, false, 0);
							} else {
								return sendQuestDialog(env, 2120);
							}
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				}
				default:
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203701) { //Lavirintos.
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 10002);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}