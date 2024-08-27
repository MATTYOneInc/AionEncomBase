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
package quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4542The_Secret_Of_The_Seiren_Treasure extends QuestHandler {

    private final static int questId = 4542;
    public _4542The_Secret_Of_The_Seiren_Treasure() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestNpc(204768).addOnQuestStart(questId);
        qe.registerQuestNpc(204768).addOnTalkEvent(questId);
        qe.registerQuestNpc(204743).addOnTalkEvent(questId);
        qe.registerQuestNpc(204808).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204768) {
                switch (env.getDialog()) {
                    case START_DIALOG:
                        return sendQuestDialog(env, 4762);
                    case ACCEPT_QUEST_SIMPLE:
                        giveQuestItem(env, 182215327, 1);
                        return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 204743:
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case STEP_TO_1:
                            if (var == 0) {
                                giveQuestItem(env, 182215328, 1);
                                removeQuestItem(env, 182215327, 1);
                                return defaultCloseDialog(env, 0, 1);
                            }
                    }
                    break;
                case 204768:
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            } else if (var == 5) {
                                return sendQuestDialog(env, 2716);
                            }
                        case STEP_TO_2:
                            removeQuestItem(env, 182215328, 1);
                            playQuestMovie(env, 239);
                            return defaultCloseDialog(env, 1, 2);
                        case SELECT_REWARD:
                            removeQuestItem(env, 182215330, 1);
					        changeQuestStep(env, 5, 6, true);
                            return sendQuestEndDialog(env);
                    }
                    break;
                case 204808:
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            } if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            } if (var == 4) {
                                return sendQuestDialog(env, 2376);
                            }
                        case STEP_TO_3:
                            if (var == 2) {
                                playQuestMovie(env, 240);
                                return defaultCloseDialog(env, 2, 3);
                            }
                        case CHECK_COLLECTED_ITEMS:
                            return checkQuestItems(env, 3, 4, false, 10000, 10001);
                        case STEP_TO_5:
                            return defaultCloseDialog(env, 4, 5, false, false, 182215330, 1, 0, 0);
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204768) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}