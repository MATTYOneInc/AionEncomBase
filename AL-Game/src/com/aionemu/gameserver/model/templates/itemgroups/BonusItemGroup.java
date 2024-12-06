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
package com.aionemu.gameserver.model.templates.itemgroups;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.rewards.BonusType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusItemGroup")
@XmlSeeAlso({ CraftItemGroup.class, CraftRecipeGroup.class, ManastoneGroup.class, FoodGroup.class, MedicineGroup.class,
		OreGroup.class, GatherGroup.class, EnchantGroup.class, BossGroup.class })
public abstract class BonusItemGroup {

	@XmlAttribute(name = "bonusType", required = true)
	protected BonusType bonusType;

	@XmlAttribute(name = "chance")
	protected Float chance;

	public BonusType getBonusType() {
		return bonusType;
	}

	public float getChance() {
		if (chance == null) {
			return 0.0f;
		}
		return chance.floatValue();
	}

	public abstract ItemRaceEntry[] getRewards();
}