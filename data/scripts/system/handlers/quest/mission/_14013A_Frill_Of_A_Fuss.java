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

public class _14013A_Frill_Of_A_Fuss extends QuestHandler {

    private final static int questId = 14013;
    public _14013A_Frill_Of_A_Fuss() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] mobs = {210126, 210200, 210201, 210202};
        qe.registerQuestNpc(203129).addOnTalkEvent(questId);
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14010, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null) {
            return false;
        } if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case (203129): {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            if (var == 0) {
                                return defaultCloseDialog(env, 0, 1);
                            }
						}
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203129) {
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 1) {
                int[] rakeclaw = {210126};
                int[] giantrakeclaw = {210200, 210201};
                int[] trandila = {210202};
                switch (targetId) {
                    case 210126: {
                        return defaultOnKillEvent(env, rakeclaw, 0, 5, 1);
                    } case 210200:
                      case 210201: {
                        return defaultOnKillEvent(env, giantrakeclaw, 0, 7, 2);
                    } case 210202: {
                        return defaultOnKillEvent(env, trandila, 1, true);
                    }
                }
            }
        }
        return false;
    }
}