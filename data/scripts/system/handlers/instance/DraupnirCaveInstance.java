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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: http://aion.power.plaync.com/wiki/%EB%93%9C%EB%9D%BC%EC%9B%81%EB%8B%88%EB%A5%B4+%EB%8F%99%EA%B5%B4+-+%EB%A7%88%EC%8A%A4%ED%84%B0+%EB%B3%B4%EC%8A%A4
/****/

@InstanceID(320080000)
public class DraupnirCaveInstance extends GeneralInstanceHandler
{
	//**Npc 4.9**//
	private Race spawnRace;
	private int bakarmaCharger;
	private int adjutantsKilled;
	private Future<?> abyssGateTask;
	protected boolean isInstanceDestroyed = false;
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		//You must kill Afrane, Saraswati, Lakshmi, and Nimbarka to make Commander Bakarma appear.
		sendMsgByRace(1400757, Race.PC_ALL, 10000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				spawn(237276, 495.48535f, 392.0867f, 616.5717f, (byte) 89); //Akhal's Phantasm.
			}
		}, 10000);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnIDDF3DragonSP();
		}
	}
	
	private void SpawnIDDF3DragonSP() {
		final int npc1 = spawnRace == Race.ASMODIANS ? 805737 : 805736;
		spawn(npc1, 498.74973f, 379.33267f, 621.2866f, (byte) 54);
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 702658: //Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); //[Event] Abbey Bundle.
		    break;
			case 702659: //Noble Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); //[Event] Noble Abbey Bundle.
		    break;
			case 213780: //Commander Bakarma.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053265, 1)); //Bakarma's Fabled Weapon Box.
					    break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053271, 1)); //Bakarma's Weapon Box.
					    break;
					}
				}
			break;
			case 237275: //Akhal.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054175, 1)); //Master Bakarma's Weapon Box.
					}
				}
			break;
		}
	}
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		switch (Rnd.get(1, 4)) {
		    case 1:
				spawn(213587, 567.438f, 700.875f, 538.701f, (byte) 7); //Hungry Ooze.
			break;
			case 2:
				spawn(213588, 166.8f, 536.285f, 505.802f, (byte) 9); //Lucky Golden Saam.
			break;
			case 3:
				spawn(213771, 497.006f, 434.713f, 616.584f, (byte) 71); //Protector Rakkan.
			break;
			case 4:
				spawn(213773, 380.694f, 611.956f, 598.523f, (byte) 98); //Dragonpriest Tairgus.
			break;
        }
    }
	
    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 213776: //Instructor Afrane.
			case 213778: //Beautiful Lakshmi.
			case 213779: //Commander Nimbarka.
			case 213802: //Kind Saraswati.
				adjutantsKilled++;
				if (adjutantsKilled == 1) {
					//You must kill 3 more Adjutants to make Commander Bakarma appear.
				    sendMsgByRace(1400758, Race.PC_ALL, 0);
				} else if (adjutantsKilled == 2) {
					//You must kill 2 more Adjutants to make Commander Bakarma appear.
				    sendMsgByRace(1400759, Race.PC_ALL, 0);
				} else if (adjutantsKilled == 3) {
					//You must kill 1 more Adjutant to make Commander Bakarma appear.
				    sendMsgByRace(1400760, Race.PC_ALL, 0);
				} else if (adjutantsKilled == 4) {
					spawnCommanderBakarma();
					//Commander Bakarma has appeared at Beritra's Oracle.
				    sendMsgByRace(1400751, Race.PC_ALL, 0);
					deleteNpc(214026); //Deputy Brigade General Yavant.
				}
			break;
			case 213780: //Commander Bakarma.
				sendMsg("[SUCCES]: You have finished <Draupnir Cave>");
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 787.32513f, 431.49173f, 319.62155f, (byte) 33); //Abbey Box.
					break;
					case 2:
					    spawn(702659, 787.32513f, 431.49173f, 319.62155f, (byte) 33); //Noble Abbey Box.
					break;
				}
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawnAkhal();
						//A powerful Balaur has appeared in Beritra's Oracle Chamber.
						sendMsgByRace(1403068, Race.PC_ALL, 0);
					}
				}, 60000);
			break;
			case 236900: //Bakarma Charger.
			    bakarmaCharger++;
				if (bakarmaCharger == 18) {
					abyssGateTask.cancel(true);
					//The Abyss Gate Enhancer has been neutralized.
					sendMsgByRace(1403065, Race.PC_ALL, 0);
				}
			break;
        }
    }
	
   /**
	* Central Control Room Raid.
	*/
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 702857: //Balaur Abyss Gate Enhancer.
				despawnNpc(npc);
				//Balaur are swarming to defend the Abyss Gate Enhancer.
				sendMsgByRace(1403063, Race.PC_ALL, 0);
				//The Balaur have been alerted to the presence of intruders.
				sendMsgByRace(1403064, Race.PC_ALL, 4000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startAbyssGateRaid1();
				    }
			    }, 5000);
				abyssGateTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						//Balaur are swarming to defend the Abyss Gate Enhancer.
						sendMsgByRace(1403063, Race.PC_ALL, 0);
						startAbyssGateRaid2();
				    }
			    }, 60000);
			break;
			case 702858: //Balaur Abyss Gate Booster.
			    despawnNpc(npc);
				//Find and overload the Abyss Gate Enhancer in the Central Control Room.
				sendMsgByRace(1403058, Race.PC_ALL, 0);
				//The Balaur's Abyss Gate Enhancer is active.
				//The enhancer protection device will activate in 3 minutes, preventing it from being destroyed.
				sendMsgByRace(1403081, Race.PC_ALL, 5000);
				spawn(702857, 469.00000f, 563.0000f, 510.49686f, (byte) 29); //Balaur Abyss Gate Enhancer.
				spawn(702857, 511.36166f, 591.0183f, 510.60300f, (byte) 60); //Balaur Abyss Gate Enhancer.
				spawn(702857, 466.00000f, 617.0000f, 511.22543f, (byte) 96); //Balaur Abyss Gate Enhancer.
			break;
		}
	}
	
	private void spawnCommanderBakarma() {
		spawn(213780, 777.46985f, 431.09888f, 321.7541f, (byte) 62); //Commander Bakarma.
	}
	
	private void spawnAkhal() {
		spawn(237275, 777.46985f, 431.09888f, 321.7541f, (byte) 62); //Akhal.
	}
	
	public void startAbyssGateRaid1() {
	    abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
		abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
		abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
	}
	
	public void startAbyssGateRaid2() {
	    abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
		abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
		abyssGateRaid((Npc)spawn(236900, 514.45465f, 614.66077f, 515.35785f, (byte) 67));
	}
	
	private void abyssGateRaid(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player: instance.getPlayersInside()) {
						npc.setTarget(player);
						((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
						npc.setState(1);
						npc.getMoveController().moveToTargetObject();
						PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
					}
				}
			}
		}, 1000);
	}
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
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
}