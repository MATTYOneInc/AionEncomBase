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
package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _3076Bolstering_The_Aetheric_Field extends QuestHandler {
	
	private final static int questId = 3076;
	public _3076Bolstering_The_Aetheric_Field() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(798155).addOnQuestStart(questId); //Atropos.
		qe.registerQuestNpc(798155).addOnTalkEvent(questId); //Atropos.
		qe.registerQuestNpc(278503).addOnTalkEvent(questId); //Calon.
		qe.registerQuestNpc(278556).addOnTalkEvent(questId); //Cymaon.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs == null || qs.getStatus() == QuestStatus.NONE) {
		    if (targetId == 798155) { //Atropos.
                switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case ASK_ACCEPTION: {
					    return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
		    if (targetId == 278503) { //Calon.
				switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 0) {
						    return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 0) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 278556) { //Cymaon.
				switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 1) {
						    return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						if (var == 1) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 798155) { //Atropos.
				switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 2) {
						    return sendQuestDialog(env, 2375);
						}
					} case SELECT_ACTION_2376: {
						if (var == 2) {
							return sendQuestDialog(env, 2376);
						}
					} case SELECT_REWARD: {
                        changeQuestStep(env, 2, 3, true);
						return sendQuestEndDialog(env);
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 798155) { //Atropos.
			    switch (env.getDialog()) {
					case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
						return sendQuestEndDialog(env);
				}
		    }
		}
		return false;
	}
}