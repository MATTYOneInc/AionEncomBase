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
package quest.aturam_sky_fortress;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18300Floating_Death extends QuestHandler
{
	private final static int questId = 18300;
	
	public _18300Floating_Death() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(804699).addOnQuestStart(questId);
		qe.registerQuestNpc(804699).addOnTalkEvent(questId);
		qe.registerQuestNpc(804820).addOnTalkEvent(questId);
		qe.registerQuestNpc(799530).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("ATURAM_SKY_FORTRESS_2_300240000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804699) {
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 4762);
					default:
						return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
			if (targetId == 804820) {
				switch (env.getDialog()) {
					case START_DIALOG: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
                    } case SELECT_ACTION_1013:
						return sendQuestDialog(env, 1013);
					case STEP_TO_1:
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
				}
			}
		} else if ((qs.getStatus() == QuestStatus.REWARD)) {
			if (targetId == 799530) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("ATURAM_SKY_FORTRESS_2_300240000")) {
			Player player = env.getPlayer();
			if (player == null) {
				return false;
			}
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 1) {
					changeQuestStep(env, 1, 1, true);
					return true;
				}
			}
		}
		return false;
	}
}