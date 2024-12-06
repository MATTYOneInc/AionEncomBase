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
package quest.norsvold;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _25680Scared_Cellatu extends QuestHandler {

	private final static int questId = 25680;
	private final static int[] npcs = {806105, 806194, 806692, 806695};
	private final static int[] F6QTauricAn = {247074};
	public _25680Scared_Cellatu() {
		super(questId);
	}
	
	public void register() {
		for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: F6QTauricAn) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerQuestItem(182216206, questId);
		qe.registerQuestNpc(806105).addOnQuestStart(questId);
		qe.registerOnEnterZone(ZoneName.get("LF6_ITEMUSEAREA_Q25680"), questId);
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
			if (targetId == 806105) { 
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
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (targetId == 806194) {
				switch (env.getDialog()) {
				    case START_DIALOG: {
						if (var == 0) {
						    return sendQuestDialog(env, 1011);
						} if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_3058: {
						if (var == 6) {
							return sendQuestDialog(env, 3058);
						}
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					} case SET_REWARD: {
						qs.setQuestVar(7);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806692) {
                switch (env.getDialog()) {
				    case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						} if (var == 3) {
							return sendQuestDialog(env, 2034);
						} if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case SELECT_ACTION_2035: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case SELECT_ACTION_2717: {
						if (var == 5) {
							return sendQuestDialog(env, 2717);
						}
					} case STEP_TO_2: {
						changeQuestStep(env, 1, 2, false);
						QuestService.addNewSpawn(210100000, 1, 247074, player.getX(), player.getY(), player.getZ(), (byte) 0);
						return closeDialogWindow(env);
					} case STEP_TO_4: {
						return defaultCloseDialog(env, 3, 4, false, false, 182216206, 1, 0, 0);
					} case STEP_TO_6: {
						changeQuestStep(env, 5, 6, false);
						npc.getController().onDelete();
						QuestService.addNewSpawn(210100000, 1, 806695, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						QuestService.addNewSpawn(210100000, 1, 282465, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						return closeDialogWindow(env);
					}
				}
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806105) {
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
			if (!player.isInsideZone(ZoneName.get("LF6_ITEMUSEAREA_Q25680"))) {
				return HandlerResult.UNKNOWN;
			}
            int var = qs.getQuestVarById(0);
            if (var == 4) {
				giveQuestItem(env, 182216206, 1);
				giveQuestItem(env, 182216207, 1);
                return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 5, false));
            }
        }
        return HandlerResult.FAILED;
    }
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				if (env.getTargetId() == 247074) { //F6_Q_Tauric_An.
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}
}