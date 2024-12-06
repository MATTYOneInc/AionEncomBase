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

public class _4122Domination_Of_Spite extends QuestHandler
{
	private final static int questId = 4122;
	
	public _4122Domination_Of_Spite() {
		super(questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"));
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(798155).addOnTalkEvent(questId);
		qe.registerQuestNpc(237245).addOnKillEvent(questId); //Suspicious Pot.
		qe.registerQuestNpc(237240).addOnKillEvent(questId); //Enthralled Gutorum.
		qe.registerQuestNpc(237241).addOnKillEvent(questId); //Enthralled Karemiwen.
		qe.registerQuestNpc(237243).addOnKillEvent(questId); //Enthralled Zeeturun.
		qe.registerQuestNpc(237244).addOnKillEvent(questId); //Enthralled Lannok.
		qe.registerQuestNpc(237239).addOnKillEvent(questId); //Death Reaper.
		qe.registerOnEnterZone(ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"), questId);
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
				case 205225: {
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
		    if (targetId == 205225) {
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
			if (var == 0) { //Suspicious Pot.
				return defaultOnKillEvent(env, 237245, 0, 1);
			} else if(var == 1) { //Enthralled Gutorum.
				return defaultOnKillEvent(env, 237240, 1, 2);
			} else if(var == 2) { //Enthralled Karemiwen.
				return defaultOnKillEvent(env, 237241, 2, 3);
			} else if(var == 3) { //Enthralled Zeeturun.
				return defaultOnKillEvent(env, 237243, 3, 4);
			} else if(var == 4) { //Enthralled Lannok.
				return defaultOnKillEvent(env, 237244, 4, 5);
			} else if(var == 5) { //Death Reaper.
				return defaultOnKillEvent(env, 237239, 5, true);
			}
		}
		return false;
	}
}