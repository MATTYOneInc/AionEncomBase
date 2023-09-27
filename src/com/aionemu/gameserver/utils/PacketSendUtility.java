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
package com.aionemu.gameserver.utils;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.SiegeZoneInstance;

public class PacketSendUtility
{
	public static void sendMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.GOLDEN_YELLOW));
	}
	public static void sendWhiteMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}
	public static void sendWhiteMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE_CENTER));
	}
	public static void sendYellowMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW));
	}
	public static void sendYellowMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW_CENTER));
	}
	public static void sendBrightYellowMessage(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW));
	}
	public static void sendBrightYellowMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW_CENTER));
	}
	public static void sendSys1Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.GROUP_LEADER));
	}
	public static void sendSys2Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}
	public static void sendSys3Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COMMAND));
	}
	public static void sendSys4Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.LEGION));
	}
	public static void sendSys5Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COALITION));
	}
	public static void sendSys6Message(Player player, String sender, String msg) {
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.LEAGUE));
	}
	public static void sendWarnMessageOnCenter(Player player, String msg) {
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.LEAGUE_ALERT));
	}

	public static void sendPacket(Player player, AionServerPacket packet) {
		if (player.getClientConnection() != null) {
			player.getClientConnection().sendPacket(packet);
		}
	}

	/**
	 * Player Send Packet
	 */
	public static void playerSendPacketTime(final Player player, final AionServerPacket packet, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (player.getClientConnection() != null) {
					player.getClientConnection().sendPacket(packet);
				}
			}
		}, time);
	}

	/**
	 * Npc Send Packet
	 */
	public static void npcSendPacketTime(final Npc npc, final AionServerPacket packet, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				npc.getKnownList().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, packet);
						}
					}
				});
			}
		}, time);
	}

	public static void sendMessageTime(final Player player, final String message, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendMessage(player, message);
						}
					}
				});
			}
		}, time);
	}

	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf) {
		if (toSelf) {
			sendPacket(player, packet);
		}
		broadcastPacket(player, packet);
	}

	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet) {
		if (visibleObject instanceof Player) {
			sendPacket((Player) visibleObject, packet);
		}
		broadcastPacket(visibleObject, packet);
	}

	public static void broadcastPacket(VisibleObject visibleObject, final AionServerPacket packet) {
		visibleObject.getKnownList().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					sendPacket(player, packet);
				}
			}
		});
	}

	public static void broadcastPacket(Player player, final AionServerPacket packet, boolean toSelf, final ObjectFilter<Player> filter) {
		if (toSelf) {
			sendPacket(player, packet);
		}
		player.getKnownList().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player object) {
				if (filter.acceptObject(object)) {
					sendPacket(object, packet);
				}
			}
		});
	}

	public static void broadcastPacket(final VisibleObject visibleObject, final AionServerPacket packet, final int distance) {
		visibleObject.getKnownList().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player p) {
				if (MathUtil.isIn3dRange(visibleObject, p, distance)) {
					sendPacket(p, packet);
				}
			}
		});
	}

	public static void broadcastFilteredPacket(final AionServerPacket packet, final ObjectFilter<Player> filter) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				if (filter.acceptObject(object)) {
					sendPacket(object, packet);
				}
			}
		});
	}

	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet) {
		for (Player onlineLegionMember : legion.getOnlineLegionMembers()) {
			sendPacket(onlineLegionMember, packet);
		}
	}

	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId) {
		for (Player onlineLegionMember : legion.getOnlineLegionMembers()) {
			if (onlineLegionMember.getObjectId() != playerObjId) {
				sendPacket(onlineLegionMember, packet);
			}
		}
	}

	public static void broadcastPacketToZone(SiegeZoneInstance zone, final AionServerPacket packet) {
		zone.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				sendPacket(player, packet);
			}
		});
	}
}