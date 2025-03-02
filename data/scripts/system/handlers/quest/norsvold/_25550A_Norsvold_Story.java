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
package quest.norsvold;

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
public class _25550A_Norsvold_Story extends QuestHandler {

	private final static int questId = 25550;
	public _25550A_Norsvold_Story() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(806116).addOnQuestStart(questId); //Reinhard.
		qe.registerQuestNpc(806116).addOnTalkEvent(questId); //Reinhard.
		qe.registerQuestNpc(806115).addOnTalkEvent(questId); //Svanhild.
		qe.registerQuestNpc(806135).addOnTalkEvent(questId); //Conrto.
		qe.registerQuestNpc(806101).addOnTalkEvent(questId); //Vadorei.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806116) { //Reinhard.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} if (qs == null) {
			return false;
		} if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806115: { //Svanhild.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1011);
						} case STEP_TO_1: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
					        return closeDialogWindow(env);
						}
					}
				} case 806135: { //Conrto.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case STEP_TO_2: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
					        return closeDialogWindow(env);
						}
					}
				} case 806101: { //Vadorei.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						} case SELECT_REWARD: {
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806101) { //Vadorei.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}