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

public class _14122Oasis_Wasted extends QuestHandler {

    private final static int questId = 14122;
    public _14122Oasis_Wasted() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(203917).addOnQuestStart(questId); //Gaia
        qe.registerQuestNpc(203917).addOnTalkEvent(questId); //Gaia
		qe.registerQuestNpc(203992).addOnTalkEvent(questId); //Ophelos
		qe.registerQuestNpc(203987).addOnTalkEvent(questId); //Heratos
		qe.registerQuestNpc(203934).addOnTalkEvent(questId); //Sirink
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203917) { //Gaia
			    if (env.getDialog() == QuestDialog.START_DIALOG) {
				   return sendQuestDialog(env, 1011);
			    } else {
				   return sendQuestStartDialog(env, 182215480, 1);
			    }
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 203992) { //Ophelos
			    if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				} else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				}
			} else if (targetId == 203987) { //Heratos
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1693);
				} else if (env.getDialog() == QuestDialog.STEP_TO_2) {
					return defaultCloseDialog(env, 1, 2);
				}
			} else if (targetId == 203934) { //Sirink
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 2375);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return checkQuestItems(env, 2, 2, true, 5, 2716);
				}
			}
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203934) { //Sirink
                return sendQuestEndDialog(env);
			}
		}
        return false;
    }
}