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

import com.aionemu.gameserver.model.templates.abyss_op.AbyssOp;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "abyss_ops")
@XmlAccessorType(XmlAccessType.FIELD)
public class AbyssOpData
{
	@XmlElement(name="abyss_op")
	private List<AbyssOp> aolist;
	
	@XmlTransient
	private TIntObjectHashMap<AbyssOp> opData = new TIntObjectHashMap<AbyssOp>();
	
	@XmlTransient
	private Map<Integer, AbyssOp> opDataMap = new HashMap<Integer, AbyssOp>(1);
	
	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
		for (AbyssOp abyssOp: aolist) {
			opData.put(abyssOp.getId(), abyssOp);
			opDataMap.put(abyssOp.getId(), abyssOp);
		}
	}
	
	public int size() {
		return opData.size();
	}
	
	public AbyssOp getAbyssOpId(int id) {
		return opData.get(id);
	}
	
	public Map<Integer, AbyssOp> getAll() {
		return opDataMap;
	}
}