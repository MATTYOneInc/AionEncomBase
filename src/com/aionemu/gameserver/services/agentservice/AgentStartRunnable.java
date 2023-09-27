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
package com.aionemu.gameserver.services.agentservice;

import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AgentService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Map;

/**
 * @author Rinzler (Encom)
 */

public class AgentStartRunnable implements Runnable
{
	private final int id;
	
	public AgentStartRunnable(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		//The Agent battle will start in 10 minutes.
		AgentService.getInstance().agentBattleMsg1(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
			    //The Agent battle will start in 5 minutes.
				AgentService.getInstance().agentBattleMsg2(id);
			}
		}, 300000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
			    Map<Integer, AgentLocation> locations = AgentService.getInstance().getAgentLocations();
				for (final AgentLocation loc: locations.values()) {
					if (loc.getId() == id) {
						//Governor Sunayaka 5.8
						AgentService.getInstance().governorSunayakaMsg(id);
						//Berserker Sunayaka 5.8
						AgentService.getInstance().berserkerSunayakaMsg(id);
						//Agent Fight 4.7
						AgentService.getInstance().startAgentFight(loc.getId());
					}
				}
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Agent has spawned.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite);
					}
				});
			}
		}, 600000);
	}
}