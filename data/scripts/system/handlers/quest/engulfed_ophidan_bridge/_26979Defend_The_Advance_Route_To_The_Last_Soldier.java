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
package quest.engulfed_ophidan_bridge;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _26979Defend_The_Advance_Route_To_The_Last_Soldier extends QuestHandler {

	public static final int questId = 26979;
	public _26979Defend_The_Advance_Route_To_The_Last_Soldier() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(802026).addOnQuestStart(questId); //Moireste.
		qe.registerQuestNpc(801764).addOnTalkEvent(questId); //Undgankt.
		qe.registerQuestNpc(801764).addOnTalkEvent(questId); //Undgankt.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 802026) { //Moireste.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else if (env.getDialog() == QuestDialog.ASK_ACCEPTION) {
                    return sendQuestDialog(env, 4);
				} else if (env.getDialog() == QuestDialog.ACCEPT_QUEST) {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 801764: { //Undgankt.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 801764) { //Undgankt.
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
}