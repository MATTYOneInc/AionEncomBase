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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24013Poison_In_The_Waters extends QuestHandler {

    private final static int questId = 24013;
    private final static int[] mobs = {210454, 210455, 210456, 210457, 210458, 214032, 214039};
	
    public _24013Poison_In_The_Waters() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(203631).addOnTalkEvent(questId);
        qe.registerQuestNpc(203621).addOnTalkEvent(questId);
        for (int mob : mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
        qe.registerQuestItem(182215359, questId);
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
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203631: { //Nokir.
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case SELECT_ACTION_1012: {
                            playQuestMovie(env, 63);
                            return sendQuestDialog(env, 1012);
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                } case 203621: { //Shania.
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            giveQuestItem(env, 182215359, 1);
                            return defaultCloseDialog(env, 1, 2);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203631) { //Nokir.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 2375);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        if (var >= 3 && var < 7) {
            qs.setQuestVarById(0, var + 1);
            updateQuestStatus(env);
            return true;
        } else if (var == 7) {
            qs.setStatus(QuestStatus.REWARD);
            updateQuestStatus(env);
            return true;
        }
        return false;
    }
	
    @Override
    public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
        Player player = env.getPlayer();
        if (player.isInsideZone(ZoneName.get("DF1A_ITEMUSEAREA_Q2016"))) {
            return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 3, false));
        }
        return HandlerResult.FAILED;
    }
}