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
package quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24210Burning_The_Dragon extends QuestHandler {

    private final static int questId = 24210;
    public _24210Burning_The_Dragon() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerQuestNpc(802465).addOnQuestStart(questId);
		qe.registerQuestNpc(802663).addOnTalkEvent(questId);
		qe.registerQuestNpc(802664).addOnTalkEvent(questId);
		qe.registerQuestNpc(802665).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			 //Herailt.
			if (targetId == 802465 ||
			    targetId == 802663 ||
				targetId == 802664 ||
				targetId == 802665) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 802465 ||
			    targetId == 802663 ||
				targetId == 802664 ||
				targetId == 802665) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					}
					case SELECT_REWARD: {
						changeQuestStep(env, 0, 0, true);
						return sendQuestEndDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		     //Herailt.
			if (targetId == 802465 ||
			    targetId == 802663 ||
				targetId == 802664 ||
				targetId == 802665) {
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
}