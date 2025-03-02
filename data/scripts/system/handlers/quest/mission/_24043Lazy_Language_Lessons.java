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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.MathUtil;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _24043Lazy_Language_Lessons extends QuestHandler {

    private final static int questId = 24043;
    public _24043Lazy_Language_Lessons() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {278003, 278086, 278039, 279027, 204210};
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerQuestNpc(253610).addOnAttackEvent(questId);
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
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
                case 278003: { //Hisui.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
                } case 278086: { //Sinjah.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        }
                    }
                } case 278039: { //Grunn.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_4: {
                            return defaultCloseDialog(env, 3, 4);
                        }
                    }
                } case 279027: { //Kaoranerk.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 4) {
                                return sendQuestDialog(env, 2375);
                            } else if (var == 6) {
                                return sendQuestDialog(env, 3057);
                            }
                        } case SELECT_ACTION_3058: {
                            removeQuestItem(env, 182215373, 1);
                            playQuestMovie(env, 293);
                            return sendQuestDialog(env, 3058);
                        } case STEP_TO_5: {
                            return defaultCloseDialog(env, 4, 5);
                        } case SET_REWARD: {
                            return defaultCloseDialog(env, 6, 6, true, false);
                        }
                    }
                } case 204210: { //Phosphor.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 5) {
                                return sendQuestDialog(env, 2716);
                            }
                        } case STEP_TO_6: {
                            return defaultCloseDialog(env, 5, 6, 182215373, 1, 0, 0);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278003) { //Hisui.
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
	@Override
    public boolean onAttackEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
                final Npc npc = (Npc) env.getVisibleObject();
                final SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(npc.getWorldId(), 278086); //Sinjah.
                if (MathUtil.getDistance(searchResult.getSpot().getX(), searchResult.getSpot().getY(), searchResult.getSpot().getZ(), npc.getX(), npc.getY(), npc.getZ()) <= 30) {
                    npc.getController().onDie(player);
                    changeQuestStep(env, 2, 3, false);
                    return true;
                }
            }
        }
        return true;
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 24042, false);
    }
}