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
package com.aionemu.gameserver.model.siege;

import java.util.List;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLegionReward;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLocationTemplate;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeReward;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author Source
 */
public class FortressLocation extends SiegeLocation {

	protected List<SiegeReward> siegeRewards;
	protected List<SiegeLegionReward> siegeLegionRewards;
	protected boolean isUnderShield;
    protected boolean isUnderAssault;
	protected boolean isCanTeleport;

	public FortressLocation() {
	}

	public FortressLocation(SiegeLocationTemplate template) {
		super(template);
		this.siegeRewards = template.getSiegeRewards() != null ? template.getSiegeRewards() : null;
		this.siegeLegionRewards = template.getSiegeLegionRewards() != null ? template.getSiegeLegionRewards() : null;
	}

	public List<SiegeReward> getReward() {
		return this.siegeRewards;
	}

	public List<SiegeLegionReward> getLegionReward() {
		return this.siegeLegionRewards;
	}

	/**
	 * @return isEnemy
	 */
	public boolean isEnemy(Creature creature) {
    return creature.getRace().getRaceId() != getRace().getRaceId();
    }

	/**
	 * @return isUnderShield
	 */
	@Override
	public boolean isUnderShield() {
		return this.isUnderShield;
	}

	/**
	 * @param value new undershield value
	 */
	@Override
	public void setUnderShield(boolean value) {
		this.isUnderShield = value;
	}

	/**
	 * @return isCanTeleport
	 */
	@Override
	public boolean isCanTeleport(Player player) {
	    if (player == null)
      return isCanTeleport;
		return isCanTeleport && player.getRace().getRaceId() == getRace().getRaceId();
	}

	/**
	 * @param status Teleportation status
	 */
	@Override
	public void setCanTeleport(boolean status) {
		this.isCanTeleport = status;
	}

	/**
	 * @return DescriptionId object with fortress name
	 */
	public DescriptionId getNameAsDescriptionId() {
		return new DescriptionId(template.getNameId());
	}

	public void onEnterZone(Creature creature, ZoneInstance zone) {
    super.onEnterZone(creature, zone);
    if (isVulnerable())
      creature.setInsideZoneType(ZoneType.SIEGE);
    }
  
	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		super.onLeaveZone(creature, zone);
		if (this.isVulnerable())
			creature.unsetInsideZoneType(ZoneType.SIEGE);
	}

	public void clearLocation() {
		for (Creature creature: getCreatures().values()) {
			if ((isEnemy(creature)) && ((creature instanceof Kisk))) {
				Kisk kisk = (Kisk)creature;
				kisk.getController().die();
			}
		}
    }
}