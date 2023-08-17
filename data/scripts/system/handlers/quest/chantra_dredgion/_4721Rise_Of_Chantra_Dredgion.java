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
package quest.chantra_dredgion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4721Rise_Of_Chantra_Dredgion extends QuestHandler
{
	private static final int questId = 4721;

	public _4721Rise_Of_Chantra_Dredgion() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(799226).addOnQuestStart(questId); //Valetta.
		qe.registerQuestNpc(799226).addOnTalkEvent(questId); //Valetta.
		qe.registerQuestNpc(799403).addOnTalkEvent(questId); //Yorgen.
		qe.registerQuestNpc(700948).addOnTalkEvent(questId); //Balaur Weapon.
		qe.registerQuestNpc(216886).addOnKillEvent(questId); //Captain Zanata.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		int var = qs.getQuestVarById(0);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799226) { //Valetta.
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 799403) { //Yorgen.
				switch (dialog) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case STEP_TO_1: {
						return defaultCloseDialog(env, 0, 1);
					}
				}
			} else if (targetId == 700948) { //Balaur Weapon.
				if (dialog == QuestDialog.USE_OBJECT) {
					closeDialogWindow(env);
					return true;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799226) { //Valetta.
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 216886, 2, true); //Captain Zanata.
	}
}