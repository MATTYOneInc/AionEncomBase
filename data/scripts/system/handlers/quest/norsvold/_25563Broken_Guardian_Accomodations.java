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
package quest.norsvold;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _25563Broken_Guardian_Accomodations extends QuestHandler {

	private final static int questId = 25563;
	public _25563Broken_Guardian_Accomodations() {
		super(questId);
	}
	
	@Override
	public void register() {
        qe.registerQuestItem(182215977, questId);  
		qe.registerQuestNpc(806116).addOnTalkEvent(questId); //Reinhard.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();  
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 0) {
                switch (dialog) {
                    case ACCEPT_QUEST:
				    case ACCEPT_QUEST_SIMPLE: {
					    return sendQuestStartDialog(env);
				    } case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
                    }
                }
			}
        }
		if (qs == null || qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806116: { //Reinhard.
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 10002);
						} case SELECT_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 806116) { //Reinhard.
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
		}
		return HandlerResult.FAILED;
	}
}