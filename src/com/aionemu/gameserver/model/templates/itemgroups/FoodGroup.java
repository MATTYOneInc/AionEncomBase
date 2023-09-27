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

import com.aionemu.gameserver.model.templates.rewards.IdLevelReward;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rolandas
 *
 */

/**
 * <p>
 * Java class for FoodGroup complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FoodGroup">
 *   &lt;complexContent>
 *     &lt;extension base="{}ItemGroup">
 *       &lt;sequence>
 *         &lt;element name="item" type="{}IdLevelReward" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FoodGroup")
public class FoodGroup extends BonusItemGroup {

	@XmlElement(name = "item")
	protected List<IdLevelReward> items;

	/**
	 * Gets the value of the item property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the item property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getItems().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link IdLevelReward }
	 */
	public List<IdLevelReward> getItems() {
		if (items == null) {
			items = new ArrayList<IdLevelReward>();
		}
		return this.items;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.templates.itemgroups.ItemGroup#getRewards()
	 */
	@Override
	public ItemRaceEntry[] getRewards() {
		return getItems().toArray(new ItemRaceEntry[0]);
	}
}