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
package com.aionemu.gameserver.model.templates.quest;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rewards", propOrder = {"selectableRewardItem", "rewardItem"})
public class Rewards
{
	@XmlElement(name = "selectable_reward_item")
	protected List<QuestItems> selectableRewardItem;
	
	@XmlElement(name = "reward_item")
	protected List<QuestItems> rewardItem;
	
	@XmlAttribute
	protected Integer gold;
	
	@XmlAttribute
	protected Integer exp;
	
	@XmlAttribute
	protected Integer expBoost;
	
	@XmlAttribute
	protected Integer dp;
	
	@XmlAttribute
	protected Integer ap;
	
	@XmlAttribute
	protected Integer gp;
	
	@XmlAttribute
	protected Integer abyssOp;

	@XmlAttribute
	protected Integer cp;
	
	@XmlAttribute
	protected Integer title;
	
	@XmlAttribute(name = "extend_inventory")
	protected Integer extendInventory;
	
	@XmlAttribute(name = "extend_stigma")
	protected Integer extendStigma;
	
	public List<QuestItems> getSelectableRewardItem() {
		if (selectableRewardItem == null) {
			selectableRewardItem = new ArrayList<QuestItems>();
		}
		return this.selectableRewardItem;
	}
	
	public List<QuestItems> getRewardItem() {
		if (rewardItem == null) {
			rewardItem = new ArrayList<QuestItems>();
		}
		return this.rewardItem;
	}
	
	public Integer getGold() {
		return gold;
	}
	
	public Integer getExp() {
		return exp;
	}
	
	public Integer getExpBoost() {
		return expBoost;
	}
	
	public Integer getDp() {
		return dp;
	}
	
	public Integer getAp() {
		return ap;
	}
	
	public Integer getGp() {
		return gp;
	}

	public Integer getCP() {
		return cp;
	}
	
	public Integer getAbyssOp() {
		return abyssOp;
	}
	
	public Integer getTitle() {
		return title;
	}
	
	public Integer getExtendInventory() {
		return extendInventory;
	}
	
	public Integer getExtendStigma() {
		return extendStigma;
	}
}