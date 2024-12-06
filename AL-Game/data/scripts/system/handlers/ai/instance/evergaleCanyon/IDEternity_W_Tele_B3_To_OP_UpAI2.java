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

@AIName("IDEternity_W_Tele_B3_To_OP_Up")
public class IDEternity_W_Tele_B3_To_OP_UpAI2 extends NpcAI2
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
					IDEternity_W_Tele_B3_To_OP_L_Up();
					announceTele01E();
				} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
					IDEternity_W_Tele_B3_To_OP_D_Up();
					announceTele02A();
				}
        	}
        }
    }
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		announceTele09();
	}
	
	private void IDEternity_W_Tele_B3_To_OP_L_Up() {
		despawnNpc(835290);
		despawnNpc(835454);
		AI2Actions.deleteOwner(IDEternity_W_Tele_B3_To_OP_UpAI2.this);
		spawn(835278, 746.86560f, 850.73126f, 347.88959f, (byte) 0, 257);
		spawn(835453, 746.86560f, 850.73126f, 347.88959f, (byte) 0, 323);
    }
	private void IDEternity_W_Tele_B3_To_OP_D_Up() {
		despawnNpc(835278);
		despawnNpc(835453);
		AI2Actions.deleteOwner(IDEternity_W_Tele_B3_To_OP_UpAI2.this);
		spawn(835290, 746.86560f, 850.73126f, 347.88959f, (byte) 0, 257);
		spawn(835454, 746.86560f, 850.73126f, 347.88959f, (byte) 0, 327);
    }
	
	private void announceTele09() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//B3�?� 순간�?��?� 장치가 �?령 가능합니다.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_09);
				}
			}
		});
	}
	private void announceTele01E() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//B3�?� 순간�?��?� 장치를 천족�?� �?령해 �?�용 가능 합니다.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_01);
				}
			}
		});
	}
	private void announceTele02A() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//B3�?� 순간�?��?� 장치를 마족�?� �?령해 �?�용 가능 합니다.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_War_tele_02);
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