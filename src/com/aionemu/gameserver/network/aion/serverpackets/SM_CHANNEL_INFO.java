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

import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.world.WorldPosition;

public class SM_CHANNEL_INFO extends AionServerPacket
{
	int instanceCount = 0;
	int currentChannel = 0;
	
	public SM_CHANNEL_INFO(WorldPosition position) {
		WorldMapTemplate template = position.getWorldMapInstance().getTemplate();
        if (position.getWorldMapInstance().isBeginnerInstance()) {
            this.instanceCount = template.getBeginnerTwinCount();
            if (WorldConfig.WORLD_EMULATE_A_STATION) {
                this.instanceCount += template.getTwinCount();
            }
            this.currentChannel = position.getInstanceId() - 1;
        } else {
            this.instanceCount = template.getTwinCount();
            if (WorldConfig.WORLD_EMULATE_A_STATION) {
                this.instanceCount += template.getBeginnerTwinCount();
            }
            this.currentChannel = position.getInstanceId() - 1;
        }
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(currentChannel);
		writeD(instanceCount);
	}
}