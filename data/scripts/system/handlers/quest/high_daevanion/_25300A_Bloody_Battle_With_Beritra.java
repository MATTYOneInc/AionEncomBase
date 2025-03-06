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
package quest.high_daevanion;

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25300A_Bloody_Battle_With_Beritra extends QuestHandler {

	public static final int questId = 25300;
	public _25300A_Bloody_Battle_With_Beritra() {
		super(questId);
	}
	
    @Override
    public void register() {
        int[] npcs = {209883, 805339, 805364, 805365, 805366, 805377};
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(237228).addOnKillEvent(questId); //Lava Protector.
		qe.registerQuestNpc(237229).addOnKillEvent(questId); //Heatvent Protector.
		qe.registerQuestNpc(237224).addOnKillEvent(questId); //Fetid Phantomscorch Chimera.
		qe.registerQuestNpc(237225).addOnKillEvent(questId); //Rapacious Kadena.
		qe.registerQuestNpc(237226).addOnKillEvent(questId); //Rapacious Kadena.
		qe.registerQuestNpc(237227).addOnKillEvent(questId); //Rapacious Kadena.
		qe.registerQuestNpc(237231).addOnKillEvent(questId); //Exhausted Orissan.
		qe.registerQuestNpc(237236).addOnKillEvent(questId); //Commander Virtsha.
		qe.registerQuestNpc(237238).addOnKillEvent(questId); //Beritra [Dragon Form].
    }
	
    @Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 20507, true);
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
            if (targetId == 805339) { //Skuldun.
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
            } if (targetId == 805365) { //God Marchutan.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1353);
                        } else if (var == 13) {
							return sendQuestDialog(env, 7523);
						}
					} case SELECT_ACTION_1352: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        }
					} case STEP_TO_2: {
                        changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					} case STEP_TO_14: {
						giveQuestItem(env, 182215871, 1); //Empyrean Lord's Mark.
						changeQuestStep(env, 13, 14, true);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805364) { //Grito.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1694);
                        }
					} case SELECT_ACTION_1693: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        }
					} case STEP_TO_3: {
                        if (player.isInGroup2()) {
							//You must leave your group or alliance to enter.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403080));
							return true;
						} else {
							WorldMapInstance drakenspireDepthsQ = InstanceService.getNextAvailableInstance(301520000);
							InstanceService.registerPlayerWithInstance(drakenspireDepthsQ, player);
							TeleportService2.teleportTo(player, 301520000, drakenspireDepthsQ.getInstanceId(), 326, 183, 1687);
							changeQuestStep(env, 2, 3, false);
							return closeDialogWindow(env);
						}
					}
                }
            } if (targetId == 209883) { //Parsia.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 4) {
                            return sendQuestDialog(env, 2376);
                        }
					} case SELECT_ACTION_2375: {
                        if (var == 4) {
                            return sendQuestDialog(env, 2375);
                        }
					} case STEP_TO_5: {
                        changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805366) { //Aimah.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 8) {
                            return sendQuestDialog(env, 3740);
                        } else if (var == 9) {
							return sendQuestDialog(env, 4080);
						}
					} case STEP_TO_9: {
                        changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					} case STEP_TO_10: {
                        changeQuestStep(env, 9, 10, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 805377) { //Agony's Well.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 11) {
                            return sendQuestDialog(env, 6841);
                        }
					} case STEP_TO_12: {
						TeleportService2.teleportTo(env.getPlayer(), 301520000, 174.43976f, 527.9976f, 1749.7006f, (byte) 67);
                        changeQuestStep(env, 11, 12, false);
						return closeDialogWindow(env);
					}
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805339) { //Skuldun.
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
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (var == 3) {
				switch (targetId) {
                    case 237228: //Lava Protector.
					case 237229: { //Heatvent Protector.
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 5) {
				switch (targetId) {
					case 237224: { //Fetid Phantomscorch Chimera.
						qs.setQuestVar(6);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 6) {
				switch (targetId) {
                    case 237225:
                    case 237226:
					case 237227: { //Rapacious Kadena.
						qs.setQuestVar(7);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 7) {
				switch (targetId) {
					case 237231: { //Exhausted Orissan.
						qs.setQuestVar(8);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 10) {
				switch (targetId) {
					case 237236: { //Commander Virtsha.
						qs.setQuestVar(11);
						updateQuestStatus(env);
						return true;
					}
                }
			} else if (var == 12) {
				switch (targetId) {
					case 237238: { //Beritra [Dragon Form].
						qs.setQuestVar(13);
						updateQuestStatus(env);
						return true;
					}
                }
			}
		}
		return false;
    }
}