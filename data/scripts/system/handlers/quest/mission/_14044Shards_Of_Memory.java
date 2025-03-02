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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14044Shards_Of_Memory extends QuestHandler {

    private final static int questId = 14044;
    public _14044Shards_Of_Memory() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        qe.registerQuestNpc(279029).addOnTalkEvent(questId);
        qe.registerQuestNpc(278501).addOnTalkEvent(questId);
        qe.registerQuestNpc(790001).addOnTalkEvent(questId);
        qe.registerQuestNpc(700355).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 278501: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case STEP_TO_1:
                            if (var == 0) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
						        return closeDialogWindow(env);
                            }
                    }
                }
                case 279029: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        case STEP_TO_3:
                            if (var == 2) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                playQuestMovie(env, 271);
						        return closeDialogWindow(env);
                            }
                    }
                }
                case 700355:
                    return useQuestObject(env, 3, 3, true, false);
                case 790001: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        case STEP_TO_2:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
						        return closeDialogWindow(env);
                            }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 279029) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else if (env.getDialogId() == 1009) {
                    return sendQuestDialog(env, 5);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
            return false;
        }
        return false;
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14040, false);
    }
}