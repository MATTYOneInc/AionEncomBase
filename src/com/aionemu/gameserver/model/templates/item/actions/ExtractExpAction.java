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
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtractExpAction")
public class ExtractExpAction extends AbstractItemAction
{
	@XmlAttribute(name = "expextractionrate")
    protected Integer expextractionrate;
	
	@XmlAttribute(name = "reward")
    protected Integer reward;
    
    public ExtractExpAction() {
    }
	
    public ExtractExpAction(Integer expextractionrate) {
        this.expextractionrate = expextractionrate;
    }
	
    public Integer getRate() {
        return expextractionrate;
    }
	
    public void setRate(Integer expextractionrate) {
        this.expextractionrate = expextractionrate;
    }
    
    public Integer getReward() {
        return reward;
    }
	
    public void setReward(Integer reward) {
        this.reward = reward;
    }
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		return true;
	}
	
    @Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		ItemTemplate itemTemplate = parentItem.getItemTemplate();
		ItemService.addItem(player, getReward(), 1);
		player.getCommonData().addExp((long) - ((player.getCommonData().getExpNeed() * getRate()) / 100f), RewardType.HUNTING);
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
		player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
    }
}