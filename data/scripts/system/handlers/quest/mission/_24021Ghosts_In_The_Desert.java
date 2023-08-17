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

public class _24021Ghosts_In_The_Desert extends QuestHandler
{
    private final static int questId = 24021;
    private int itemId = 182215363;
	
    public _24021Ghosts_In_The_Desert() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerQuestItem(itemId, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(204302).addOnTalkEvent(questId);
        qe.registerQuestNpc(204329).addOnTalkEvent(questId);
        qe.registerQuestNpc(802046).addOnTalkEvent(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24020, true);
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
                case 204302: { //Bragi.
                    switch (dialog) {
                        case START_DIALOG: {
                            return sendQuestDialog(env, 1011);
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                    break;
                } case 204329: { //Tofa.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case SELECT_ACTION_1353: {
                            if (var == 1) {
                                playQuestMovie(env, 73);
                                return sendQuestDialog(env, 1353);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        }

                    }
                } case 802046: { //Tofynir.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            } else if (var == 3) {
                                return sendQuestDialog(env, 10000);
                            }
                        } case CHECK_COLLECTED_ITEMS: {
                            return checkQuestItems(env, 2, 3, false, 10000, 10001);
                        } case STEP_TO_4: {
                            if (!player.getInventory().isFullSpecialCube()) {
                                return defaultCloseDialog(env, 3, 4, 182215363, 1, 0, 0);
                            } else {
                                return sendQuestSelectionDialog(env);
                            }
                        } case FINISH_DIALOG:
                            return sendQuestSelectionDialog(env);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204329) { //Tofa.
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
    @Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (item.getItemId() == 182215363 && player.isInsideZone(ZoneName.get("DF2_ITEMUSEAREA_Q2032"))) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true, 88));
            }
        }
        return HandlerResult.FAILED;
    }
}