/*
 * This file is part of Encom.
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.abyss_entry;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

public class _1922Deliveron_Your_Promises extends QuestHandler {

	private int choice = 0;
	private final static int questId = 1922;
	private final static int[] npcIds = {203830, 203901, 203764};
    private final static int[] mobIds = {213580, 213581, 213582};
	public _1922Deliveron_Your_Promises() {
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1921);
	}
	
	@Override
	public void register() {
		for (int npc: npcIds) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: mobIds) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnQuestTimerEnd(questId);
		qe.registerOnMovieEndQuest(165, questId);
        qe.registerOnMovieEndQuest(166, questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203830: { //Fuchsia.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else if (var == 4) {
								return sendQuestSelectionDialog(env);
							}
						} case STEP_TO_12: {
							choice = 1;
							return defaultCloseDialog(env, 0, 4);
						}
					}
					break;
				} case 203901: { //Telemachus.
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (var == 7) {
								return sendQuestDialog(env, 3739);
							}
						} case SELECT_REWARD: {
							if (var == 7) {
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 6);
							}
						}
					}
					break;
				} case 203764: { //Epeios.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 1693);
							} else if (qs.getQuestVarById(4) == 10) {
								return sendQuestDialog(env, 2034);
							}
						} case STEP_TO_3: {
							WorldMapInstance sanctumUndergroundArena = InstanceService.getNextAvailableInstance(310080000); //Sanctum Underground Arena.
						    InstanceService.registerPlayerWithInstance(sanctumUndergroundArena, player);
						    TeleportService2.teleportTo(player, 310080000, sanctumUndergroundArena.getInstanceId(), 275.6234f, 291.29333f, 162.53488f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 4, 5, false);
							return closeDialogWindow(env);
						} case STEP_TO_4: {
							changeQuestStep(env, 5, 7, false);
                            return closeDialogWindow(env);
						}
					}
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203901) { //Telemachus.
				return sendQuestEndDialog(env, choice);
			}
		}
		return false;
	}
	
	@Override
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        if (movieId == 165) {
            QuestService.questTimerStart(env, 240);
            return true;
        } else if (movieId == 166) {
            Player player = env.getPlayer();
            TeleportService2.teleportTo(player, 110010000, (float) 1474, (float) 1352, (float) 564, (byte) 21);
            return true;
        }
        return false;
    }
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (qs.getQuestVarById(0) == 5) {
                if (qs.getQuestVarById(4) < 9) {
                    return defaultOnKillEvent(env, mobIds, 0, 9, 4);
                } else if (qs.getQuestVarById(4) == 9) {
                    defaultOnKillEvent(env, mobIds, 9, 10, 4);
                    QuestService.questTimerEnd(env);
                    for (Npc npcInside: player.getPosition().getWorldMapInstance().getNpcs()) {
                        NpcActions.delete(npcInside);
                    }
                    playQuestMovie(env, 166);
                    return true;
                }
            }
        }
        return false;
    }
	
	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var4 = qs.getQuestVarById(4);
			if (var4 < 10) {
				qs.setQuestVar(4);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var4 = qs.getQuestVars().getVarById(4);
			if (var == 5 && var4 != 10) {
				if (player.getWorldId() != 310080000) { //Sanctum Underground Arena.
					QuestService.questTimerEnd(env);
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				} else {
					playQuestMovie(env, 165);
					return true;
				}
			}
		}
		return false;
	}
}