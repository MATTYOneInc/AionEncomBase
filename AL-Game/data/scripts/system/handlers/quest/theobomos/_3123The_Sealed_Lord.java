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
package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
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

public class _3123The_Sealed_Lord extends QuestHandler
{
	private final static int questId = 3123;
	
	public _3123The_Sealed_Lord() {
		super(questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("THEOBOMOS_LAB_INTERIOR_310110000"));
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(237258).addOnTalkEvent(questId);
		qe.registerQuestNpc(237253).addOnKillEvent(questId); //Fiery Sealing Stone.
		qe.registerQuestNpc(237246).addOnKillEvent(questId); //Watcher Queen Arachne.
		qe.registerQuestNpc(237248).addOnKillEvent(questId); //Watcher Silikor Of Memory.
		qe.registerQuestNpc(237249).addOnKillEvent(questId); //Watcher Jilitia.
		qe.registerQuestNpc(237250).addOnKillEvent(questId); //Sealed Unstable Triroan.
		qe.registerQuestNpc(237251).addOnKillEvent(questId); //Corrupted Ifrit.
		qe.registerOnEnterZone(ZoneName.get("THEOBOMOS_LAB_INTERIOR_310110000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 0) {
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 4762);
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 237258: {
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 10002);
						} case SELECT_REWARD: {
							return sendQuestEndDialog(env);
						} default:
							return sendQuestEndDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 237258) {
			    switch (dialog) {
					case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
						return sendQuestEndDialog(env);
				}
		    }
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0) { //Fiery Sealing Stone.
				return defaultOnKillEvent(env, 237253, 0, 1);
			} else if(var == 1) { //Watcher Queen Arachne.
				return defaultOnKillEvent(env, 237246, 1, 2);
			} else if(var == 2) { //Watcher Silikor Of Memory.
				return defaultOnKillEvent(env, 237248, 2, 3);
			} else if(var == 3) { //Watcher Jilitia.
				return defaultOnKillEvent(env, 237249, 3, 4);
			} else if(var == 4) { //Sealed Unstable Triroan.
				return defaultOnKillEvent(env, 237250, 4, 5);
			} else if(var == 5) { //Corrupted Ifrit.
				return defaultOnKillEvent(env, 237251, 5, true);
			}
		}
		return false;
	}
}