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

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _2876Votan_Emergency_Orders extends QuestHandler
{
    private final static int questId = 2876;
	
    public _2876Votan_Emergency_Orders() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestNpc(278001).addOnQuestStart(questId); //Votan.
        qe.registerQuestNpc(278016).addOnTalkEvent(questId); //Lisya.
		qe.registerQuestNpc(278017).addOnTalkEvent(questId); //Semotor.
		qe.registerQuestNpc(278001).addOnTalkEvent(questId); //Votan.
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 278001) { //Votan.
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
			}
		} if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 278016) { //Lisya.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case SELECT_ACTION_1012: {
						return sendQuestDialog(env, 1012);
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
                    }
				}
			} if (targetId == 278017) { //Semotor.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1352);
					} case SELECT_ACTION_1353: {
						return sendQuestDialog(env, 1353);
					} case SET_REWARD: {
                        changeQuestStep(env, 1, 2, false);
						qs.setStatus(QuestStatus.REWARD);
						return closeDialogWindow(env);
                    }
				}
			}
		} 
		else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278001) { //Votan.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
            }
        }
		return false;
	}
}