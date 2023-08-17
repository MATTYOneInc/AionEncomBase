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
package com.aionemu.gameserver.model.templates.teleport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "telelocation")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeleportLocation {

	@XmlAttribute(name = "loc_id", required = true)
	private int locId;
	
	@XmlAttribute(name = "teleportid")
	private int teleportid = 0;
	
	@XmlAttribute(name = "price", required = true)
	private int price = 0;
	
	@XmlAttribute(name = "pricePvp")
	private int pricePvp = 0;
	
	@XmlAttribute(name = "requiredQuest")
    private int requiredQuest = 0;
	
	@XmlAttribute(name = "type", required = true)
	private TeleportType type;
	
	public int getLocId() {
		return locId;
	}
	
	public int getTeleportId() {
		return teleportid;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getPricePvp() {
		return pricePvp;
	}
	
	public int getRequiredQuest() {
        return requiredQuest;
    }
	
	public TeleportType getType() {
		return type;
	}
}