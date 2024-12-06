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
package com.aionemu.gameserver.services.events;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ArmorType;

/**
 * @author Rinzler (Encom)
 */
public class DisplayService {
	public static int getDisplayTemplate(Player player, Item item) {
		if (player.isBandit() || player.isFFA()) {
			if (item.getItemTemplate().isWeapon()) {
				switch (item.getItemTemplate().getWeaponType()) {
				case SWORD_1H: // Boundless Long Sword Of Glory.
					return 100002013;
				case MACE_1H: // Boundless Mace Of Glory.
					return 100101495;
				case DAGGER_1H: // Boundless Dagger Of Glory.
					return 100201676;
				case ORB_2H: // Boundless Orb Of Glory.
					return 100501453;
				case BOOK_2H: // Boundless Spellbook Of Glory.
					return 100601571;
				case SWORD_2H: // Boundless Great Sword Of Glory.
					return 100901530;
				case POLEARM_2H: // Boundless Polearm Of Glory.
					return 101301414;
				case STAFF_2H: // Boundless Staff Of Glory.
					return 101501516;
				case BOW: // Boundless Bow Of Glory.
					return 101701511;
				case GUN_1H: // Boundless Magic Gun Of Glory.
					return 101801346;
				case CANNON_2H: // Boundless Magic Cannon Of Glory.
					return 101901251;
				case HARP_2H: // Boundless String Instrument Of Glory.
					return 102001374;
				case KEYBLADE_2H: // Boundless Keyblade Of Glory.
					return 102101189;
				default:
					return 100002013; // is by default.
				}
			} else if (player.isFFA() && item.getEquipmentSlot() == 8) { // Executioner's Outfit.
				return 110901014;
			} else if (player.isFFA() && item.getEquipmentSlot() == 4) { // Executioner's Mask.
				return 125045594;
			} else if (item.getItemTemplate().getArmorType() == ArmorType.SHIELD) {
				return 115001971; // Boundless Shield Of Glory.
			} else if (item.getEquipmentSlot() == 8 && player.getBattleground() != null) {
				if (player.getRace() == Race.ELYOS) {
					return 110101255; // Elite Legion Uniform.
				} else {
					return 110101257; // Elite Legion Uniform.
				}
			} else {
				return item.getItemSkinTemplate().getTemplateId();
			}
		} else {
			return item.getItemSkinTemplate().getTemplateId();
		}
	}

	public static String getDisplayName(Player player) {
		if (player.isBandit()) {
			return "[PK] Bandit";
		} else if (player.isFFA()) {
			return "Opponent";
		} else if (player.getBattleground() != null) {
			return player.getPlayerClass().name();
		} else {
			return player.getName();
		}
	}

	public static String getDisplayLegionName(Player player) {
		if (player.isBandit()) {
			return "Wanted";
		} else if (player.isFFA()) {
			return "Free For All";
		} else {
			return player.getLegion().getLegionName();
		}
	}
}