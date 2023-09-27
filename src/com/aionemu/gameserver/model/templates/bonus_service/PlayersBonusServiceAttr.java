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
package com.aionemu.gameserver.model.templates.bonus_service;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranastic (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlayersBonusServiceAttr", propOrder = {"playersBonusAttr"})
public class PlayersBonusServiceAttr
{
	@XmlElement(name = "apply_bonus")
	protected List<PlayersBonusPenaltyAttr> playersBonusAttr;
	
	@XmlAttribute(name = "buff_id", required = true)
	protected int buffId;
	
	public List<PlayersBonusPenaltyAttr> getPenaltyAttr() {
		if (playersBonusAttr == null) {
			playersBonusAttr = new ArrayList<PlayersBonusPenaltyAttr>();
		}
		return playersBonusAttr;
	}
	
	public int getBuffId() {
		return buffId;
	}
	
	public void setBuffId(int value) {
		buffId = value;
	}
}