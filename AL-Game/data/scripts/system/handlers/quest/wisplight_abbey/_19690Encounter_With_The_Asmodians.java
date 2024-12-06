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
package quest.wisplight_abbey;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _19690Encounter_With_The_Asmodians extends QuestHandler
{
	private final static int questId = 19690;
	
	public _19690Encounter_With_The_Asmodians() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnKillInWorld(0, questId);
		qe.registerQuestNpc(806698).addOnQuestStart(questId);
		qe.registerQuestNpc(806698).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onKillInWorldEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		if (env.getVisibleObject() instanceof Player && player != null) {
			if ((env.getPlayer().getLevel() >= (((Player)env.getVisibleObject()).getLevel() - 5)) &&
			    (env.getPlayer().getLevel() <= (((Player)env.getVisibleObject()).getLevel() + 9))) {
				return defaultOnKillRankedEvent(env, 0, 3, true);
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 806698) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			    switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					}
				}
			} if (qs.getStatus() == QuestStatus.REWARD) {
				if (targetId == 806698) {
				    if (env.getDialog() == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 10002);
					} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
						return sendQuestDialog(env, 5);
					} else {
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
}