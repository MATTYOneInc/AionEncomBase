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
package ai.siege;

import ai.GeneralNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO3;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_INFO;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("siege_teleporter")
public class Siege_TeleporterAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDespawned() {
		siegeTeleport(false);
		artifactTeleport(false);
		super.handleDespawned();
	}
	
	@Override
	protected void handleSpawned() {
		siegeTeleport(true);
		artifactTeleport(true);
		super.handleSpawned();
	}
	
	private void siegeTeleport(final boolean status) {
		final int id = ((SiegeNpc) getOwner()).getSiegeId();
		SiegeService.getInstance().getFortress(id).setCanTeleport(status);
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(id, status));
			}
		});
	}
	
	private void artifactTeleport(final boolean status) {
        final int id = ((SiegeNpc) getOwner()).getSiegeId();
        SiegeService.getInstance().getArtifact(id).setCanTeleport(status);
        getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(id, status));
            }
        });
    }
}