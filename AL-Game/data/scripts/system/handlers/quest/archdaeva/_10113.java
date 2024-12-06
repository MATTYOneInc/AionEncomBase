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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author (Encom)
/****/

public class _10113 extends QuestHandler
{
    public static final int questId = 10113;
	private final static int[] npcs = {805351, 731844, 731845};
	
    public _10113() {
        super(questId);
    }
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(248013).addOnKillEvent(questId); //IDAbRe_Core_03_Witch_Boss_Ae.
		qe.registerQuestNpc(248452).addOnKillEvent(questId); //IDAbRe_Core_03_Resurrect_Drakan_Atk_04.
		qe.registerQuestNpc(248950).addOnKillEvent(questId); //IDAbRe_Core_03_Real_Witch_1_3_An.
		qe.registerOnEnterZone(ZoneName.get("IDABRE_CORE_03_Q10113_A_301720000"), questId);
    }
	
	@Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10112, true);
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
            if (targetId == 805351) { //Giscours.
                switch (env.getDialog()) {
					case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1013: {
						if (var == 0) {
							return sendQuestDialog(env, 1013);
						}
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 731844) { //IDAbRe_Core_03_FOBJ_Empty_Altar_Q10113A.
                switch (env.getDialog()) {
				    case START_DIALOG: {
						if (var == 4) {
							return sendQuestDialog(env, 2378);
						}
					} case SELECT_ACTION_2379: {
						if (var == 4) {
							return sendQuestDialog(env, 2379);
						}
					} case STEP_TO_5: {
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
				}
            } if (targetId == 731845) { //IDAbRe_Core_03_FOBJ_Ere_Mirror_Q10113A.
                switch (env.getDialog()) {
				    case START_DIALOG: {
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					} case SELECT_ACTION_3058: {
						if (var == 6) {
							return sendQuestDialog(env, 3058);
						}
					} case SET_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						changeQuestStep(env, 6, 7, false);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805351) { //Giscours.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 301720000) { //Mirash Sanctuary.
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 1) {
                    changeQuestStep(env, 1, 2, false);
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
			if (zoneName == ZoneName.get("IDABRE_CORE_03_Q10113_A_301720000")) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		final Npc npc = (Npc) env.getVisibleObject();
		switch (env.getTargetId()) {
			case 248452: //IDAbRe_Core_03_Resurrect_Drakan_Atk_04.
			case 248950: //IDAbRe_Core_03_Real_Witch_1_3_An.
			    if (var == 3)  {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			break;
			case 248013: //IDAbRe_Core_03_Witch_Boss_Ae.
			    if (var == 5)  {
					qs.setQuestVar(6);
					updateQuestStatus(env);
					return true;
				}
			break;
		}
		return false;
	}
}