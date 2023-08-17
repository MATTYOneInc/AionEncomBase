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
package ai.instance.evergaleCanyon;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("IDEternity_W_Tele_G_To_OP_Up")
public class IDEternity_W_Tele_G_To_OP_UpAI2 extends NpcAI2
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
        	if (MathUtil.isIn3dRange(getOwner(), creature, 10)) {
        		if (player.getCommonData().getRace() == Race.ELYOS) {
					IDEternity_W_Tele_G_To_OP_L_Up();
					announceTele07E();
				} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
					IDEternity_W_Tele_G_To_OP_D_Up();
					announceTele08A();
				}
        	}
        }
    }
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		announceTele12();
	}
	
	private void IDEternity_W_Tele_G_To_OP_L_Up() {
		despawnNpc(835289);
		despawnNpc(835454);
		AI2Actions.deleteOwner(IDEternity_W_Tele_G_To_OP_UpAI2.this);
		spawn(835277, 719.26935f, 396.20844f, 305.75839f, (byte) 0, 256);
		spawn(835453, 719.26935f, 396.20844f, 305.75839f, (byte) 0, 306);
    }
	private void IDEternity_W_Tele_G_To_OP_D_Up() {
		despawnNpc(835277);
		despawnNpc(835453);
		AI2Actions.deleteOwner(IDEternity_W_Tele_G_To_OP_UpAI2.this);
		spawn(835289, 719.26935f, 396.20844f, 305.75839f, (byte) 0, 256);
		spawn(835454, 719.26935f, 396.20844f, 305.75839f, (byte) 0, 319);
    }
	
	private void announceTele12() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Gì?˜ ìˆœê°„ì?´ë?™ ìž¥ì¹˜ê°€ ì ?ë ¹ ê°€ëŠ¥í•©ë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_12);
				}
			}
		});
	}
	private void announceTele07E() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Gì?˜ ìˆœê°„ì?´ë?™ ìž¥ì¹˜ë¥¼ ì²œì¡±ì?´ ì ?ë ¹í•´ ì?´ìš© ê°€ëŠ¥ í•©ë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_07);
				}
			}
		});
	}
	private void announceTele08A() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Gì?˜ ìˆœê°„ì?´ë?™ ìž¥ì¹˜ë¥¼ ë§ˆì¡±ì?´ ì ?ë ¹í•´ ì?´ìš© ê°€ëŠ¥ í•©ë‹ˆë‹¤.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_08);
				}
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
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}