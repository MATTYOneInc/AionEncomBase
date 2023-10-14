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
package com.aionemu.gameserver.model.drop;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dropGroup", propOrder = { "drop" })
public class DropGroup implements DropCalculator {

	protected List<Drop> drop;
	@XmlAttribute
	protected Race race = Race.PC_ALL;
	@XmlAttribute(name = "use_category")
	protected Boolean useCategory = true;
	@XmlAttribute(name = "name")
	protected String group_name;

	public List<Drop> getDrop() {
		return this.drop;
	}

    public Race getRace() {
        return race;
    }

    public Boolean isUseCategory() {
        return useCategory;
    }
		/**
		 * @return the name
		 */
		public String getGroupName() {
			if (group_name == null) {
				return "";
			}
			return group_name;
		}

	@Override
	public int dropCalculator(Set<DropItem> result, int index, float dropModifier, Race race, Collection<Player> groupMembers) {
		if (useCategory) {
			Drop d = drop.get(Rnd.get(0, drop.size() - 1));
			return d.dropCalculator(result, index, dropModifier, race, groupMembers);
		}

		for (int i = 0; i < drop.size(); i++) {
			Drop d = drop.get(i);
			index = d.dropCalculator(result, index, dropModifier, race, groupMembers);
		}
		return index;
	}
}