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

public class _25590Collating_The_Reports extends QuestHandler
{
	private final static int questId = 25590;
	
	public _25590Collating_The_Reports() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(806114).addOnQuestStart(questId); //Reinhard.
		qe.registerQuestNpc(806114).addOnTalkEvent(questId); //Reinhard.
		qe.registerQuestNpc(806228).addOnTalkEvent(questId); //Bastok.
		qe.registerQuestNpc(806229).addOnTalkEvent(questId); //Duisys.
		qe.registerQuestNpc(806230).addOnTalkEvent(questId); //Sieden.
		qe.registerQuestNpc(806231).addOnTalkEvent(questId); //Norte.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806114) { //Ilisia.
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
				case 806228: { //Bastok.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1011);
						} case STEP_TO_1: {
							giveQuestItem(env, 182215982, 1);
							return defaultCloseDialog(env, 0, 1);
						}
					}
				} case 806229: { //Duisys.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case STEP_TO_2: {
							giveQuestItem(env, 182215983, 1);
							return defaultCloseDialog(env, 1, 2);
						}
					}
				} case 806230: { //Sieden.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						} case STEP_TO_3: {
							giveQuestItem(env, 182215984, 1);
							return defaultCloseDialog(env, 2, 3);
						}
					}
				} case 806231: { //Norte.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2034);
						} case SET_REWARD: {
							giveQuestItem(env, 182215985, 1);
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 4, 4, true, false);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806116) { //Reinhard.
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}