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
package quest.redemption_landing;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15400Aiding_General_Giscours extends QuestHandler
{
	public static final int questId = 15400;
	
	public _15400Aiding_General_Giscours() {
		super(questId);
	}
	
    @Override
    public void register() {
        int[] npcs = {805351, 805352, 805353, 805354, 805355, 702830, 702831, 702832};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215897, questId); //Beritra Supply Mark.
		qe.registerQuestItem(182215898, questId); //Repair Device Gear.
		qe.registerQuestItem(182215899, questId); //Ereshkigal Legion Mark.
		qe.registerQuestNpc(885101).addOnKillEvent(questId); //Ereshkigal's Searcher.
		qe.registerOnEnterZone(ZoneName.get("KROTAN_REFUGE_400010000"), questId);
    }
	
    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player.getWorldId() == 400010000) { //Reshanta.
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
            if (targetId == 805351) { //Giscours.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }
					} case STEP_TO_1: {
                        changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805352) { //Kirwan.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        } else if (var == 8) {
                            playQuestMovie(env, 277);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
                        }
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805353) { //Buard.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        }
					} case STEP_TO_3: {
                        changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 702830) { //Beritra Supply Unit Box.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						switch (player.getRace()) {
							case ELYOS:
								giveQuestItem(env, 182215897, 1); //Beritra Supply Mark.
							break;
						}
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 702831) { //Destroyed Gate Reinforcer.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						switch (player.getRace()) {
							case ELYOS:
								giveQuestItem(env, 182215898, 1); //Repair Device Gear.
							break;
						}
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 702832) { //Heavy Bomb Box.
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						switch (player.getRace()) {
							case ELYOS:
								giveQuestItem(env, 182215899, 1); //Ereshkigal Legion Mark.
							break;
						}
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805354) { //Lagund.
                switch (env.getDialog()) {
                    case START_DIALOG: {
						if (var == 3) {
                            return sendQuestDialog(env, 2034);
                        }
					} case STEP_TO_4: {
                        changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							changeQuestStep(env, 3, 4, false);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					}
                }
            } if (targetId == 805355) { //Ferriere.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 6) {
                            return sendQuestDialog(env, 2716);
                        }
					} case STEP_TO_7: {
                        playQuestMovie(env, 276);
						changeQuestStep(env, 7, 8, false);
						return closeDialogWindow(env);
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805351) { //Giscours.
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
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
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("KROTAN_REFUGE_400010000")) {
				if (var == 5) {
					changeQuestStep(env, 5, 6, false);
					return true;
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
            if (var == 6) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 1) {
                    return defaultOnKillEvent(env, 885101, var1, var1 + 1, 1); //Ereshkigal's Searcher.
                } else if (var1 == 1) {
                    qs.setQuestVar(7);
					updateQuestStatus(env);
                    return true;
                }
            }
        }
        return false;
    }
}