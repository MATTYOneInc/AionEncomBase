/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.poeta;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rhys2002
 */
public class _1114TheNymphsGown extends QuestHandler {

	private final static int questId = 1114;
	private final static int[] npc_ids = { 203075, 203058, 700008 };
	public _1114TheNymphsGown() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182200214, questId);
		for (int npc_id : npc_ids)
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (targetId == 0) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialogId() == 1002) {
					QuestService.startQuest(env);
					if (!giveQuestItem(env, 182200226, 1));
					removeQuestItem(env, 182200214, 1); // Namus's Diary with double-click to start the quest
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
					return true;
				}
				else
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
			}
		}
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203075 && var == 4) { // Namus
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 6);
				else
					return sendQuestEndDialog(env);
			}
			else if (targetId == 203058 && var == 3) // Asteros
				return sendQuestEndDialog(env);
		}
		else if (qs.getStatus() != QuestStatus.START)
			return false;
		if (targetId == 203075) { // Namus
			switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 0)
						return sendQuestDialog(env, 1011);
					else if (var == 2)
						return sendQuestDialog(env, 1693);
					else if (var == 3)
						return sendQuestDialog(env, 2375);
				case SELECT_REWARD:
					if (var == 2) {
						qs.setQuestVarById(0, var + 2);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182200217, 1);
						return sendQuestDialog(env, 6);
					}
					if (var == 3) {
						qs.setQuestVarById(0, var + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182200217, 1);
						return sendQuestDialog(env, 6);
					}
				case STEP_TO_1:
					if (var == 0) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						removeQuestItem(env, 182200226, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case STEP_TO_2:
					if (var == 2) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if (targetId == 700008) { // Seirenia's clothes
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 1) {
						for (VisibleObject obj : player.getKnownList().getKnownObjects().values()) {
							if (!(obj instanceof Npc))
								continue;
							if (((Npc) obj).getNpcId() != 203175) // Seirenia
								continue;
							((Npc) obj).getAggroList().addDamage(player, 50);
						}
						// Nymph's Dress
						if (!giveQuestItem(env, 182200217, 1))
							; // wtf ?
						qs.setQuestVarById(0, 2);
						updateQuestStatus(env);
					}
					return true;
			}
		}
		if (targetId == 203058) {// Asteros
			switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 3)
						return sendQuestDialog(env, 2034);
				case STEP_TO_3:
					if (var == 3) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182200217, 1);
						return sendQuestDialog(env, 5);
					}
				case STEP_TO_2:
					if (var == 3) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (id != 182200214)
			return HandlerResult.UNKNOWN;
		PacketSendUtility.broadcastPacket(player,
			new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 20, 1, 0), true);
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
			sendQuestDialog(env, 4);
		return HandlerResult.SUCCESS;
	}
}