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
package quest.gelkmaros;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _20035Silentera_Support extends QuestHandler
{
    private final static int questId = 20035;
    private final static int[] mobs = {216107, 216450, 216104, 216449, 216112, 216451, 216109, 216108, 216101, 216448};
	
    public _20035Silentera_Support() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {799226, 799329, 799323, 799283, 799309, 799225};
		for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        } for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215659, questId);
		qe.registerQuestItem(182215660, questId);
        qe.registerOnEnterZoneMissionEnd(questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] gelkmarosQuests = {20031, 20032, 20033, 20034};
		return defaultOnZoneMissionEndEvent(env, gelkmarosQuests);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] gelkmarosQuests = {20031, 20032, 20033, 20034};
		return defaultOnLvlUpEvent(env, gelkmarosQuests, true);
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
                case 799226: {
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
							removeQuestItem(env, 182215660, 1);
							qs.setStatus(QuestStatus.REWARD);
                            return defaultCloseDialog(env, 7, 8);
                        }
                    }
                    break;
                } case 799329: { 
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
                } case 799323: { 
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            }
                        } case STEP_TO_3: {
							giveQuestItem(env, 182215596, 1);
							giveQuestItem(env, 182215597, 1);
                            return defaultCloseDialog(env, 2, 3);
                        }
                    }
                    break;
                } case 799283: { 
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case STEP_TO_4: {
							removeQuestItem(env, 182215596, 1);
                            return defaultCloseDialog(env, 3, 4);
                        }
                    }
                    break;
                } case 799309: { 
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 4) {
                                return sendQuestDialog(env, 2375);
                            }
                        } case STEP_TO_5: {
							removeQuestItem(env, 182215597, 1);
							giveQuestItem(env, 182215659, 1);
							playQuestMovie(env, 567);
                            return defaultCloseDialog(env, 4, 5);
                        }
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 799225) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
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
                    return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
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
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        Player player = env.getPlayer();
        if (player.isInsideZone(ZoneName.get("DF4_ITEMUSEAREA_Q20035"))) {
			giveQuestItem(env, 182215660, 1);
            return HandlerResult.fromBoolean(useQuestItem(env, item, 6, 7, false));
        }
        return HandlerResult.FAILED;
    }
}