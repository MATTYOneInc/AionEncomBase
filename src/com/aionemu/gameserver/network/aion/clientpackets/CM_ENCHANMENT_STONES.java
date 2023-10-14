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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.actions.EnchantItemAction;
import com.aionemu.gameserver.model.templates.item.actions.EnchantStigmaAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_ENCHANMENT_STONES extends AionClientPacket {
	Logger log = LoggerFactory.getLogger(CM_ENCHANMENT_STONES.class);

	private int npcObjId;
	private int slotNum;
	private int actionType;
	private int targetFusedSlot;
	private int stoneUniqueId;
	private int targetItemUniqueId;
	private int supplementUniqueId;
	@SuppressWarnings("unused")
	private ItemCategory actionCategory;
	@SuppressWarnings("unused")
	private int unk;
	private int toppedItemObjId;

	public CM_ENCHANMENT_STONES(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionType = readC();
		targetFusedSlot = readC();
		targetItemUniqueId = readD();
		switch (actionType) {
		case 1:
		case 2:
			stoneUniqueId = readD();
			supplementUniqueId = readD();
			break;
		case 3:
			slotNum = readC();
			readC();
			readH();
			npcObjId = readD();
			break;
		case 4:
			stoneUniqueId = readD();
			unk = readD();
			break;
		case 8:
			toppedItemObjId = readD();
			stoneUniqueId = readD();
			break;
		default:
			log.error("Unknown enchantment type? 0x" + Integer.toHexString(actionType/* !!!!! */).toUpperCase());
			break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		VisibleObject obj = player.getKnownList().getObject(npcObjId);
		switch (actionType) {
		case 1: // Enchant Stone.
		case 2: // Add Manastone.
			EnchantItemAction action = new EnchantItemAction();
			EnchantStigmaAction action2 = new EnchantStigmaAction();
			Item manastone = player.getInventory().getItemByObjId(stoneUniqueId);
			Item stigmaStone = player.getInventory().getItemByObjId(stoneUniqueId);
			Item targetItem = player.getEquipment().getEquippedItemByObjId(targetItemUniqueId);
			Item targetStone = player.getInventory().getItemByObjId(targetItemUniqueId);
			if (targetItem == null) {
				targetItem = player.getInventory().getItemByObjId(targetItemUniqueId);
			}
			// Enchant Stigma.
			if (stigmaStone.getItemTemplate().isStigma() || stigmaStone.getItemTemplate().isEnchantmentStigmaStone()) {
				action2.act(player, stigmaStone, targetItem);
			} else {
				// Enchant Stone.
				if (action.canAct(player, manastone, targetItem)) {
					Item supplement = player.getInventory().getFirstItemByItemId(supplementUniqueId);
					if (supplement != null) {
						if (supplement.getItemId() / 100000 != 1661) {
							return;
						}
					}
					action.act(player, manastone, targetItem, supplement, targetFusedSlot);
				}
			}
			break;
		case 3: // Remove Manastone.
			long price = PricesService.getPriceForService(17161, player.getRace());
			if (player.getInventory().getKinah() > price) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(price));
				player.getInventory().decreaseKinah(price);
				if (targetFusedSlot == 1) {
					ItemSocketService.removeManastone(player, targetItemUniqueId, slotNum);
				} else {
					ItemSocketService.removeFusionstone(player, targetItemUniqueId, slotNum);
				}
			}
			break;
		case 4: // Godstone Socket.
			ItemSocketService.socketGodstone(player, targetItemUniqueId, stoneUniqueId);
			break;
		case 8: // Amplification.
			ItemSocketService.amplification(player, targetItemUniqueId, toppedItemObjId, stoneUniqueId);
			break;
		}
	}
}