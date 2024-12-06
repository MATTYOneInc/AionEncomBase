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

import com.aionemu.gameserver.model.templates.panel_cp.PanelCp;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Ghostfur (Aion-Unique)
 */
@XmlRootElement(name = "panel_cps")
@XmlAccessorType(XmlAccessType.FIELD)
public class PanelCpData {

	@XmlElement(name = "panel_cp")
	private List<PanelCp> pclist;

	@XmlTransient
	private TIntObjectHashMap<PanelCp> cpData = new TIntObjectHashMap<PanelCp>();

	@XmlTransient
	private Map<Integer, PanelCp> cpDataMap = new HashMap<Integer, PanelCp>(1);

	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
		for (PanelCp panelCp : pclist) {
			cpData.put(panelCp.getId(), panelCp);
			cpDataMap.put(panelCp.getId(), panelCp);
		}
	}

	public int size() {
		return cpData.size();
	}

	public PanelCp getPanelCpId(int id) {
		return cpData.get(id);
	}

	public Map<Integer, PanelCp> getAll() {
		return cpDataMap;
	}
}