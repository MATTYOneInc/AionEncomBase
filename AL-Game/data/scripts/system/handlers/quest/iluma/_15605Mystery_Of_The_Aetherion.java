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
public class _15605Mystery_Of_The_Aetherion extends QuestHandler {

    public static final int questId = 15605;
    public _15605Mystery_Of_The_Aetherion() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(806162).addOnQuestStart(questId); //Polonius.
		qe.registerQuestNpc(806162).addOnTalkEvent(questId); //Polonius.
		qe.registerQuestNpc(703137).addOnTalkEvent(questId); //Solar Relic.
		qe.registerQuestNpc(703138).addOnTalkEvent(questId); //Lunar Relic.
        qe.registerQuestNpc(241162).addOnKillEvent(questId); //Auronos.
		qe.registerOnEnterZone(ZoneName.get("COURT_OF_AURONUS_210100000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806162) { //Polonius.
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
		} else if (qs.getStatus() == QuestStatus.START) {
           	int var = qs.getQuestVarById(0);
			if (targetId == 806162) { //Polonius.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        } else if (var == 1) {
							return sendQuestDialog(env, 1353);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1354: {
						if (var == 1) {
							return sendQuestDialog(env, 1354);
						}
					} case STEP_TO_2: {
						playQuestMovie(env, 1002);
						giveQuestItem(env, 182215995, 1);
						changeQuestStep(env, 1, 2, false);
						removeQuestItem(env, 182216010, 1); //ν?¬λ―Έν• κ³ λ€μ? μ μ ? μ΅°κ°?.
						removeQuestItem(env, 182216011, 1); //λΉ›λ‚λ” κ³ λ€μ? μ μ ? μ΅°κ°?.
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							changeQuestStep(env, 0, 1, false);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
				}
			} if (targetId == 703137) { //Solar Relic.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 0) {
							return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 703138) { //Lunar Relic.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
                        if (var == 0) {
							return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806162) { //Polonius.
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
			if (var == 3) {
				switch (targetId) {
                    case 241162: { //Auronos.
						qs.setStatus(QuestStatus.REWARD);
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
			if (zoneName == ZoneName.get("COURT_OF_AURONUS_210100000")) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}
}