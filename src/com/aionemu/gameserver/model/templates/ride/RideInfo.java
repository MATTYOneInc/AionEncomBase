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
package com.aionemu.gameserver.model.templates.ride;

import com.aionemu.gameserver.model.templates.RideBound;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="RideInfo", propOrder={"ridebound"})
public class RideInfo
{
    protected RideBound ridebound;
	
    @XmlAttribute(name="cost_fp")
    protected Integer costFp;
	
    @XmlAttribute(name="start_fp")
    protected int startFp;
	
    @XmlAttribute(name="sprint_speed")
    protected float sprintSpeed;
	
    @XmlAttribute(name="fly_speed")
    protected float flySpeed;
	
    @XmlAttribute(name="move_speed")
    protected float moveSpeed;
	
    @XmlAttribute
    protected Integer type;
	
    @XmlAttribute(required=true)
    protected int id;
	
    public RideBound getRideBound() {
        return ridebound;
    }
	
    public Integer getCostFp() {
        return costFp;
    }
	
    public int getStartFp() {
        return startFp;
    }
	
    public float getSprintSpeed() {
        return sprintSpeed;
    }
	
    public float getFlySpeed() {
        return flySpeed;
    }
	
    public float getMoveSpeed() {
        return moveSpeed;
    }
	
    public Integer getType() {
        return type;
    }
	
    public int getNpcId() {
        return id;
    }
	
    public boolean canSprint() {
        return sprintSpeed != 0.0f;
    }
}