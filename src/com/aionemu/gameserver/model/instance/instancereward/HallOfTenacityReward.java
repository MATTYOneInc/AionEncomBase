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
package com.aionemu.gameserver.model.instance.instancereward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.instanceposition.GenerealInstancePosition;
import com.aionemu.gameserver.model.instance.instanceposition.HallOfTenacityInstancePosition;
import com.aionemu.gameserver.model.instance.playerreward.HallOfTenacityPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * @author Ranastic
 */
public class HallOfTenacityReward extends InstanceReward<HallOfTenacityPlayerReward> {

	private final Logger log = LoggerFactory.getLogger(HallOfTenacityReward.class);
	protected WorldMapInstance instance;
	private long instanceTime;
	private final byte buffId;
	private Point3D myBattlePosition;
	private Point3D opponentBattlePosition;
	private int bonusTime;
	private GenerealInstancePosition instancePosition;

	public HallOfTenacityReward(Integer mapId, int instanceId, WorldMapInstance instance) {
		super(mapId, instanceId);
		this.instance = instance;
		bonusTime = 0;
		buffId = 0;
		instancePosition = new HallOfTenacityInstancePosition();
		instancePosition.initsialize(mapId, instanceId);
	}

	public int AbyssReward(boolean isWin) {
		int Win = 3163;
		int Loss = 1031;
		return isWin ? Win : Loss;
	}

	public int GloryReward(boolean isWin) {
		int Win = 150;
		int Loss = 30;
		return isWin ? Win : Loss;
	}

	public int ExpReward(boolean isWin) {
		int Win = 10000;
		int Loss = 5000;
		return isWin ? Win : Loss;
	}

	public void setStartPositions() {
		Point3D my = new Point3D(256.12454f, 292.78516f, 74.00548f); // Zone A
		Point3D opponent = new Point3D(256.00023f, 219.35153f, 73.99652f); // Zone B
		myBattlePosition = my;
		opponentBattlePosition = opponent;
	}

	public void portToArena(Player player) {
		if (player.getHOTVSId() == 0) {
			TeleportService2.teleportTo(player, 302310000, instanceId, myBattlePosition.getX(), myBattlePosition.getY(),
					myBattlePosition.getZ());// TODO you and opponent pos
		} else if (player.getHOTVSId() == 1) {
			TeleportService2.teleportTo(player, 302310000, instanceId, opponentBattlePosition.getX(),
					opponentBattlePosition.getY(), opponentBattlePosition.getZ());// TODO you and opponent pos
		}
	}

	public void portToHall(Player player) {
		regPlayerReward(player.getObjectId());
		HallOfTenacityPlayerReward playerReward = getPlayerReward(player.getObjectId());
		playerReward.setPosition(1);
		if (player.getRace() == Race.ASMODIANS) {
			playerReward.setZone(0);
		} else if (player.getRace() == Race.ELYOS) {
			playerReward.setZone(1);
		}
		instancePosition.port(player, playerReward.getZone(), playerReward.getPosition());
	}

	@Override
	public void clear() {
		super.clear();
	}

	public void regPlayerReward(Integer object) {
		if (!containPlayer(object)) {
			addPlayerReward(new HallOfTenacityPlayerReward(object, bonusTime, buffId));
		}
	}

	@Override
	public void addPlayerReward(HallOfTenacityPlayerReward reward) {
		super.addPlayerReward(reward);
	}

	@Override
	public HallOfTenacityPlayerReward getPlayerReward(Integer object) {
		return (HallOfTenacityPlayerReward) super.getPlayerReward(object);
	}

	public FastList<Player> getPlayersInside() {
		FastList<Player> players = new FastList<Player>();
		for (Player playerInside : instance.getPlayersInside()) {
			if (containPlayer(playerInside.getObjectId())) {
				players.add(playerInside);
			}
		}
		return players;
	}

	public void sendPacket(final int type, final Integer object) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player,
						new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
			}
		});
	}

	public void sendPacket() {
		final List<Player> players = instance.getPlayersInside();
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), getInstanceReward(), players));
			}
		});
	}

	public int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 90000) {
			return (int) (90000 - result);
		} else if (result < 1800000) { // 30-Mins
			return (int) (1800000 - (result - 90000));
		}
		return 0;
	}

	public byte getBuffId() {
		return buffId;
	}

	public void setInstanceStartTime() {
		this.instanceTime = System.currentTimeMillis();
	}

	// TODO VSId
	public void setCoupleSlotForBattle32() {
		int size = 15;// 32/2=16 (packet first slot count from 0 to 15)
		ArrayList<Integer> containRandomPlayerCoupleSlots = new ArrayList<Integer>();
		ArrayList<Integer> totalCoupleSlots = new ArrayList<Integer>(size);

		ArrayList<Player> totalPlayer = new ArrayList<Player>(getPlayersInside());
		ArrayList<Player> matchLeft = new ArrayList<Player>();
		ArrayList<Player> matchRight = new ArrayList<Player>();

		// do shuffle players
		Collections.shuffle(totalPlayer);

		// sort all players into left or right match
		for (Player entry : totalPlayer) {
			if (matchLeft.size() > matchRight.size())
				matchRight.add(entry);
			else
				matchLeft.add(entry);
		}

		// sort couple slot
		for (int i = 0; i <= size; i++) {
			totalCoupleSlots.add(i);
		}

		// do random player couple slot
		Random rand = new Random();
		while (totalCoupleSlots.size() > 0) {
			int index = rand.nextInt(totalCoupleSlots.size());
			containRandomPlayerCoupleSlots.add(totalCoupleSlots.remove(index));
		}

		// left matching
		Iterator<Player> iterLeft = matchLeft.iterator();
		while (iterLeft.hasNext()) {
			Player player1 = iterLeft.next();
			Player player2 = iterLeft.hasNext() ? iterLeft.next() : player1;

			int rnds = rand.nextInt(containRandomPlayerCoupleSlots.size());
			int coupleId = containRandomPlayerCoupleSlots.remove(rnds);

			player1.setHOTCoupleId(coupleId);
			player2.setHOTCoupleId(coupleId);

			player1.setHOTVSId(0);
			player2.setHOTVSId(1);

			player1.setHOTMyOpponentObjId(player2.getObjectId());
			player2.setHOTMyOpponentObjId(player1.getObjectId());
			log.info("LEFT player1:" + player1.getName() + " player2:" + player2.getName());
		}

		// right matching
		Iterator<Player> iterRight = matchRight.iterator();
		while (iterRight.hasNext()) {
			Player player1 = iterRight.next();
			Player player2 = iterRight.hasNext() ? iterRight.next() : player1;

			int rnds = rand.nextInt(containRandomPlayerCoupleSlots.size());
			int coupleId = containRandomPlayerCoupleSlots.remove(rnds);

			player1.setHOTCoupleId(coupleId);
			player2.setHOTCoupleId(coupleId);

			player1.setHOTVSId(0);
			player2.setHOTVSId(1);

			player1.setHOTMyOpponentObjId(player2.getObjectId());
			player2.setHOTMyOpponentObjId(player1.getObjectId());
			log.info("RIGHT player1:" + player1.getName() + " player2:" + player2.getName());
		}
	}
}