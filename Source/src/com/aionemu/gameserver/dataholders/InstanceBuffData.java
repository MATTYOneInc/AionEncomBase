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

import com.aionemu.gameserver.model.templates.instance_bonusatrr.InstanceBonusAttr;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "instanceBonusattr"
})
@XmlRootElement(name = "instance_bonusattrs")
public class InstanceBuffData {

    @XmlElement(name = "instance_bonusattr")
    protected List<InstanceBonusAttr> instanceBonusattr;
    @XmlTransient
    private TIntObjectHashMap<InstanceBonusAttr> templates = new TIntObjectHashMap<InstanceBonusAttr>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (InstanceBonusAttr template : instanceBonusattr) {templates.put(template.getBuffId(), template);
        }
        instanceBonusattr.clear();
        instanceBonusattr = null;
    }
   
    public int size() {
        return templates.size();
    }

    public InstanceBonusAttr getInstanceBonusattr(int buffId) {
        return templates.get(buffId);
    }
}