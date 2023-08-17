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
package quest.mission;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14016A_Gate_Agape extends QuestHandler
{
	private final static int questId = 14016;
	
	public _14016A_Gate_Agape() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203098, 700141, 700142};
		qe.registerOnDie(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnMovieEndQuest(153, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(233873).addOnKillEvent(questId);
		for (int npcId: npcs) {
			qe.registerQuestNpc(npcId).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("AERDINA_310030000"), questId);
	}
	
	@Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        int[] verteronQuests = {14010, 14011, 14012, 14013, 14014, 14015};
        return defaultOnZoneMissionEndEvent(env, verteronQuests);
    }
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] verteronQuests = {14010, 14011, 14012, 14013, 14014, 14015};
		return defaultOnLvlUpEvent(env, verteronQuests, true);
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
				case 203098: //Spatalos.
					switch (dialog) {
						case START_DIALOG:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						break;
						case STEP_TO_1:
							TeleportService2.teleportTo(player, 210030000, 2683.2085f, 1068.8977f, 199.375f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
					}
				break;
				case 700141: //Abyss Gate.
					playQuestMovie(env, 153);
					removeQuestItem(env, 182215317, 1);
					return useQuestObject(env, 2, 2, true, false);
				case 700142: //Abyss Gate Guardian Stone.
					if (dialog == QuestDialog.USE_OBJECT) {
						QuestService.addNewSpawn(310030000, player.getInstanceId(), 233873, (float) 251.91177, (float) 262.239, (float) 228.30093, (byte) 89);
					}
					Npc npc = (Npc) env.getVisibleObject();
                    npc.getController().onDelete();
				break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203098) { //Spatalos.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 5);
				} else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
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
			int var = qs.getQuestVars().getQuestVars();
			if (var == 2) {
				changeQuestStep(env, 2, 1, false);
				removeQuestItem(env, 182215317, 1);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("AERDINA_310030000")) {
			final Player player = env.getPlayer();
			if (player == null)
				return false;
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null)
				return false;
			if (qs.getQuestVars().getQuestVars() == 1) {
				changeQuestStep(env, 1, 2, false);
				return true;
			} else if (qs.getQuestVars().getQuestVars() == 2) {
				changeQuestStep(env, 2, 1, false);
				removeQuestItem(env, 182215317, 1);
				return true;
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
			if (var == 2) {
				if (env.getTargetId() == 233873) {
					if (player.getInventory().getItemCountByItemId(182215317) < 1) {
						return giveQuestItem(env, 182215317, 1);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		if (movieId == 153) {
			TeleportService2.teleportTo(player, 210030000, 1702.7107f, 1493.9282f, 121.661995f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
			return true;
		}
		return false;
	}
}