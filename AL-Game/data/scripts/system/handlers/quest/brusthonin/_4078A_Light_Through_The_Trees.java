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
package quest.brusthonin;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4078A_Light_Through_The_Trees extends QuestHandler
{
	private final static int questId = 4078;
	private final static int[] npc_ids = {205157, 700427, 700428, 700429};
	
	public _4078A_Light_Through_The_Trees() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(205157).addOnQuestStart(questId);
		for (int npc_id: npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (targetId == 205157) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205157) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					} case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
						return sendQuestEndDialog(env);
				}
			}
		} else if (qs.getStatus() != QuestStatus.START) {
			return false;
		} if (targetId == 205157) {
			switch (env.getDialog()) {
				case START_DIALOG: {
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				} case CHECK_COLLECTED_ITEMS: {
					if (player.getInventory().getItemCountByItemId(182209049) >= 9) {
						if (!giveQuestItem(env, 182209050, 1)) {
							return true;
						}
						removeQuestItem(env, 182209049, 9);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return sendQuestDialog(env, 10000);
					} else {
						return sendQuestDialog(env, 10001);
					}
				}
			}
		} else if (targetId == 700428) {
			switch (env.getDialog()) {
				case USE_OBJECT: {
					if (var == 1) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 1, 2, false, false);
						}
					}
					return false;
				}
			}
		} else if (targetId == 700427) {
			switch (env.getDialog()) {
				case USE_OBJECT: {
					if (var == 2) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 2, 3, false, false);
						}
					}
					return false;
				}
			}
		} else if (targetId == 700429) {
			switch (env.getDialog()) {
				case USE_OBJECT: {
					if (var == 3) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 3, 4, true, false);
						}
					}
					return false;
				}
			}
		}
		return false;
	}
}