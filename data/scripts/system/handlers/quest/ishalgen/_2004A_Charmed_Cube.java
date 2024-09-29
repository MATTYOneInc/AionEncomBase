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

import com.aionemu.gameserver.model.gameobjects.Npc;
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

public class _2004A_Charmed_Cube extends QuestHandler
{
	private final static int questId = 2004;
	private int[] mobs = {210402, 210403};

	public _2004A_Charmed_Cube() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203539, 700047, 203550};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		} for (int mob: mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2100, false);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203539: { //Derot.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						} case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						} case STEP_TO_2: {
							giveQuestItem(env, 182203005, 1);
							return sendQuestSelectionDialog(env);
						} case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 2, false, 1438, 1353);
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				} case 700047: { //Tombstone.
					if (var == 1 && env.getVisibleObject().getObjectTemplate().getTemplateId() == 700047
						&& dialog == QuestDialog.USE_OBJECT) {
						Npc npc = (Npc) env.getVisibleObject();
						QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 211755, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
						return true;
					}
				} case 203550: { //Munin.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							} else if (var == 6) {
								return sendQuestDialog(env, 2034);
							}
						} case STEP_TO_3: {
							return defaultCloseDialog(env, 2, 3, 0, 0, 182203005, 1);
						} case STEP_TO_4: {
							return defaultCloseDialog(env, 6, 6, true, false);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203539) { //Derot.
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, mobs, 3, 6);
	}
}