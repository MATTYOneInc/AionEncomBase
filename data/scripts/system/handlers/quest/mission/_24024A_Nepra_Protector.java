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
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24024A_Nepra_Protector extends QuestHandler {

    private final static int questId = 24024;
    public _24024A_Nepra_Protector() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(212861).addOnKillEvent(questId);
        qe.registerQuestNpc(204369).addOnTalkEvent(questId);
        qe.registerQuestNpc(730739).addOnTalkEvent(questId);
        qe.registerQuestNpc(204361).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("ALTAR_OF_THE_BLACK_DRAGON_220020000"), questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24020, false);
    }
	
    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        if (zoneName == ZoneName.get("ALTAR_OF_THE_BLACK_DRAGON_220020000")) {
            Player player = env.getPlayer();
            if (player == null) {
                return false;
            }
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                if (var == 3) {
                    playQuestMovie(env, 81);
                    changeQuestStep(env, 3, 4, false);
                    return true;
                }
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        return defaultOnKillEvent(env, 212861, 4, true);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204369: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case STEP_TO_1:
                            if (var == 0) {
                                playQuestMovie(env, 80);
                                return defaultCloseDialog(env, 0, 1);
                            }
                    }
                }
                case 204361: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        case STEP_TO_2:
                            if (var == 1) {
                                return defaultCloseDialog(env, 1, 2);
                            }

                    }
                }
                case 730739: {
                    switch (env.getDialog()) {
                        case START_DIALOG:
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        case STEP_TO_3:
                            if (var == 2) {
                                TeleportService2.teleportTo(env.getPlayer(), 220020000, 1979.7205f, 853.2568f, 248.5204f, (byte) 115);
                                return defaultCloseDialog(env, 2, 3);
                            }

                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204369) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}