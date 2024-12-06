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
package quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _1851Dispatched_Shugo_Craftsmen extends QuestHandler
{
	private final static int questId = 1851;
	
	public _1851Dispatched_Shugo_Craftsmen() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(278533).addOnQuestStart(questId); //Rentia.
		qe.registerQuestNpc(278533).addOnTalkEvent(questId); //Rentia.
		qe.registerQuestNpc(279025).addOnTalkEvent(questId); //Guuminerk.
		qe.registerQuestNpc(279036).addOnTalkEvent(questId); //Muirunerk.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 278533) { //Rentia
            	switch (env.getDialog()) {
            		case START_DIALOG: {
            			return sendQuestDialog(env, 1011);
            		} case ASK_ACCEPTION: {
            			return sendQuestDialog(env, 4);
            		} case ACCEPT_QUEST: {
            			return sendQuestStartDialog(env);
            		} case REFUSE_QUEST: {
            			return sendQuestDialog(env, 1004);
            		}
            	}
            }
		} if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 279025: { //Guuminerk
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case STEP_TO_1: {
							qs.setQuestVar(1);
            				updateQuestStatus(env);
            				return closeDialogWindow(env);
						}
					}
				} case 279036: { //Muirunerk
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						} case STEP_TO_2: {
							qs.setQuestVar(2);
        					qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
        					return closeDialogWindow(env);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 278533) { //Rentia
            	switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					} case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
					    return sendQuestEndDialog(env);
            	}
            }
        }
		return false;
	}
}