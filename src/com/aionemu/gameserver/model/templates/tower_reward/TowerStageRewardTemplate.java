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
package com.aionemu.gameserver.model.templates.tower_reward;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Wnkrz on 17/10/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TowerStageReward")
public class TowerStageRewardTemplate
{
    @XmlAttribute(name = "floor")
    protected int floor;
	
    @XmlAttribute(name = "name")
    protected String name;
	
    @XmlAttribute(name = "item_id")
    protected int itemId;
	
    @XmlAttribute(name = "item_count")
    protected int itemCount;
	
    @XmlAttribute(name = "item_id2")
    protected int itemId2;
	
    @XmlAttribute(name = "item_count2")
    protected int itemCount2;
	
    @XmlAttribute(name = "ap_count")
    protected int apCount;
	
    @XmlAttribute(name = "gp_count")
    protected int gpCount;
	
    @XmlAttribute(name = "kinah_count")
    protected int kinahCount;
	
    @XmlAttribute(name = "exp_count")
    protected int expCount;
	
    public int getFloor() {
        return this.floor;
    }
	
    public String getName() {
        return this.name;
    }
	
    public int getItemId() {
        return this.itemId;
    }
	
    public int getItemId2() {
        return this.itemId2;
    }
	
    public int getItemCount() {
        return this.itemCount;
    }
	
    public int getItemCount2() {
        return this.itemCount2;
    }
	
    public int getApCount() {
        return this.apCount;
    }
	
    public int getGpCount() {
        return this.gpCount;
    }
	
    public int getKinahCount() {
        return this.kinahCount;
    }
	
    public int getExpCount() {
        return this.expCount;
    }
}