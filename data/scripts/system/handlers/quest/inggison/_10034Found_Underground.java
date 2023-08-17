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

import com.aionemu.gameserver.model.TeleportAnimation;
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
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _10034Found_Underground extends QuestHandler
{
	private final static int questId = 10034;

	public _10034Found_Underground() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {799030, 799029, 798990, 730295, 700604, 730229};
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215628, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(216531).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10033, true);
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var == 3) {
				if (player.getWorldId() == 300160000) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				} else {
					if (player.getInventory().getItemCountByItemId(182215627) == 0) {
						return giveQuestItem(env, 182215627, 1);
					}
				}
			} else if (var >= 4 && var < 7) {
				if (player.getWorldId() != 300160000) {
					changeQuestStep(env, var, 3, false);
					if (player.getInventory().getItemCountByItemId(182215627) == 0) {
						giveQuestItem(env, 182215627, 1);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int instanceId = player.getInstanceId();
		if (qs == null) {
			return false;
		}
		QuestDialog dialog = env.getDialog();
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799030: {
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
				} case 799029: {
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
				} case 798990: {
					switch (dialog) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						} case STEP_TO_3: {
							playQuestMovie(env, 504);
							return defaultCloseDialog(env, 2, 3);
						}
					}
					break;
				} case 730295: {
					switch (dialog) {
						case START_DIALOG: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						} case STEP_TO_4: {
							if (var == 3) {
								if (player.getInventory().getItemCountByItemId(182215627) > 0) {
									removeQuestItem(env, 182215627, 1);
									WorldMapInstance IDTempleLow = InstanceService.getNextAvailableInstance(300160000);
									InstanceService.registerPlayerWithInstance(IDTempleLow, player);
									TeleportService2.teleportTo(player, 300160000, IDTempleLow.getInstanceId(), 795.28143f, 918.806f, 149.80243f, (byte) 73);
									return true;
								} else {
									return sendQuestDialog(env, 10001);
								}
							}
						}
					}
					break;
				} case 700604: {
					if (var == 4 && dialog == QuestDialog.USE_OBJECT) {
						return useQuestObject(env, 4, 5, false, 0);
					}
					break;
				} case 730229: { //Traveller's Bag.
					if (dialog == QuestDialog.USE_OBJECT) {
						if (var == 6) {
							giveQuestItem(env, 182215628, 1);
							Npc npc = (Npc) env.getVisibleObject();
							npc.getController().onDelete();
							changeQuestStep(env, 6, 7, false);
							return closeDialogWindow(env);
						}
					}
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799030) {
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
    public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
		int itemId = item.getItemId();
			if (itemId == 182215628) {
				qs.setQuestVar(8);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				TeleportService2.teleportTo(env.getPlayer(), 210130000, 389.17194f, 980.16077f, 460.25f, (byte) 2);
				return HandlerResult.SUCCESS;
			}
		}
        return HandlerResult.FAILED;
    }
	
	@Override
	public boolean onKillEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		final Npc npc = (Npc) env.getVisibleObject();
		switch (env.getTargetId()) {
			case 216531:
			    if (var == 5)  {
					QuestService.addNewSpawn(300160000, instanceId, 730229, 740.4573f, 874.8399f, 152.78526f, (byte) 23); //Traveller's Bag.
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return true;
				}
			break;
		}
		return false;
	}
}