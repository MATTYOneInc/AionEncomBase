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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.serial_killer.RankRestriction;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"rankRestriction"})
@XmlRootElement(name = "serial_killers")
public class SerialKillerData
{
    @XmlElement(name = "rank_restriction")
    protected List<RankRestriction> rankRestriction;
	
    @XmlTransient
    private TIntObjectHashMap<RankRestriction> templates = new TIntObjectHashMap<RankRestriction>();
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
    	for (RankRestriction template : rankRestriction) {
            templates.put(template.getRankNum(), template);
    	}
    	rankRestriction.clear();
    	rankRestriction = null;
    }
	
    public int size() {
        return templates.size();
    }
	
    public RankRestriction getRankRestriction(int rank) {
        return templates.get(rank);
    }
}