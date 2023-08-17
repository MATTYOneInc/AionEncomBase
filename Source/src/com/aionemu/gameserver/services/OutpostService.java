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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.OutpostDAO;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.outpost.OutpostLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.outpost.Outpost;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Wnkrz on 27/08/2017.
 */

public class OutpostService
{
    private static final Logger log = LoggerFactory.getLogger(OutpostService.class);
	
    private final Map<Integer, Outpost<?>> active = new FastMap<Integer, Outpost<?>>().shared();
    private Map<Integer, OutpostLocation> outposts;
	
    public void initOutpostLocations() {
        log.info("[OutpostService] is initialized...");
        outposts = DataManager.OUTPOST_DATA.getOutpostLocations();
        DAOManager.getDAO(OutpostDAO.class).loadOutposLocations(outposts);
    }
	
	public void initOutposts() {
        for (OutpostLocation outpost : getOutpostLocations().values()) {
            start(outpost.getId());
        }
    }
	
	public void initOupostReset() {
        Race race = null;
        log.info("[OutpostService] initializing <Outpost Reset>");
        String weekly = "0 0 9 ? * WED *";
        CronService.getInstance().schedule(new Runnable() {
            public void run() {
                //Inggison.
                capture(101, Race.NPC);
                capture(102, Race.NPC);
                capture(103, Race.NPC);
                capture(104, Race.NPC);
                capture(105, Race.NPC);
                capture(106, Race.NPC);
                capture(107, Race.NPC);
                captureArtifact(101, Race.NPC);
                captureArtifact(102, Race.NPC);
                captureArtifact(103, Race.NPC);
                captureArtifact(104, Race.NPC);
                captureArtifact(105, Race.NPC);
                captureArtifact(106, Race.NPC);
                captureArtifact(107, Race.NPC);
                //Gelkmaros.
                capture(201, Race.NPC);
                capture(202, Race.NPC);
                capture(203, Race.NPC);
                capture(204, Race.NPC);
                capture(205, Race.NPC);
                capture(206, Race.NPC);
                capture(207, Race.NPC);
                captureArtifact(201, Race.NPC);
                captureArtifact(202, Race.NPC);
                captureArtifact(203, Race.NPC);
                captureArtifact(204, Race.NPC);
                captureArtifact(205, Race.NPC);
                captureArtifact(206, Race.NPC);
                captureArtifact(207, Race.NPC);
            }
        }, weekly);
    }
	
    public Map<Integer, OutpostLocation> getOutpostLocations() {
        return outposts;
    }
	
    public OutpostLocation getOutpostLocation(int id) {
        return outposts.get(id);
    }
	
    public void start(final int id) {
        final Outpost<?> outpost;
        synchronized (this) {
            if (active.containsKey(id)) {
                return;
            }
            outpost = new Outpost<>(getOutpostLocation(id));
            active.put(id, outpost);
        }
        outpost.start();
    }
	
    public void stop(int id) {
        if (!isActive(id)) {
            log.info("Trying to stop not active outpost:" + id);
            return;
        }
        Outpost<?> outpost;
        synchronized (this) {
            outpost = active.remove(id);
        } if (outpost == null || outpost.isFinished()) {
            log.info("Trying to stop null or finished outpost:" + id);
            return;
        }
        outpost.stop();
        start(id);
    }
	
	public void capture(int id, Race race) {
        if (!isActive(id)) {
            log.info("Detecting not active outpost capture.");
            return;
        }
        getActiveOutpost(id).setRace(race);
        stop(id);
        broadcastUpdate(getOutpostLocation(id));
        getDAO().updateLocation(getOutpostLocation(getActiveOutpost(id).getId()));
    }
	
    public void captureArtifact(int id, Race race) {
        //Capture Artifact.
        SiegeRace sr = null;
        if (race == Race.ASMODIANS) {
            sr = SiegeRace.ASMODIANS;
        } else if (race == Race.ELYOS) {
            sr = SiegeRace.ELYOS;
        } else {
            sr = SiegeRace.BALAUR;
        }
        SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(getOutpostLocation(id).getArtifactId());
        SiegeService.getInstance().deSpawnNpcs(getOutpostLocation(id).getArtifactId());
        loc.setVulnerable(false);
        loc.setUnderShield(false);
        loc.setRace(sr);
        loc.setLegionId(0);
        SiegeService.getInstance().spawnNpcs(getOutpostLocation(id).getArtifactId(), sr, SiegeModType.SIEGE);
        DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(loc);
    }
	
    public boolean isActive(int id) {
        return active.containsKey(id);
    }
	
    public Outpost<?> getActiveOutpost(int id) {
        return active.get(id);
    }
	
    public void onEnterOutpostWorld(Player player) {
        for (OutpostLocation outpostLocation : getOutpostLocations().values()) {
            if (outpostLocation.getWorldId() == player.getWorldId() && isActive(outpostLocation.getId())) {
                Outpost<?> outpost = getActiveOutpost(outpostLocation.getId());
                PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, outpost.getFlag()));
                player.getController().updateZone();
                player.getController().updateNearbyQuests();
            }
        }
    }
	
    public void broadcastUpdate(final OutpostLocation outpostLocation) {
        World.getInstance().getWorldMap(outpostLocation.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (isActive(outpostLocation.getId())) {
                    Outpost<?> outpost = getActiveOutpost(outpostLocation.getId());
                    PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, outpost.getFlag()));
                    player.getController().updateZone();
                    player.getController().updateNearbyQuests();
                }
            }
        });
    }
	
    public static OutpostService getInstance() {
        return OutpostServiceHolder.INSTANCE;
    }
	
    private static class OutpostServiceHolder {
        private static final OutpostService INSTANCE = new OutpostService();
    }
	
    private OutpostDAO getDAO() {
        return DAOManager.getDAO(OutpostDAO.class);
    }
}