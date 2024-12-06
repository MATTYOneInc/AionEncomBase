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
package quest.mission;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24044Change_The_Future extends QuestHandler
{
    private final static int questId = 24044;
	
    public _24044Change_The_Future() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(278036).addOnTalkEvent(questId);
        qe.registerQuestNpc(203550).addOnTalkEvent(questId);
        qe.registerQuestNpc(204207).addOnTalkEvent(questId);
        qe.registerQuestNpc(798067).addOnTalkEvent(questId);
        qe.registerQuestNpc(279029).addOnTalkEvent(questId);
        qe.registerQuestNpc(700355).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null) {
            return false;
        }
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 278036: { //Scoda
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                } case 203550: { //Munin
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        }
                    }
                    break;
                } case 204207: { //Kasir
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
                            return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                } case 798067: { //Lyeanenerk
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_4: {
                            return defaultCloseDialog(env, 3, 4);
                        }
                    }
                    break;
                } case 279029: { //Lugbug
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 4) {
                                return sendQuestDialog(env, 2375);
                            } else if (var == 6) {
                                return sendQuestDialog(env, 3057);
                            }
                        } case STEP_TO_5: {
                            return defaultCloseDialog(env, 4, 5, 188020000, 1, 0, 0);
                        } case SET_REWARD: {
                            return defaultCloseDialog(env, 6, 6, true, false);
                        }
                    }
                    break;
                } case 700355: { //Artifact Of The Inception.
                    switch (dialog) {
                        case USE_OBJECT: {
                            if (var == 5) {
                                if (player.getInventory().getItemCountByItemId(188020000) > 0) {
                                    return useQuestObject(env, 5, 6, false, 0, 0, 0, 188020000, 1, 291, false);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278036) { //Scoda.
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24043, false);
    }
}