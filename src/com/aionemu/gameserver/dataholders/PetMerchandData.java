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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.pet.PetMerchandEntry;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rinzler
 */

@XmlRootElement(name = "merchands")
@XmlAccessorType(XmlAccessType.FIELD)
public class PetMerchandData {
	@XmlElement(name = "merchand")
	private List<PetMerchandEntry> list;

	@XmlTransient
	private TIntObjectHashMap<PetMerchandEntry> merchandsById = new TIntObjectHashMap<PetMerchandEntry>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (PetMerchandEntry merch : list) {
			merchandsById.put(merch.getId(), merch);
		}
		list.clear();
		list = null;
	}

	public int size() {
		return merchandsById.size();
	}

	public PetMerchandEntry getMerchandTemplate(int id) {
		return merchandsById.get(id);
	}
}