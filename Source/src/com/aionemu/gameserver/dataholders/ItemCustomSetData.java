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

import com.aionemu.gameserver.model.templates.item.ItemCustomSetTeamplate;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="item_custom_sets")
public class ItemCustomSetData
{
    @XmlElement(name = "item_custom_set", required = true)
    protected List<ItemCustomSetTeamplate> customTemplates;
	
    @XmlTransient
    private TIntObjectHashMap<ItemCustomSetTeamplate> custom = new TIntObjectHashMap<ItemCustomSetTeamplate>();
	
    public ItemCustomSetTeamplate getCustomTemplate(int id) {
        return custom.get(id);
    }
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ItemCustomSetTeamplate it : customTemplates) {
            getCustomMap().put(it.getId(), it);
        }
    }
	
    private TIntObjectHashMap<ItemCustomSetTeamplate> getCustomMap() {
        return custom;
    }
	
    public int size() {
        return custom.size();
    }
}