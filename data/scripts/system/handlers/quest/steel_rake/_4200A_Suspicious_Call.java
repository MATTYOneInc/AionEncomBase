/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.steel_rake;

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
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _4200A_Suspicious_Call extends QuestHandler {

	private final static int questId = 4200;
	private final static int[] npc_ids = {204839, 798332, 700522, 205233, 805839};
	
	public _4200A_Suspicious_Call() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(204839).addOnQuestStart(questId); //Uikinerk.
		qe.registerQuestItem(182209097, questId); //Teleport Scroll.
		for (int npc_id: npc_ids)
		qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		int var = qs.getQuestVarById(0);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204839) { //Uikinerk.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
			return false;
		} if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805839) { //Peorinerk.
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
			return false;
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204839) { //Uikinerk.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1003);
					} case SELECT_ACTION_1011: {
						return sendQuestDialog(env, 1011);
					} case STEP_TO_1: {
						WorldMapInstance steelRake = InstanceService.getNextAvailableInstance(300100000); //Steel Rake.
						InstanceService.registerPlayerWithInstance(steelRake, player);
						TeleportService2.teleportTo(player, 300100000, steelRake.getInstanceId(), 403.55f, 508.11f, 885.77f);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}
				}
			} else if (targetId == 798332 && var == 1) { //Haorunerk.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1352);
					} case SELECT_ACTION_1353: {
						playQuestMovie(env, 431);
						return sendQuestDialog(env, 1353);
					} case STEP_TO_2: {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				}
			} else if (targetId == 700522 && var == 2) { //Haorunerk's Bag.
				switch (env.getDialog()) {
				    case USE_OBJECT: {
						giveQuestItem(env, 182209097, 1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			} else if (targetId == 205233 && var == 3) { //Hudrunerk.
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2034);
					} case SELECT_ACTION_2035: {
						return sendQuestDialog(env, 2035);
					} case SET_REWARD: {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null || qs.getQuestVarById(0) != 2) {
			TeleportService2.teleportTo(player, 220040000, 367.9981f, 429.9916f, 222.11166f, (byte) 38);
			qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
			removeQuestItem(env, 182209097, 1); //Teleport Scroll.
			updateQuestStatus(env);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}