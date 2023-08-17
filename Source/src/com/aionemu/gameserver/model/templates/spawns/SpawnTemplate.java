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
package com.aionemu.gameserver.model.templates.spawns;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import javolution.util.FastList;
import org.apache.commons.lang.StringUtils;

public class SpawnTemplate
{
	private float x;
	private float y;
	private float z;
	private byte h;
	private int entityId;
	private int randomWalk;
	private String walkerId;
	private int walkerIdx;
	private int fly;
	private String anchor;
	private boolean isUsed;
	private SpawnGroup2 spawnGroup;
	private EventTemplate eventTemplate;
	private SpawnModel model;
	private int state;
	private int astate;
	private int bstate;
	private int cstate;
	private int dstate;
	private int estate;
	private int istate;
	private int mstate;
	private int nstate;
	private int ostate;
	private int pstate;
	private int rstate;
	private int tstate;
	private int zstate;
	private int iustate;
	private int opstate;
	private int creatorId;
	private String masterName = StringUtils.EMPTY;
	private TemporarySpawn temporarySpawn;
	private VisibleObject visibleObject;
	private FastList<VisibleObject> visibleObjects;
	
	public SpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		this.spawnGroup = spawnGroup;
		x = spot.getX();
		y = spot.getY();
		z = spot.getZ();
		h = spot.getHeading();
		entityId = spot.getEntityId();
		randomWalk = spot.getRandomWalk();
		walkerId = spot.getWalkerId();
		fly = spot.getFly();
		anchor = spot.getAnchor();
		walkerIdx = spot.getWalkerIndex();
		model = spot.getModel();
		state = spot.getState();
		astate = spot.getAState();
		bstate = spot.getBState();
		cstate = spot.getCState();
		dstate = spot.getDState();
		estate = spot.getEState();
		istate = spot.getIState();
		mstate = spot.getMState();
		nstate = spot.getNState();
		ostate = spot.getOState();
		pstate = spot.getPState();
		rstate = spot.getRState();
		tstate = spot.getTState();
		zstate = spot.getZState();
		iustate = spot.getIUState();
		opstate = spot.getOPState();
		temporarySpawn = spot.getTemporarySpawn();
	}
	
	public SpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int entityId, int fly) {
		this.spawnGroup = spawnGroup;
		this.x = x;
		this.y = y;
		this.z = z;
		h = heading;
		this.randomWalk = randWalk;
		this.walkerId = walkerId;
		this.entityId = entityId;
		this.fly = fly;
		addTemplate();
	}
	
	private void addTemplate() {
		spawnGroup.addSpawnTemplate(this);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public byte getHeading() {
		return h;
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
	public int getRandomWalk() {
		return randomWalk;
	}
	
	public void setRandomWalk(int randomWalk) {
		this.randomWalk = randomWalk;
	}
	
	public int getFly() {
		return fly;
	}
	
	public boolean canFly() {
		return fly > 0;
	}
	
	public void setUse(boolean use) {
		isUsed = use;
	}
	
	public boolean isUsed() {
		return isUsed;
	}
	
	public int getNpcId() {
		return spawnGroup.getNpcId();
	}
	
	public int getWorldId() {
		return spawnGroup.getWorldId();
	}
	
	public SpawnTemplate changeTemplate(int instanceId) {
		return spawnGroup.getRndTemplate(instanceId);
	}
	
	public int getRespawnTime() {
		return spawnGroup.getRespawnTime();
	}
	
	public void setRespawnTime(int respawnTime) {
		spawnGroup.setRespawnTime(respawnTime);
	}
	
	public TemporarySpawn getTemporarySpawn() {
		return temporarySpawn != null ? temporarySpawn : spawnGroup.geTemporarySpawn();
	}
	
	public SpawnHandlerType getHandlerType() {
		return spawnGroup.getHandlerType();
	}
	
	public String getAnchor() {
		return anchor;
	}
	
	public boolean hasRandomWalk() {
		return randomWalk != 0;
	}
	
	public boolean isNoRespawn() {
		return spawnGroup.getRespawnTime() == 0;
	}
	
	public boolean hasPool() {
		return spawnGroup.hasPool();
	}
	
	public String getWalkerId() {
		return walkerId;
	}
	
	public void setWalkerId(String walkerId) {
		this.walkerId = walkerId;
	}
	
	public int getWalkerIndex() {
		return walkerIdx;
	}
	
	public boolean isTemporarySpawn() {
		return spawnGroup.isTemporarySpawn();
	}
	
	public boolean isEventSpawn() {
		return eventTemplate != null;
	}
	
	public EventTemplate getEventTemplate() {
		return eventTemplate;
	}
	
	public void setEventTemplate(EventTemplate eventTemplate) {
		this.eventTemplate = eventTemplate;
	}
	
	public SpawnModel getModel() {
		return model;
	}
	
	public int getState() {
		return state;
	}
	
	public int getAState() {
		return astate;
	}
	
	public int getBState() {
		return bstate;
	}
	
	public int getCState() {
		return cstate;
	}
	
	public int getDState() {
		return dstate;
	}
	
	public int getEState() {
		return estate;
	}
	
	public int getIState() {
		return istate;
	}
	
	public int getMState() {
		return mstate;
	}
	
	public int getNState() {
		return nstate;
	}
	
	public int getOState() {
		return ostate;
	}
	
	public int getPState() {
		return pstate;
	}
	
	public int getRState() {
		return rstate;
	}
	
	public int getTState() {
		return tstate;
	}
	
	public int getZState() {
		return zstate;
	}
	
	public int getIUState() {
		return iustate;
	}
	
	public int getOPState() {
		return opstate;
	}
	
	public int getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getMasterName() {
		return masterName;
	}
	
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	
	public VisibleObject getVisibleObject() {
		return visibleObject;
	}
	
	public void setVisibleObject(VisibleObject visibleObject) {
		this.visibleObject = visibleObject;
	}
	
	public FastList<VisibleObject> getVisibleObjects() {
		return this.visibleObjects;
	}
	
	public void addVisibleObject(VisibleObject visibleObject) {
		if (this.visibleObjects == null) {
			this.visibleObjects = new FastList();
		}
		this.visibleObjects.add(visibleObject);
	}
}