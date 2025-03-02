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
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24012An_Ominous_Crop extends QuestHandler {

    private final static int questId = 24012;
    public _24012An_Ominous_Crop() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(203605).addOnTalkEvent(questId);
        qe.registerQuestNpc(700096).addOnTalkEvent(questId);
        qe.registerOnEnterZone(ZoneName.get("MUMU_FARMLAND_220030000"), questId);
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
    public boolean onDialogEvent(final QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        final int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203605: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 5) {
                                return sendQuestDialog(env, 2716);
                            }
                        case STEP_TO_1:
                            return defaultCloseDialog(env, 0, 1, 182215356, 1, 0, 0);
                        case CHECK_COLLECTED_ITEMS:
                            return checkQuestItems(env, 5, 5, true, 5, 2120);
                    }
                } case 700096: {
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
                            if (var >= 2 && var < 4) {
                                return useQuestObject(env, var, var + 1, false, true);
                            } else if (var == 4) {
                                return useQuestObject(env, 4, 5, false, true);
                            }
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203605) {
                removeQuestItem(env, 182215356, 1);
            }
            return sendQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        if (zoneName == ZoneName.get("MUMU_FARMLAND_220030000")) {
            Player player = env.getPlayer();
            if (player == null) {
                return false;
            }
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                if (var == 1) {
                    changeQuestStep(env, 1, 2, false);
                    return true;
                }
            }
        }
        return false;
    }
}