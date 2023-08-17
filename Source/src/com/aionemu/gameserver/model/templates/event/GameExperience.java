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
package com.aionemu.gameserver.model.templates.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name="game_experience_item")
@XmlAccessorType(XmlAccessType.NONE)
public class GameExperience
{
	@XmlAttribute(name="id", required = true)
	private int id;
	
	@XmlAttribute(name="account_type", required = true)
	private AccountType accountType;
	
	@XmlAttribute(name="reward_item", required = true)
	private int rewardItem;
	
	public int getId() {
		return id;
	}
	
	public AccountType getAccountType() {
		return accountType;
	}
	
	public int getRewardItem() {
		return rewardItem;
	}
}