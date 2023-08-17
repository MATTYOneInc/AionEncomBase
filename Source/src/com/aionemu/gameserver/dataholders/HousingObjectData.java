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

import com.aionemu.gameserver.model.templates.housing.PlaceableHouseObject;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "housingObjects" })
@XmlRootElement(name = "housing_objects")
public class HousingObjectData
{
	@XmlElements({ 
		@XmlElement(name = "postbox", type = com.aionemu.gameserver.model.templates.housing.HousingPostbox.class),
		@XmlElement(name = "use_item", type = com.aionemu.gameserver.model.templates.housing.HousingUseableItem.class),
		@XmlElement(name = "move_item", type = com.aionemu.gameserver.model.templates.housing.HousingMoveableItem.class),
		@XmlElement(name = "chair", type = com.aionemu.gameserver.model.templates.housing.HousingChair.class),
		@XmlElement(name = "picture", type = com.aionemu.gameserver.model.templates.housing.HousingPicture.class),
		@XmlElement(name = "passive", type = com.aionemu.gameserver.model.templates.housing.HousingPassiveItem.class),
		@XmlElement(name = "npc", type = com.aionemu.gameserver.model.templates.housing.HousingNpc.class),
		@XmlElement(name = "storage", type = com.aionemu.gameserver.model.templates.housing.HousingStorage.class),
		@XmlElement(name = "jukebox", type = com.aionemu.gameserver.model.templates.housing.HousingJukeBox.class),
		@XmlElement(name = "moviejukebox", type = com.aionemu.gameserver.model.templates.housing.HousingMovieJukeBox.class),
		@XmlElement(name = "emblem", type = com.aionemu.gameserver.model.templates.housing.HousingEmblem.class) })
	protected List<PlaceableHouseObject> housingObjects;
	
	@XmlTransient
	protected TIntObjectHashMap<PlaceableHouseObject> objectTemplatesById = new TIntObjectHashMap<PlaceableHouseObject>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (housingObjects == null) {
			return;
		}
		for (PlaceableHouseObject obj : housingObjects) {
			objectTemplatesById.put(obj.getTemplateId(), obj);
		}
		housingObjects.clear();
		housingObjects = null;
	}
	
	public int size() {
		return objectTemplatesById.size();
	}
	
	public PlaceableHouseObject getTemplateById(int templateId) {
		return objectTemplatesById.get(templateId);
	}
}