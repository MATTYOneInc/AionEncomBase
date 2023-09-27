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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.EnchantService;
import javolution.util.FastList;

public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket
{
	public int playerId;
    public int size;
    public FastList<Item> items;

    public SM_UPDATE_PLAYER_APPEARANCE(int playerId, FastList<Item> items) {
        this.playerId = playerId;
        this.items = items;
        this.size = items.size();
    }

    @Override
	protected void writeImpl(AionConnection con) {
		writeD(playerId);

		int mask = 0;
		for (Item item : items) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				mask |= slots[0].getSlotIdMask();
			} else {
				mask |= item.getEquipmentSlot();
			}
		}

		writeD(mask); // item size HBS

		for (Item item : items) {
			writeD(item.getItemSkinTemplate().getTemplateId());
			GodStone godStone = item.getGodStone();
			writeD(godStone != null ? godStone.getItemId() : 0);
			writeD(item.getItemColor());
			writeH(EnchantService.EnchantLevel(item));// unk (0x00)
			writeH(0x00);
		}
	}
}