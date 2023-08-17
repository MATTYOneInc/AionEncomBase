/*
 * This file is part of Encom.
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
package ai.worlds.iluma;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.windstreams.Location2D;
import com.aionemu.gameserver.model.templates.windstreams.WindstreamTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WINDSTREAM_ANNOUNCE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("LF6_WindStream")
public class LF6_WindStreamAI2 extends NpcAI2
{
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		Npc npc = getOwner();
		startWindStream(npc);
		announceWindPathInvasion();
        windStreamAnnounce(getOwner(), 0);
    }
	
	private void startWindStream(final Npc npc) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Npc npc2 = (Npc) spawn(210100000, 857863, 1494.05f, 1411.85f, 335.94f, (byte) 0, 0, 1);
				windStreamAnnounce(npc2, 1);
				despawnNpc(857862);
                spawn(857863, 1494.4084f, 1411.7194f, 331.51108f, (byte) 0, 1281);
                PacketSendUtility.broadcastPacket(npc2, new SM_WINDSTREAM_ANNOUNCE(1, 210100000, 302, 1));
                if (npc2 != null) {
                    npc2.getController().onDelete();
                } if (npc != null) {
                    npc.getController().onDelete();
                }
            }
        }, 5000);
    }
	
	private void announceWindPathInvasion() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The wind road will disappear in 1 minute. You need to exit the wind road.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Windpath_NOTICE_01, 3540000);
				//The wind road to the defense frigate will vanish in 30 seconds.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Windpath_Off_01, 3570000);
				//The wind road will disappear in 10 seconds. You need to exit the wind road.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Windpath_NOTICE_02, 3590000);
			}
		});
	}
	
    private void windStreamAnnounce(final Npc npc, final int state) {
        WindstreamTemplate template = DataManager.WINDSTREAM_DATA.getStreamTemplate(npc.getPosition().getMapId());
        for (Location2D wind: template.getLocations().getLocation()) {
            if (wind.getId() == 302) {
                wind.setState(state);
                break;
            }
        }
        npc.getPosition().getWorld().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_WINDSTREAM_ANNOUNCE(1, 210100000, 302, state));
            }
        });
    }
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
}