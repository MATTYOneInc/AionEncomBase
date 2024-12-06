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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;

import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=hO-QSwBfeXI
/****/

@InstanceID(301620000)
public class DrakenseerLairInstance extends GeneralInstanceHandler
{
	private int abyssGateEnhancerKilled;
	private boolean isStartTimer = false;
	protected boolean isInstanceDestroyed = false;
	private final FastList<Future<?>> drakenseerLairTask = FastList.newInstance();
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 220450: //Akhal The Oracle.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166030005, 5)); //Tempering Solution.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166040001, 1)); //Essence Core Solution.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058413, 1)); //ì?´ê³„ ì•”ë£¡ì?˜ ë¬´ê¸° ìƒ?ìž?.
                        switch (Rnd.get(1, 4)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057624, 1)); //Oracle's Illusion Godstone Bundle.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057625, 1)); //Oracle Greater Enchant Supplement Bundle.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057626, 1)); //Oracle Ancient Relic Bundle.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057627, 1)); //Arkhal's Accessory Box.
				            break;
						} switch (Rnd.get(1, 2)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054910, 1)); //Akhal's Weapon Box.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054911, 1)); //Akhal's Armor Box.
				            break;
						}
					}
                }
            break;
        }
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		spawnDrakenseerLairRings();
		//You have entered Drakenseer's Lair.
		sendMsgByRace(1403376, Race.PC_ALL, 5000);
		Npc npc = instance.getNpc(220450); //Akhal The Oracle.
		if (npc != null) {
			SkillEngine.getInstance().getSkill(npc, 21791, 60, npc).useNoAnimationSkill(); //Turning Tide.
		}
	}
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("DRAKENSEER_LAIR")) {
		    if (!isStartTimer) {
			    isStartTimer = true;
			    System.currentTimeMillis();
			    instance.doOnAllPlayers(new Visitor<Player>() {
			        @Override
			        public void visit(Player player) {
						if (player.isOnline()) {
							startDrakenseerLairTimer();
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 600));
							//Destroy the Shielding Conduits within 10 minutes and defeat Akhal.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403377));
						}
					}
				});
			}
		}
		return false;
	}
	
	private void spawnDrakenseerLairRings() {
        FlyRing f1 = new FlyRing(new FlyRingTemplate("DRAKENSEER_LAIR", mapId,
        new Point3D(283.44757, 342.6241, 336.25607),
        new Point3D(276.73062, 339.42966, 345.29074),
        new Point3D(270.43948, 340.3889, 336.3338), 93), instanceId);
        f1.spawn();
    }
	
	protected void startDrakenseerLairTimer() {
		//Enter Drakenseer's Lair and destroy the Shielding Conduits.
		this.sendMessage(1403375, 1 * 60 * 1000);
		//You have one minute left to destroy the remaining Shielding Conduits.
		this.sendMessage(1403382, 9 * 60 * 1000);
		drakenseerLairTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        }, 600000)); //10 Minutes.
    }
	
	@Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 857974: //Balaur Abyss Gate Enhancer A.
			case 857975: //Balaur Abyss Gate Enhancer B.
			case 857976: //Balaur Abyss Gate Enhancer C.
				abyssGateEnhancerKilled++;
				if (abyssGateEnhancerKilled == 1) {
					//Two Shielding Conduits remain.
				    sendMsgByRace(1403379, Race.PC_ALL, 0);
				} else if (abyssGateEnhancerKilled == 2) {
					//One Shielding Conduit remains.
					sendMsgByRace(1403380, Race.PC_ALL, 0);
				} else if (abyssGateEnhancerKilled == 3) {
					stopDrakenseerLairTimer(player);
					//With all the Shielding Conduits destroyed, Akhal finally appears.
				    sendMsgByRace(1403381, Race.PC_ALL, 2000);
					Npc akhalTheOracle = instance.getNpc(220450); //Akhal The Oracle.
					akhalTheOracle.getEffectController().removeEffect(21791); //Turning Tide.
					instance.doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							if (player.isOnline()) {
								PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
							}
						}
					});
				}
				despawnNpc(npc);
			break;
			case 220450: //Akhal The Oracle.
			    spawn(806240, 299.1905f, 258.07004f, 319.67477f, (byte) 110); //Drakenseer's Lair Exit.
				//sendMsg("[SUCCES]: You have finished <Drakenseer's Lair>");
			break;
		}
	}
	
	protected void stopDrakenseerLairTimer(Player player) {
        stopDrakenseerLairTask();
	}
	
	private void stopDrakenseerLairTask() {
        for (FastList.Node<Future<?>> n = drakenseerLairTask.head(), end = drakenseerLairTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	private void sendMessage(final int msgId, long delay) {
        if (delay == 0) {
            this.sendMsg(msgId);
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                public void run() {
                    sendMsg(msgId);
                }
            }, delay);
        }
    }
	
	@Override
	public void onInstanceDestroy() {
		stopDrakenseerLairTask();
		isInstanceDestroyed = true;
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
}