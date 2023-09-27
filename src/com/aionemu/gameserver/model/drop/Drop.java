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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "drop")
public class Drop implements DropCalculator
{
	@XmlAttribute(name = "item_id", required = true)
	protected int itemId;
	
	@XmlAttribute(name = "min_amount", required = true)
	protected int minAmount;
	
	@XmlAttribute(name = "max_amount", required = true)
	protected int maxAmount;
	
	@XmlAttribute(required = true)
	protected float chance;
	
	@XmlAttribute(name = "no_reduce")
	protected Boolean noReduce = false;
	
	@XmlAttribute(name = "eachmember")
	protected boolean eachMember = false;
	
	private ItemTemplate template;
	
	public Drop() {
	}
	
	public Drop(int itemId, int minAmount, int maxAmount, float chance, boolean noReduce, boolean eachMember) {
		this.itemId = itemId;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.chance = chance;
		this.noReduce = noReduce;
		this.eachMember = eachMember;
		template = DataManager.ITEM_DATA.getItemTemplate(itemId);
	}
	
	public ItemTemplate getItemTemplate() {
		return template == null ? DataManager.ITEM_DATA.getItemTemplate(itemId) : template;
	}
	
	public int getItemId() {
		return itemId;
	}
	
    public int getMinAmount() {
        return minAmount;
    }
	
    public int getMaxAmount() {
        return maxAmount;
    }
	
    public float getChance() {
        return chance;
    }
	
	public boolean isNoReduction() {
		return noReduce;
	}
	
	public Boolean isEachMember() {
		return eachMember;
	}
	
	@Override
	public int dropCalculator(Set<DropItem> result, int index, float dropModifier, Race race, Collection<Player> groupMembers) {
		float percent = chance;
		if (!noReduce) {
			percent *= dropModifier;
			percent = percent - RateConfig.DROP_RATE_REDUCE;
		} if (Rnd.get() * 100 < percent) {
			if (eachMember && (groupMembers != null) && (!groupMembers.isEmpty())) {
				for (Player player : groupMembers) {
					DropItem dropitem = new DropItem(this);
					dropitem.calculateCount();
					dropitem.setIndex(index++);
					dropitem.setPlayerObjId(player.getObjectId());
					dropitem.setWinningPlayer(player);
					dropitem.isDistributeItem(true);
					result.add(dropitem);
				}
			} else {
				DropItem dropitem = new DropItem(this);
				dropitem.calculateCount();
				dropitem.setIndex(index++);
				result.add(dropitem);
			}
		}
		return index;
	}
	
    public static Drop load(ByteBuffer buffer) {
        Drop drop = new Drop();
        drop.itemId = buffer.getInt();
        drop.chance = buffer.getFloat();
        drop.minAmount = buffer.getInt();
        drop.maxAmount = buffer.getInt();
        drop.noReduce = buffer.get() == 1 ? true : false;
        drop.eachMember = buffer.get() == 1 ? true : false;
        return drop;
    }
	
    public String toString() {
        return "Drop [itemId=" + itemId + ", minAmount=" + minAmount + ", maxAmount=" + maxAmount + ", chance=" + chance + ", noReduce=" + noReduce + ", eachMember=" + eachMember + "]";
    }
}