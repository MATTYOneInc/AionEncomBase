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
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14053Danger_Cubed extends QuestHandler {

    private final static int questId = 14053;
    public _14053Danger_Cubed() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(204020).addOnTalkEvent(questId);
        qe.registerQuestNpc(204602).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14050, false);
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
        } if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204602) {
                return sendQuestEndDialog(env);
            }
        } else if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (targetId == 204602) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 0) {
                        return sendQuestDialog(env, 1011);
                    } else if (var == 2) {
                        return sendQuestDialog(env, 1693);
                    } else if (var == 3) {
                        return sendQuestDialog(env, 2034);
                    }
                case STEP_TO_1:
                    if (var == 0) {
                        TeleportService2.teleportTo(player, 210020000, 1601f, 1528f, 318.6f, (byte) 118);
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
					    return closeDialogWindow(env);
                    }
                case CHECK_COLLECTED_ITEMS:
                    if (QuestService.collectItemCheck(env, true)) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 10001);
                    }
                case SELECT_ACTION_1694:
                    playQuestMovie(env, 191);
                    break;
                case STEP_TO_3:
                    if (var == 2) {
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
					    return closeDialogWindow(env);
                    }
            }
        } else if (targetId == 204020) {
            switch (env.getDialog()) {
                case START_DIALOG:
                    if (var == 1) {
                        return sendQuestDialog(env, 1352);
                    }
                case STEP_TO_2:
                    if (var == 1) {
                        TeleportService2.teleportTo(player, 210040000, 2006.7308f, 1392.1913f, 118.125f, (byte) 4);
                        qs.setQuestVarById(0, var + 1);
                        updateQuestStatus(env);
					    return closeDialogWindow(env);
                    }
            }
        }
        return false;
    }
}