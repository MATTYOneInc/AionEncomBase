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
package com.aionemu.gameserver.services.outpost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.outpost.OutpostNpc;
import com.aionemu.gameserver.model.outpost.OutpostLocation;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.outpostspawns.OutpostSpawnTemplate;
import com.aionemu.gameserver.services.OutpostService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * Created by Wnkrz on 27/08/2017.
 */

public class Outpost <OL extends OutpostLocation>
{
    private Npc boss, flag;
    private boolean started;
    private final OL outpostLocation;
    private Future<?> startAssault, stopAssault;
    private List<Race> list = new ArrayList<Race>();
    private List<Npc> spawned = new ArrayList<Npc>();
    private List<Npc> attackers = new ArrayList<Npc>();
    private final AtomicBoolean finished = new AtomicBoolean();
    private final OutpostBossDeathListener baseBossDeathListener = new OutpostBossDeathListener(this);
	
    public Outpost(OL outpostLocation) {
        list.add(Race.ASMODIANS);
        list.add(Race.ELYOS);
        list.add(Race.NPC);
        this.outpostLocation = outpostLocation;
    }
	
    public final void start() {
        boolean doubleStart = false;
        synchronized (this) {
            if (started) {
                doubleStart = true;
            } else {
                started = true;
            }
        } if (doubleStart) {
            return;
        }
        spawn();
    }
	
    public final void stop() {
        if (finished.compareAndSet(false, true)) {
            despawn(getId());
        }
    }
	
    private List<SpawnGroup2> getOutpostSpawns() {
        List<SpawnGroup2> spawns = DataManager.SPAWNS_DATA2.getOutpostSpawnsByLocId(getId());
        if (spawns == null) {
        }
        return spawns;
    }
	
    protected void spawn() {
        for (SpawnGroup2 group : getOutpostSpawns()) {
            for (SpawnTemplate spawn : group.getSpawnTemplates()) {
                final OutpostSpawnTemplate template = (OutpostSpawnTemplate) spawn;
                if (template.getOutpostRace().equals(getOutpostLocation().getRace())) {
                    if (template.getHandlerType() == null) {
                        Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
                        NpcTemplate npcTemplate = npc.getObjectTemplate();
                        if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG)) {
                            setFlag(npc);
                        }
                        getSpawned().add(npc);
                    }
                }
            }
        }
    }
	
    public boolean isAttacked() {
        for (Npc attacker : getAttackers()) {
            if (!attacker.getLifeStats().isAlreadyDead()) {
                return true;
            }
        }
        return false;
    }
	
    protected void despawn(int outpostLocationId) {
        setFlag(null);
        Collection<OutpostNpc> outpostNpcs = World.getInstance().getLocalOutpostNpcs(outpostLocationId);
        for (OutpostNpc npc : outpostNpcs) {
            npc.getController().onDelete();
        }
    }
	
    private void delayedAssault() {
        startAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                chooseAttackersRace();
            }
        }, Rnd.get(120, 180) * 60000);
    }
	
    protected void chooseAttackersRace() {
        AtomicBoolean next = new AtomicBoolean(Math.random() < 0.5);
        for (Race race : list) {
            if (!race.equals(getRace())) {
                if (next.compareAndSet(true, false)) {
                    continue;
                }
                spawnAttackers(race);
            }
        }
    }
	
    public void spawnAttackers(Race race) {
        if (getFlag() == null) {
        } else if (!getFlag().getPosition().getMapRegion().isMapRegionActive()) {
            if (Math.random() < 0.5) {
                OutpostService.getInstance().capture(getId(), race);
                OutpostService.getInstance().captureArtifact(getId(), race);
            } else {
                delayedAssault();
            }
            return;
        } if (!isAttacked()) {
            despawnAttackers();
            for (SpawnGroup2 group : getOutpostSpawns()) {
                for (SpawnTemplate spawn : group.getSpawnTemplates()) {
                    final OutpostSpawnTemplate template = (OutpostSpawnTemplate) spawn;
                    if (template.getOutpostRace().equals(race)) {
                        if (template.getHandlerType() != null && template.getHandlerType().equals(SpawnHandlerType.SLAYER)) {
                            Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
                            getAttackers().add(npc);
                        }
                    }
                }
            } if (getAttackers().isEmpty()) {
            } else {
                stopAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        despawnAttackers();
                        delayedAssault();
                    }
                }, 5 * 60000);
            }
        }
    }
	
    protected void despawnAttackers() {
        for (Npc attacker : getAttackers()) {
            attacker.getController().onDelete();
        }
        getAttackers().clear();
    }
	
    public Npc getFlag() {
        return flag;
    }
	
    public void setFlag(Npc flag) {
        this.flag = flag;
    }
	
    public Npc getBoss() {
        return boss;
    }
	
    public void setBoss(Npc boss) {
        this.boss = boss;
    }
	
    public OutpostBossDeathListener getOutpostBossDeathListener() {
        return baseBossDeathListener;
    }
	
    public boolean isFinished() {
        return finished.get();
    }
	
    public OL getOutpostLocation() {
        return outpostLocation;
    }
	
    public int getId() {
        return outpostLocation.getId();
    }
	
    public Race getRace() {
        return outpostLocation.getRace();
    }
	
    public void setRace(Race race) {
        outpostLocation.setRace(race);
    }
	
    public List<Npc> getAttackers() {
        return attackers;
    }
	
    public List<Npc> getSpawned() {
        return spawned;
    }
}