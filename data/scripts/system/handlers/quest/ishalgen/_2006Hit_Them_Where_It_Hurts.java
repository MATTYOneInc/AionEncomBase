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
package quest.ishalgen;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _2006Hit_Them_Where_It_Hurts extends QuestHandler
{
	private final static int questId = 2006;

	public _2006Hit_Them_Where_It_Hurts() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(203540).addOnTalkEvent(questId);
		qe.registerQuestNpc(700095).addOnTalkEvent(questId);
		qe.registerQuestNpc(203516).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2005, false);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		QuestDialog dialog = env.getDialog();
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203540: { //Mijou.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						} case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						} case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 1, true, 1438, 1353);
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				} case 700095: { //Mau Grain Sack.
					if (var == 1) {
						return true;
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203516) { //Ulgorn.
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 1693);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}