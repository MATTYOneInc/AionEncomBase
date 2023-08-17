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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.world.World;

public abstract class VisibleObjectController<T extends VisibleObject>
{
	private T owner;
	
	public void setOwner(T owner) {
		this.owner = owner;
	}
	
	public T getOwner() {
		return owner;
	}
	
	public void see(VisibleObject object) {
	}
	
	public void notSee(VisibleObject object, boolean isOutOfRange) {
	}
	
	public void delete() {
		if (getOwner().isSpawned()) {
			World.getInstance().despawn(getOwner());
		}
		World.getInstance().removeObject(getOwner());
	}
	
	public void onBeforeSpawn() {
	}
	
	public void onAfterSpawn() {

	}
	
	public void onDespawn() {
	}
	
	public void onDelete() {
		if (getOwner().isInWorld()) {
			this.onDespawn();
			this.delete();
		}
	}
}