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
package ai.instance.fissureOfOblivion;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("Orkia_Spire_Teleport_Stone")
public class Orkia_Spire_Teleport_StoneAI2 extends NpcAI2
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
        	if (MathUtil.isIn3dRange(getOwner(), creature, 10)) {
        		OrkiaSpireTeleportStone();
        	}
        }
    }
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		announceOrkiaSpire();
	}
	
	private void OrkiaSpireTeleportStone() {
		AI2Actions.deleteOwner(Orkia_Spire_Teleport_StoneAI2.this);
		spawn(281446, 522.48053f, 573.51971f, 321.80389f, (byte) 0);
		spawn(834190, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
    }
	
	private void announceOrkiaSpire() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//You can use the Orkia Spire Teleport Stone.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDTransform_SavePoint_03);
				}
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}