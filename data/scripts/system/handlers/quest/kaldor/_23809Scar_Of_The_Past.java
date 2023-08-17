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
package quest.kaldor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _23809Scar_Of_The_Past extends QuestHandler
{
	private final static int questId = 23809;
	
	public _23809Scar_Of_The_Past() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(802429).addOnQuestStart(questId); //Vidarr.
        qe.registerQuestNpc(802429).addOnTalkEvent(questId); //Vidarr.
        qe.registerQuestNpc(730969).addOnTalkEvent(questId); //Scorched Tree.
        qe.registerQuestNpc(730970).addOnTalkEvent(questId); //Cindery Tree.
		qe.registerQuestNpc(730971).addOnTalkEvent(questId); //Burnt Tree.
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 802429) { //Vidarr.
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 730969: { //Scorched Tree.
                    switch (env.getDialog()) {
						case USE_OBJECT: {
                            return useQuestObject(env, 0, 1, false, 0);
                        }
                    }
                    break;
                } case 730970: { //Cindery Tree.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
                            return useQuestObject(env, 1, 2, false, 0);
                        }
                    }
                    break;
                } case 730971: { //Burnt Tree.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
                            return useQuestObject(env, 2, 3, false, 0);
                        }
                    }
                    break;
                } case 802429: { //Vidarr.
				    switch (dialog) {
					    case START_DIALOG:
						    return sendQuestDialog(env, 2375);
					    case SELECT_REWARD:
						    changeQuestStep(env, 3, 4, true);
						    return sendQuestDialog(env, 5);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 802429) { //Vidarr.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}