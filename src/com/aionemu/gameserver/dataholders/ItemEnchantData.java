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

import com.aionemu.gameserver.model.templates.item.EnchantType;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Ranastic (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="enchant_templates")
public class ItemEnchantData {

	@XmlElement(name = "enchant_template", required = true)
    protected List<ItemEnchantTemplate> enchantTemplates;

    @XmlTransient
    private TIntObjectHashMap<ItemEnchantTemplate> enchants = new TIntObjectHashMap<ItemEnchantTemplate>();

    @XmlTransient
    private TIntObjectHashMap<ItemEnchantTemplate> authorizes = new TIntObjectHashMap<ItemEnchantTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ItemEnchantTemplate it : enchantTemplates) {
            getEnchantMap(it.getEnchantType()).put(it.getId(), it);
        }
    }

    private TIntObjectHashMap<ItemEnchantTemplate> getEnchantMap(EnchantType type) {
        if (type == EnchantType.ENCHANT) {
            return enchants;
        }
        return authorizes;
    }

    public ItemEnchantTemplate getEnchantTemplate(EnchantType type, int id) {
        if (type == EnchantType.ENCHANT) {
            return enchants.get(id);
        }
        return authorizes.get(id);
    }

    public int size() {
        return enchants.size() + authorizes.size();
    }
}