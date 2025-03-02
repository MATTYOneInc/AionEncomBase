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
package quest.levinshor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _23700Advancement_In_Levinshor extends QuestHandler {

    private final static int questId = 23700;
    public _23700Advancement_In_Levinshor() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerQuestNpc(804719).addOnQuestStart(questId); //Haldor.
		qe.registerQuestNpc(804719).addOnTalkEvent(questId); //Haldor.
		qe.registerQuestNpc(802353).addOnTalkEvent(questId); //Yasan.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804719) { //Haldor.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 802353: { //Yasan.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							playQuestMovie(env, 717);
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 802353) { //Yasan.
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
}