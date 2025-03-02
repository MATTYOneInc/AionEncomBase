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
import com.aionemu.gameserver.model.TeleportAnimation;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14024A_Kralling_Suspicion extends QuestHandler {

    private final static int questId = 14024;
    private final static int[] npc_ids = {203904, 204045, 204004, 204020};
    public _14024A_Kralling_Suspicion() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc_id: npc_ids) {
            qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14020, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        } 
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
            if (targetId == 203904) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
                        if (var == 0) {
                            changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 204045) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        }
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
                        if (var == 1) {
                            changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 204004) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 2034);
                        }
                    }
                    case SELECT_ACTION_2035: {
                        if (var == 2) {
                            return sendQuestDialog(env, 2035);
                        }
                    }
                    case CHECK_COLLECTED_ITEMS: {
                        return checkQuestItems(env, 2, 3, false, 2120, 2035);
                    }
                    case STEP_TO_4: {
                        TeleportService2.teleportTo(player, 210020000, 1610f, 1528f, 318f, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
                        changeQuestStep(env, 3, 3, true);
						return closeDialogWindow(env);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204020) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 2375);
                }
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}