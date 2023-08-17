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

public class _15590Tower_Of_Eternity_Fragment_Report extends QuestHandler
{
	private final static int questId = 15590;
	
	public _15590Tower_Of_Eternity_Fragment_Report() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(806114).addOnQuestStart(questId); //Ilisia.
		qe.registerQuestNpc(806114).addOnTalkEvent(questId); //Ilisia.
		qe.registerQuestNpc(806224).addOnTalkEvent(questId); //Este.
		qe.registerQuestNpc(806225).addOnTalkEvent(questId); //Ovest.
		qe.registerQuestNpc(806226).addOnTalkEvent(questId); //Meridies.
		qe.registerQuestNpc(806227).addOnTalkEvent(questId); //Ceber.
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
				case 806224: { //Este.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1011);
						} case STEP_TO_1: {
							giveQuestItem(env, 182215978, 1);
							return defaultCloseDialog(env, 0, 1);
						}
					}
				} case 806225: { //Ovest.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case STEP_TO_2: {
							giveQuestItem(env, 182215979, 1);
							return defaultCloseDialog(env, 1, 2);
						}
					}
				} case 806226: { //Meridies.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						} case STEP_TO_3: {
							giveQuestItem(env, 182215980, 1);
							return defaultCloseDialog(env, 2, 3);
						}
					}
				} case 806227: { //Ceber.
				    switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2034);
						} case SET_REWARD: {
							giveQuestItem(env, 182215981, 1);
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 4, 4, true, false);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806114) { //Ilisia.
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