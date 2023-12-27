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
public class _4937Recognition_Of_The_Preceptors extends QuestHandler {

	private final static int questId = 4937;
	
	public _4937Recognition_Of_The_Preceptors() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {204053, 204059, 204058, 204057, 204056, 204075, 801222, 801223};
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
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204053) { //Kvasir.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else if (env.getDialog() == QuestDialog.ASK_ACCEPTION) {
					return sendQuestDialog(env, 4);
                } else if (env.getDialog() == QuestDialog.ACCEPT_QUEST) {
					giveQuestItem(env, 182207112, 1);
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204059: { //Freyr.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1011);
						} case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				} case 204058: { //Sif.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						} case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				} case 204057: { //Sigyn.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						} case STEP_TO_3: {
							return defaultCloseDialog(env, 2, 3);
						}
					}
					break;
				} case 204056: { //Traufnir.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						} case STEP_TO_4: {
							return defaultCloseDialog(env, 3, 4, 182207113, 1, 182207112, 1);
						}
					}
					break;
				} case 801222: { //Hadubrant.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						} case STEP_TO_5: {
							return defaultCloseDialog(env, 4, 5);
						}
					}
					break;
				} case 801223: { //Brynhilde.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						} case STEP_TO_6: {
							return defaultCloseDialog(env, 5, 6);
						}
					}
					break;
				} case 204075: { //Balder.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 6 && checkItemExistence(env, 182207113, 1, false)) {
								return sendQuestDialog(env, 3058);
							}
						} case FINISH_DIALOG: {
							return defaultCloseDialog(env, var, var);
						} case SET_REWARD: {
							return checkItemExistence(env, 6, 7, true, 186000084, 1, true, 0, 3143, 0, 0);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204053) { //Kvasir.
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					if (checkItemExistence(env, 182207113, 1, true)) {
						return sendQuestEndDialog(env);
					} else {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		return false;
	}
}