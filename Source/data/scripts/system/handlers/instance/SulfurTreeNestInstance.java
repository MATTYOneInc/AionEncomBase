/*
 * This file is part of Encom.
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
package instance;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.Future;

/**
Author (Encom)

Once you pass the barrier you want to only aggo the minimum mobs required to get to the centre.
All participants should then stand on the island and not move off, so as to avoid aggoing the patrols and other mobs nearby.
The treasure chest itself requires quite a beat down to open.
It is based on total hits not DPS so the best option is to all put your weapons away and simply punch the box, it is your fastest attack.
If you have a Spiritmaster then he should additionally train the pet to hit the box for additional hits but make sure it is in a spot which will not aggro anything.
The box itself takes up to 10 minutes to open if this is done correctly, far longer if you don’t.
Also, you will probably have these annoying little monsters chewing at your legs for 1 hit each time.
Just ignore them and keep hitting that box!
**/

@InstanceID(300060000)
public class SulfurTreeNestInstance extends GeneralInstanceHandler
{
	private Future<?> sulfurTreeNestTask;
	private boolean isStartTimer = false;
	protected boolean isInstanceDestroyed = false;
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        spawnSulfurTreeNestRings();
    }
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("SULFUR_TREE_NEST")) {
		    if (!isStartTimer) {
			    isStartTimer = true;
			    System.currentTimeMillis();
			    instance.doOnAllPlayers(new Visitor<Player>() {
			        @Override
			        public void visit(Player player) {
						if (player.isOnline()) {
							startSulfurTreeNestTimer();
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
							//The Balaur protective magic ward has been activated.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_START_IDABRE);
						}
					}
				});
			}
		}
		return false;
	}
	
	private void startSulfurTreeNestTimer() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
				    sulfurTreeNestTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									onExitInstance(player);
								}
							});
							onInstanceDestroy();
						}
					}, 910000); //15 Minutes + 10s.
				}
			}
		});
    }
	
	private void spawnSulfurTreeNestRings() {
        FlyRing f1 = new FlyRing(new FlyRingTemplate("SULFUR_TREE_NEST", mapId,
        new Point3D(462.9394, 380.34888, 168.97256),
        new Point3D(462.9394, 380.34888, 174.97256),
        new Point3D(468.9229, 380.7933, 168.97256), 6), instanceId);
        f1.spawn();
    }
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}