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

/**
* Author Ghostfur & Unknown (Aion-Unique)
* Rework: MATTY (ADev.Team)
*/

public class _24016A_Strange_New_Threat extends QuestHandler {

    private final static int questId = 24016;
    private final static int[] npcs = {203557, 700140, 700184};
    public _24016A_Strange_New_Threat() {
        super(questId);
    }
	
    @Override
	public void register() {
		qe.registerOnDie(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnMovieEndQuest(154, questId);
		qe.registerQuestNpc(233876).addOnKillEvent(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("BREGIRUN_320030000"), questId);
	}
	
	@Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        int[] altgardQuests = {24010, 24011, 24012, 24013, 24014, 24015};
        return defaultOnZoneMissionEndEvent(env, altgardQuests);
    }
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] altgardQuests = {24010, 24011, 24012, 24013, 24014, 24015};
		return defaultOnLvlUpEvent(env, altgardQuests, true);
	}
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203557: { // Suthran
                    if (env.getDialog() == QuestDialog.START_DIALOG && var == 0) {
                        return sendQuestDialog(env, 1011);
                    } else if (env.getDialog() == QuestDialog.STEP_TO_1) {
                        TeleportService2.teleportTo(player, 220030000, 2453.1934f, 2555.148f, 316.267f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                        changeQuestStep(env, 0, 1, false); // 1
                        return closeDialogWindow(env);
                    } else if (env.getDialogId() == 1013) {
                        playQuestMovie(env, 66);
                        return sendQuestDialog(env, 1013);
                    }
                }
                case 700140: { // Gate Guardian Stone
                    if (var == 2) {
                        if (env.getDialog() == QuestDialog.USE_OBJECT) {
                            QuestService.addNewSpawn(320030000, player.getInstanceId(), 233876, (float) 260.12, (float) 234.93, (float) 216.00, (byte) 90);
                            return useQuestObject(env, 2, 3, false, false); // 3
                        }
                    } else if (var == 4) {
                        if (env.getDialog() == QuestDialog.USE_OBJECT) {
                            return playQuestMovie(env, 154);
                        }
                    }
                }
                case 700184: { // Abbys Gate
					playQuestMovie(env, 154);
					return useQuestObject(env, 4, 4, true, false);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203557) { // Suthran
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 1352);
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
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("BREGIRUN_320030000")) {
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
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 233876, 3, 4);
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		if (movieId == 154) {
			TeleportService2.teleportTo(player, 220030000, 1683.0532f, 1758.4905f, 259.49335f, (byte) 68, TeleportAnimation.BEAM_ANIMATION);
			return true;
		}
		return false;
	}
}