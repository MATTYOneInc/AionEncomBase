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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Rolandas
 */
public class CollisionDieActor extends AbstractCollisionObserver implements IActor
{
	private boolean isEnabled = true;
	
	public CollisionDieActor(Creature creature, Spatial geometry) {
		super(creature, geometry, CollisionIntention.MATERIAL.getId());
	}
	
	@Override
	public void setEnabled(boolean enable) {
		isEnabled = enable;
	}
	
	@Override
	public void onMoved(CollisionResults collisionResults) {
		if (isEnabled && collisionResults.size() != 0) {
			if (GeoDataConfig.GEO_MATERIALS_SHOWDETAILS && creature instanceof Player) {
				Player player = (Player) creature;
			}
			act();
		}
	}
	
	@Override
	public void act() {
		if (isEnabled) {
			creature.getController().die();
		}
	}
	
	@Override
	public void abort() {
	}
}