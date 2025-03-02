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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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
public class _15680Shuket_Of_The_Iron_Star_Pirates extends QuestHandler {

	private final static int questId = 15680;
	private final static int[] npcs = {806093, 806691, 806693, 806694};
	public _15680Shuket_Of_The_Iron_Star_Pirates() {
		super(questId);
	}
	
	public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerQuestItem(182216204, questId);
		qe.registerQuestNpc(806093).addOnQuestStart(questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_ITEMUSEAREA_Q15680"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final Npc npc = (Npc) env.getVisibleObject();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } 
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806093) { 
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
				}
			}
		}
        if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 806691) {
				switch (env.getDialog()) {
				    case START_DIALOG: {
						if (qs.getQuestVarById(0) == 0) {
						    return sendQuestDialog(env, 1011);
						} else if (qs.getQuestVarById(0) == 1) {
							return sendQuestDialog(env, 1352);
						} else if (qs.getQuestVarById(0) == 2) {
							return sendQuestDialog(env, 1693);
						} else if (qs.getQuestVarById(0) == 4) {
							return sendQuestDialog(env, 2375);
						} else if (qs.getQuestVarById(0) == 7) {
							return sendQuestDialog(env, 3398);
						}
					} case SELECT_ACTION_1012: {
						if (qs.getQuestVarById(0) == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1353: {
						if (qs.getQuestVarById(0) == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case SELECT_ACTION_1694: {
						if (qs.getQuestVarById(0) == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case SELECT_ACTION_2376: {
						if (qs.getQuestVarById(0) == 4) {
							return sendQuestDialog(env, 2376);
						}
					} case SELECT_ACTION_3399: {
						if (qs.getQuestVarById(0) == 7) {
							return sendQuestDialog(env, 3399);
						}
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					} case STEP_TO_3: {
						removeQuestItem(env, 182216203, 1);
						return defaultCloseDialog(env, 2, 3, false, false, 182216204, 1, 0, 0);
					} case STEP_TO_5: {
						removeQuestItem(env, 182216205, 1);
						changeQuestStep(env, 4, 5, false);
						QuestService.addNewSpawn(220110000, 1, 806693, player.getX(), player.getY(), player.getZ(), (byte) 0);
						return closeDialogWindow(env);
					} case SET_REWARD: {
						changeQuestStep(env, 7, 8, true);
						return closeDialogWindow(env);
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 1, 2, false, 10000, 10001);
					}
				}
			} if (targetId == 806693) { //DF6_FOBJ_Flowerpot_Q15680_A
                switch (env.getDialog()) {
				    case USE_OBJECT: {
						if (qs.getQuestVarById(0) == 5) {
							return sendQuestDialog(env, 2716);
						}
					} case SELECT_ACTION_2717: {
						if (qs.getQuestVarById(0) == 5) {
							return sendQuestDialog(env, 2717);
						}
					} case STEP_TO_6: {
						QuestService.addNewSpawn(220110000, 1, 806694, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						QuestService.addNewSpawn(220110000, 1, 282465, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						changeQuestStep(env, 5, 6, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
            } if (targetId == 806694) { //DF6_FOBJ_Flowerpot_Q15680_B
                switch (env.getDialog()) {
				    case USE_OBJECT: {
						if (qs.getQuestVarById(0) == 6) {
							return sendQuestDialog(env, 3057);
						}
					} case SELECT_ACTION_3058: {
						if (qs.getQuestVarById(0) == 6) {
							return sendQuestDialog(env, 3058);
						}
					} case STEP_TO_7: {
						changeQuestStep(env, 6, 7, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806093) {
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
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (!player.isInsideZone(ZoneName.get("DF6_ITEMUSEAREA_Q15680"))) {
				return HandlerResult.UNKNOWN;
			}
            int var = qs.getQuestVarById(0);
            if (var == 3) {
				giveQuestItem(env, 182216204, 1);
				giveQuestItem(env, 182216205, 1);
                return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
            }
        }
        return HandlerResult.FAILED;
    }
}