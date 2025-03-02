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
package quest.mission;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14045Rumors_On_Wings extends QuestHandler {

    private final static int questId = 14045;
	private final static int[] npc_ids = {278506, 279006, 279023};
    public _14045Rumors_On_Wings() {
        super(questId);
    }
	
    @Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc_id: npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}
    
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 14040, false);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();
        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 278506: //Tellus.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        } case SELECT_ACTION_1013: {
                            playQuestMovie(env, 272);
                            break;
                        } case STEP_TO_1: {
                            //Tigraki Island.
							TeleportService2.teleportTo(player, 400010000, 136.4838f, 790.4456f, 2836.5127f, (byte) 48);
                            return defaultCloseDialog(env, 0, 1);
                        }
                    }
				case 279023: //Agemonerk.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        } case STEP_TO_2: {
                            giveQuestItem(env, 182215918, 1);
							qs.setQuestVarById(0, var + 1);
						    updateQuestStatus(env);
						    player.setState(CreatureState.FLIGHT_TELEPORT);
						    player.unsetState(CreatureState.ACTIVE);
						    player.setFlightTeleportId(57001);
						    PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 57001, 0));
						    return closeDialogWindow(env);
                        }
                    }
				case 279006: //Garkbinerk.
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            } else if (var == 3) {
                                return sendQuestDialog(env, 2034);
                            }
                        } case SELECT_ACTION_1694: {
							removeQuestItem(env, 182215918, 1);
							return sendQuestDialog(env, 1694);
						} case STEP_TO_3: {
                            if (var == 2) {
                                qs.setQuestVarById(0, 12);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
						        player.setState(CreatureState.FLIGHT_TELEPORT);
						        player.unsetState(CreatureState.ACTIVE);
						        player.setFlightTeleportId(58001);
						        PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 58001, 0));
						        return closeDialogWindow(env);
                            }
                        }
                    }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 279023) { //Agemonerk.
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}