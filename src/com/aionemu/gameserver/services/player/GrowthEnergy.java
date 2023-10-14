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
package com.aionemu.gameserver.services.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * Created by wanke on 26/02/2017.
 */

public class GrowthEnergy
{
    private Logger log = LoggerFactory.getLogger(GrowthEnergy.class);
    private boolean dailyGenerated = true;
	
    public void init() {
        log.info("<Aura Of Growth Reset>");
        String daily = "0 0 9 1/1 * ? *";
        CronService.getInstance().schedule(new Runnable() {
            public void run() {
                dailyGenerated = false;
                updateGrowthEnergy();
            }
        }, daily);
    }
	
    private void updateGrowthEnergy() {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(final Player player) {
                player.getCommonData().setAuraOfGrowth(0);
                PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
                DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
            }
        });
    }
	
    public void onLogin(Player player) {
        if (player.getCommonData().getAuraOfGrowth() != 0) {
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        }
    }
	
    public void addGrowthEnergy(Player player) {
        PlayerCommonData pcd = player.getCommonData();
        if (pcd.isReadyForAuraOfGrowth()) {
            long auraOfGrowthpercent = pcd.getAuraOfGrowthPoints();
            pcd.addAuraOfGrowth(auraOfGrowthpercent);
            DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
            sendMessage(player);
        }
    }
	
    public void sendMessage(Player player) {
        long points = player.getCommonData().getAuraOfGrowthPoints();
        if (player.getCommonData().getAuraOfGrowth() == points) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(1));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 10) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(10));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 20) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(20));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 30) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(30));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 40) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(40));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 50) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(50));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 60) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(60));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 70) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(70));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 80) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(80));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 90) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(90));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        } else if (player.getCommonData().getAuraOfGrowth() == points * 100) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT_NORMAL(100));
            updateGrowthEnergy();
        }
    }
	
    public static GrowthEnergy getInstance() {
        return SingletonHolder.instance;
    }
	
    private static class SingletonHolder {
        protected static final GrowthEnergy instance = new GrowthEnergy();
    }
}