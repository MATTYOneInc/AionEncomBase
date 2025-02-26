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

import com.aionemu.gameserver.model.gameobjects.Npc;
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

public class _25051Treasure_Of_Ancient_King extends QuestHandler {

    private final static int questId = 25051;
    public _25051Treasure_Of_Ancient_King() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(804915).addOnQuestStart(questId);
        qe.registerQuestNpc(804915).addOnTalkEvent(questId);
        qe.registerQuestNpc(731553).addOnTalkEvent(questId);
        qe.registerQuestNpc(805160).addOnTalkEvent(questId);
		qe.registerQuestNpc(731554).addOnTalkEvent(questId);
		qe.registerQuestNpc(731555).addOnTalkEvent(questId);
		qe.registerQuestNpc(804916).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 804915) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 731553) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    if (var == 0) {
                        return sendQuestDialog(env, 1011);
                    }
                } else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 805160, 2046.8f, 1588.8f, 348.4f, (byte) 90);
					changeQuestStep(env, 0, 1, false);
                    return closeDialogWindow(env);
                }
            } if (targetId == 805160) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    if (var == 1) {
                        return sendQuestDialog(env, 1352);
                    }
                } else if (env.getDialog() == QuestDialog.STEP_TO_2) {
					Npc npc = (Npc) env.getVisibleObject();
                    npc.getController().onDelete();
					changeQuestStep(env, 1, 2, false);
                    return closeDialogWindow(env);
                }
            } if (targetId == 731554) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    if (var == 2) {
                        return sendQuestDialog(env, 1693);
                    }
                } else if (env.getDialog() == QuestDialog.STEP_TO_3) {
					giveQuestItem(env, 182215720, 1);
					changeQuestStep(env, 2, 3, false);
                    return closeDialogWindow(env);
                }
            } if (targetId == 731555) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    if (var == 3) {
                        return sendQuestDialog(env, 2034);
                    }
                } else if (dialog == QuestDialog.SET_REWARD) {
					removeQuestItem(env, 182215720, 1);
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 220031, 1933.8f, 1418.8f, 359.6f, (byte) 37);
					qs.setStatus(QuestStatus.REWARD);
					changeQuestStep(env, 3, 4, false);
                    return closeDialogWindow(env);
                }
            }
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804916) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 2376);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}