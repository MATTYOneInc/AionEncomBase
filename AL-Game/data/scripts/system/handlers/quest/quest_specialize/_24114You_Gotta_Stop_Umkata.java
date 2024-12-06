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
package quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
 
/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _24114You_Gotta_Stop_Umkata extends QuestHandler {

    private final static int questId = 24114;
	
	private final static int[] heroSpirit = {210722, 210588}; //Hero Spirit
	
    public _24114You_Gotta_Stop_Umkata() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(203649).addOnQuestStart(questId); //Gulkalla
        qe.registerQuestNpc(203649).addOnTalkEvent(questId); //Gulkalla
		qe.registerQuestNpc(700097).addOnTalkEvent(questId); //Umkata's Jewel Box
		qe.registerQuestNpc(700098).addOnTalkEvent(questId); //Umkata's Grave
		qe.registerQuestNpc(210752).addOnKillEvent(questId); //Umkata
		for (int mob: heroSpirit) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203649) { //Gulkalla
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
		} else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 203649) { //Gulkalla
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 3) {
							return sendQuestDialog(env, 1011);
						} else if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					} case STEP_TO_1: {
						return defaultCloseDialog(env, 3, 4);
					}
				}
			} else if (targetId == 700097) {
                if (env.getDialog() == QuestDialog.USE_OBJECT && var == 4) {
					giveQuestItem(env, 182215475, 1);
                    return false;
                }
			} else if (targetId == 700098) { //Umkata's Grave
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (var == 4) {
							return sendQuestDialog(env, 1693);
						}
                        } case CHECK_COLLECTED_ITEMS: {
                            QuestService.addNewSpawn(220030000, 1, 210752, 2889.9834f, 1741.3108f, 254.75f, (byte) 0);
						return checkQuestItems(env, 4, 4, false, 0, 0);
					}
				}
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203649) { //Gulkalla
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1352);
					} case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default: {
                        return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } if (var >= 0 && var < 3) {
			return defaultOnKillEvent(env, heroSpirit, var, var + 1);
		} else if (var == 4) {
			return defaultOnKillEvent(env, 210752, 4, true);
		}
		return false;
	}
}