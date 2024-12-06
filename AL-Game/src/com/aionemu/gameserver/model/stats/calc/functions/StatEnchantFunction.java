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
package com.aionemu.gameserver.model.stats.calc.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ArmorType;

public class StatEnchantFunction extends StatAddFunction {
	private static final Logger log = LoggerFactory.getLogger(StatEnchantFunction.class);

	private Item item;
	private int point;

	public StatEnchantFunction(Item owner, StatEnum stat, int point) {
		this.stat = stat;
		this.item = owner;
		this.point = point;
	}

	@Override
	public final int getPriority() {
		return 30;
	}

	@Override
	public void apply(Stat2 stat) {
		if (!item.isEquipped()) {
			return;
		}
		int enchantLvl = this.item.getEnchantLevel();
		if ((this.item.getItemTemplate().isAccessory())) {
			enchantLvl = this.item.getAuthorize();
		}
		if (enchantLvl == 0) {
			return;
		}
		if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) != 0
				|| (item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0) {
			return;
		}
		if (item.getItemTemplate().getArmorType() == ArmorType.PLUME) {
			stat.addToBonus(getEnchantAdditionModifier(enchantLvl, stat));
		} else {
			stat.addToBase(getEnchantAdditionModifier(enchantLvl, stat));
		}
	}

	private int getEnchantAdditionModifier(int enchantLvl, Stat2 stat) {
		int enchantAdvLvl=0;
		if (enchantLvl>15){
			enchantAdvLvl=enchantLvl-15;
			enchantLvl=15;
		}
		if (item.getItemTemplate().isWeapon()) {
			return getWeaponModifiers(enchantLvl, enchantAdvLvl);
		}
		if (this.item.getItemTemplate().isAccessory() && !this.item.getItemTemplate().isPlume()
				&& !this.item.getItemTemplate().isBracelet()) {
			if (this.point == 0) {
				return getAccessoryModifiers(enchantLvl);
			}
			return this.point;
		}
		if (this.item.getItemTemplate().isArmor()
				|| this.item.getItemTemplate().isPlume() && !this.item.getItemTemplate().isBracelet()) {
			return getArmorModifiers(enchantLvl, enchantAdvLvl, stat);
		}
		if (this.item.getItemTemplate().isArmor()
				|| this.item.getItemTemplate().isBracelet() && !this.item.getItemTemplate().isPlume()) {
			return getBraceleteModifiers(enchantLvl);
		}
		return 0;
	}

	private int getWeaponModifiers(int enchantLvl, int enchantAdvLvl) {
		switch (stat) {
		case MAIN_HAND_POWER:
		case OFF_HAND_POWER:
		case PHYSICAL_ATTACK:
			switch (item.getItemTemplate().getWeaponType()) {
			case GUN_1H:
			case SWORD_1H:
			case DAGGER_1H:
				return 2 * enchantLvl + 4 * enchantAdvLvl;
			case BOW:
			case SWORD_2H:
			case POLEARM_2H:
				return 4 * enchantLvl + 8 * enchantAdvLvl;
			case MACE_1H:
			case STAFF_2H:
				return 3 * enchantLvl + 6 * enchantAdvLvl;
			}
			return 0;
		case BOOST_MAGICAL_SKILL:
			switch (item.getItemTemplate().getWeaponType()) {
			case ORB_2H:
			case GUN_1H:
			case HARP_2H:
			case BOOK_2H:
			case MACE_1H:
			case STAFF_2H:
			case CANNON_2H:
			case KEYBLADE_2H:
				return 20 * enchantLvl + 40 * enchantAdvLvl;
			}
			return 0;
		case MAGICAL_ATTACK:
			switch (item.getItemTemplate().getWeaponType()) {
			case GUN_1H:
				return 2 * enchantLvl + 4 * enchantAdvLvl;
			case BOOK_2H:
				return 3 * enchantLvl + 6 * enchantAdvLvl;
			case ORB_2H:
			case HARP_2H:
			case CANNON_2H:
			case KEYBLADE_2H:
				return 4 * enchantLvl + 8 * enchantAdvLvl;
			}
			return 0;
		default:
			return 0;
		}
	}

	private int getAccessoryModifiers(int autorizeLvl) {
		switch (this.stat) {
		case PVP_ATTACK_RATIO:
			switch (autorizeLvl) {
			case 1:
				return 2;
			case 2:
				return 7;
			case 3:
				return 12;
			case 4:
				return 17;
			case 5:
				return 25;
			case 6:
				return 60;
			case 7:
				return 75;
			}
			return 0;
		case PVP_DEFEND_RATIO:
			switch (autorizeLvl) {
			case 1:
				return 3;
			case 2:
				return 9;
			case 3:
				return 15;
			case 4:
				return 21;
			case 5:
				return 31;
			case 6:
				return 41;
			case 7:
				return 55;
			}
			return 0;
		}
		return 0;
	}

	private int getBraceleteModifiers(int autorizeLvl) {
		switch (this.stat) {
		case PVP_ATTACK_RATIO:
			switch (autorizeLvl) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				return 0;
			case 6:
				return 5;
			case 7:
				return 5;
			case 8:
				return 10;
			case 9:
				return 15;
			case 10:
				return 20;
			}
			return 0;
		case PVP_DEFEND_RATIO:
			switch (autorizeLvl) {
			case 1:
				return 3;
			case 2:
				return 7;
			case 3:
				return 11;
			case 4:
				return 16;
			case 5:
				return 21;
			case 6:
				return 27;
			case 7:
				return 33;
			case 8:
				return 40;
			case 9:
				return 48;
			case 10:
				return 57;
			}
			return 0;
		}
		return 0;
	}

	private int getArmorModifiers(int enchantLvl, int enchantAdvLvl, Stat2 applyStat) {
		ArmorType armorType = item.getItemTemplate().getArmorType();
		if (armorType == null) {
			return 0;
		}
		long slot = item.getEquipmentSlot();
		int equipmentSlot = new Long(slot).intValue();
		switch (item.getItemTemplate().getArmorType()) {
		/**
		 * 4.9 Enchant Stats
		 */
		case ROBE:
			switch (equipmentSlot) {
			case 1 << 5:
			case 1 << 11:
			case 1 << 4:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return enchantLvl + 2 * enchantAdvLvl;
				case MAXHP:
					return 20 * enchantLvl + 40 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAXHP:
					return 22 * enchantLvl + 44 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAXHP:
					return 24 * enchantLvl + 48 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				}
				return 0;
			}
			return 0;
		case LEATHER:
			switch (equipmentSlot) {
			case 1 << 5:
			case 1 << 11:
			case 1 << 4:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAXHP:
					return 18 * enchantLvl + 36 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAXHP:
					return 20 * enchantLvl + 40 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAXHP:
					return 22 * enchantLvl + 44 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				}
				return 0;
			}
			return 0;
		case CHAIN:
			switch (equipmentSlot) {
			case 1 << 5:
			case 1 << 11:
			case 1 << 4:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAXHP:
					return 16 * enchantLvl + 32 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAXHP:
					return 18 * enchantLvl + 36 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 5 * enchantLvl + 10 * enchantAdvLvl;
				case MAXHP:
					return 20 * enchantLvl + 40 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				}
				return 0;
			}
			return 0;
		case PLATE:
			switch (equipmentSlot) {
			case 1 << 5:
			case 1 << 11:
			case 1 << 4:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAXHP:
					return 14 * enchantLvl + 28 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 5 * enchantLvl + 10 * enchantAdvLvl;
				case MAXHP:
					return 16 * enchantLvl + 32 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl + 4 * enchantAdvLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl + 2 * enchantAdvLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case PHYSICAL_DEFENSE:
					return 6 * enchantLvl + 12 * enchantAdvLvl;
				case MAXHP:
					return 18 * enchantLvl + 36 * enchantAdvLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl + 8 * enchantAdvLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl + 6 * enchantAdvLvl;
				}
				return 0;
			}
			return 0;
		case SHIELD:
			switch (stat) {
			case DAMAGE_REDUCE:
				float reduceRate = enchantLvl > 10 ? 0.2f : enchantLvl * 0.02f;
				return Math.round(reduceRate * applyStat.getBase());
			case BLOCK:
				if (enchantLvl > 10) {
					int blocktemp = 30 * (enchantLvl - 10 + enchantAdvLvl);
					if (blocktemp>300)
						blocktemp=300;				
				return blocktemp;
				}
			case MAXHP:
				return 100 * enchantAdvLvl;
			case PHYSICAL_DEFENSE:
				if (enchantAdvLvl>5)
					return 50 * (enchantAdvLvl - 5);
			case MAGIC_SKILL_BOOST_RESIST:
				if (enchantAdvLvl>5)
					return 20 * (enchantAdvLvl - 5);				
			}
			return 0;
		case PLUME:
			int plumeench = enchantLvl + enchantAdvLvl;
			switch (this.stat) {
			case MAXHP:
				return 150 * plumeench;
			case PHYSICAL_ATTACK:
				return 4 * plumeench;
			case BOOST_MAGICAL_SKILL:
				return 20 * plumeench;
			case PHYSICAL_CRITICAL:
				return 12 * plumeench;
			case PHYSICAL_ACCURACY:
				return 16 * plumeench;
			case MAGICAL_ACCURACY:
				return 8 * plumeench;
			case MAGICAL_CRITICAL:
				return 8 * plumeench;
			}
			return 0;
		/**
		 * 5.0 Wings Enchant
		 */
		 // lol
		case WING:
			if (enchantLvl+enchantAdvLvl<20)
				enchantLvl = enchantLvl+enchantAdvLvl;
			else
				enchantLvl = 20;
			if (enchantAdvLvl - 5 < 0) 
				enchantAdvLvl = 0;
			else
				enchantAdvLvl -= 5;			
			switch (this.stat) {		
			case PHYSICAL_ATTACK:
				return 1 * enchantLvl + 2 * enchantAdvLvl;
			case BOOST_MAGICAL_SKILL:
				return 4 * enchantLvl + 8 * enchantAdvLvl;
			case MAXHP:
				return 40 * enchantLvl + 80 * enchantAdvLvl;
			case PHYSICAL_CRITICAL_RESIST:
				return 2 * enchantLvl + 8 * enchantAdvLvl;
			case FLY_TIME:
				return 10 * enchantLvl + 20 * enchantAdvLvl;
			case MAGICAL_CRITICAL_RESIST:
				return 1 * enchantLvl + 4 * enchantAdvLvl;
			}
			return 0;
		}
		return 0;
	}
}