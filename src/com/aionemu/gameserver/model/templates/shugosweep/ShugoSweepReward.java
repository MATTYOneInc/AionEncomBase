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
package com.aionemu.gameserver.model.templates.shugosweep;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Wnkrz on 23/10/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShugoSweepReward")
public class ShugoSweepReward {
	@XmlAttribute(name = "board_id")
	protected int boardId;

	@XmlAttribute(name = "reward_num")
	protected int rewardNum;

	@XmlAttribute(name = "item_id")
	protected int itemId;

	@XmlAttribute(name = "count")
	protected int count;

	public int getBoardId() {
		return boardId;
	}

	public int getRewardNum() {
		return rewardNum;
	}

	public int getItemId() {
		return itemId;
	}

	public int getCount() {
		return count;
	}
}