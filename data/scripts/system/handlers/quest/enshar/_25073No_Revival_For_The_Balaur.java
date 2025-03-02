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
package quest.enshar;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _25073No_Revival_For_The_Balaur extends QuestHandler {

	private static final int questId = 25073;
	public _25073No_Revival_For_The_Balaur() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(804918).addOnQuestStart(questId);
		qe.registerQuestNpc(804918).addOnTalkEvent(questId);
		qe.registerQuestNpc(731556).addOnTalkEvent(questId);
		qe.registerQuestNpc(804732).addOnTalkEndEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804918) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		} else if (targetId == 731556) {
				if (dialog == QuestDialog.USE_OBJECT) {
					if (player.getInventory().getItemCountByItemId(182215725) == 1) {
						return sendQuestDialog(env, 1011);
					}
				}
				else if (dialog == QuestDialog.SET_REWARD) {
					removeQuestItem(env, 182215725, 1);
					qs.setStatus(QuestStatus.REWARD);
					changeQuestStep(env, 0, 1, false);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}	
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804732) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}