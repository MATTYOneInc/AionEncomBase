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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /** Modified by Ranastic /
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusAddExpAction")
public class BonusAddExpAction extends AbstractItemAction {
	@XmlAttribute(name = "rate")
	protected Integer rate;

	@XmlAttribute()
	protected boolean isPercent = true;

	public BonusAddExpAction() {
	}

	public BonusAddExpAction(Integer rate) {
		this.rate = rate;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
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
		long exp = player.getCommonData().getExpNeed();
		long expPercent = isPercent ? exp * rate / 100 : rate;
		if (player.getInventory().decreaseByObjectId(parentItem.getObjectId().intValue(), 1)) {
			player.getCommonData().setExp(player.getCommonData().getExp() + expPercent, false);
			player.getObserveController().notifyItemuseObservers(parentItem);
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(expPercent));
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
					parentItem.getObjectId().intValue(), itemTemplate.getTemplateId()), true);
		}
	}
}