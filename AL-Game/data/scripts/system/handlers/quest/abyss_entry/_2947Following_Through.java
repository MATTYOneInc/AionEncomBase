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

public class _2947Following_Through extends QuestHandler
{
    private int choice = 0;
	private final static int questId = 2947;
	private final static int[] npcIds = {204053, 204301, 204089, 700268};
	private final static int[] mobIds = {212396, 212611, 212408, 213583, 290048, 211987, 290047, 290050, 211986, 290049, 213584, 211982};
	
	public _2947Following_Through() {
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2946);
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
		qe.registerOnMovieEndQuest(168, questId);
		qe.registerOnMovieEndQuest(167, questId);
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
				case 204053: { //Kvasir.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							} else if (var == 4) {
								return sendQuestDialog(env, 1019);
							}
						} case STEP_TO_12: {
							choice = 1;
							if (var == 0) {
								return defaultCloseDialog(env, 0, 4);
							} else if (var == 4) {
								return defaultCloseDialog(env, 4, 4);
							}
						} case FINISH_DIALOG: {
							if (var == 0) {
								return defaultCloseDialog(env, 0, 0);
							}
						}
					}
					break;
				} case 204089: { //Garm.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 4) {
								return sendQuestDialog(env, 1693);
							} else if (qs.getQuestVarById(4) == 10) {
								return sendQuestDialog(env, 2034);
							}
						} case STEP_TO_3: {
							WorldMapInstance trinielUndergroundArena = InstanceService.getNextAvailableInstance(320090000); //Triniel Underground Arena.
							InstanceService.registerPlayerWithInstance(trinielUndergroundArena, player);
							TeleportService2.teleportTo(player, 320090000, trinielUndergroundArena.getInstanceId(), 276, 294, 163, (byte) 90);
							changeQuestStep(env, 4, 5, false);
							return closeDialogWindow(env);
						} case STEP_TO_4: {
							qs.setStatus(QuestStatus.REWARD);
							qs.setQuestVar(9);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 9, 9, true, false);
						}
					}
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			switch (targetId) {
				case 204301: { //Aegir.
					return sendQuestEndDialog(env, choice);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 5) {
				int var4 = qs.getQuestVarById(4);
				int[] mobIds = {212396, 212611, 212408, 213583, 290048, 211987, 290047, 290050, 211986, 290049, 213584, 211982};
				if (var4 < 9) {
					return defaultOnKillEvent(env, mobIds, 0, 9, 4);
				} else if (var4 == 9) {
					defaultOnKillEvent(env, mobIds, 9, 10, 4);
					QuestService.questTimerEnd(env);
					for (Npc npcInside: player.getPosition().getWorldMapInstance().getNpcs()) {
                        NpcActions.delete(npcInside);
                    }
					playQuestMovie(env, 168);
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
			if (var4 != 10) {
				qs.setQuestVar(4);
				updateQuestStatus(env);
				TeleportService2.teleportTo(player, 120010000, 1006.1f, 1526, 222.2f, (byte) 90);
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
				if (player.getWorldId() != 320090000) {
					QuestService.questTimerEnd(env);
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				} else {
					playQuestMovie(env, 167);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (movieId == 168) {
				TeleportService2.teleportTo(player, 120010000, 1006.1f, 1526.0f, 222.2f, (byte) 90);
				return true;
			} else if (movieId == 167) {
				QuestService.questTimerStart(env, 240);
				return true;
			}
		}
		return false;
	}
}