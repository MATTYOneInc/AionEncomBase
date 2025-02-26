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
package quest.hero_alliance;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _13900When_Kaisinel_Calls extends QuestHandler {

    private final static int questId = 13900;
    public _13900When_Kaisinel_Calls() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerQuestNpc(804699).addOnQuestStart(questId);
		qe.registerQuestNpc(804699).addOnTalkEvent(questId); 
		qe.registerQuestNpc(798926).addOnTalkEvent(questId);
		qe.registerQuestNpc(203726).addOnTalkEvent(questId);
		qe.registerQuestNpc(798514).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} 
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804699) { 
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		}
        if (qs == null || qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798926: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case SELECT_ACTION_1353: {
							return sendQuestDialog(env, 1353);
						} case STEP_TO_1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
				            return closeDialogWindow(env);
						}
					}
				} case 203726: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						} case SELECT_ACTION_1694: {
							return sendQuestDialog(env, 1694);
						} case STEP_TO_2: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
				            return closeDialogWindow(env);
						}
					}
				} case 798514: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_ACTION_2376: {
							return sendQuestDialog(env, 2376);
						} case SELECT_REWARD: {
							qs.setQuestVar(3);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798514) {
                return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}