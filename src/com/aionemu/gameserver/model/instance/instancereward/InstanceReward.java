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
package com.aionemu.gameserver.model.instance.instancereward;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;

import javolution.util.FastList;

public class InstanceReward<T extends InstancePlayerReward>
{
	private final Logger log = LoggerFactory.getLogger(InstanceReward.class);
	protected FastList<T> instanceRewards = new FastList<T>();
	private InstanceScoreType instanceScoreType = InstanceScoreType.START_PROGRESS;
	protected Integer mapId;
	protected int instanceId;
	private long instanceTime;
	
	public InstanceReward(Integer mapId, int instanceId) {
		this.mapId = mapId;
		this.instanceId = instanceId;
	}
	
	public FastList<T> getInstanceRewards() {
		return instanceRewards;
	}
	
	public boolean containPlayer(Integer object) {
		for (InstancePlayerReward instanceReward : instanceRewards) {
			if (instanceReward.getOwner().equals(object)) {
				return true;
			}
		}
		return false;
	}
	
	public void removePlayerReward(T reward) {
		if (instanceRewards.contains(reward)) {
			instanceRewards.remove(reward);
		}
	}
	
	public InstancePlayerReward getPlayerReward(Integer object) {
		for (InstancePlayerReward instanceReward : instanceRewards) {
			if (instanceReward.getOwner().equals(object)) {
				return instanceReward;
			}
		}
		return null;
	}
	
	public void addPlayerReward(T reward) {
		instanceRewards.add(reward);
	}
	
	public void setInstanceScoreType(InstanceScoreType instanceScoreType) {
		this.instanceScoreType = instanceScoreType;
	}
	
	public InstanceScoreType getInstanceScoreType() {
		return instanceScoreType;
	}
	
	public Integer getMapId() {
		return mapId;
	}
	
	public int getInstanceId() {
		return instanceId;
	}
	
	public boolean isRewarded() {
		return instanceScoreType.isEndProgress();
	}
	
	public boolean isPreparing() {
		return instanceScoreType.isPreparing();
	}
	
	public boolean isStartProgress() {
		return instanceScoreType.isStartProgress();
	}
	
	public void setInstanceStartTime() {
        this.instanceTime = System.currentTimeMillis();
    }
	
	public void clear() {
		instanceRewards.clear();
	}
	
	protected InstanceReward<?> getInstanceReward() {
		return this;
	}
	
	public void sendLog(String log) {
		this.log.info(log);
	}
}