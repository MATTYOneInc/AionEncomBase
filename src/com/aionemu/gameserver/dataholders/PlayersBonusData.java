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

import com.aionemu.gameserver.model.templates.bonus_service.PlayersBonusServiceAttr;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Ranastic (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"playersServiceBonusattr"})
@XmlRootElement(name = "players_service_bonusattrs")
public class PlayersBonusData
{
	@XmlElement(name = "players_service_bonusattr")
	protected List<PlayersBonusServiceAttr> playersServiceBonusattr;
	
	@XmlTransient
	private TIntObjectHashMap<PlayersBonusServiceAttr> templates = new TIntObjectHashMap<PlayersBonusServiceAttr>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (PlayersBonusServiceAttr template : playersServiceBonusattr) {
			templates.put(template.getBuffId(), template);
		}
		playersServiceBonusattr.clear();
		playersServiceBonusattr = null;
	}
	
	public int size() {
		return templates.size();
	}
	
	public PlayersBonusServiceAttr getInstanceBonusattr(int buffId) {
		return templates.get(buffId);
	}
}