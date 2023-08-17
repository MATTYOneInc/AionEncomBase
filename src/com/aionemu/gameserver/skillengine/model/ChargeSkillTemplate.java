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
package com.aionemu.gameserver.skillengine.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Dr.Nism
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "charge_skill")
public class ChargeSkillTemplate {

	@XmlAttribute(name = "id")
    private int id;
	
	@XmlAttribute(name = "charge_set_name")
    private String charge_set_name;
    
	@XmlAttribute(name = "first")
    private int first;
    
	@XmlAttribute(name = "second")
    private int second;
    
	@XmlAttribute(name = "third")
    private int third;
    
	@XmlAttribute(name = "min_charge")
    private int min_charge;
	
    @XmlElement(name = "charge")
    private List<ChargeTemplate> charges;
	
	@XmlAttribute(name = "bonus_type", required = true)
	protected BonusChargeType type = BonusChargeType.NONE;
    
	public int getId() {
        return id;
    }
	
	public String getChargeSetName() {
		return charge_set_name;
	}
	
    public int getFirstId() {
        return first;
    }
    
    public int getSecondId() {
        return second;
    }
    
    public int getThirdId() {
        return third;
    }
    
    /**
     * @return the MinCharge
     */
    public int getMinCharge() {
        return min_charge;
    }
    
    /**
     * @return the Charges
     */
    public List<ChargeTemplate> getCharges() {
        return charges;
    }
	
	/**
     * @return the BonusChargeType
     */
	public BonusChargeType getBonusChargeType() {
		return type;
	}
}