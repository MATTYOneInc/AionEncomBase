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
package com.aionemu.gameserver.services.territory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.LegionDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionTerritory;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STONESPEAR_SIEGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TERRITORY_LIST;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class TerritoryService
{
    private TerritoryBuff territoryBuff;
    private FastMap<Integer, TerritoryBuff> buffs = new FastMap<Integer, TerritoryBuff>();
    private TreeMap<Integer, LegionTerritory> territories = new TreeMap<Integer, LegionTerritory>();
    private TreeMap<Integer, TreeMap<Integer, WorldPosition>> teleporters = new TreeMap<Integer, TreeMap<Integer, WorldPosition>>();
	
    public void initTerritory() {
        LegionService ls = LegionService.getInstance();
        Collection<Legion> legions = new ArrayList<Legion>();
        int counter = 0;
        for (int i = 1; i <= 6; i++) {
            territories.put(i, new LegionTerritory(i));
        } for (Integer legionId : DAOManager.getDAO(LegionDAO.class).getLegionIdsWithTerritories()) {
            legions.add(ls.getLegion(legionId));
        } for (Legion legion : legions) {
            LegionTerritory territory = legion.getTerritory();
            territories.remove(territory.getId());
            territories.put(territory.getId(),territory);
            counter++;
        }
    }
	
    public void onTeleport(Player player, int npcid) {
        if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0) {
            return;
		}
        int territoryId = player.getLegion().getTerritory().getId();
        TreeMap<Integer, WorldPosition> teleportMap = teleporters.get(territoryId);
        WorldPosition pos = null;
        if (teleportMap.containsKey(npcid)) {
            pos = teleportMap.get(npcid);
		} if (pos != null) {
            TeleportService2.teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
		}
    }
	
    public void onEnterWorld(Player player) {
        PacketSendUtility.sendPacket(player, new SM_TERRITORY_LIST(territories.values()));
    }
	
    public void sendStoneSpearPacket(Player player) {
        //PacketSendUtility.sendPacket(player, new SM_STONESPEAR_SIEGE(player.getLegion(), 0));
    }
	
    public void onEnterTerritory(Player player) {
        if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0) {
            return;
		}
        territoryBuff = new TerritoryBuff();
        territoryBuff.applyEffect(player);
        buffs.put(player.getObjectId(), territoryBuff);
    }
	
    public void onLeaveTerritory(Player player) {
        if (player.getLegion() == null || player.getLegion().getTerritory().getId() == 0) {
            return;
		} if (buffs.containsKey(player.getObjectId())) {
            buffs.get(player.getObjectId()).endEffect(player);
            buffs.remove(player.getObjectId());
        }
    }
	
    public void scanForIntruders(Player player) {
        Collection<Player> players = new ArrayList<Player>();
        Iterator<Player> playerIt = World.getInstance().getPlayersIterator();
        while (playerIt.hasNext()) {
            Player enemy = playerIt.next();
            if (player.getWorldId() == enemy.getWorldId() && player.getRace() != enemy.getRace()) {
                players.add(enemy);
			}
        }
        //PacketSendUtility.sendPacket(player, new SM_SERIAL_KILLER(players, false));
    }
	
    public void onConquerTerritory(Legion legion, int id) {
        if (legion.ownsTerretory()) {
            onLooseTerritory(legion);
        }
        LegionTerritory territory = new LegionTerritory(id);
        territory.setLegionId(legion.getLegionId());
        territory.setLegionName(legion.getLegionName());
        legion.setTerritory(territory);
        territories.remove(id);
        territories.put(id,territory);
        broadcastTerritoryList(territories);
        broadcastToLegion(legion);
    }
	
    private void broadcastToLegion(Legion legion) {
        PacketSendUtility.broadcastPacketToLegion(legion , new SM_LEGION_INFO(legion));
        PacketSendUtility.broadcastPacketToLegion(legion , new SM_STONESPEAR_SIEGE(legion, 0));
    }
	
    public void onLooseTerritory(Legion legion) {
        int oldTerritoryId = legion.getTerritory().getId();
        legion.clearTerritory();
        if (oldTerritoryId == 0) {
		}
        LegionTerritory fakeTerritory = new LegionTerritory(oldTerritoryId);
        territories.remove(oldTerritoryId);
        territories.put(oldTerritoryId, fakeTerritory);
        TreeMap<Integer,LegionTerritory> lostTerr = new TreeMap<Integer,LegionTerritory>();
        lostTerr.put(oldTerritoryId, fakeTerritory);
        broadcastTerritoryList(lostTerr);
        broadcastToLegion(legion);
    }
	
    public void broadcastTerritoryList(TreeMap<Integer,LegionTerritory> terr) {
        Collection<Player> players = World.getInstance().getAllPlayers();
        for (Player player: players) {
            if (!player.isOnline()) {
                return;
			}
            PacketSendUtility.sendPacket(player, new SM_TERRITORY_LIST(terr.values()));
        }
    }
	
    public Collection<LegionTerritory> getTerritories() {
        return territories.values();
    }
	
    public static TerritoryService getInstance() {
        return TerritoryService.SingletonHolder.instance;
    }
	
    private static class SingletonHolder {
        protected static final TerritoryService instance = new TerritoryService();
    }
}