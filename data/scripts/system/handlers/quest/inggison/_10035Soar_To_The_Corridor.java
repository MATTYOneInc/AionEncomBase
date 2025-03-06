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
public class _10035Soar_To_The_Corridor extends QuestHandler {

    private final static int questId = 10035;
    private final static int[] basrasaTrapper = {216775, 220021, 220022};
    public _10035Soar_To_The_Corridor() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {798928, 799025, 798958, 798996, 702663, 798926};
        for (int mob: basrasaTrapper) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        } for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
        qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("ANGRIEF_GATE_210130000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] inggisonQuests = {10031, 10032, 10033, 10034};
		return defaultOnZoneMissionEndEvent(env, inggisonQuests);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] inggisonQuests = {10031, 10032, 10033, 10034};
		return defaultOnLvlUpEvent(env, inggisonQuests, true);
	}
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 798928: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 7) {
                                return sendQuestDialog(env, 3399);
                            }
                        } case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1);
                        } case SET_REWARD: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return defaultCloseDialog(env, 7, 8);
                        }
                    }
                    break;
                } case 799025: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            return defaultCloseDialog(env, 1, 2);
                        }
                    }
                    break;
                } case 798958: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
                            return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                } case 798996: {
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_4: {
							giveQuestItem(env, 182215629, 1);
							playQuestMovie(env, 517);
                            return defaultCloseDialog(env, 3, 4);
                        }
                    }
                    break;
                } case 702663: {
                    switch (dialog) {
                        case USE_OBJECT: {
                            if (var == 6) {
                                removeQuestItem(env, 182215629, 1);
								Npc npc = (Npc) env.getVisibleObject();
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return closeDialogWindow(env);
                            }
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798926) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
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
            if (var == 5) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 9) {
                    return defaultOnKillEvent(env, basrasaTrapper, var1, var1 + 1, 1);
                } else if (var1 == 9) {
                    qs.setQuestVar(6);
                    updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (zoneName.equals(ZoneName.get("ANGRIEF_GATE_210130000"))) {
                if (var == 4) {
                    changeQuestStep(env, 4, 5, false);
                    return true;
                }
            }
        }
        return false;
    }
}