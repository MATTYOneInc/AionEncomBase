/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;

public class ManaStoneInfoBlobEntry extends ItemBlobEntry
{
	ManaStoneInfoBlobEntry() {
		super(ItemBlobType.MANA_SOCKETS);
	}
	
	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;
		writeC(buf, item.isSoulBound() ? 1 : 0);
		writeC(buf, item.getEnchantLevel());
		writeD(buf, item.getItemSkinTemplate().getTemplateId());
		writeC(buf, item.getOptionalSocket());
		writeC(buf, item.hasEnchantBonus() ? item.getEnchantBonus() : 0);
		writeItemStones(buf);
		ItemStone god = item.getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());
		int itemColor = item.getItemColor();
		int dyeExpiration = item.getColorTimeLeft();
		if ((dyeExpiration > 0 && item.getColorExpireTime() > 0 || dyeExpiration == 0 && item.getColorExpireTime() == 0) && item.getItemTemplate().isItemDyePermitted()) {
			writeC(buf, itemColor == 0 ? 0 : 1);
			writeD(buf, itemColor);
			writeD(buf, 0);
			writeD(buf, dyeExpiration);
		} else {
			writeC(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}
		IdianStone idianStone = item.getIdianStone();
		if (idianStone != null && idianStone.getPolishNumber() > 0) {
			writeD(buf, idianStone.getItemId());
			writeC(buf, idianStone.getPolishNumber());
		} else {
			writeD(buf, 0);
			writeC(buf, 0);
		}
		writeC(buf, item.getAuthorize());
		writeH(buf, 0);
		writePlumeBonusStat(buf);
		writeB(buf, new byte[36]);
		writeAmplification(buf);
		writeB(buf, new byte[11]);
		writeEnhance(buf);
		writeD(buf, item.isLunaReskin() ? 1 : 0);
		writeC(buf, item.getReductionLevel());
	}
	
	private void writeAmplification(ByteBuffer buf) {
		Item item = this.ownerItem;
		writeC(buf, item.isAmplified() ? 1 : 0);
		writeD(buf, item.getAmplificationSkill());
	}
	
	private void writeEnhance(ByteBuffer buf) {
		Item item = this.ownerItem;
		writeC(buf, item.isEnhance() ? 1 : 0);
		writeD(buf, item.getEnhanceSkillId());
		writeD(buf, item.getEnhanceEnchantLevel());
	}

	private void writePlumeBonusStat(ByteBuffer buf) {
		Item item = ownerItem;
		if (item.getItemTemplate().isPlume()) {
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 42);
			writeD(buf, item.getAuthorize() * 150);
			if (item.getItemTemplate().getTemperingTableId() == 10051 || item.getItemTemplate().getTemperingTableId() == 10063 || item.getItemTemplate().getTemperingTableId() == 10107) {
				writeD(buf, 30);
				writeD(buf, item.getAuthorize() * 4);
				writeD(buf, 0);
				writeD(buf, 0);
			} else if (item.getItemTemplate().getTemperingTableId() == 10052 || item.getItemTemplate().getTemperingTableId() == 10064 || item.getItemTemplate().getTemperingTableId() == 10108) {
				writeD(buf, 35);
				writeD(buf, item.getAuthorize() * 20);
				writeD(buf, 0);
				writeD(buf, 0);
			} else if (item.getItemTemplate().getTemperingTableId() == 10056 || item.getItemTemplate().getTemperingTableId() == 10065 || item.getItemTemplate().getTemperingTableId() == 10109) {
				writeD(buf, 33);
				writeD(buf, item.getAuthorize() * 12);
				writeD(buf, 0);
				writeD(buf, 0);
			} else if (item.getItemTemplate().getTemperingTableId() == 10057 || item.getItemTemplate().getTemperingTableId() == 10066 || item.getItemTemplate().getTemperingTableId() == 10110) {
				writeD(buf, 36);
				writeD(buf, item.getAuthorize() * 8);
				writeD(buf, 0);
				writeD(buf, 0);
			} else if (item.getItemTemplate().getTemperingTableId() == 10103 || item.getItemTemplate().getTemperingTableId() == 10105) {
				writeD(buf, 32);
				writeD(buf, item.getAuthorize() * 16);
				writeD(buf, 0);
				writeD(buf, 0);
			} else if (item.getItemTemplate().getTemperingTableId() == 10104 || item.getItemTemplate().getTemperingTableId() == 10106) {
				writeD(buf, 34);
				writeD(buf, item.getAuthorize() * 8);
				writeD(buf, 0);
				writeD(buf, 0);
			}
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		} else {
			writeB(buf, new byte[64]);
		}
	}
	
	private void writeItemStones(ByteBuffer buf) {
		Item item = ownerItem;
		int count = 0;
		if (item.hasManaStones()) {
			Set<ManaStone> itemStones = item.getItemStones();
			ArrayList<ManaStone> basicStones = new ArrayList<ManaStone>();
			ArrayList<ManaStone> ancientStones = new ArrayList<ManaStone>();
			for (ManaStone itemStone : itemStones) {
				if (itemStone.isBasic()) {
					basicStones.add(itemStone);
				} else {
					ancientStones.add(itemStone);
				}
			} if (item.getItemTemplate().getSpecialSlots() > 0) {
				if (ancientStones.size() > 0) {
					for (ManaStone ancientStone : ancientStones) {
						if (count == 6) {
							break;
						}
						writeD(buf, ancientStone.getItemId());
						count++;
					}
				} for (int i = count; i < item.getItemTemplate().getSpecialSlots(); i++) {
					writeD(buf, 0);
					count++;
				}
			} for (ManaStone basicStone : basicStones) {
				if (count == 6) {
					break;
				}
				writeD(buf, basicStone.getItemId());
				count++;
			}
			skip(buf, (6 - count) * 4);
		} else {
			skip(buf, 24);
		}
	}
	
	@Override
	public int getSize() {
		return 187;
	}
}