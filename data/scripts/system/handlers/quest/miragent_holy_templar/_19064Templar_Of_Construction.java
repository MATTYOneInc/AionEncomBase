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
package quest.miragent_holy_templar;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _19064Templar_Of_Construction extends QuestHandler {
	
	private final static int questId = 19064;
	public _19064Templar_Of_Construction() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203701, 798450, 203752};
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203701) {
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798450:
					switch (dialog) {
						case START_DIALOG:
							return sendQuestDialog(env, 1352);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
				break;
				case 203752:
					switch (dialog) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 2375);
						    }
                        }						
						case CHECK_COLLECTED_ITEMS_SIMPLE: {
					        return checkQuestItemsSimple(env, 1, 2, true, 5, 0, 0);
						}
						case SELECT_NO_REWARD: {
							return sendQuestEndDialog(env);
						}
					}
				break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
					return sendQuestEndDialog(env);
				}
		return false;
	}
}