/*

 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.templates.CubeExpandTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CubeExpandService {
	private static final Logger log = LoggerFactory.getLogger(CubeExpandService.class);
	private static final int MIN_EXPAND = 0;
	private static final int MAX_EXPAND = 15;

	public static void expandCube(final Player player, Npc npc) {
		final CubeExpandTemplate expandTemplate = DataManager.CUBEEXPANDER_DATA
				.getCubeExpandListTemplate(npc.getNpcId());
		if (expandTemplate == null) {
			log.error("Cube Expand Template could not be found for Npc ID: " + npc.getObjectId());
			return;
		}
		if (npcCanExpandLevel(expandTemplate, player.getNpcExpands() + 1) && canExpand(player)) {
			if (player.getNpcExpands() >= 15) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EXTEND_INVENTORY_CANT_EXTEND_MORE);
				return;
			}
			final int price = getPriceByLevel(expandTemplate, player.getNpcExpands() + 1);
			RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
				@Override
				public void acceptRequest(Creature requester, Player responder) {
					if (price > player.getInventory().getKinah()) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_EXPAND_NOT_ENOUGH_MONEY);
						return;
					}
					expand(responder, true);
					player.getInventory().decreaseKinah(price);
				}

				@Override
				public void denyRequest(Creature requester, Player responder) {
				}
			};
			boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING,
					responseHandler);
			if (result) {
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
						SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING, 0, 0, String.valueOf(price)));
			}
		} else
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300430));
	}

	public static void expand(Player player, boolean isNpcExpand) {
		if (!canExpand(player)) {
			return;
		}
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300431, "9"));
		if (isNpcExpand) {
			player.setNpcExpands(player.getNpcExpands() + 1);
		} else {
			player.setQuestExpands(player.getQuestExpands() + 1);
		}
		PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.cubeSize(StorageType.CUBE, player));
	}

	public static boolean canExpand(Player player) {
		return validateNewSize(player.getNpcExpands() + player.getQuestExpands() + 1);
	}

	public static boolean canExpandByTicket(Player player, int ticketLevel) {
		if (!canExpand(player))
			return false;
		int ticketExpands = player.getQuestExpands() - getCompletedCubeQuests(player);
		return ticketExpands < ticketLevel;
	}

	private static boolean validateNewSize(int level) {
		if (level < MIN_EXPAND || level > MAX_EXPAND)
			return false;
		return true;
	}

	private static boolean npcCanExpandLevel(CubeExpandTemplate clist, int level) {
		if (!clist.contains(level)) {
			return false;
		}
		return true;
	}

	private static int getCompletedCubeQuests(Player player) {
		int result = 0;
		QuestStateList qs = player.getQuestStateList();
		int[] questIds = { 1800, 1947, 2833, 2937, 1797 };
		for (int q : questIds) {
			if (qs.getQuestState(q) != null && qs.getQuestState(q).getStatus().equals(QuestStatus.COMPLETE))
				result++;
		}
		return result > 2 ? 2 : result;
	}

	private static int getPriceByLevel(CubeExpandTemplate clist, int level) {
		return clist.get(level).getPrice();
	}
}