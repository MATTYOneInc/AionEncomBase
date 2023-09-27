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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class DredgionService2
{
	private static final Logger log = LoggerFactory.getLogger(DredgionService2.class);
	
	private boolean registerAvailable;
	private FastList<Integer> playersWithCooldown = new FastList<Integer>();
	private SM_AUTO_GROUP[] autoGroupUnreg, autoGroupReg;
	private final byte maskLvlGradeC = 1, maskLvlGradeB = 2, maskLvlGradeA = 3;
	public static final byte minLevel = 46, capLevel = 61;
	
	public DredgionService2() {
		this.autoGroupUnreg = new SM_AUTO_GROUP[this.maskLvlGradeA + 1];
		this.autoGroupReg = new SM_AUTO_GROUP[this.autoGroupUnreg.length];
		for (byte i = this.maskLvlGradeC; i <= this.maskLvlGradeA; i++) {
			this.autoGroupUnreg[i] = new SM_AUTO_GROUP(i, SM_AUTO_GROUP.wnd_EntryIcon, true);
			this.autoGroupReg[i] = new SM_AUTO_GROUP(i, SM_AUTO_GROUP.wnd_EntryIcon);
		}
	}
	
	public void initDredgion() {
		if (AutoGroupConfig.DREDGION_ENABLED) {
			log.info("[Baranath/Chantra/Terath] Dredgion");
			//Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "12PM-1PM"
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startDredgionRegistration();
				}
			}, AutoGroupConfig.DREDGION_SCHEDULE_MIDDAY);
			//Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "8PM-9PM"
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startDredgionRegistration();
				}
			}, AutoGroupConfig.DREDGION_SCHEDULE_EVENING);
			//Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "23PM-0AM"
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startDredgionRegistration();
				}
			}, AutoGroupConfig.DREDGION_SCHEDULE_MIDNIGHT);
		}
	}

	private void startUregisterDredgionTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				registerAvailable = false;
				playersWithCooldown.clear();
				AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeA);
				AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeB);
				AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeC);
				Iterator<Player> iter = World.getInstance().getPlayersIterator();
				while (iter.hasNext()) {
					Player player = iter.next();
					if (player.getLevel() > minLevel) {
						int instanceMaskId = getInstanceMaskId(player);
						if (instanceMaskId > 0) {
							PacketSendUtility.sendPacket(player, DredgionService2.this.autoGroupUnreg[instanceMaskId]);
						}
					}
				}
			}
		}, AutoGroupConfig.DREDGION_TIMER * 60 * 1000);
	}
	
	private void startDredgionRegistration() {
		this.registerAvailable = true;
		startUregisterDredgionTask();
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			Player player = iter.next();
			if (player.getLevel() > minLevel && player.getLevel() < capLevel) {
				int instanceMaskId = getInstanceMaskId(player);
				if (instanceMaskId > 0) {
					PacketSendUtility.sendPacket(player, this.autoGroupReg[instanceMaskId]);
					switch (instanceMaskId) {
						case maskLvlGradeC:
							//An infiltration route into the Dredgion is open.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDAB1_DREADGION);
						break;
						case maskLvlGradeB:
						    //An infiltration passage into the Chantra Dredgion has opened.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDDREADGION_02);				
						break;
						case maskLvlGradeA:
							//An infiltration passage into the Terath Dredgion has opened.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDDREADGION_03);
						break;
					}
				}
			}
		}
	}
	
	public boolean isDredgionAvailable() {
		return this.registerAvailable;
	}
	
	public byte getInstanceMaskId(Player player) {
        int level = player.getLevel();
        if (level < minLevel || level >= capLevel) {
            return 0;
        } if (level < 51) {
            return this.maskLvlGradeC;
        } else if (level < 56) {
            return this.maskLvlGradeB;
        } else {
            return this.maskLvlGradeA;
        }
    }
	
	public void addCoolDown(Player player) {
        this.playersWithCooldown.add(player.getObjectId());
    }
	
    public boolean hasCoolDown(Player player) {
        return this.playersWithCooldown.contains(player.getObjectId());
    }
	
    public void showWindow(Player player, int instanceMaskId) {
        if (getInstanceMaskId(player) != instanceMaskId) {
            return;
        } if (!this.playersWithCooldown.contains(player.getObjectId())) {
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId));
        }
    }
	
	private static class SingletonHolder {
		protected static final DredgionService2 instance = new DredgionService2();
	}
	
	public static DredgionService2 getInstance() {
		return SingletonHolder.instance;
	}
}