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

public class _24014Stomp_Out_The_Plot extends QuestHandler {

    private final static int questId = 24014;
    public _24014Stomp_Out_The_Plot() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(203665).addOnTalkEvent(questId);
        qe.registerQuestNpc(203668).addOnTalkEvent(questId);
        qe.registerQuestNpc(210562).addOnKillEvent(questId);
		qe.registerQuestNpc(210563).addOnKillEvent(questId);
		qe.registerQuestNpc(210583).addOnKillEvent(questId);
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
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        final int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203665:
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case STEP_TO_1:
                            return defaultCloseDialog(env, 0, 1);
                    }
                case 203668:
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            } else if (var == 5) {
                                return sendQuestDialog(env, 2716);
                            } else if (var == 6) {
                                return sendQuestDialog(env, 2034);
                            }
                        case STEP_TO_2:
                            if (var == 1) {
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
					            return closeDialogWindow(env);
                            }
                        case CHECK_COLLECTED_ITEMS:
                            return checkQuestItems(env, 5, 5, true, 5, 2120);
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203668) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if ((targetId == 210562 || targetId == 210563 || targetId == 210583) && var >= 2 && var < 5) {
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}