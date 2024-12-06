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

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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
 
public class _10032Help_In_The_Hollow extends QuestHandler
{
	private final static int questId = 10032;

	public _10032Help_In_The_Hollow() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(215488).addOnKillEvent(questId);
		int[] npc_ids = {798952, 798954, 799022, 799503};
		for (int npc_id: npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215618, questId);
		qe.registerQuestItem(182215619, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10031, true);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} 		if (qs.getStatus() == QuestStatus.START) {
			switch(targetId) {
				case 798952: { //Crosia
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case STEP_TO_1:{
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
				}
				case 798954: { //Tialla
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;
					}
				}
				case 799022: { // Lothas
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 2)
								return sendQuestDialog(env, 1693);
						}
						case STEP_TO_3: {
							if (player.isInGroup2()) {
								return sendQuestDialog(env, 1864);
							} else {
								//if (giveQuestItem(env, 182215618, 1) && giveQuestItem(env, 182215619, 1)) {
						        if (var == 2) { //detele if you want return giveitem 
							        WorldMapInstance talocHollow = InstanceService.getNextAvailableInstance(300190000);
							        InstanceService.registerPlayerWithInstance(talocHollow, player);
							        TeleportService2.teleportTo(player, 300190000, talocHollow.getInstanceId(), 202.26694f, 226.0532f, 1098.236f, (byte) 30);
							        changeQuestStep(env, 2, 3, false);
									return closeDialogWindow(env);
								} else {
									PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_FULL_INVENTORY);
									return sendQuestStartDialog(env);
								}
							}
						}
						default:
							break;
					}
				}
				case 799503: { //Taloc's Mirage
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						}
						case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 6, 7, false, 10000, 10001); // 7
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default:
							break;
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798952) { //Crosia
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
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} switch (targetId) {
			case 215488: //Celestius.
				if (qs.getQuestVarById(0) == 6) {
					return defaultOnKillEvent(env, 215488, 6, 6);
				}
			break;
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() == 300190000) {
				int itemId = item.getItemId();
				int var = qs.getQuestVarById(0);
				int var1 = qs.getQuestVarById(1);
				if (itemId == 182215618) {
					changeQuestStep(env, 4, 5, false);
					return HandlerResult.SUCCESS;
				} else if (itemId == 182215619) {
					if (var == 5) {
						if (var1 >= 0 && var1 < 19) {
							changeQuestStep(env, var1, var1 + 1, false, 1);
							return HandlerResult.SUCCESS;
						} else if (var1 == 19) {
							qs.setQuestVar(6);
							updateQuestStatus(env);
							return HandlerResult.SUCCESS;
						}
					}
				}
			}
		}
		return HandlerResult.UNKNOWN;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.getWorldId() != 300190000) {
				int var = qs.getQuestVarById(0);
				if (var >= 4 && var < 6) {
					removeQuestItem(env, 182215618, 1);
					removeQuestItem(env, 182215619, 1);
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return true;
				} else if (var == 7) {
					removeQuestItem(env, 182215618, 1);
					removeQuestItem(env, 182215619, 1);
					qs.setQuestVar(8);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			} else if (player.getWorldId() == 300190000) {
				int var = qs.getQuestVarById(0);
				if (var == 3) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var >= 4 && var < 6) {
				removeQuestItem(env, 182215618, 1);
				removeQuestItem(env, 182215619, 1);
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var >= 4 && var < 6) {
				removeQuestItem(env, 182215618, 1);
				removeQuestItem(env, 182215619, 1);
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}