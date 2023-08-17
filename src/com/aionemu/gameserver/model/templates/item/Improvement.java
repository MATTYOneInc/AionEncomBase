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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Improvement")
public class Improvement
{
	@XmlAttribute(name = "way", required = true)
    private int way;
	
    @XmlAttribute(name = "price2")
    private int price2;
    
    @XmlAttribute(name = "price1")
    private int price1;
    
    @XmlAttribute(name = "burn_defend")
    private int burnDefend;
    
    @XmlAttribute(name = "burn_attack")
    private int burnAttack;
    
    @XmlAttribute(name = "level")
    private int level;
    
    @XmlAttribute(name = "recommend_rank")
    private int recommend_rank;

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the way
     */
    public int getChargeWay() {
        return way;
    }

    /**
     * @return the price1
     */
    public int getPrice1() {
        return price1;
    }

    /**
     * @return the price2
     */
    public int getPrice2() {
        return price2;
    }

    public int getBurnAttack() {
        return burnAttack;
    }

    public int getBurnDefend() {
        return burnDefend;
    }

	/**
     * @return the recommend_rank
     */
    public int getRecomendRank() {
    	return recommend_rank;
    }
    
    public boolean verifyRecomendRank(int rank) {
        return recommend_rank <= rank;
    }
}