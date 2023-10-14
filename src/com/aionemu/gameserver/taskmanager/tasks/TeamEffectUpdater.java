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
package com.aionemu.gameserver.taskmanager.tasks;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.common.legacy.GroupEvent;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.taskmanager.AbstractIterativePeriodicTaskManager;

public final class TeamEffectUpdater extends AbstractIterativePeriodicTaskManager<Player> {
	private static final class SingletonHolder {
		private static final TeamEffectUpdater INSTANCE = new TeamEffectUpdater();
	}

	public static TeamEffectUpdater getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public TeamEffectUpdater() {
		super(500);
	}

	@Override
	protected void callTask(Player player) {
		if (player.isOnline()) {
			if (player.isInGroup2()) {
				PlayerGroupService.updateGroup(player, GroupEvent.UPDATE);
				PlayerGroupService.updateGroup(player, GroupEvent.UNK_53);
			}
			if (player.isInAlliance2()) {
				PlayerAllianceService.updateAlliance(player, PlayerAllianceEvent.UPDATE);
			}
		}
		this.stopTask(player);
	}

	@Override
	protected String getCalledMethodName() {
		return "teamEffectUpdate()";
	}
}