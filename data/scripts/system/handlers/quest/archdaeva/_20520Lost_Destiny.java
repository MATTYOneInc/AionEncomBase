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
package quest.archdaeva;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author (Encom)
/** Source KOR: https://www.youtube.com/watch?v=8Qt-ZODwhoA
/****/

public class _20520Lost_Destiny extends QuestHandler
{
	public static final int questId = 20520;
	private final static int[] npcs = {204075, 204191, 806077, 806080};
	
	public _20520Lost_Destiny() {
		super(questId);
	}
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215954, questId); //Orders To Report To Norsvold.
		qe.registerQuestItem(182215974, questId); //Sealed Letter From Munin.
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q20520_220110000"), questId);
		qe.registerOnMovieEndQuest(867, questId);
    }
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player.getWorldId() == 120010000) { //Pandaemonium.
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs == null) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
                    return true;
                }
            }
        }
        return false;
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204075) { //Balder.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case STEP_TO_1: {
						//Sealed Letter From Munin.
						giveQuestItem(env, 182215974, 1);
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806077) { //Edorin.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_3: {
						//Orders To Report To Norsvold.
						giveQuestItem(env, 182215954, 1);
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 204191) { //Doman.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					} case SELECT_ACTION_1694: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_4: {
						TeleportService2.teleportTo(env.getPlayer(), 220110000, 1757.3667f, 2008.911f, 196.59653f, (byte) 0);
						//Orders To Report To Norsvold.
						removeQuestItem(env, 182215954, 1);
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806080) { //Feregran.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					} case SELECT_ACTION_2717: {
						if (var == 5) {
							return sendQuestDialog(env, 2717);
						}
					} case SET_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806080) { //Feregran.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
                    int[] norsvoldMission = {20521, 20522, 20523, 20524, 20525, 20526, 20527, 20528, 20529, 20530};
                    for (int quest: norsvoldMission) {
                        QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), quest, env.getDialogId()));
                    }
                    return sendQuestEndDialog(env);
                }
            }
        }
		return false;
	}
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			int instanceId = player.getInstanceId();
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q20520_220110000")) {
				if (var == 4) {
					playQuestMovie(env, 867);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		if (movieId == 867) {
			changeQuestStep(env, 4, 5, false);
			WorldMapInstance SanctuaryDungeon = InstanceService.getNextAvailableInstance(301580000);
			InstanceService.registerPlayerWithInstance(SanctuaryDungeon, player);
			TeleportService2.teleportTo(player, 301580000, SanctuaryDungeon.getInstanceId(), 431, 491, 99);
			return true;
		}
		return false;
	}
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return HandlerResult.UNKNOWN;
		} if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			//Sealed Letter From Munin.
			if (item.getItemTemplate().getTemplateId() == 182215974) {
				if (var == 1) {
					qs.setQuestVar(2);
					updateQuestStatus(env);
					//Sealed Letter From Munin.
					removeQuestItem(env, 182215974, 1);
					return HandlerResult.SUCCESS;
				}
			}
		}
		return HandlerResult.FAILED;
	}
}