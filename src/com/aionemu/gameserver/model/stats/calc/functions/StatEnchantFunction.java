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
package com.aionemu.gameserver.model.stats.calc.functions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatEnchantFunction extends StatAddFunction
{
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
        } if (enchantLvl == 0) {
            return;
        } if ((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) != 0 || (item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0) {
            return;
        } if (item.getItemTemplate().getArmorType() == ArmorType.PLUME) {
        	stat.addToBonus(getEnchantAdditionModifier(enchantLvl, stat));
        } else {
        	stat.addToBase(getEnchantAdditionModifier(enchantLvl, stat));
        }
    }

	private int getEnchantAdditionModifier(int enchantLvl, Stat2 stat) {
        if (item.getItemTemplate().isWeapon()) {
            return getWeaponModifiers(enchantLvl);
        } if (this.item.getItemTemplate().isAccessory() && !this.item.getItemTemplate().isPlume() && !this.item.getItemTemplate().isBracelet()) {
            if (this.point == 0) {
                return getAccessoryModifiers(enchantLvl);
            }
            return this.point;
        } if (this.item.getItemTemplate().isArmor() || this.item.getItemTemplate().isPlume() && !this.item.getItemTemplate().isBracelet()) {
            return getArmorModifiers(enchantLvl, stat);
        } if (this.item.getItemTemplate().isArmor() || this.item.getItemTemplate().isBracelet() && !this.item.getItemTemplate().isPlume()) {
			return getBraceleteModifiers(enchantLvl);
		}
        return 0;
    }

	private int getWeaponModifiers(int enchantLvl) {
		switch (stat) {
		case MAIN_HAND_POWER:
		case OFF_HAND_POWER:
		case PHYSICAL_ATTACK:
			switch (item.getItemTemplate().getWeaponType()) {
			case GUN_1H:
			case SWORD_1H:
			case DAGGER_1H:
				return 2 * enchantLvl;
			case BOW:
			case SWORD_2H:
			case POLEARM_2H:
				return 4 * enchantLvl;
			case MACE_1H:
			case STAFF_2H:
				return 3 * enchantLvl;
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
				return 20 * enchantLvl;
			}
			return 0;
		case MAGICAL_ATTACK:
			switch (item.getItemTemplate().getWeaponType()) {
			case GUN_1H:
				return 2 * enchantLvl;
			case ORB_2H:
			case BOOK_2H:
			case HARP_2H:
				return 3 * enchantLvl;
			case CANNON_2H:
			case KEYBLADE_2H:
				return 4 * enchantLvl;
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

	private int getArmorModifiers(int enchantLvl, Stat2 applyStat) {
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
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return enchantLvl;
				case MAXHP:
					return 20 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
			    return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 2 * enchantLvl;
				case MAXHP:
					return 22 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl;
				case MAXHP:
					return 24 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl;
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
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 2 * enchantLvl;
				case MAXHP:
					return 18 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl;
				case MAXHP:
					return 20 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl;
				case MAXHP:
					return 22 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl;
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
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 3 * enchantLvl;
				case MAXHP:
					return 16 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl;
				case MAXHP:
					return 18 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 5 * enchantLvl;
				case MAXHP:
					return 20 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl;
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
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 4 * enchantLvl;
				case MAXHP:
					return 14 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 2 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 12:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 5 * enchantLvl;
				case MAXHP:
					return 16 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 3 * enchantLvl;
				case MAGICAL_DEFEND:
					return 2 * enchantLvl;
				}
				return 0;
			case 1 << 3:
				switch (stat) {
				case PHYSICAL_ATTACK:
					return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
					return 4 * enchantLvl;
				case PHYSICAL_DEFENSE:
					return 6 * enchantLvl;
				case MAXHP:
					return 18 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
					return 4 * enchantLvl;
				case MAGICAL_DEFEND:
					return 3 * enchantLvl;
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
				if (enchantLvl > 10)
					return 30 * (enchantLvl - 10);
				return 0;
			}

		case PLUME:
			switch (this.stat) {
			    case MAXHP: 
				    return 150 * enchantLvl;
			    case PHYSICAL_ATTACK: 
				    return 4 * enchantLvl;
			    case BOOST_MAGICAL_SKILL: 
				    return 20 * enchantLvl;
			    case PHYSICAL_CRITICAL:
				    return 12 * enchantLvl;
				case PHYSICAL_ACCURACY:
				    return 16 * enchantLvl;
			    case MAGICAL_ACCURACY:
				    return 8 * enchantLvl;
				case MAGICAL_CRITICAL:
				    return 8 * enchantLvl;
			}
			return 0;
		/**
		 * 5.0 Wings Enchant
		 */
		case WING:
			switch (this.stat) {
				case PHYSICAL_ATTACK:
				    return 1 * enchantLvl;
				case BOOST_MAGICAL_SKILL:
				    return 4 * enchantLvl;
				case MAXHP:
				    return 20 * enchantLvl;
				case PHYSICAL_CRITICAL_RESIST:
				    return 2 * enchantLvl;
				case FLY_TIME:
				    return 10 * enchantLvl;
				case MAGICAL_CRITICAL_RESIST:
				    return 1 * enchantLvl;
			}
			return 0;
		}
		return 0;
	}
}