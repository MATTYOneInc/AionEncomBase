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

import com.aionemu.gameserver.model.templates.luna_dice.LunaDiceItem;
import com.aionemu.gameserver.model.templates.luna_dice.LunaDiceTable;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Wnkrz on 26/07/2017.
 */

@XmlRootElement(name = "luna_dice")
@XmlAccessorType(XmlAccessType.FIELD)
public class LunaDiceData {

    @XmlElement(name = "table")
    private List<LunaDiceTable> lunaDiceTabTemplate;
    private TIntObjectHashMap<List<LunaDiceItem>> diceItemList = new TIntObjectHashMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        diceItemList.clear();
        for (LunaDiceTable template : lunaDiceTabTemplate) {
            diceItemList.put(template.getId(), template.getLunaDiceTabItems());
        }
    }

    public int size() {
        return diceItemList.size();
    }

    public List<LunaDiceItem> getLunaDiceTabById(int id) {
        return diceItemList.get(id);
    }

    public List<LunaDiceTable> getLunaDiceTabs() {
        return lunaDiceTabTemplate;
    }
}