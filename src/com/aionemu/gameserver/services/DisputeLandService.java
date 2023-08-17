/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DISPUTE_LAND;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneAttributes;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rinzler (Encom)
 */ 

public class DisputeLandService
{
	private boolean active;
    private FastList<Integer> worlds = new FastList<Integer>();
	private static final int duration = CustomConfig.DISPUTE_LAND_DURATION;
	private static final Logger log = LoggerFactory.getLogger(DisputeLandService.class);
	
	private DisputeLandService() {
	}
	
	public static DisputeLandService getInstance() {
		return DisputeLandServiceHolder.INSTANCE;
	}
	
	public void initDisputeLand() {
		if (CustomConfig.DISPUTE_LAND_ENABLED) {
			log.info("[DisputeLandService] is initialized...");
		    CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (isActive()) {
                        ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
                                setActive(false);
                            }
                        }, duration * 3600 * 1000);
					}
                }
			}, CustomConfig.DISPUTE_LAND_SCHEDULE);
		}
		worlds.add(210020000); //Eltnen.
		worlds.add(210040000); //Heiron.
        worlds.add(210130000); //Inggison [Master Server].
		worlds.add(210060000); //Theobomos.
		worlds.add(220020000); //Morheim.
		worlds.add(220040000); //Beluslan.
		worlds.add(220050000); //Brusthonin.
		worlds.add(220140000); //Gelkmaros [Master Server].
		//4.7
		worlds.add(600090000); //Kaldor.
		worlds.add(600100000); //Levinshor.
		//4.8
		worlds.add(210070000); //Cygnea.
		worlds.add(220080000); //Enshar.
		//5.0
		worlds.add(210100000); //Iluma.
		worlds.add(220110000); //Norsvold.
    }
	
	public boolean isActive() {
        return active;
    }
	
	public void setActive(boolean value) {
        active = value;
        syncState();
        broadcast();
    }
	
	private void syncState() {
        for (int world: worlds) {
            if (world == 210020000 || //Eltnen.
			    world == 210040000 || //Heiron.
			    world == 210130000 || //Inggison [Master Server].
				world == 210060000 || //Theobomos.
				world == 210070000 || //Cygnea.
				world == 220020000 || //Morheim.
				world == 220040000 || //Beluslan.
				world == 220050000 || //Brusthonin.
				world == 220140000 || //Gelkmaros [Master Server].
				world == 220080000 || //Enshar.
				world == 210100000 || //Iluma.
				world == 220110000 || //Norsvold.
				world == 600090000 || //Kaldor.
				world == 600100000) { //Levinshor.
                continue;
            } if (active) {
                World.getInstance().getWorldMap(world).setWorldOption(ZoneAttributes.PVP_ENABLED);
            } else {
                World.getInstance().getWorldMap(world).removeWorldOption(ZoneAttributes.PVP_ENABLED);
            }
        }
    }
	
	private void broadcast(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DISPUTE_LAND(worlds, active));
	}
	
	private void broadcast() {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                broadcast(player);
            }
        });
    }
	
	public void onLogin(Player player) {
		broadcast(player);
	}
	
	private static class DisputeLandServiceHolder {
		private static final DisputeLandService INSTANCE = new DisputeLandService();
	}
}