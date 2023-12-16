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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _18602Nightmare_In_Shining_Armor extends QuestHandler {

	private final static int questId = 18602;
	private final static int[] kaliga = {217005, 217006};
	private final static int[] npc_ids = {205229, 700939};
	
	public _18602Nightmare_In_Shining_Armor() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnDie(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnMovieEndQuest(454, questId);
		for (int npc_id: npc_ids) {
		    qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		    qe.registerQuestNpc(205229).addOnQuestStart(questId);
		} for (int mob: kaliga) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() != 300230000) {
				int var = qs.getQuestVarById(0);
				if (var > 0) {
					changeQuestStep(env, var, 0, false);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var > 0) {
				changeQuestStep(env, var, 0, false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch (targetId) {
			case 217005: //Shadow Judge Kaliga.
				return defaultOnKillEvent(env, targetId, 3, true);
			case 217006: //Kaliga The Unjust.
				return defaultOnKillEvent(env, targetId, 3, true);
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 454)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		changeQuestStep(env, 1, 2, false);
		return true;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 205229) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 205229) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else if (env.getDialog() == QuestDialog.STEP_TO_1) {
					WorldMapInstance kromedeTrial = InstanceService.getNextAvailableInstance(300230000);
					InstanceService.registerPlayerWithInstance(kromedeTrial, player);
					TeleportService2.teleportTo(player, 300230000, kromedeTrial.getInstanceId(), 244.98566f, 244.14162f, 189.52058f, (byte) 30);
					changeQuestStep(env, 0, 1, false);
					return closeDialogWindow(env);
				}
			} else if (targetId == 700939) {
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				} else if (env.getDialog() == QuestDialog.STEP_TO_3) {
					//Oh, Robstin.... I'll avenge you with blood!
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1111307, player.getObjectId(), 2));
					return defaultCloseDialog(env, 2, 3);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205229) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}