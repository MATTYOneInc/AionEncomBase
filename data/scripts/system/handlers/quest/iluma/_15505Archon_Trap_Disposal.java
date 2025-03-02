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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15505Archon_Trap_Disposal extends QuestHandler {

    private final static int questId = 15505;
    public _15505Archon_Trap_Disposal() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806090).addOnQuestStart(questId);
        qe.registerQuestNpc(806090).addOnTalkEvent(questId);
		qe.registerQuestNpc(703064).addOnTalkEvent(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806090) {
				switch (env.getDialog()) {
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
		} else if (targetId == 703064) {
			if (env.getDialog() == QuestDialog.USE_OBJECT) {
				closeDialogWindow(env);
				return true;
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 806090) {
				switch (env.getDialog()) {
					case START_DIALOG: {
                        return sendQuestDialog(env, 1011);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806090) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}