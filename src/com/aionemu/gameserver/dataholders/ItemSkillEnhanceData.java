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

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.templates.item.ItemSkillEnhance;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanke on 01/03/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="item_skill_enhances")
public class ItemSkillEnhanceData
{
    @XmlElement(name = "item_skill_enhance", required = true)
    protected List<ItemSkillEnhance> skillEnhances;
	
    @XmlTransient
    protected List<ItemSkillEnhance> enhanceSkillList = new ArrayList<ItemSkillEnhance>();
	
    public ItemSkillEnhance getSkillEnhance(int id) {
        for (ItemSkillEnhance enhance : enhanceSkillList) {
            if (enhance.getId() == id) {
                return enhance;
            }
        }
        return null;
    }
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ItemSkillEnhance enhance : skillEnhances) {
            enhanceSkillList.add(enhance);
        }
        skillEnhances.clear();
        skillEnhances = null;
    }
	
    public int size() {
        return enhanceSkillList.size();
    }
}