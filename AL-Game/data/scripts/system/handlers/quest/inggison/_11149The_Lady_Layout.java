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
package quest.inggison;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _11149The_Lady_Layout extends QuestHandler
{
	private final static int questId = 11149;

	public _11149The_Lady_Layout() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(296491).addOnQuestStart(questId);
		qe.registerQuestNpc(296491).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("LF4_SENSORY_AREA_Q11149_A_210130000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF4_SENSORY_AREA_Q11149_B_210130000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF4_SENSORY_AREA_Q11149_C_210130000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 296491) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 296491) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null)
			return false;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
  		    int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			if (zoneName == ZoneName.get("LF4_SENSORY_AREA_Q11149_A_210130000")) {
				if (var == 0) {
					qs.setQuestVarById(1, 1);
					updateQuestStatus(env);
					cheakReward(env);
					return true;
				}
			} else if (zoneName == ZoneName.get("LF4_SENSORY_AREA_Q11149_B_210130000")) {
				if (var1 == 0) {
					qs.setQuestVarById(2, 1);
					updateQuestStatus(env);
					cheakReward(env);
					return true;
				}
			} else if (zoneName == ZoneName.get("LF4_SENSORY_AREA_Q11149_C_210130000")) {
				if (var2 == 0) {
					qs.setQuestVarById(3, 1);
					updateQuestStatus(env);
					cheakReward(env);
					return true;
				}
			}
		}
		return false;
	}
	
	private void cheakReward(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(1);
		int var1 = qs.getQuestVarById(2);
		int var2 = qs.getQuestVarById(3);
		if (var == 1 && var1 == 1 && var2 == 1) {
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}
	}
}