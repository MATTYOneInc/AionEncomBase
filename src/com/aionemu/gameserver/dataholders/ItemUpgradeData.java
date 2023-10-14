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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.model.templates.item.upgrade.ItemUpgradeTemplate;
import com.aionemu.gameserver.model.templates.item.upgrade.UpgradeResultItem;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Ranastic (Encom)
 */

@XmlRootElement(name = "item_upgrades")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemUpgradeData {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);

	@XmlElement(name = "item_upgrade")
	protected List<ItemUpgradeTemplate> itemUpgradeTemplates;

	private TIntObjectHashMap<ItemUpgradeTemplate> itemUpgradeSets;
	private FastMap<Integer, FastMap<Integer, UpgradeResultItem>> upgradeResultItemMap;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		itemUpgradeSets = new TIntObjectHashMap<ItemUpgradeTemplate>();
		upgradeResultItemMap = new FastMap<Integer, FastMap<Integer, UpgradeResultItem>>();
		for (ItemUpgradeTemplate set : itemUpgradeTemplates) {
			itemUpgradeSets.put(set.getUpgrade_base_item_id(), set);
			upgradeResultItemMap.put(set.getUpgrade_base_item_id(), new FastMap<Integer, UpgradeResultItem>());
			if (!set.getUpgrade_result_item().isEmpty()) {
				for (UpgradeResultItem resultItem : set.getUpgrade_result_item()) {
					upgradeResultItemMap.get(set.getUpgrade_base_item_id()).put(resultItem.getItem_id(), resultItem);
				}
			}
		}
		itemUpgradeTemplates = null;
	}

	public ItemUpgradeTemplate getItemUpgradeTemplate(int itemSetId) {
		return itemUpgradeSets.get(itemSetId);
	}

	public FastMap<Integer, UpgradeResultItem> getResultItemMap(int baseItemId) {
		if (upgradeResultItemMap.containsKey(baseItemId)) {
			if (!upgradeResultItemMap.get(baseItemId).isEmpty()) {
				return upgradeResultItemMap.get(baseItemId);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public int size() {
		return itemUpgradeSets.size();
	}
}