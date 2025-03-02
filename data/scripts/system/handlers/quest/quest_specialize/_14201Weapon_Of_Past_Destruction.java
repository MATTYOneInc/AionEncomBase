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
public class _14201Weapon_Of_Past_Destruction extends QuestHandler {
	
    private final static int questId = 14201;
    public _14201Weapon_Of_Past_Destruction() {
        super(questId);
    }    
	
    @Override
    public void register() {
        qe.registerQuestNpc(798155).addOnQuestStart(questId); //Atropos.
        qe.registerQuestNpc(798155).addOnTalkEvent(questId); //Atropos.
		qe.registerQuestNpc(800407).addOnTalkEvent(questId); //Hongras.
		qe.registerQuestNpc(798212).addOnTalkEvent(questId); //Serimnir.
    }
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798155) { //Atropos.
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 800407) { //Hongras.
				switch (dialog) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					} case STEP_TO_1: {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 798212) { //Serimnir.
				switch (dialog) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1693);
						}
					}
					case STEP_TO_2: {
						qs.setQuestVar(2);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 798155) { //Atropos.
				switch (dialog) {
					case START_DIALOG: {
						if (var == 2) {
							return sendQuestDialog(env, 2375);
						}
					} case CHECK_COLLECTED_ITEMS_SIMPLE: {
						if (QuestService.collectItemCheck(env, true)) {
							changeQuestStep(env, 2, 2, true);
							return sendQuestDialog(env, 5);
                        } else {
							return closeDialogWindow(env);
						}
					}
				}
			}
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798155) { //Atropos.
                return sendQuestEndDialog(env);
			}
		}
        return false;
    }
}