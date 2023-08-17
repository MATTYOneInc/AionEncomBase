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
package com.aionemu.gameserver.model.templates.pet;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetBonusAttr", propOrder = {"penaltyAttr"})
public class PetBonusAttr {

	@XmlElement(name = "penalty_attr")
    protected List<PetPenaltyAttr> penaltyAttr;
	
    @XmlAttribute(name = "buff_id", required = true)
    protected int buffId;
    
    @XmlAttribute(name = "food_count", required = true)
    protected int foodCount;
	
    public List<PetPenaltyAttr> getPenaltyAttr() {
        if (penaltyAttr == null) {
            penaltyAttr = new ArrayList<PetPenaltyAttr>();
        }
        return this.penaltyAttr;
    }
	
    public int getBuffId() {
        return buffId;
    }
	
    public void setBuffId(int value) {
        this.buffId = value;
    }
    
    public int getFoodCount() {
        return foodCount;
    }
}