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
package com.aionemu.gameserver.services.item;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.*;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.SummonHouseObjectAction;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import org.apache.commons.lang.IncompleteArgumentException;
import org.joda.time.DateTime;

public final class HouseObjectFactory {

	public static HouseObject<?> createNew(House house, int objectId, int objectTemplateId) {
		PlaceableHouseObject template = DataManager.HOUSING_OBJECT_DATA.getTemplateById(objectTemplateId);
		if ((template instanceof HousingChair)) {
			return new ChairObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingJukeBox)) {
			return new JukeBoxObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingMoveableItem)) {
			return new MoveableObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingNpc)) {
			return new NpcObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingPicture)) {
			return new PictureObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingPostbox)) {
			return new PostboxObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingStorage)) {
			return new StorageObject(house, objectId, template.getTemplateId());
		}
		if ((template instanceof HousingUseableItem)) {
			return new UseableItemObject(house, objectId, template.getTemplateId());
		}
		return new PassiveObject(house, objectId, template.getTemplateId());
	}

	public static HouseObject<?> createNew(House house, ItemTemplate itemTemplate) {
		if (itemTemplate.getActions() == null) {
			throw new IncompleteArgumentException("template actions null");
		}
		SummonHouseObjectAction action = itemTemplate.getActions().getHouseObjectAction();
		if (action == null) {
			throw new IncompleteArgumentException("template actions miss SummonHouseObjectAction");
		}
		int objectTemplateId = action.getTemplateId();
		HouseObject<?> obj = createNew(house, IDFactory.getInstance().nextId(), objectTemplateId);
		if (obj.getObjectTemplate().getUseDays() > 0) {
			int expireEnd = (int) (DateTime.now().plusDays(obj.getObjectTemplate().getUseDays()).getMillis() / 1000L);
			obj.setExpireTime(expireEnd);
		}
		return obj;
	}
}