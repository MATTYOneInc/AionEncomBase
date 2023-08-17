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
package ai.worlds.panesterra;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("teleport_district")
public class District_Group_TeleportAI2 extends NpcAI2
{
	@Override
    protected void handleCreatureSee(Creature creature) {
        checkDistance(this, creature);
    }
	
    @Override
    protected void handleCreatureMoved(Creature creature) {
        checkDistance(this, creature);
    }
	
	private void checkDistance(NpcAI2 ai, Creature creature) {
        if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
        	final Player player = (Player) creature;
			if (MathUtil.isIn3dRange(getOwner(), creature, 5)) {
        		sendDistrictGroupRequest(player);
        	}
        }
    }
	
	public void sendDistrictGroupRequest(final Player player) {
        String message = "Accept use District Group Teleport ?";
        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
				switch (getNpcId()) {
					case 833268:
						switch (responder.getWorldId()) {
						    case 400020000: //Belus.
							    TeleportService2.teleportTo(responder, 400020000, 240.24854f, 1610.5688f, 1463.8889f, (byte) 86); //[Sylvan District]
							break;
							case 400040000: //Aspida.
							    TeleportService2.teleportTo(responder, 400040000, 240.24854f, 1610.5688f, 1463.8889f, (byte) 86); //[Nebulum District]
							break;
							case 400050000: //Atanatos.
							    TeleportService2.teleportTo(responder, 400050000, 240.24854f, 1610.5688f, 1463.8889f, (byte) 86); //[Bronzium District]
							break;
							case 400060000: //Disillon.
							    TeleportService2.teleportTo(responder, 400060000, 240.24854f, 1610.5688f, 1463.8889f, (byte) 86); //[Divinatum District]
							break;
						}
					break;
					case 833269:
						switch (responder.getWorldId()) {
						    case 400020000: //Belus.
							    TeleportService2.teleportTo(responder, 400020000, 1609.0776f, 1810.1611f, 1463.9395f, (byte) 112); //[Heirloom District]
							break;
							case 400040000: //Aspida.
							    TeleportService2.teleportTo(responder, 400040000, 1609.0776f, 1810.1611f, 1463.9395f, (byte) 112); //[Blaekmor District]
							break;
							case 400050000: //Atanatos.
							    TeleportService2.teleportTo(responder, 400050000, 1609.0776f, 1810.1611f, 1463.9395f, (byte) 112); //[Aureus District]
							break;
							case 400060000: //Disillon.
							    TeleportService2.teleportTo(responder, 400060000, 1609.0776f, 1810.1611f, 1463.9395f, (byte) 112); //[Fulminaer District]
							break;
						}
					break;
					case 833270:
						switch (responder.getWorldId()) {
							case 400020000: //Belus.
							    TeleportService2.teleportTo(responder, 400020000, 1812.733f, 431.72452f, 1463.8632f, (byte) 85); //[Vernalium District]
							break;
							case 400040000: //Aspida.
							    TeleportService2.teleportTo(responder, 400040000, 1812.733f, 431.72452f, 1463.8632f, (byte) 85); //[Myrkin District]
							break;
							case 400050000: //Atanatos.
							    TeleportService2.teleportTo(responder, 400050000, 1812.733f, 431.72452f, 1463.8632f, (byte) 85); //[Cyprian District]
							break;
							case 400060000: //Disillon.
							    TeleportService2.teleportTo(responder, 400060000, 1812.733f, 431.72452f, 1463.8632f, (byte) 85); //[Thonderen District]
							break;
						}
					break;
					case 833271:
						switch (responder.getWorldId()) {
						    case 400020000: //Belus.
							    TeleportService2.teleportTo(responder, 400020000, 438.7579f, 234.54915f, 1464.1918f, (byte) 58); //[Evergreen District]
							break;
							case 400040000: //Aspida.
							    TeleportService2.teleportTo(responder, 400040000, 438.7579f, 234.54915f, 1464.1918f, (byte) 58); //[Shaedwian District]
							break;
							case 400050000: //Atanatos.
							    TeleportService2.teleportTo(responder, 400050000, 438.7579f, 234.54915f, 1464.1918f, (byte) 58); //[Braesen District]
							break;
							case 400060000: //Disillon.
							    TeleportService2.teleportTo(responder, 400060000, 438.7579f, 234.54915f, 1464.1918f, (byte) 58); //[Severus District]
							break;
						}
					break;
				}
            }
            @Override
            public void denyRequest(Creature requester, Player responder) {
            }
        };
        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, message));
        }
    }
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}