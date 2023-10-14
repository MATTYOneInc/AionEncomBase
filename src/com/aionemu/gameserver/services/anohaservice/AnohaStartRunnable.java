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
package com.aionemu.gameserver.services.anohaservice;

import java.util.Map;

import com.aionemu.gameserver.model.anoha.AnohaLocation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AnohaService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler (Encom)
 */

public class AnohaStartRunnable implements Runnable {
	private final int id;

	public AnohaStartRunnable(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		// Berserk Anoha Sword Effect.
		AnohaService.getInstance().adventSwordEffectSP(id);
		// Berserk Anoha will return to Kaldor in 30 minutes.
		AnohaService.getInstance().berserkAnohaMsg1(id);
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				AnohaService.getInstance().sendRequest(player);
			}
		});
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// Enraged Wealhtheow Guardian will appear in 5 minutes.
				AnohaService.getInstance().wealhtheowGuardianMsg1(id);
			}
		}, 1500000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// Enraged Wealhtheow Guardian will appear in 3 minutes.
				AnohaService.getInstance().wealhtheowGuardianMsg2(id);
			}
		}, 1620000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// Enraged Wealhtheow Guardian will appear in 1 minute.
				AnohaService.getInstance().wealhtheowGuardianMsg3(id);
			}
		}, 1740000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Map<Integer, AnohaLocation> locations = AnohaService.getInstance().getAnohaLocations();
				for (final AnohaLocation loc : locations.values()) {
					if (loc.getId() == id) {
						AnohaService.getInstance().startAnoha(loc.getId());
					}
				}
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						// Summon Berserk Anoha.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Anoha_Spawn);
					}
				});
			}
		}, 1800000);
	}
}