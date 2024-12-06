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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author Sweetkr
 */
public class SM_TARGET_IMMOBILIZE extends AionServerPacket {

	private Creature creature;

	public SM_TARGET_IMMOBILIZE(Creature creature) {
		this.creature = creature;
	}

    //modified (Aion Reconstruction Project - Yoress) - Added geoZ check for non player entities to avoid floating mobs.
    //and update check to mob altitude when they are stunned (mobs should appear to float in the air less often).
	@Override
	protected void writeImpl(AionConnection con) {
		if (!(creature instanceof Player)) {
			if (GeoDataConfig.GEO_ENABLE && creature.getGameStats().checkGeoNeedUpdate()) {
				float z = GeoService.getInstance().getZ(creature.getWorldId(), creature.getX(), creature.getY(), creature.getZ(), 0.0F, creature.getInstanceId());
				creature.setXYZH(null, null, z, null);
			}
		}
		PacketLoggerService.getInstance().logPacketSM(this.getPacketName());
		writeD(creature.getObjectId());
		writeF(creature.getX());
		writeF(creature.getY());
		writeF(creature.getZ());
		writeC(creature.getHeading());
	}
}