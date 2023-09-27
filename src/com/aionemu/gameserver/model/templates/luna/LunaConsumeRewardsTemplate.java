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
package com.aionemu.gameserver.model.templates.luna;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Ranastic
 */
@XmlType(name = "luna_consume_reward")
@XmlAccessorType(XmlAccessType.NONE)
public class LunaConsumeRewardsTemplate
{
	@XmlAttribute
	protected int id;
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected int luna_sum_count;
	@XmlAttribute
	protected int gacha_cost;
	@XmlAttribute
	protected int create_1;
	@XmlAttribute
	protected int num_1;
	
	public int getId() {
        return this.id;
    }
	public String getName() {
		return name;
	}
	public int getSumCount() {
		return luna_sum_count;
	}
	public int getGachaCost() {
		return gacha_cost;
	}
	public int getCreateItemId() {
		return create_1;
	}
	public int getCreateItemCount() {
		return num_1;
	}
}