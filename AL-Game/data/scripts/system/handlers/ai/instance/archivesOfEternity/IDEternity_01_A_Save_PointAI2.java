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
package ai.instance.archivesOfEternity;

import com.aionemu.commons.utils.Rnd;

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

@AIName("IDEternity01Teleporter")
public class IDEternity_01_A_Save_PointAI2 extends NpcAI2
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
        		IDEternity01ASavePoint();
        	}
        }
    }
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
            case 731809:
                announceTeleporter01();
            break;
			case 731810:
                announceTeleporter02();
            break;
			case 731811:
                announceTeleporter03();
            break;
			case 731812:
                announceTeleporter04();
            break;
        }
	}
	
	private void IDEternity01ASavePoint() {
		AI2Actions.deleteOwner(IDEternity_01_A_Save_PointAI2.this);
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(281446, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
				spawn(731809, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading()); //IDEternity_01_Teleporter_01.
			break;
			case 2:
				spawn(281446, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
			    spawn(731810, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading()); //IDEternity_01_Teleporter_02.
			break;
			case 3:
				spawn(281446, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
				spawn(731811, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading()); //IDEternity_01_Teleporter_03.
			break;
			case 4:
				spawn(281446, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
				spawn(731812, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading()); //IDEternity_01_Teleporter_04.
			break;
		}
    }
	
	private void announceTeleporter01() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//ì£¼ì‹ ì?˜ ê¸°ë¡?ë³´ê´€ì†Œ ì?´ë?™ìž¥ì¹˜ê°€ í™œì„±í™” ë?˜ì—ˆìŠµë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_Teleport_MSG_01);
				}
			}
		});
	}
	private void announceTeleporter02() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//ì?¸ê°„ì?˜ ê¸°ë¡?ë³´ê´€ì†Œ ì?´ë?™ìž¥ì¹˜ê°€ í™œì„±í™” ë?˜ì—ˆìŠµë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_Teleport_MSG_02);
				}
			}
		});
	}
	private void announceTeleporter03() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//ì•„íŠ¸ë ˆì?´ì•„ì?˜ ê¸°ë¡?ë³´ê´€ì†Œ ì?´ë?™ìž¥ì¹˜ê°€ í™œì„±í™” ë?˜ì—ˆìŠµë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_Teleport_MSG_03);
				}
			}
		});
	}
	private void announceTeleporter04() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//ê·¼ì›?ì?˜ ê¸°ë¡?ë³´ê´€ì†Œ ì?´ë?™ìž¥ì¹˜ê°€ í™œì„±í™” ë?˜ì—ˆìŠµë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_Teleport_MSG_04);
				}
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}