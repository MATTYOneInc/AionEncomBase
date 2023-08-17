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
package ai.base;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("ldf4_advance_chief")
public class LDF4_Advance_ChiefAI2 extends AggressiveNpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleDied() {
		addGpPlayer();
		sendAdventurersBase2();
		super.handleDied();
	}
	
	private void sendAdventurersBase2() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isInGroup2() && MathUtil.isIn3dRange(player, getOwner(), 15)) {
					HTMLService.sendGuideHtml(player, "adventurers_base2");
				}
			}
		});
	}
	private void addGpPlayer() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isInGroup2() && MathUtil.isIn3dRange(player, getOwner(), 15)) {
					AbyssPointsService.addGp(player, 50);
				}
			}
		});
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 234619: //West Picket Garrison Warcaptain.
				case 234627: //West Picket Squad Warcaptain.
				case 234635: //West Picket Force Warcaptain.
				    announceBattleV01();
					checkForSupport(creature);
				break;
				case 235066: //North Picket Force Subjugator.
				case 235146: //North Picket Garrison Warcaptain.
				case 235159: //North Picket Defense Warcaptain.
				    announceBattleV02();
					checkForSupport(creature);
				break;
				case 234616: //North Outpost Garrison Warcaptain.
				case 234624: //North Outpost Squad Warcaptain.
				case 234632: //North Outpost Force Warcaptain.
				    announceBattleV03();
					checkForSupport(creature);
				break;
				case 234164: //North Relay Force Commander.
				case 234640: //North Relay Garrison Warcaptain.
				case 234643: //North Relay Squad Warcaptain.
				    announceBattleV04();
					checkForSupport(creature);
				break;
				case 234618: //East Relay Garrison Warcaptain.
				case 234626: //East Relay Squad Warcaptain.
				case 234634: //East Relay Force Warcaptain.
				    announceBattleV05();
					checkForSupport(creature);
				break;
				case 234617: //East Outpost Garrison Warcaptain.
				case 234625: //East Outpost Squad Warcaptain.
				case 234633: //East Outpost Force Warcaptain.
				    announceBattleV06();
					checkForSupport(creature);
				break;
				case 234623: //East Picket Garrison Warcaptain.
				case 234631: //East Picket Squad Warcaptain.
				case 234639: //East Picket Force Warcaptain.
				    announceBattleV07();
					checkForSupport(creature);
				break;
				case 235072: //South Picket Force Warcaptain.
				case 235152: //South Picket Garrison Warcaptain.
				case 235165: //South Picket Defense Warcaptain.
				    announceBattleV08();
					checkForSupport(creature);
				break;
				case 234620: //South Outpost Garrison Warcaptain.
				case 234628: //South Outpost Squad Warcaptain.
				case 234636: //South Outpost Force Warcaptain.
				    announceBattleV09();
					checkForSupport(creature);
				break;
				case 234622: //West Relay Garrison Warcaptain.
				case 234630: //West Relay Squad Warcaptain.
				case 234638: //West Relay Force Warcaptain.
				    announceBattleV10();
					checkForSupport(creature);
				break;
				case 234621: //West Outpost Garrison Warcaptain.
				case 234629: //West Outpost Squad Warcaptain.
				case 234637: //West Outpost Force Warcaptain.
				    announceBattleV11();
					checkForSupport(creature);
				break;
				case 234166: //South Relay Force Commander.
				case 234641: //South Relay Garrison Warcaptain.
				case 234644: //South Relay Squad Warcaptain.
				    announceBattleV12();
					checkForSupport(creature);
				break;
				case 234162: //Shrine Force Commander.
				case 234642: //Shrine Garrison Warcaptain.
				case 234645: //Shrine Squad Warcaptain.
				    announceBattleV13();
					checkForSupport(creature);
				break;
			}
		}
	}
	
	private void checkForSupport(Creature creature) {
		for (VisibleObject object: getKnownList().getKnownObjects().values()) {
			if (object instanceof Npc && isInRange(object, 30)) {
				((Npc) object).getAi2().onCreatureEvent(AIEventType.CREATURE_AGGRO, creature);
			}
		}
	}
	
	private void announceBattleV01() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v01);
			}
		});
	}
	
	private void announceBattleV02() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v02);
			}
		});
	}
	
	private void announceBattleV03() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v03);
			}
		});
	}
	
	private void announceBattleV04() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v04);
			}
		});
	}
	
	private void announceBattleV05() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v05);
			}
		});
	}
	
	private void announceBattleV06() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v06);
			}
		});
	}
	
	private void announceBattleV07() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v07);
			}
		});
	}
	
	private void announceBattleV08() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v08);
			}
		});
	}
	
	private void announceBattleV09() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v09);
			}
		});
	}
	
	private void announceBattleV10() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v10);
			}
		});
	}
	
	private void announceBattleV11() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v11);
			}
		});
	}
	
	private void announceBattleV12() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v12);
			}
		});
	}
	
	private void announceBattleV13() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_chief_v13);
			}
		});
	}
}