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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.quest.Rewards;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Balthazar, Cheatkiller
 */
public class _1640TeleporterRepairs extends QuestHandler {

	private final static int questId = 1640;
	public _1640TeleporterRepairs() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730033).addOnQuestStart(questId);
		qe.registerQuestNpc(730033).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730033) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case STEP_TO_1: {
						QuestService.startQuest(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null)
			return false;
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 730033) {
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 1352);
					case STEP_TO_2:
						if (player.getInventory().getItemCountByItemId(182201790) >= 1) {
							removeQuestItem(env, 182201790, 1);
							qs.setStatus(QuestStatus.REWARD);
							QuestService.finishQuest(env);
							return closeDialogWindow(env);
						} else
							return sendQuestDialog(env, 1353);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.COMPLETE) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					TeleportService2.teleportTo(player, WorldMapType.HEIRON.getId(), 187.71689f, 2712.14870f, 141.91672f, (byte) 195);
				}
			}, 1000);
		}
		return false;
	}
}