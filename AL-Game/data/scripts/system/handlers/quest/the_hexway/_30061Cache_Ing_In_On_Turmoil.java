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
package quest.the_hexway;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _30061Cache_Ing_In_On_Turmoil extends QuestHandler {

	private final static int questId = 30061;
	public _30061Cache_Ing_In_On_Turmoil() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(798927).addOnTalkEvent(questId); //Versetti.
		qe.registerQuestNpc(799381).addOnTalkEvent(questId); //Lania.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 798927) { //Versetti.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1352);
					} case STEP_TO_1: {
						return defaultCloseDialog(env, 0, 1);
					}
				}
			} else if (targetId == 799381) { //Lania.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					} case SELECT_REWARD: {
						changeQuestStep(env, 1, 1, true);
						return sendQuestDialog(env, 5);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799381) { //Lania.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}