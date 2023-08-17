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

import com.aionemu.gameserver.model.templates.abyss_bonus.AbyssGroupAttr;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @Author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"abyssGroupattr"})
@XmlRootElement(name = "abyss_groupattrs")
public class AbyssGroupData
{
	@XmlElement(name = "abyss_groupattr")
	protected List<AbyssGroupAttr> abyssGroupattr;
	
	@XmlTransient
	private TIntObjectHashMap<AbyssGroupAttr> templates = new TIntObjectHashMap<AbyssGroupAttr>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (AbyssGroupAttr template: abyssGroupattr) {
			templates.put(template.getBuffId(), template);
		}
		abyssGroupattr.clear();
		abyssGroupattr = null;
	}
	
	public int size() {
		return templates.size();
	}
	
	public AbyssGroupAttr getInstanceBonusattr(int buffId) {
		return templates.get(buffId);
	}
}