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
package com.aionemu.gameserver.model.autogroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.HallOfTenacityReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.services.instance.HallOfTenacityService;

/**
 * @author Ranastic
 */
public class AutoHallOfTenacityInstance extends AutoInstance
{
	private Logger log = LoggerFactory.getLogger(AutoHallOfTenacityInstance.class);
	
	@Override
	public AGQuestion addPlayer(Player player, SearchInstance searchInstance) {
		super.writeLock();
		try {
			if (!satisfyTime(searchInstance) || (players.size() >= agt.getPlayerSize())) {
				return AGQuestion.FAILED;
			}
			players.put(player.getObjectId(), new AGPlayer(player));
			return instance != null ? AGQuestion.ADDED : (players.size() == agt.getPlayerSize() ? AGQuestion.READY : AGQuestion.ADDED);
		}
		finally {
			super.writeUnlock();
		}
	}
	
	@Override
	public void onPressEnter(Player player) {
		super.onPressEnter(player);
		HallOfTenacityService.getInstance().addCoolDown(player);
		((HallOfTenacityReward) instance.getInstanceHandler().getInstanceReward()).portToHall(player);
		instance.register(player.getObjectId());
	}

	@Override
	public void onLeaveInstance(Player player) {
		super.unregister(player);
		PlayerGroupService.removePlayer(player);
	}
}