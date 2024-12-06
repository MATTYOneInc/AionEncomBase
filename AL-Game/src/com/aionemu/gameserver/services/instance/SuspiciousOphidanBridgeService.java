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
package com.aionemu.gameserver.services.instance;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import javolution.util.FastList;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class SuspiciousOphidanBridgeService {
	private static final Logger log = LoggerFactory.getLogger(SuspiciousOphidanBridgeService.class);
	private boolean registerAvailable;
	private final FastList<Integer> playersWithCooldown = FastList.newInstance();
	public static final byte minLevel = 66, capLevel = 76;
	public static final int maskId = 122;

	public void initSuspiciousOphidan() {
		if (AutoGroupConfig.OPHIDAN_WARPATH_ENABLED) {
			log.info("Ophidan Warpath 5.1");
			// Ophidan Warpath TUE-THU "11PM-00AM"
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startSuspiciousOphidanRegistration();
				}
			}, AutoGroupConfig.OPHIDAN_WARPATH_SCHEDULE_MIDNIGHT);
		}
	}

	private void startUregisterSuspiciousTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				registerAvailable = false;
				playersWithCooldown.clear();
				AutoGroupService.getInstance().unRegisterInstance(maskId);
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
				while (iter.hasNext()) {
					Player player = iter.next();
					if (player.getLevel() > minLevel) {
						int instanceMaskId = getInstanceMaskId(player);
						if (instanceMaskId > 0) {
							PacketSendUtility.sendPacket(player,
									new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon, true));
						}
					}
				}
			}
		}, AutoGroupConfig.OPHIDAN_WARPATH_TIMER * 60 * 1000);
	}

	private void startSuspiciousOphidanRegistration() {
		this.registerAvailable = true;
		startUregisterSuspiciousTask();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			if (player.getLevel() > minLevel && player.getLevel() < capLevel) {
				int instanceMaskId = getInstanceMaskId(player);
				if (instanceMaskId > 0) {
					PacketSendUtility.sendPacket(player,
							new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					// You can now participate in the Odd Ophidan Advanced Route battle.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDLDF5_Under_02_War);
				}
			}
		}
	}

	public boolean isSuspiciousAvailable() {
		return this.registerAvailable;
	}

	public byte getInstanceMaskId(Player player) {
		int level = player.getLevel();
		if (level < minLevel || level >= capLevel) {
			return 0;
		}
		return maskId;
	}

	public void addCoolDown(Player player) {
		this.playersWithCooldown.add(player.getObjectId());
	}

	public boolean hasCoolDown(Player player) {
		return this.playersWithCooldown.contains(player.getObjectId());
	}

	public void showWindow(Player player, byte instanceMaskId) {
		if (getInstanceMaskId(player) != instanceMaskId) {
			return;
		}
		if (!this.playersWithCooldown.contains(player.getObjectId())) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId));
		}
	}

	private static class SingletonHolder {
		protected static final SuspiciousOphidanBridgeService instance = new SuspiciousOphidanBridgeService();
	}

	public static SuspiciousOphidanBridgeService getInstance() {
		return SingletonHolder.instance;
	}
}