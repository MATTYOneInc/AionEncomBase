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
package com.aionemu.gameserver.questEngine.handlers.models;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "reward")
public class Reward
{
    @XmlAttribute(required = true)
    protected BigInteger count;
	
    @XmlAttribute(name = "item_id", required = true)
    protected BigInteger itemId;
	
    @XmlAttribute(required = true)
    protected BigInteger no;
	
    @XmlAttribute(required = true)
    protected BigInteger rank;
	
    public BigInteger getCount() {
        return count;
    }
	
    public void setCount(BigInteger value) {
        this.count = value;
    }
	
    public BigInteger getItemId() {
        return itemId;
    }
	
    public void setItemId(BigInteger value) {
        this.itemId = value;
    }
	
    public BigInteger getNo() {
        return no;
    }
	
    public void setNo(BigInteger value) {
        this.no = value;
    }
	
    public BigInteger getRank() {
        return rank;
    }
	
    public void setRank(BigInteger value) {
        this.rank = value;
    }
}