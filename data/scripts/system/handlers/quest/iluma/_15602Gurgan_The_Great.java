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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
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

public class _15602Gurgan_The_Great extends QuestHandler {

    public static final int questId = 15602;
    public _15602Gurgan_The_Great() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(806159).addOnQuestStart(questId); //Alexandria.
		qe.registerQuestNpc(806159).addOnTalkEvent(questId); //Alexandria.
        qe.registerQuestNpc(806194).addOnTalkEvent(questId); //Celaday.
		qe.registerQuestNpc(703136).addOnTalkEvent(questId); //Cauldron.
        qe.registerQuestNpc(241153).addOnKillEvent(questId); //Gurgan.
		qe.registerOnEnterZone(ZoneName.get("CELLATUN_CIRCLE_210100000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806159) { //Alexandria.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
		    int var = qs.getQuestVarById(0);
			if (targetId == 806194) { //Celaday.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        } else if (var == 3) {
                            return sendQuestDialog(env, 2034);
                        }
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case SELECT_ACTION_2035: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							changeQuestStep(env, 3, 4, true);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
			} if (targetId == 703136) { //Cauldron.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 3) {
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806159) { //Alexandria.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 2) {
				switch (targetId) {
                    case 241153: { //Gurgan.
						qs.setQuestVar(3);
					    updateQuestStatus(env);
						return true;
					}
                }
			}
        }
        return false;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("CELLATUN_CIRCLE_210100000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
		}
		return false;
	}
}