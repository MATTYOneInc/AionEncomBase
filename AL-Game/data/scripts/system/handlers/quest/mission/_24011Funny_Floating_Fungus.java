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

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24011Funny_Floating_Fungus extends QuestHandler
{
    private final static int questId = 24011;
	
    public _24011Funny_Floating_Fungus() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] talkNpcs = {203558, 203572, 203558};
        qe.registerQuestNpc(700092).addOnKillEvent(questId);
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        for (int id : talkNpcs) {
            qe.registerQuestNpc(id).addOnTalkEvent(questId);
        }
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24010, false);
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (targetId == 700092) {
            if (var > 0 && var < 6) {
                qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                updateQuestStatus(env);
                return true;
            } else if (var == 6) {
                changeQuestStep(env, 6, 6, true);
                return true;
            }
        }
        return false;
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 203558) {
                switch (env.getDialog()) {
                    case START_DIALOG:
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
                    case STEP_TO_1:
                        return defaultCloseDialog(env, 0, 1);
                }
            } else if (targetId == 203572) {
                switch (env.getDialog()) {
                    case START_DIALOG:
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        }
                    case STEP_TO_2:
                        playQuestMovie(env, 60);
                        return defaultCloseDialog(env, 1, 2);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203558) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}