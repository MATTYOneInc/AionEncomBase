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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24022Sneak_Behind_The_Ice_Claw extends QuestHandler {

    private final static int questId = 24022;

    public _24022Sneak_Behind_The_Ice_Claw() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {204329, 204335, 204332, 700246, 204301, 802047};
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(204417).addOnKillEvent(questId);
        qe.registerQuestNpc(212877).addOnKillEvent(questId);
        qe.registerQuestItem(182215364, questId); //Hard Flint.
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
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
    public boolean onDialogEvent(final QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204329: { //Tofa.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 7) {
                                return sendQuestDialog(env, 3399);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        } case SET_REWARD: {
                            return defaultCloseDialog(env, 7, 7, true, false);
                        }
                    }
                } case 204335: { //Aprily.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        }
                    }
                } case 204332: { //Jorund.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
                            if (var == 2) {
                                return defaultCloseDialog(env, 2, 3, 182215364, 1, 0, 0);
                            }
                        }
                    }
                } case 700246: { //Dead Fire.
                    if (dialog == QuestDialog.USE_OBJECT) {
                        if (var == 3) {
                            if (player.getInventory().getItemCountByItemId(182215365) > 0) {
                                Npc npc = (Npc) env.getVisibleObject();
								QuestService.addNewSpawn(220020000, player.getInstanceId(), 204417, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
                                removeQuestItem(env, 182215365, 1);
                                return defaultCloseDialog(env, 3, 4);
                            }
                        }
                    }
                } case 802047: { //Landver.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 5) {
                                return sendQuestDialog(env, 2716);
                            }
                        } case STEP_TO_6: {
                            player.getTitleList().addTitle(58, true, 0);
                            return defaultCloseDialog(env, 5, 6);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204301) { //Aegir
                if (dialog == QuestDialog.USE_OBJECT) {
                    removeQuestItem(env, 182215364, 1);
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
    @Override
    public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
        Player player = env.getPlayer();
        if (player.isInsideZone(ZoneName.get("ALTAR_OF_TRIAL_220020000"))) {
            return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
        }
        return HandlerResult.FAILED;
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int targetId = env.getTargetId();
            switch (targetId) {
                case 204417: {
                    return defaultOnKillEvent(env, 204417, 4, 5);
                } case 212877: {
                    return defaultOnKillEvent(env, 212877, 6, 7);
                }
            }
        }
        return false;
    }
}