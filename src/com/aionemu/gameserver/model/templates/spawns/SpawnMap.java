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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.spawns.agentspawns.AgentSpawn;
import com.aionemu.gameserver.model.templates.spawns.anohaspawns.AnohaSpawn;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawn;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawn;
import com.aionemu.gameserver.model.templates.spawns.conquestspawns.ConquestSpawn;
import com.aionemu.gameserver.model.templates.spawns.dynamicriftspawns.DynamicRiftSpawn;
import com.aionemu.gameserver.model.templates.spawns.idiandepthsspawns.IdianDepthsSpawn;
import com.aionemu.gameserver.model.templates.spawns.instanceriftspawns.InstanceRiftSpawn;
import com.aionemu.gameserver.model.templates.spawns.iuspawns.IuSpawn;
import com.aionemu.gameserver.model.templates.spawns.landingspawns.LandingSpawn;
import com.aionemu.gameserver.model.templates.spawns.landingspecialspawns.LandingSpecialSpawn;
import com.aionemu.gameserver.model.templates.spawns.legiondominionspawns.LegionDominionSpawn;
import com.aionemu.gameserver.model.templates.spawns.moltenusspawns.MoltenusSpawn;
import com.aionemu.gameserver.model.templates.spawns.nightmarecircusspawns.NightmareCircusSpawn;
import com.aionemu.gameserver.model.templates.spawns.outpostspawns.OutpostSpawn;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawn;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawn;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawn;
import com.aionemu.gameserver.model.templates.spawns.svsspawns.SvsSpawn;
import com.aionemu.gameserver.model.templates.spawns.towerofeternityspawns.TowerOfEternitySpawn;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawn;
import com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns.ZorshivDredgionSpawn;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SpawnMap")
public class SpawnMap
{
	@XmlElement(name = "spawn")
	private List<Spawn> spawns;
	
	@XmlElement(name = "siege_spawn")
	private List<SiegeSpawn> siegeSpawns;
	
	@XmlElement(name = "legion_dominion_spawn")
	private List<LegionDominionSpawn> legionDominionSpawns;
	
	@XmlElement(name = "base_spawn")
	private List<BaseSpawn> baseSpawns;
	
	@XmlElement(name = "outpost_spawn")
	private List<OutpostSpawn> outpostSpawns;
	
	@XmlElement(name = "rift_spawn")
	private List<RiftSpawn> riftSpawns;
	
	@XmlElement(name = "vortex_spawn")
	private List<VortexSpawn> vortexSpawns;
	
	@XmlElement(name = "beritra_spawn")
	private List<BeritraSpawn> beritraSpawns;
	
	@XmlElement(name = "agent_spawn")
	private List<AgentSpawn> agentSpawns;
	
	@XmlElement(name = "anoha_spawn")
	private List<AnohaSpawn> anohaSpawns;
	
	@XmlElement(name = "conquest_spawn")
	private List<ConquestSpawn> conquestSpawns;
	
	@XmlElement(name = "svs_spawn")
	private List<SvsSpawn> svsSpawns;
	
	@XmlElement(name = "rvr_spawn")
	private List<RvrSpawn> rvrSpawns;
	
	@XmlElement(name = "iu_spawn")
	private List<IuSpawn> iuSpawns;
	
	@XmlElement(name = "moltenus_spawn")
	private List<MoltenusSpawn> moltenusSpawns;
	
	@XmlElement(name = "dynamic_rift_spawn")
	private List<DynamicRiftSpawn> dynamicRiftSpawns;
	
	@XmlElement(name = "instance_rift_spawn")
	private List<InstanceRiftSpawn> instanceRiftSpawns;
	
	@XmlElement(name = "nightmare_circus_spawn")
	private List<NightmareCircusSpawn> nightmareCircusSpawns;

	@XmlElement(name = "idian_depths_spawn")
	private List<IdianDepthsSpawn> idianDepthsSpawns;
	
	@XmlElement(name = "zorshiv_dredgion_spawn")
	private List<ZorshivDredgionSpawn> zorshivDredgionSpawns;

	@XmlElement(name="landing_spawn")
	private List<LandingSpawn> landingSpawns;
	
	@XmlElement(name="landing_special_spawn")
	private List<LandingSpecialSpawn> landingSpecialSpawns;
	
	@XmlElement(name = "tower_of_eternity_spawn")
	private List<TowerOfEternitySpawn> towerOfEternitySpawns;
	
	@XmlAttribute(name = "map_id")
	private int mapId;
	
	public SpawnMap() {
	}
	
	public SpawnMap(int mapId) {
		this.mapId = mapId;
	}
	
	public int getMapId() {
		return mapId;
	}
	
	public List<Spawn> getSpawns() {
		if (spawns == null) {
			spawns = new ArrayList<Spawn>();
		}
		return spawns;
	}
	
	public void addSpawns(Spawn spawns) {
		getSpawns().add(spawns);
	}
	
	public void removeSpawns(Spawn spawns) {
		getSpawns().remove(spawns);
	}
	
	public List<SiegeSpawn> getSiegeSpawns() {
		if (siegeSpawns == null) {
			siegeSpawns = new ArrayList<SiegeSpawn>();
		}
		return siegeSpawns;
	}
	
	public List<LegionDominionSpawn> getLegionDominionSpawns() {
		if (legionDominionSpawns == null) {
			legionDominionSpawns = new ArrayList<LegionDominionSpawn>();
		}
		return legionDominionSpawns;
	}
	
	public List<BaseSpawn> getBaseSpawns() {
		if (baseSpawns == null) {
			baseSpawns = new ArrayList<BaseSpawn>();
		}
		return baseSpawns;
	}
	
	public List<OutpostSpawn> getOutpostSpawns() {
		if (outpostSpawns == null) {
			outpostSpawns = new ArrayList<OutpostSpawn>();
		}
		return outpostSpawns;
	}
	
	public List<RiftSpawn> getRiftSpawns() {
		if (riftSpawns == null) {
			riftSpawns = new ArrayList<RiftSpawn>();
		}
		return riftSpawns;
	}
	
	public List<VortexSpawn> getVortexSpawns() {
		if (vortexSpawns == null) {
			vortexSpawns = new ArrayList<VortexSpawn>();
		}
		return vortexSpawns;
	}
	
	public List<BeritraSpawn> getBeritraSpawns() {
		if (beritraSpawns == null) {
			beritraSpawns = new ArrayList<BeritraSpawn>();
		}
		return beritraSpawns;
	}
	
	public List<AgentSpawn> getAgentSpawns() {
		if (agentSpawns == null) {
			agentSpawns = new ArrayList<AgentSpawn>();
		}
		return agentSpawns;
	}
	
	public List<AnohaSpawn> getAnohaSpawns() {
		if (anohaSpawns == null) {
			anohaSpawns = new ArrayList<AnohaSpawn>();
		}
		return anohaSpawns;
	}
	
	public List<ConquestSpawn> getConquestSpawns() {
		if (conquestSpawns == null) {
			conquestSpawns = new ArrayList<ConquestSpawn>();
		}
		return conquestSpawns;
	}
	
	public List<SvsSpawn> getSvsSpawns() {
		if (svsSpawns == null) {
			svsSpawns = new ArrayList<SvsSpawn>();
		}
		return svsSpawns;
	}
	
	public List<RvrSpawn> getRvrSpawns() {
		if (rvrSpawns == null) {
			rvrSpawns = new ArrayList<RvrSpawn>();
		}
		return rvrSpawns;
	}
	
	public List<IuSpawn> getIuSpawns() {
		if (iuSpawns == null) {
			iuSpawns = new ArrayList<IuSpawn>();
		}
		return iuSpawns;
	}
	
	public List<MoltenusSpawn> getMoltenusSpawns() {
		if (moltenusSpawns == null) {
			moltenusSpawns = new ArrayList<MoltenusSpawn>();
		}
		return moltenusSpawns;
	}
	
	public List<DynamicRiftSpawn> getDynamicRiftSpawns() {
		if (dynamicRiftSpawns == null) {
			dynamicRiftSpawns = new ArrayList<DynamicRiftSpawn>();
		}
		return dynamicRiftSpawns;
	}
	
	public List<InstanceRiftSpawn> getInstanceRiftSpawns() {
		if (instanceRiftSpawns == null) {
			instanceRiftSpawns = new ArrayList<InstanceRiftSpawn>();
		}
		return instanceRiftSpawns;
	}
	
	public List<NightmareCircusSpawn> getNightmareCircusSpawns() {
		if (nightmareCircusSpawns == null) {
			nightmareCircusSpawns = new ArrayList<NightmareCircusSpawn>();
		}
		return nightmareCircusSpawns;
	}

	public List<IdianDepthsSpawn> getIdianDepthsSpawns() {
		if (idianDepthsSpawns == null) {
			idianDepthsSpawns = new ArrayList<IdianDepthsSpawn>();
		}
		return idianDepthsSpawns;
	}
	
	public List<ZorshivDredgionSpawn> getZorshivDredgionSpawns() {
		if (zorshivDredgionSpawns == null) {
			zorshivDredgionSpawns = new ArrayList<ZorshivDredgionSpawn>();
		}
		return zorshivDredgionSpawns;
	}
	
	public List<LandingSpawn> getLandingSpawns() {
        if (landingSpawns == null) {
            landingSpawns = new ArrayList<LandingSpawn>();
        }
        return landingSpawns;
    }
	
	public List<LandingSpecialSpawn> getLandingSpecialSpawns() {
		if (landingSpecialSpawns == null) {
			landingSpecialSpawns = new ArrayList<LandingSpecialSpawn>();
		}
		return landingSpecialSpawns;
	}
	
	public List<TowerOfEternitySpawn> getTowerOfEternitySpawns() {
		if (towerOfEternitySpawns == null) {
			towerOfEternitySpawns = new ArrayList<TowerOfEternitySpawn>();
		}
		return towerOfEternitySpawns;
	}
	
	public void addSiegeSpawns(SiegeSpawn spawns) {
		getSiegeSpawns().add(spawns);
	}
	
	public void addBaseSpawns(BaseSpawn spawns) {
		getBaseSpawns().add(spawns);
	}
	
	public void addRiftSpawns(RiftSpawn spawns) {
		getRiftSpawns().add(spawns);
	}
	
	public void addVortexSpawns(VortexSpawn spawns) {
		getVortexSpawns().add(spawns);
	}
	
	public void addBeritraSpawns(BeritraSpawn spawns) {
		getBeritraSpawns().add(spawns);
	}
	
	public void addAgentSpawns(AgentSpawn spawns) {
		getAgentSpawns().add(spawns);
	}
	
	public void addAnohaSpawns(AnohaSpawn spawns) {
		getAnohaSpawns().add(spawns);
	}
	
	public void addConquestSpawns(ConquestSpawn spawns) {
		getConquestSpawns().add(spawns);
	}
	
	public void addSvsSpawns(SvsSpawn spawns) {
		getSvsSpawns().add(spawns);
	}
	
	public void addRvrSpawns(RvrSpawn spawns) {
		getRvrSpawns().add(spawns);
	}
	
	public void addIuSpawns(IuSpawn spawns) {
		getIuSpawns().add(spawns);
	}
	
	public void addMoltenusSpawns(MoltenusSpawn spawns) {
		getMoltenusSpawns().add(spawns);
	}
	
	public void addDynamicRiftSpawns(DynamicRiftSpawn spawns) {
		getDynamicRiftSpawns().add(spawns);
	}
	
	public void addInstanceRiftSpawns(InstanceRiftSpawn spawns) {
		getInstanceRiftSpawns().add(spawns);
	}
	
	public void addNightmareCircusSpawns(NightmareCircusSpawn spawns) {
		getNightmareCircusSpawns().add(spawns);
	}

	public void addIdianDepthsSpawns(IdianDepthsSpawn spawns) {
		getIdianDepthsSpawns().add(spawns);
	}
	
	public void addZorshivDredgionSpawns(ZorshivDredgionSpawn spawns) {
		getZorshivDredgionSpawns().add(spawns);
    }
	
    public void addLandingSpawns(LandingSpawn spawns) {
        getLandingSpawns().add(spawns);
    }
	
	public void addLandingSpecialSpawns(LandingSpecialSpawn spawns) {
		getLandingSpecialSpawns().add(spawns);
	}
	
	public void addTowerOfEternitySpawns(TowerOfEternitySpawn spawns) {
		getTowerOfEternitySpawns().add(spawns);
	}
}