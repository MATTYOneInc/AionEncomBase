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
package com.aionemu.gameserver.model.templates.spawns;

import com.aionemu.commons.taskmanager.AbstractLockManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.agent.AgentStateType;
import com.aionemu.gameserver.model.anoha.AnohaStateType;
import com.aionemu.gameserver.model.beritra.BeritraStateType;
import com.aionemu.gameserver.model.conquest.ConquestStateType;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftStateType;
import com.aionemu.gameserver.model.idiandepths.IdianDepthsStateType;
import com.aionemu.gameserver.model.instancerift.InstanceRiftStateType;
import com.aionemu.gameserver.model.iu.IuStateType;
import com.aionemu.gameserver.model.landing.LandingStateType;
import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;
import com.aionemu.gameserver.model.legiondominion.LegionDominionModType;
import com.aionemu.gameserver.model.legiondominion.LegionDominionRace;
import com.aionemu.gameserver.model.moltenus.MoltenusStateType;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusStateType;
import com.aionemu.gameserver.model.rvr.RvrStateType;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.svs.SvsStateType;
import com.aionemu.gameserver.model.templates.spawns.agentspawns.AgentSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.anohaspawns.AnohaSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.conquestspawns.ConquestSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.dynamicriftspawns.DynamicRiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.idiandepthsspawns.IdianDepthsSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.instanceriftspawns.InstanceRiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.iuspawns.IuSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.landingspawns.LandingSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.landingspecialspawns.LandingSpecialSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.legiondominionspawns.LegionDominionSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.moltenusspawns.MoltenusSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.nightmarecircusspawns.NightmareCircusSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.outpostspawns.OutpostSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.svsspawns.SvsSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.towerofeternityspawns.TowerOfEternitySpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns.ZorshivDredgionSpawnTemplate;
import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityStateType;
import com.aionemu.gameserver.model.vortex.VortexStateType;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionStateType;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnGroup2 extends AbstractLockManager
{
	private static final Logger log = LoggerFactory.getLogger(SpawnGroup2.class);
	
	private int worldId;
	private int npcId;
	private int pool;
	private byte difficultId;
	private TemporarySpawn temporarySpawn;
	private int respawnTime;
	private SpawnHandlerType handlerType;
	private List<SpawnTemplate> spots = new ArrayList<SpawnTemplate>();
	private HashMap<Integer, HashMap<SpawnTemplate, Boolean>> poolUsedTemplates;
	
	public SpawnGroup2(int worldId, Spawn spawn) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			SpawnTemplate spawnTemplate = new SpawnTemplate(this, template);
			if (spawn.isEventSpawn()) {
				spawnTemplate.setEventTemplate(spawn.getEventTemplate());
			}
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, Race race) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			BaseSpawnTemplate spawnTemplate = new BaseSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setBaseRace(race);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, Race race, int miss) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			OutpostSpawnTemplate spawnTemplate = new OutpostSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setOutpostRace(race);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int siegeId, SiegeRace race, SiegeModType mod) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			SiegeSpawnTemplate spawnTemplate = new SiegeSpawnTemplate(this, template);
			spawnTemplate.setSiegeId(siegeId);
			spawnTemplate.setSiegeRace(race);
			spawnTemplate.setSiegeModType(mod);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int legionDominionId, LegionDominionRace race, LegionDominionModType mod) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			LegionDominionSpawnTemplate spawnTemplate = new LegionDominionSpawnTemplate(this, template);
			spawnTemplate.setLegionDominionId(legionDominionId);
			spawnTemplate.setLegionDominionRace(race);
			spawnTemplate.setLegionDominionModType(mod);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			RiftSpawnTemplate spawnTemplate = new RiftSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, VortexStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			VortexSpawnTemplate spawnTemplate = new VortexSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, BeritraStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			BeritraSpawnTemplate spawnTemplate = new BeritraSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setBStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, AgentStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			AgentSpawnTemplate spawnTemplate = new AgentSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setAStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, AnohaStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			AnohaSpawnTemplate spawnTemplate = new AnohaSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setCStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, ConquestStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			ConquestSpawnTemplate spawnTemplate = new ConquestSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setOStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, SvsStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			SvsSpawnTemplate spawnTemplate = new SvsSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setPStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, RvrStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			RvrSpawnTemplate spawnTemplate = new RvrSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setRStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, IuStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			IuSpawnTemplate spawnTemplate = new IuSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setIUStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, DynamicRiftStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			DynamicRiftSpawnTemplate spawnTemplate = new DynamicRiftSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setDStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, InstanceRiftStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			InstanceRiftSpawnTemplate spawnTemplate = new InstanceRiftSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setEStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, NightmareCircusStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			NightmareCircusSpawnTemplate spawnTemplate = new NightmareCircusSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setNStateType(type);
			spots.add(spawnTemplate);
		}
	}

	public SpawnGroup2(int worldId, Spawn spawn, int id, IdianDepthsStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			IdianDepthsSpawnTemplate spawnTemplate = new IdianDepthsSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setIStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, ZorshivDredgionStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			ZorshivDredgionSpawnTemplate spawnTemplate = new ZorshivDredgionSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setZStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, MoltenusStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			MoltenusSpawnTemplate spawnTemplate = new MoltenusSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setMStateType(type);
			spots.add(spawnTemplate);
        }
    }
	
    public SpawnGroup2(int worldId, Spawn spawn, int landingId, LandingStateType state) {
        this.worldId = worldId;
        initializing(spawn);
        for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
            LandingSpawnTemplate spawnTemplate = new LandingSpawnTemplate(this, template);
            spawnTemplate.setId(landingId);
            spawnTemplate.setEStateType(state);
            spots.add(spawnTemplate);
        }
    }
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, LandingSpecialStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			LandingSpecialSpawnTemplate spawnTemplate = new LandingSpecialSpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setFStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
	public SpawnGroup2(int worldId, Spawn spawn, int id, TowerOfEternityStateType type) {
		this.worldId = worldId;
		initializing(spawn);
		for (SpawnSpotTemplate template : spawn.getSpawnSpotTemplates()) {
			TowerOfEternitySpawnTemplate spawnTemplate = new TowerOfEternitySpawnTemplate(this, template);
			spawnTemplate.setId(id);
			spawnTemplate.setTStateType(type);
			spots.add(spawnTemplate);
		}
	}
	
    private void initializing(Spawn spawn) {
        temporarySpawn = spawn.getTemporarySpawn();
        respawnTime = spawn.getRespawnTime();
		pool = spawn.getPool();
		npcId = spawn.getNpcId();
		handlerType = spawn.getSpawnHandlerType();
		difficultId = spawn.getDifficultId();
		if (hasPool()) {
			poolUsedTemplates = new HashMap<Integer, HashMap<SpawnTemplate, Boolean>>();
		}
	}
	
	public SpawnGroup2(int worldId, int npcId) {
		this.worldId = worldId;
		this.npcId = npcId;
	}
	
	public List<SpawnTemplate> getSpawnTemplates() {
		return spots;
	}
	
	public void addSpawnTemplate(SpawnTemplate spawnTemplate) {
		spots.add(spawnTemplate);
	}
	
	public int getWorldId() {
		return worldId;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public TemporarySpawn geTemporarySpawn() {
		return temporarySpawn;
	}
	
	public int getPool() {
		return pool;
	}
	
	public boolean hasPool() {
		return pool > 0;
	}
	
	public int getRespawnTime() {
		return respawnTime;
	}
	
	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}
	
	public boolean isTemporarySpawn() {
		return temporarySpawn != null;
	}
	
	public SpawnHandlerType getHandlerType() {
		return handlerType;
	}
	
	public SpawnTemplate getRndTemplate(int instanceId) {
		final List<SpawnTemplate> allTemplates = spots;
		List<SpawnTemplate> templates = new ArrayList<SpawnTemplate>();
		super.readLock();
		try {
			for (SpawnTemplate template : allTemplates) {
				if (!isTemplateUsed(instanceId, template)) {
					templates.add(template);
				}
			} if (templates.size() == 0) {
				log.warn("Pool size more then spots, npcId: " + npcId + ", worldId: " + worldId);
				return null;
			}
		}
		finally {
			super.readUnlock();
		}
		SpawnTemplate spawnTemplate = templates.get(Rnd.get(0, templates.size() - 1));
		setTemplateUse(instanceId, spawnTemplate, true);
		return spawnTemplate;
	}
	
	public void setTemplateUse(int instanceId, SpawnTemplate template, boolean isUsed) {
		super.writeLock();
		try {
			HashMap<SpawnTemplate, Boolean> states = poolUsedTemplates.get(instanceId);
			if (states == null) {
				states = new HashMap<SpawnTemplate, Boolean>();
				poolUsedTemplates.put(instanceId, states);
			}
			states.put(template, isUsed);
		}
		finally {
			super.writeUnlock();
		}
	}
	
	public boolean isTemplateUsed(int instanceId, SpawnTemplate template) {
		super.readLock();
		try {
			HashMap<SpawnTemplate, Boolean> states = poolUsedTemplates.get(instanceId);
			if (states == null)
				return false;
			Boolean state = states.get(template);
			if (state == null)
				return false;
			return state;
		}
		finally {
			super.readUnlock();
		}
	}
	
	public void resetTemplates(int instanceId) {
		HashMap<SpawnTemplate, Boolean> states = poolUsedTemplates.get(instanceId);
		if (states == null)
			return;
		super.writeLock();
		try {
			for (SpawnTemplate template : states.keySet()) {
				states.put(template, false);
			}
		}
		finally {
			super.writeUnlock();
		}
	}
	
	public byte getDifficultId() {
        return difficultId;
    }
}