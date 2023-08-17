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
package quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4737Victory_Against_The_7th extends QuestHandler
{
	private final static int questId = 4737;
	
	public _4737Victory_Against_The_7th() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(278035).addOnQuestStart(questId); //Konto.
		qe.registerQuestNpc(278035).addOnTalkEvent(questId); //Konto.
		qe.registerOnKillRanked(AbyssRankEnum.GRADE7_SOLDIER, questId);
	}
	
	@Override
    public boolean onKillRankedEvent(QuestEnv env) {
        return defaultOnKillRankedEvent(env, 0, 1, true);
    }
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 278035) { //Konto.
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			    switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					}
					case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
				}
			} if (qs.getStatus() == QuestStatus.REWARD) {
				if (targetId == 278035) { //Konto.
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