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
package com.aionemu.gameserver.model.items;

import java.util.ArrayList;
import java.util.List;

public enum ItemSlot
{
	MAIN_HAND(1L),
	SUB_HAND(1L << 1),
	HELMET(1L << 2),
	TORSO(1L << 3),
	GLOVES(1L << 4),
	BOOTS(1L << 5),
	EARRINGS_LEFT(1L << 6),
	EARRINGS_RIGHT(1L << 7),
	RING_LEFT(1L << 8),
	RING_RIGHT(1L << 9),
	NECKLACE(1L << 10),
	SHOULDER(1L << 11),
	PANTS(1L << 12),
	POWER_SHARD_RIGHT(1L << 13),
	POWER_SHARD_LEFT(1L << 14),
	WINGS(1L << 15),
	WAIST(1L << 16),
	MAIN_OFF_HAND(1L << 17),
	SUB_OFF_HAND(1L << 18),
	PLUME(1L << 19),
	BRACELET(1L << 21),
	
	MAIN_OR_SUB(MAIN_HAND.slotIdMask | SUB_HAND.slotIdMask, true),
	MAIN_OFF_OR_SUB_OFF(MAIN_OFF_HAND.slotIdMask | SUB_OFF_HAND.slotIdMask, true),
	EARRING_RIGHT_OR_LEFT(EARRINGS_LEFT.slotIdMask | EARRINGS_RIGHT.slotIdMask, true),
	RING_RIGHT_OR_LEFT(RING_LEFT.slotIdMask | RING_RIGHT.slotIdMask, true),
	SHARD_RIGHT_OR_LEFT(POWER_SHARD_LEFT.slotIdMask | POWER_SHARD_RIGHT.slotIdMask, true),
	RIGHT_HAND(MAIN_HAND.slotIdMask | MAIN_OFF_HAND.slotIdMask, true),
	LEFT_HAND(SUB_HAND.slotIdMask | SUB_OFF_HAND.slotIdMask, true),

	//STIGMA slots
	STIGMA1(1L << 30), //4.8 checked
	STIGMA2(1L << 31), //4.8 checked
	STIGMA3(1L << 32), //4.8 checked
	STIGMA4(1L << 33), //4.8 checked
	STIGMA5(1L << 34), //4.8 checked
	STIGMA6(1L << 35), //4.8 checked
	STIGMA_SPECIAL(1L << 36), //5.6 checked
	
	//ESTIMA slots
	CP_SLOT1(1L << 40), //5.3 checked
	CP_SLOT2(1L << 41), //5.3 checked
	CP_SLOT3(1L << 42), //5.3 checked
	CP_SLOT4(1L << 43), //5.5 checked
	CP_SLOT5(1L << 44), //5.5 checked
	CP_SLOT6(1L << 45), //5.5 checked
	
	CP_SLOT(CP_SLOT1.slotIdMask | CP_SLOT2.slotIdMask | CP_SLOT3.slotIdMask | CP_SLOT4.slotIdMask | CP_SLOT5.slotIdMask | CP_SLOT6.slotIdMask, true),
	ALL_CP_SLOT(CP_SLOT.slotIdMask, true),
	
	REGULAR_STIGMAS(STIGMA1.slotIdMask | STIGMA2.slotIdMask | STIGMA3.slotIdMask | STIGMA4.slotIdMask | STIGMA5.slotIdMask | STIGMA6.slotIdMask | STIGMA_SPECIAL.slotIdMask, true),
	ALL_STIGMA(REGULAR_STIGMAS.slotIdMask, true);
	
	private long slotIdMask;
	private boolean combo;
	
	private ItemSlot(long mask) {
		this(mask, false);
	}
	
	private ItemSlot(long mask, boolean combo) {
		this.slotIdMask = mask;
		this.combo = combo;
	}
	
	public long getSlotIdMask() {
		return slotIdMask;
	}
	
	public boolean isCombo() {
		return combo;
	}
	
	public static boolean isRegularStigma(long slot) {
		return (REGULAR_STIGMAS.slotIdMask & slot) == slot;
	}
	
	public static boolean isStigma(long slot) {
		return (ALL_STIGMA.slotIdMask & slot) == slot;
	}

	public static boolean isEstisma(long slot) {
		return (ALL_CP_SLOT.slotIdMask & slot) == slot;
	}
	
	public static ItemSlot[] getSlotsFor(long slot) {
		List<ItemSlot> slots = new ArrayList<ItemSlot>();
		for (ItemSlot itemSlot : values()) {
			if (slot != 0 && !itemSlot.isCombo() && (slot & itemSlot.slotIdMask) == itemSlot.slotIdMask) {
				slots.add(itemSlot);
			}
		}
		return slots.toArray(new ItemSlot[slots.size()]);
	}
	
	public static ItemSlot getSlotFor(long slot) {
		ItemSlot[] slots = getSlotsFor(slot);
		if (slots != null && slots.length > 0) {
			return slots[0];
		}
		throw new IllegalArgumentException("Invalid provided slotIdMask " + slot);
	}
}