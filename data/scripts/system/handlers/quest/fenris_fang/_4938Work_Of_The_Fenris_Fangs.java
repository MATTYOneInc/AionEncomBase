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
package quest.fenris_fang;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _4938Work_Of_The_Fenris_Fangs extends QuestHandler {

	private final static int questId = 4938;
	public _4938Work_Of_The_Fenris_Fangs() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {204053, 798367, 798368, 798369, 798370, 798371, 798372, 798373, 798374, 204075};
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
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
			if (targetId == 204053) { //Kvasir.
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 798367: { //Riikaard.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						}
					}
				} case 798368: { //Herosir.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2);
						}
					}
				} case 798369: { //Gellner.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_3: {
							return defaultCloseDialog(env, 2, 3);
						}
					}
				} case 798370: { //Natorp.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_4: {
							return defaultCloseDialog(env, 3, 4);
						}
					}
				} case 798371: { //Needham.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						}
						case STEP_TO_5: {
							return defaultCloseDialog(env, 4, 5);
						}
					}
				} case 798372: { //Landsberg.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						}
						case STEP_TO_6: {
							return defaultCloseDialog(env, 5, 6);
						}
					}
				} case 798373: { //Levinard.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						}
						case STEP_TO_7: {
							return defaultCloseDialog(env, 6, 7);
						}
					}
				} case 798374: { //Lonergan.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 7) {
								return sendQuestDialog(env, 3398);
							}
						}
						case STEP_TO_8: {
							return defaultCloseDialog(env, 7, 8);
						}
					}
				} case 204075: { //Balder.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 8) {
								return sendQuestDialog(env, 3739);
							}
						} case SET_REWARD: {
							if (player.getInventory().getItemCountByItemId(186000084) >= 1) {
								removeQuestItem(env, 186000084, 1);
								return defaultCloseDialog(env, 8, 8, true, false, 0);
							} else {
								return sendQuestDialog(env, 3825);
							}
						}
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204053) { //Kvasir.
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