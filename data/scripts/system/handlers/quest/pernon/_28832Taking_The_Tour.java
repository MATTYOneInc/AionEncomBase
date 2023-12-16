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
package quest.pernon;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _28832Taking_The_Tour extends QuestHandler {

	private static final int questId = 28832;
	public _28832Taking_The_Tour() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(830532).addOnQuestStart(questId);
		qe.registerQuestNpc(830532).addOnTalkEvent(questId);
		qe.registerQuestNpc(830085).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 830532) {
				if (dialog == QuestDialog.START_DIALOG) 
					return sendQuestDialog(env, 1011); 
				else 
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 830085: {
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) 
								return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							playQuestMovie(env, 802);
							changeQuestStep(env, 0, 0, true);
							return sendQuestDialog(env, 5);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 830085) 
			return sendQuestEndDialog(env);
		}
		return false;
	}
}