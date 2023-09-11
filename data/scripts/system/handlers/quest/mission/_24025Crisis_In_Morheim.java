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
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24025Crisis_In_Morheim extends QuestHandler
{
    private final static int questId = 24025;
	
    public _24025Crisis_In_Morheim() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(204388).addOnTalkEvent(questId);
        qe.registerQuestNpc(204414).addOnTalkEvent(questId);
        qe.registerQuestNpc(204345).addOnTalkEvent(questId);
        qe.registerQuestNpc(204304).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24024, false);
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
                case 204388: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        case STEP_TO_1:
                            return defaultCloseDialog(env, 0, 1);
                        case CHECK_COLLECTED_ITEMS:
                            if (var == 2) {
                                if (QuestService.collectItemCheck(env, true)) {
                                    removeQuestItem(env, 182215370, 1);
                                    return defaultCloseDialog(env, 2, 3);
                                }
                            }
                    }
                    break;
                } case 204414: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 1352);
                        case STEP_TO_2:
                            return defaultCloseDialog(env, 1, 2);
                    }
                    break;
                } case 204345: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            return sendQuestDialog(env, 2034);
                        case SET_REWARD:
                            return defaultCloseDialog(env, 3, 3, true, false);
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204304) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}