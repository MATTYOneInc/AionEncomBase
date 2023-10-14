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

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpawnSpotTemplate")
public class SpawnSpotTemplate
{
	@XmlAttribute(name = "state")
	private Integer state = 0;
	
	@XmlAttribute(name = "astate")
	private Integer astate = 0;
	
	@XmlAttribute(name = "bstate")
	private Integer bstate = 0;
	
	@XmlAttribute(name = "cstate")
	private Integer cstate = 0;
	
	@XmlAttribute(name = "dstate")
	private Integer dstate = 0;
	
	@XmlAttribute(name = "estate")
	private Integer estate = 0;
	
	@XmlAttribute(name = "fstate")
	private Integer fstate = 0;
	
	@XmlAttribute(name = "istate")
	private Integer istate = 0;
	
	@XmlAttribute(name = "mstate")
	private Integer mstate = 0;
	
	@XmlAttribute(name = "nstate")
	private Integer nstate = 0;
	
	@XmlAttribute(name = "ostate")
	private Integer ostate = 0;
	
	@XmlAttribute(name = "pstate")
	private Integer pstate = 0;
	
	@XmlAttribute(name = "rstate")
	private Integer rstate = 0;
	
	@XmlAttribute(name = "tstate")
	private Integer tstate = 0;
	
	@XmlAttribute(name = "zstate")
	private Integer zstate = 0;
	
	@XmlAttribute(name = "iustate")
	private Integer iustate = 0;
	
	@XmlAttribute(name = "opstate")
	private Integer opstate = 0;
	
	@XmlAttribute(name = "anchor")
	private String anchor;
	
	@XmlAttribute(name = "fly")
	private Integer fly = 0;
	
	@XmlAttribute(name = "walker_index")
	private Integer walkerIdx;
	
	@XmlAttribute(name = "walker_id")
	private String walkerId;
	
	@XmlAttribute(name = "random_walk")
	private Integer randomWalk = 0;
	
	@XmlAttribute(name = "entity_id")
	private Integer entityId = 0;
	
	@XmlAttribute(name = "h", required = true)
	private byte h;
	
	@XmlAttribute(name = "z", required = true)
	private float z;
	
	@XmlAttribute(name = "y", required = true)
	private float y;
	
	@XmlAttribute(name = "x", required = true)
	private float x;
	
	@XmlElement(name = "temporary_spawn")
	private TemporarySpawn temporaySpawn;
	
	@XmlElement(name = "model")
	private SpawnModel model;
	private static final Integer ZERO = new Integer(0);
	
	public SpawnSpotTemplate() {
	}
	
	void beforeMarshal(Marshaller marshaller) {
		if (ZERO.equals(entityId)) {
			entityId = null;
		} if (ZERO.equals(fly)) {
			fly = null;
		} if (ZERO.equals(randomWalk)) {
			randomWalk = null;
		} if (ZERO.equals(state)) {
			state = null;
		} if (ZERO.equals(astate)) {
			astate = null;
		} if (ZERO.equals(bstate)) {
			bstate = null;
		} if (ZERO.equals(cstate)) {
			cstate = null;
		} if (ZERO.equals(dstate)) {
			dstate = null;
		} if (ZERO.equals(estate)) {
			estate = null;
		} if (ZERO.equals(fstate)) {
			fstate = null;
		} if (ZERO.equals(istate)) {
			istate = null;
		} if (ZERO.equals(mstate)) {
			mstate = null;
		} if (ZERO.equals(nstate)) {
			nstate = null;
		} if (ZERO.equals(ostate)) {
			ostate = null;
		} if (ZERO.equals(pstate)) {
			pstate = null;
		} if (ZERO.equals(rstate)) {
			rstate = null;
		} if (ZERO.equals(tstate)) {
			tstate = null;
		} if (ZERO.equals(zstate)) {
			zstate = null;
		} if (ZERO.equals(iustate)) {
			iustate = null;
		} if (ZERO.equals(opstate)) {
			opstate = null;
		} if (ZERO.equals(walkerIdx)) {
			walkerIdx = null;
		}
	}
	
	void afterMarshal(Marshaller marshaller) {
		if (entityId == null) {
			entityId = 0;
		} if (fly == null) {
			fly = 0;
		} if (randomWalk == null) {
			randomWalk = 0;
		} if (state == null) {
			state = 0;
		} if (astate == null) {
			astate = 0;
		} if (bstate == null) {
			bstate = 0;
		} if (cstate == null) {
			cstate = 0;
		} if (dstate == null) {
			dstate = 0;
		} if (estate == null) {
			estate = 0;
		} if (fstate == null) {
			fstate = 0;
		} if (istate == null) {
			istate = 0;
		} if (mstate == null) {
			mstate = 0;
		} if (nstate == null) {
			nstate = 0;
		} if (ostate == null) {
			ostate = 0;
		} if (pstate == null) {
			pstate = 0;
		} if (rstate == null) {
			rstate = 0;
		} if (tstate == null) {
			tstate = 0;
		} if (zstate == null) {
			zstate = 0;
		} if (iustate == null) {
			iustate = 0;
		} if (opstate == null) {
			opstate = 0;
		} if (walkerIdx == null) {
			walkerIdx = 0;
		}
	}
	
	public SpawnSpotTemplate(float x, float y, float z, byte h, int randomWalk, String walkerId, Integer walkerIndex) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
		if (randomWalk > 0) {
			this.randomWalk = randomWalk;
		}
		this.walkerId = walkerId;
		this.walkerIdx = walkerIndex;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
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
	
	public String getWalkerId() {
		return walkerId;
	}
	
	public void setWalkerId(String walkerId) {
		this.walkerId = walkerId;
	}
	
	public int getWalkerIndex() {
		if (walkerIdx == null) {
			return 0;
		}
		return walkerIdx;
	}
	
	public int getRandomWalk() {
		return randomWalk;
	}
	
	public int getFly() {
		return fly;
	}
	
	public String getAnchor() {
		return anchor;
	}
	
	public SpawnModel getModel() {
		return model;
	}
	
	public int getState() {
		if (state == null) {
			return 0;
		}
		return state;
	}
	
	public int getAState() {
		if (astate == null) {
			return 0;
		}
		return astate;
	}
	
	public int getBState() {
		if (bstate == null) {
			return 0;
		}
		return bstate;
	}
	
	public int getCState() {
		if (cstate == null) {
			return 0;
		}
		return cstate;
	}
	
	public int getDState() {
		if (dstate == null) {
			return 0;
		}
		return dstate;
	}
	
	public int getEState() {
		if (estate == null) {
			return 0;
		}
		return estate;
	}
	
	public int getFState() {
		if (fstate == null) {
			return 0;
		}
		return fstate;
	}
	
	public int getIState() {
		if (istate == null) {
			return 0;
		}
		return istate;
	}
	
	public int getMState() {
		if (mstate == null) {
			return 0;
		}
		return mstate;
	}
	
	public int getNState() {
		if (nstate == null) {
			return 0;
		}
		return nstate;
	}
	
	public int getOState() {
		if (ostate == null) {
			return 0;
		}
		return ostate;
	}
	
	public int getPState() {
		if (pstate == null) {
			return 0;
		}
		return pstate;
	}
	
	public int getRState() {
		if (rstate == null) {
			return 0;
		}
		return rstate;
	}
	
	public int getTState() {
		if (tstate == null) {
			return 0;
		}
		return tstate;
	}
	
	public int getZState() {
		if (zstate == null) {
			return 0;
		}
		return zstate;
	}
	
	public int getIUState() {
		if (iustate == null) {
			return 0;
		}
		return iustate;
	}
	
	public int getOPState() {
		if (opstate == null) {
			return 0;
		}
		return opstate;
	}
	
	public TemporarySpawn getTemporarySpawn() {
		return temporaySpawn;
	}
}