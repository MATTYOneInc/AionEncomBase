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
package quest.inggison;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _10031A_Risk_For_The_Obelisk extends QuestHandler {

    private final static int questId = 10031;
	private final static int[] armoredSpaller = {215508, 215509};
	private final static int[] hikironFarmBalaur = {215504, 215505, 215516, 215517, 215518, 216463, 216464, 216647, 216783, 215519, 216648, 216691, 216692};
	public _10031A_Risk_For_The_Obelisk() {
        super(questId);
    }
	
    @Override
    public void register() {
		int[] npcs = {203700, 798600, 798408, 798926, 799052, 798927, 730224, 702662};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: armoredSpaller) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: hikironFarmBalaur) {
		    qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
        qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215590, questId);
    }
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env);
    }
	
    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (player.getWorldId() == 110010000) {
            if (qs == null) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
                    return true;
                }
            }
        } else if (player.getWorldId() == 210130000) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVars().getQuestVars();
                if (var == 3) {
					qs.setQuestVar(++var);
					updateQuestStatus(env);
                    return playQuestMovie(env, 501);
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
            if (targetId == 203700) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case STEP_TO_1: {
                        return defaultCloseDialog(env, 0, 1);
					}
                }
            } else if (targetId == 798600) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        }
					} case STEP_TO_2: {
						giveQuestItem(env, 182215615, 1);
						return defaultCloseDialog(env, 1, 2);
					}
                }
            } else if (targetId == 798408) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        }
					} case STEP_TO_3: {
                        qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 210130000, 1320.3668f, 253.15457f, 591.18146f, (byte) 77);
						return true;
					}
                }
            } else if (targetId == 798926) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 4) {
                            return sendQuestDialog(env, 2375);
                        }
					} case STEP_TO_5: {
						return defaultCloseDialog(env, 4, 5);
					}
                }
            } else if (targetId == 799052) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 5) {
                            return sendQuestDialog(env, 2716);
                        }
					} case STEP_TO_6: {
                        return defaultCloseDialog(env, 5, 6);
					}
                }
            } else if (targetId == 798927) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 6) {
                            return sendQuestDialog(env, 3057);
                        }
					} case STEP_TO_7: {
						giveQuestItem(env, 182215616, 1);
						giveQuestItem(env, 182215617, 1);
						playQuestMovie(env, 516);
                        return defaultCloseDialog(env, 6, 7);
					}
                }
            } else if (targetId == 730224) {
                switch (env.getDialog()) {
				    case USE_OBJECT: {
                        if (var == 7) {
                            return sendQuestDialog(env, 3398);
                        }
					} case STEP_TO_8: {
						if (var == 7) {
						    removeQuestItem(env, 182215616, 1);
							changeQuestStep(env, 7, 8, false);
							return closeDialogWindow(env);
						}
					}
                }
            } else if (targetId == 702662) {
                switch (env.getDialog()) {
				    case USE_OBJECT: {
                        if (var == 8) {
                            removeQuestItem(env, 182215617, 1);
							changeQuestStep(env, 8, 9, false);
							return closeDialogWindow(env);
                        }
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798927) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
                    int[] inggisonMission = {10031, 10032, 10033, 10034, 10035};
                    for (int quest: inggisonMission) {
                        QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), quest, env.getDialogId()));
                    }
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
        int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
            if (var == 9) {
				if (var1 + var2 < 11) {
						if (var2 < 2) {
							return defaultOnKillEvent(env, armoredSpaller, var2, var2 + 1, 2);
						}
					 else {
						if (var1 < 10) {
							return defaultOnKillEvent(env, hikironFarmBalaur, var1, var1 + 1, 1);
						}
					}
				} else {
					qs.setQuestVar(10);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
        return false;
    }
}