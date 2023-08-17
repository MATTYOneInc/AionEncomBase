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
package quest.kromedes_trial;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _28600Suspicious_Errand extends QuestHandler
{
	private final static int questId = 28600;
	
	public _28600Suspicious_Errand() {
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		int[] npcs = {204702, 205233, 804607};
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
			qe.registerQuestNpc(204702).addOnQuestStart(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		if (sendQuestNoneDialog(env, 204702, 182213004, 1)) {
			return true;
		}
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 205233) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						} else if (var == 2) {
							return sendQuestDialog(env, 2034);
						}
					} case STEP_TO_1: {
						return defaultCloseDialog(env, 0, 1);
					} case STEP_TO_3: {
						qs.setQuestVarById(0, 3);
						return defaultCloseDialog(env, 3, 3, true, false, 182213005, 1, 182213004, 1);
					}
				}
			} else if (env.getTargetId() == 804607) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1693);
						}
					} case STEP_TO_2: {
						return defaultCloseDialog(env, 1, 2, false, false, 182213004, 1, 0, 0);
					}
				}
			}
		}
		return sendQuestRewardDialog(env, 204702, 2375);
	}
}