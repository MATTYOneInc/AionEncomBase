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
package quest.aturam_sky_fortress;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _28303Char_Bombom_Boom  extends QuestHandler
{
	private final static int questId = 28303;
	
	public _28303Char_Bombom_Boom() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(799530).addOnQuestStart(questId);
		qe.registerQuestNpc(799530).addOnTalkEvent(questId);
		qe.registerQuestNpc(730390).addOnTalkEvent(questId);
		qe.registerQuestNpc(700980).addOnTalkEvent(questId);
		qe.registerQuestNpc(804821).addOnTalkEvent(questId);
		qe.registerQuestNpc(217382).addOnKillEvent(questId);
		qe.registerQuestNpc(217376).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799530) {
				switch (env.getDialog()) {
					case START_DIALOG:
						playQuestMovie(env, 470);
						return sendQuestDialog(env, 4762);
					default:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 730390) {
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 1011);
					case USE_OBJECT:
						return sendQuestDialog(env, 1007);
					case STEP_TO_1:
						return closeDialogWindow(env);
					default:
						return sendQuestStartDialog(env);
				}
			} else if (targetId == 700980) {
				return useQuestObject(env, 2, 3, true, true);
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804821) {
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 10002);
					case SELECT_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0) {
				return defaultOnKillEvent(env, 217382, 0, 1);
			} else if (var == 1) {
				return defaultOnKillEvent(env, 217376, 1, 2);
			}
		}
		return false;
	}
}