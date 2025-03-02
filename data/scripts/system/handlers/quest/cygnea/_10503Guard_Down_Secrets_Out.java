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
package quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _10503Guard_Down_Secrets_Out extends QuestHandler {

    public static final int questId = 10503;
    public _10503Guard_Down_Secrets_Out() {
        super(questId);
    }
	
    @Override
    public void register() {
        int[] npcs = {804704, 804705, 702670};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnMovieEndQuest(991, questId);
		qe.registerQuestItem(182215604, questId);
		qe.registerGetingItem(182215603, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(236252).addOnKillEvent(questId); //Aetheric Field Guard.
		qe.registerOnEnterZone(ZoneName.get("LF5_SENSORYAREA_Q10503_210070000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10502, true);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        final Npc npc = (Npc) env.getVisibleObject();
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804704) {
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
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 804705) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 3) {
                            return sendQuestDialog(env, 1693);
                        } else if (var == 5) {
                            return sendQuestDialog(env, 2716);
                        }
					} case SELECT_ACTION_1694: {
						if (var == 3) {
							return sendQuestDialog(env, 1694);
						}
					} case SELECT_ACTION_2717: {
						if (var == 5) {
							return sendQuestDialog(env, 2717);
						}
					} case STEP_TO_4: {
						giveQuestItem(env, 182215604, 1);
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					} case STEP_TO_6: {
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
                }
            } if (targetId == 702670) { //Scattered Balaur Document.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						if (var == 2) {
							return closeDialogWindow(env);
						}
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804704) {
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
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("LF5_SENSORYAREA_Q10503_210070000")) {
				if (var == 6) {
					playQuestMovie(env, 991);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 991) {
			qs.setQuestVar(7);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 1) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 2) {
                    return defaultOnKillEvent(env, 236252, var1, var1 + 1, 1); //Aetheric Field Guard.
                } else if (var1 == 2) {
                    qs.setQuestVar(2);
					updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public boolean onGetItemEvent(QuestEnv env) {
		return defaultOnGetItemEvent(env, 2, 3, false);
	}
	
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 4) {
                return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 5, false));
            }
        }
        return HandlerResult.FAILED;
    }
}