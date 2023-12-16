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
package quest.kromedes_trial;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _18600Scoring_Some_Bad_Stigma extends QuestHandler {
	private final static int questId = 18600;
	
	public _18600Scoring_Some_Bad_Stigma() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {204500, 804601, 205228};
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
			qe.registerQuestNpc(204500).addOnQuestStart(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204500) { //Perento.
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else if (env.getDialogId() == 1002) {
					giveQuestItem(env, 182213000, 1);
					return sendQuestStartDialog(env);
				} else {
					return sendQuestStartDialog(env);
				}
			} if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				} else if (env.getDialogId() == 1009) {
					removeQuestItem(env, 182213001, 1);
					return sendQuestEndDialog(env);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		} if (targetId == 804601) { //Kurochinerk.
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				} if (env.getDialog() == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1, 0, 0, 182213000, 1);
				}
			}
		} if (targetId == 205228) { //Herthia.
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1693);
				} else if (env.getDialog() == QuestDialog.STEP_TO_2) {
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
		}
		return false;
	}
}