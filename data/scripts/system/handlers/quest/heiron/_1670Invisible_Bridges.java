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
package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _1670Invisible_Bridges extends QuestHandler {

	private final static int questId = 1670;
	public _1670Invisible_Bridges() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestItem(182201770, questId);
		qe.registerQuestNpc(203837).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORY_AREA_Q1670_A_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORY_AREA_Q1670_B_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORY_AREA_Q1670_C_210040000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0) { 
				if (dialog == QuestDialog.ACCEPT_QUEST) {
				    return sendQuestStartDialog(env);
				}
				if (dialog == QuestDialog.REFUSE_QUEST) {
					return closeDialogWindow(env);
				}
			}
		} 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203837) {
				if (dialog == QuestDialog.USE_OBJECT) {
				    removeQuestItem(env, 182201770, 1);
					return sendQuestDialog(env, 1352);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
  		    int var = qs.getQuestVarById(0);
				if (zoneName == ZoneName.get("LF3_SENSORY_AREA_Q1670_A_210040000")) {
					if (var == 0) {
						changeQuestStep(env, 0, 16, false);
						return true;
					}
				} else if (zoneName == ZoneName.get("LF3_SENSORY_AREA_Q1670_B_210040000")) {
					if (var == 16) {
						changeQuestStep(env, 16, 48, false);
						return true;
					}
				} else if (zoneName == ZoneName.get("LF3_SENSORY_AREA_Q1670_C_210040000")) {
					if (var == 48) {
						changeQuestStep(env, 48, 48, true);
						return true;
					}
				}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}