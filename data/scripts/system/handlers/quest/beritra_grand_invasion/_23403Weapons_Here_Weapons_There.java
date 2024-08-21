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
package quest.beritra_grand_invasion;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _23403Weapons_Here_Weapons_There extends QuestHandler {
	
    public static final int questId = 23403;
    public _23403Weapons_Here_Weapons_There() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestNpc(203557).addOnQuestStart(questId); //Suthran.
		qe.registerQuestNpc(203559).addOnTalkEvent(questId); //Meiyer.
		qe.registerQuestNpc(203560).addOnTalkEvent(questId); //Morn.
		qe.registerQuestNpc(203628).addOnTalkEvent(questId); //Kueve.
		qe.registerQuestNpc(804605).addOnTalkEvent(questId); //Shezen.
		qe.registerQuestNpc(731643).addOnTalkEvent(questId); //Damaged Invasion Generator.
		qe.registerQuestNpc(731644).addOnTalkEvent(questId); //Inoperable Invasion Generator.
		qe.registerQuestItem(182215798, questId); //Infiltration Sensor.
		qe.registerQuestItem(182215799, questId); //Infiltration Detector.
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203557) { //Suthran.
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
			if (targetId == 203559) { //Meiyer.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1013: {
						if (var == 0) {
							return sendQuestDialog(env, 1013);
						}
					} case STEP_TO_1: {
						if (var == 0) {
					    giveQuestItem(env, 182215798, 1); //Infiltration Sensor.
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
						}
					}
				}
			} if (targetId == 203628) { //Kueve.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case SELECT_ACTION_1695: {
						if (var == 2) {
							return sendQuestDialog(env, 1695);
						}
					} case STEP_TO_3: {
						if (var == 2) {
						giveQuestItem(env, 182215799, 1); //Infiltration Detector.
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
						}
					}
				}
			} if (targetId == 804605) { //Shezen.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case SELECT_ACTION_2375: {
						if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					} case STEP_TO_5: {
						if (var == 4) {
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
						}
					}
				}
			} if (targetId == 731645) { //Ruined Invasion Generator.
			    switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 5) {
                            return sendQuestDialog(env, 2716);
                        }
					} case STEP_TO_6: {
                        if (var == 5) {
						giveQuestItem(env, 182215800, 1); //Maintained Circuit.
                        changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
                        }
					}
                }
            } if (targetId == 731646) { //Malfunctioning Invasion Generator.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 6) {
                            return sendQuestDialog(env, 3057);
                        }
					} case SET_REWARD: {
                        if (var == 6) {
						giveQuestItem(env, 182215801, 1); //Undamaged Circuit.
                        changeQuestStep(env, 6, 7, false);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
                        }
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203560) { //Morn.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
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
            int var = qs.getQuestVarById(0);
            if (var == 1) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, false));
            } if (var == 3) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
            }
        }
        return HandlerResult.FAILED;
    }
}