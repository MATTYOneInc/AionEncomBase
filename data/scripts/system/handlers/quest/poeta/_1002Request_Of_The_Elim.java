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
package quest.poeta;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _1002Request_Of_The_Elim extends QuestHandler
{
	private final static int questId = 1002;
	
	public _1002Request_Of_The_Elim() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203076, 730007, 730010, 730008, 205000, 203067};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1100, false);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203076: { //Ampeis.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						} case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				} case 730007: { //Forest Protector Noah.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							} else if (var == 5) {
								return sendQuestDialog(env, 1693);
							} else if (var == 6) {
								return sendQuestDialog(env, 2034);
							} else if (var == 12) {
								return sendQuestDialog(env, 2120);
							}
						} case SELECT_ACTION_1353: {
							if (var == 1) {
								playQuestMovie(env, 20);
								return sendQuestDialog(env, 1353);
							}
						} case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2, 182200002, 1, 0, 0);
						} case STEP_TO_3: {
							return defaultCloseDialog(env, 5, 6, 0, 0, 182200002, 1);
						} case CHECK_COLLECTED_ITEMS: {
							if (var == 6) {
								return checkQuestItems(env, 6, 12, false, 2120, 2205);
							} else if (var == 12) {
								return sendQuestDialog(env, 2120);
							}
						} case STEP_TO_4: {
							return defaultCloseDialog(env, 12, 13);
						} case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				} case 730010: { //Sleeping Elder.
					if (dialog == QuestDialog.USE_OBJECT) {
						if (player.getInventory().getItemCountByItemId(182200002) == 1) {
							if (var == 2) {
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								useQuestObject(env, 2, 4, false, false);
							} else if (var == 4) {
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								return useQuestObject(env, 4, 5, false, false);
							}
						}
					}
					break;
				} case 730008: { //Daminu.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 13) {
								return sendQuestDialog(env, 2375);
							} else if (var == 14) {
								return sendQuestDialog(env, 2461);
							}
						} case STEP_TO_5: {
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310010000);
							TeleportService2.teleportTo(player, 310010000, newInstance.getInstanceId(), 52, 174, 229);
							changeQuestStep(env, 13, 20, false);
							return closeDialogWindow(env);
						} case STEP_TO_6: {
							return defaultCloseDialog(env, 14, 14, true, false);
						}
					}
					break;
				} case 205000: { //Belpartan.
					switch (dialog) {
						case START_DIALOG: {
							if (var == 20) {
								player.setState(CreatureState.FLIGHT_TELEPORT);
								player.unsetState(CreatureState.ACTIVE);
								player.setFlightTeleportId(1001);
								PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
								ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
										changeQuestStep(env, 20, 14, false);
										TeleportService2.teleportTo(player, 210010000, 1, 603, 1537, 116, (byte) 20);
									}
								}, 43000);
								return true;
							}
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203067) { //Kalio.
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2716);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() == 310010000) {
				PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
				return true;
			} else {
				int var = qs.getQuestVarById(0);
				if (var == 20) {
					changeQuestStep(env, 20, 13, false);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		int targetId = env.getTargetId();
		if (targetId == 730010) {
			int var = qs.getQuestVarById(0);
			if (qs == null || qs.getStatus() != QuestStatus.START || var != 2 && var != 4) {
				return false;
			}
		}
		return true;
	}
}