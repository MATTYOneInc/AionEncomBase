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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.panel_cp.StoneCP;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by Rinzler (Encom)
 */
@XmlRootElement(name = "stones_cp")
@XmlAccessorType(XmlAccessType.FIELD)
public class StoneCpData
{
    @XmlElement(name="stone_cp")
    private List<StoneCP> stonelist;

    @XmlTransient
    private TIntObjectHashMap<StoneCP> stoneData = new TIntObjectHashMap<StoneCP>();

    @XmlTransient
    private Map<Integer, StoneCP> stoneDataMap = new HashMap<Integer, StoneCP>(1);

    void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
        for (StoneCP stoneCp: stonelist) {
            stoneData.put(stoneCp.getId(), stoneCp);
            stoneDataMap.put(stoneCp.getId(), stoneCp);
        }
    }

    public int size() {
        return stoneData.size();
    }

    public StoneCP getStoneCpId(int id) {
        return stoneData.get(id);
    }

    public Map<Integer, StoneCP> getAll() {
        return stoneDataMap;
    }
}