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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.TransformType;

public class TransformModel
{
	private int modelId;
	private int originalModelId;
	private TransformType originalType;
	private TransformType transformType;
	private int panelId;
	private boolean isActive = false;
	private TribeClass transformTribe;
	private TribeClass overrideTribe;
	private int ItemId;
	
	public TransformModel(Creature creature) {
		if (creature instanceof Player) {
			this.originalType = TransformType.PC;
		} else {
			this.originalType = TransformType.NONE;
		}
		this.originalModelId = creature.getObjectTemplate().getTemplateId();
		this.transformType = TransformType.NONE;
	}
	
	public int getModelId() {
		if (isActive && modelId > 0) {
			return modelId;
		}
		return originalModelId;
	}
	
	public void setModelId(int modelId) {
		if (modelId == 0 || modelId == originalModelId) {
			modelId = originalModelId;
			isActive = false;
		} else {
			this.modelId = modelId;
			isActive = true;
		}
	}
	
	public int getItemId() {
		if (ItemId > 0) {
			return ItemId;
		}
		return 0;
	}
	
	public void setItemId(int itemId) {
		if (itemId == 0) {
			ItemId = 0;
		} else {
			this.ItemId = itemId;
		}
	}
	
	public TransformType getType() {
		if (isActive) {
			return transformType;
		}
		return originalType;
	}
	
	public void setTransformType(TransformType transformType) {
		this.transformType = transformType;
	}
	
	public int getPanelId() {
		if (isActive) {
			return panelId;
		}
		return 0;
	}
	
	public void setPanelId(int id) {
		this.panelId = id;
	}
	
	public boolean isActive() {
		return this.isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public TribeClass getTribe() {
		if (isActive && transformTribe != null) {
			return transformTribe;
		}
		return overrideTribe;
	}
	
	public void setTribe(TribeClass transformTribe, boolean override) {
		if (override) {
			this.overrideTribe = transformTribe;
		} else {
			this.transformTribe = transformTribe;
		}
	}
}