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

public class _28826Free_To_A_Good_Home extends QuestHandler
{
	private static final int questId = 28826;
	
	public _28826Free_To_A_Good_Home() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(830662).addOnQuestStart(questId); //Logirunerk.
		qe.registerQuestNpc(830663).addOnQuestStart(questId); //Davinrinerk.
		qe.registerQuestNpc(830662).addOnTalkEvent(questId); //Logirunerk.
		qe.registerQuestNpc(830663).addOnTalkEvent(questId); //Davinrinerk.
		qe.registerQuestNpc(730525).addOnTalkEvent(questId); //Vintage Grab Box.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			switch (targetId) {
			    case 830662: //Logirunerk.
				case 830663: { //Davinrinerk.
				    switch (dialog) {
					    case START_DIALOG: {
						    return sendQuestDialog(env, 1011);
					    } default: {
						    return sendQuestStartDialog(env);
					    }
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 730525) { //Vintage Grab Box.
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 2375);
					} case SELECT_REWARD: {
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730525) { //Vintage Grab Box.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}