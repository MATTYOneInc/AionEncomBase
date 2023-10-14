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

import com.aionemu.gameserver.model.templates.event.BoostEvents;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by wanke on 02/03/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "boost_events")
public class BoostEventdata
{
    @XmlElement(name = "boost_event")
    protected List<BoostEvents> bonusServiceBonusattr;
	
    @XmlTransient
    private TIntObjectHashMap<BoostEvents> templates = new TIntObjectHashMap<BoostEvents>();
	
    @XmlTransient
    private Map<Integer, BoostEvents> templatesMap = new HashMap<Integer, BoostEvents>();
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (BoostEvents template : bonusServiceBonusattr) {
            templates.put(template.getId(), template);
            templatesMap.put(template.getId(), template);
        }
        bonusServiceBonusattr.clear();
        bonusServiceBonusattr = null;
    }
	
    public int size() {
        return templates.size();
    }
	
    public BoostEvents getInstanceBonusattr(int buffId) {
        return templates.get(buffId);
    }
	
    public Map<Integer, BoostEvents> getAll() {
        return templatesMap;
    }
}