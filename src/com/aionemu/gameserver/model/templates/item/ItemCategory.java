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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "item_category")
@XmlEnum
public enum ItemCategory {
	MANASTONE, SPECIAL_MANASTONE, PRIMARY_MANASTONE, GODSTONE, ENCHANTMENT, ENCHANTMENT_STIGMA,
	ENCHANTMENT_AMPLIFICATION, FLUX, BALIC_EMOTION, BALIC_MATERIAL, RAWHIDE, SOULSTONE, RECIPE, GATHERABLE,
	GATHERABLE_BONUS, SWORD, DAGGER, MACE, ORB, SPELLBOOK, GREATSWORD, POLEARM, STAFF, BOW, SHIELD, JACKET, PANTS,
	SHARD, SHOES, GLOVES, SHOULDERS, NECKLACE, EARRINGS, RINGS, HELMET, BELT, SKILLBOOK, STIGMA, COINS, MEDALS, QUEST,
	KEY, TEMPERING, CRAFT_BOOST, COMBINATION,

	// 4.0
	GUN, CANNON, HARP, KEYBLADE, KEYHAMMER, PLUME, NONE,

	// 5.1
	ESTIMA, BRACELET
}