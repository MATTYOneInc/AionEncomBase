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

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** A: Terror's Vault:
/** https://www.youtube.com/watch?v=VDMARGt33ho
/** B: Torment's Forge:
/** https://www.youtube.com/watch?v=unnAE55_M80
/** C: Hellpath:
/** https://www.youtube.com/watch?v=Wxk0vis3ZEg
/****/

@InstanceID(300610000)
public class RaksangRuinsInstance extends GeneralInstanceHandler
{
	//Terror's Vault Raid
	private Future<?> raksangRaidTaskA1;
	private Future<?> raksangRaidTaskA2;
	private int rakshaSoloSpakleA161An;
	private int rakshaSoloSkeletonS61An;
	private int rakshaSoloGraveWitchSN61An;
	//Torment's Forge Raid
	private Future<?> raksangRaidTaskB1;
	private Future<?> raksangRaidTaskB2;
	private int rakshaSoloSkeletonB161An;
	private int rakshaSoloSkeletonB261An;
	//Hellpath Raid
	private Future<?> raksangRaidTaskC1;
	private Future<?> raksangRaidTaskC2;
	private int rakshaSoloClodwormC161An;
	private int rakshaSoloClodwormC261An;
	//***//
	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnAbisoRace();
		}
    }
	
	private void SpawnAbisoRace() {
		final int abiso1 = spawnRace == Race.ASMODIANS ? 206395 : 206378;
        final int abiso2 = spawnRace == Race.ASMODIANS ? 206396 : 206379;
		final int abiso3 = spawnRace == Race.ASMODIANS ? 206397 : 206380;
		switch (Rnd.get(1, 3)) {
		    case 1:
				spawn(abiso1, 817.48f, 927.9041f, 1207.4312f, (byte) 19);
			break;
			case 2:
				spawn(abiso2, 817.48f, 927.9041f, 1207.4312f, (byte) 19);
			break;
			case 3:
				spawn(abiso3, 817.48f, 927.9041f, 1207.4312f, (byte) 19);
			break;
		}
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			//http://aion.power.plaync.com/wiki/%EB%A6%AC%EB%A9%98%ED%88%AC+-+%EB%93%9C%EB%A1%AD+%EC%95%84%EC%9D%B4%ED%85%9C
			case 236306: //Reviver Nasto.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
				switch (Rnd.get(1, 7)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053678, 1)); //Nasto's Unique Weapon Box.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053679, 1)); //Nasto's Unique Jacket Box.
				    break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053680, 1)); //Nasto's Unique Pants Box.
				    break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053681, 1)); //Nasto's Unique Pauldrons Box.
				    break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053682, 1)); //Nasto's Unique Gloves Box.
				    break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053683, 1)); //Nasto's Unique Shoes Chest.
				    break;
					case 7:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053684, 1)); //Nasto's Hero Accessory Box.
				    break;
				} switch (Rnd.get(1, 5)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080005, 2)); //Lesser Minion Contract.
					break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080006, 2)); //Greater Minion Contract.
					break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080007, 2)); //Major Minion Contract.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080008, 2)); //Cute Minion Contract.
					break;
					case 5:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190200000, 50)); //Minium.
					break;
				}
			break;
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		    case 236010: //Trained Porgus.
			    rakshaSoloSpakleA161An++;
				if (rakshaSoloSpakleA161An == 3) {
					startRaksangRaidA1Bis();
					//Prepare for combat! More enemies swarming in!
					sendMsgByRace(1402832, Race.PC_ALL, 0);
				} else if (rakshaSoloSpakleA161An == 6) {
				   startRaksangRaidA2();
				   raksangRaidTaskA1.cancel(true);
				   //Hold a little longer and you will survive.
				   sendMsgByRace(1402833, Race.PC_ALL, 0);
				   //Only a few enemies left!
				   sendMsgByRace(1402834, Race.PC_ALL, 5000);
				}
			break;
			case 236012: //Crumbling Skelesword.
			    rakshaSoloSkeletonS61An++;
				if (rakshaSoloSkeletonS61An == 4) {
					startRaksangRaidA2Bis();
					//Prepare for combat! More enemies swarming in!
					sendMsgByRace(1402832, Race.PC_ALL, 0);
				}
			break;
			case 236014: //Ragelich Adept.
			    rakshaSoloGraveWitchSN61An++;
				if (rakshaSoloGraveWitchSN61An == 4) {
					raksangRaidTaskA2.cancel(true);
					//Use the open entrance to move to the next area.
					sendMsgByRace(1402781, Race.PC_ALL, 0);
				}
			break;
			case 236019: //Trained Lava Petrahulk.
				hellpathFirstWave();
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
			break;
			case 236020: //Trained Clodworm.
			    rakshaSoloClodwormC161An++;
				if (rakshaSoloClodwormC161An == 6) {
					raksangRaidTaskC1.cancel(true);
					doors.get(107).setOpen(true);
				}
			break;
			case 236074: //Crumbling Skeleton.
			    rakshaSoloSkeletonB161An++;
				if (rakshaSoloSkeletonB161An == 6) {
					raksangRaidTaskB1.cancel(true);
					doors.get(457).setOpen(true);
				}
			break;
			case 236077: //Crumbling Skeleton.
			    rakshaSoloSkeletonB261An++;
				if (rakshaSoloSkeletonB261An == 6) {
					doors.get(64).setOpen(true);
					raksangRaidTaskB2.cancel(true);
					//Use the open entrance to move to the next area.
					sendMsgByRace(1402784, Race.PC_ALL, 0);
				}
			break;
			case 236084: //Classified Drill Camp Instructor.
				startRaksangRaidA1();
				doors.get(307).setOpen(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402780, Race.PC_ALL, 0);
				//The door cannot be opened yet.
				sendMsgByRace(1402831, Race.PC_ALL, 10000);
			break;
			case 236096: //Trained Clodworm.
			    rakshaSoloClodwormC261An++;
				if (rakshaSoloClodwormC261An == 4) {
					doors.get(324).setOpen(true);
					raksangRaidTaskC2.cancel(true);
					//Use the open entrance to move to the next area.
					sendMsgByRace(1402786, Race.PC_ALL, 0);
				}
			break;
			case 236303: //Drill Instructor Diplito.
				doors.get(294).setOpen(true);
			break;
			case 236304: //Drill Instructor Pratica.
				doors.get(118).setOpen(true);
			break;
			case 236305: //Drill Instructor Exico.
				hellpathSecondWave();
			break;
			case 236306: //Reviver Nasto.
				//sendMsg("[SUCCES]: You have finished <Raksang Ruins>");
				spawn(730445, 648.5508f, 700.05725f, 522.0487f, (byte) 80); //Raksang Exit.
			break;
		}
	}
	
   /**
	* Terror's Vault Raid A1/A2
	*/
	private void startRaksangRaidA1() {
		raksangRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
		        raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
			}
		}, 20000);
	}
	private void startRaksangRaidA1Bis() {
		raksangRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
		        raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236010, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236011, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
			}
		}, 20000);
	}
	private void startRaksangRaidA2() {
		raksangRaidTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
		        raksangRaid((Npc)spawn(236012, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236013, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236014, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236012, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236013, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236014, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
			}
		}, 20000);
	}
	private void startRaksangRaidA2Bis() {
		raksangRaidTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
		        raksangRaid((Npc)spawn(236012, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236013, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236014, 581.06055f, 224.19353f, 927.9906f, (byte) 42));
				raksangRaid((Npc)spawn(236012, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236013, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
				raksangRaid((Npc)spawn(236014, 596.07947f, 241.60663f, 927.9906f, (byte) 57));
			}
		}, 20000);
	}
	
   /**
	* Torment's Forge Raid B1/B2
	*/
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 702673: //Tombstone Of Liberation.
				despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB1();
				    }
			    }, 5000);
				raksangRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB1Bis();
				    }
			    }, 60000);
			break;
			case 702674: //Tombstone Of Liberation.
			    despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB2();
				    }
			    }, 5000);
				raksangRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB2Bis();
				    }
			    }, 60000);
			break;
			case 702675: //Tombstone Of Liberation.
			    despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB3();
				    }
			    }, 5000);
				raksangRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB3Bis();
				    }
			    }, 60000);
			break;
			case 702690: //Tombstone Of Liberation.
				despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB4();
				    }
			    }, 5000);
				raksangRaidTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB4Bis();
				    }
			    }, 60000);
			break;
			case 702691: //Tombstone Of Liberation.
				despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB6();
				    }
			    }, 5000);
				raksangRaidTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB6Bis();
				    }
			    }, 60000);
			break;
			case 702692: //Tombstone Of Liberation.
				despawnNpc(npc);
				//The switch is now operational.
				sendMsgByRace(1402782, Race.PC_ALL, 0);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402783, Race.PC_ALL, 4000);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 50000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB5();
				    }
			    }, 5000);
				raksangRaidTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						startRaksangRaidB5Bis();
				    }
			    }, 60000);
			break;
		}
	}
	
   /**
	* Torment's Forge Raid B
	*/
	public void startRaksangRaidB1() {
	    raksangRaid((Npc)spawn(236074, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
		raksangRaid((Npc)spawn(236075, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
		raksangRaid((Npc)spawn(236076, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
	}
	public void startRaksangRaidB1Bis() {
	    raksangRaid((Npc)spawn(236074, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
		raksangRaid((Npc)spawn(236075, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
		raksangRaid((Npc)spawn(236076, 963.0322f, 791.4068f, 734.0461f, (byte) 53));
	}
	public void startRaksangRaidB2() {
	    raksangRaid((Npc)spawn(236074, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
		raksangRaid((Npc)spawn(236075, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
		raksangRaid((Npc)spawn(236076, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
	}
	public void startRaksangRaidB2Bis() {
	    raksangRaid((Npc)spawn(236074, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
		raksangRaid((Npc)spawn(236075, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
		raksangRaid((Npc)spawn(236076, 962.4403f, 775.7848f, 734.05475f, (byte) 38));
	}
	public void startRaksangRaidB3() {
	    raksangRaid((Npc)spawn(236074, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
		raksangRaid((Npc)spawn(236075, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
		raksangRaid((Npc)spawn(236076, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
	}
	public void startRaksangRaidB3Bis() {
	    raksangRaid((Npc)spawn(236074, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
		raksangRaid((Npc)spawn(236075, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
		raksangRaid((Npc)spawn(236076, 941.6077f, 774.7897f, 734.0187f, (byte) 30));
	}
	public void startRaksangRaidB4() {
	    raksangRaid((Npc)spawn(236077, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
		raksangRaid((Npc)spawn(236078, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
		raksangRaid((Npc)spawn(236079, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
	}
	public void startRaksangRaidB4Bis() {
	    raksangRaid((Npc)spawn(236077, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
		raksangRaid((Npc)spawn(236078, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
		raksangRaid((Npc)spawn(236079, 989.6738f, 877.95856f, 762.55774f, (byte) 8));
	}
	public void startRaksangRaidB5() {
	    raksangRaid((Npc)spawn(236077, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
		raksangRaid((Npc)spawn(236078, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
		raksangRaid((Npc)spawn(236079, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
	}
	public void startRaksangRaidB5Bis() {
	    raksangRaid((Npc)spawn(236077, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
		raksangRaid((Npc)spawn(236078, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
		raksangRaid((Npc)spawn(236079, 995.08215f, 899.61633f, 762.55774f, (byte) 102));
	}
	public void startRaksangRaidB6() {
	    raksangRaid((Npc)spawn(236077, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
		raksangRaid((Npc)spawn(236078, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
		raksangRaid((Npc)spawn(236079, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
	}
	public void startRaksangRaidB6Bis() {
	    raksangRaid((Npc)spawn(236077, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
		raksangRaid((Npc)spawn(236078, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
		raksangRaid((Npc)spawn(236079, 1006.8747f, 894.7426f, 762.55774f, (byte) 81));
	}
	
	/**
	 * Hellpath Raid C1
	 */
	private void hellpathFirstWave() {
		//Hold a little longer and you will survive.
		sendMsgByRace(1402833, Race.PC_ALL, 60000);
		//Only a few enemies left!
		sendMsgByRace(1402834, Race.PC_ALL, 110000);
		raksangRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				raksangRaid((Npc)spawn(236020, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
				raksangRaid((Npc)spawn(236020, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
				raksangRaid((Npc)spawn(236020, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
		        raksangRaid((Npc)spawn(236021, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
		        raksangRaid((Npc)spawn(236021, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
				raksangRaid((Npc)spawn(236021, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
			}
		}, 5000);
		raksangRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				raksangRaid((Npc)spawn(236021, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
				raksangRaid((Npc)spawn(236021, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
				raksangRaid((Npc)spawn(236021, 311.0131f, 607.05383f, 146.51385f, (byte) 13));
		        raksangRaid((Npc)spawn(236020, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
		        raksangRaid((Npc)spawn(236020, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
				raksangRaid((Npc)spawn(236020, 325.99796f, 635.8432f, 146.51385f, (byte) 93));
			}
		}, 120000);
	}
	private void hellpathSecondWave() {
		//Prepare for combat! More enemies swarming in!
		sendMsgByRace(1402832, Race.PC_ALL, 4000);
		//Hold a little longer and you will survive.
		sendMsgByRace(1402833, Race.PC_ALL, 60000);
		//Only a few enemies left!
		sendMsgByRace(1402834, Race.PC_ALL, 110000);
		raksangRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				raksangRaid((Npc)spawn(236096, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
				raksangRaid((Npc)spawn(236097, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
				raksangRaid((Npc)spawn(236098, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
		        raksangRaid((Npc)spawn(236096, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
		        raksangRaid((Npc)spawn(236097, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
				raksangRaid((Npc)spawn(236098, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
			}
		}, 5000);
		raksangRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				raksangRaid((Npc)spawn(236096, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
				raksangRaid((Npc)spawn(236097, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
				raksangRaid((Npc)spawn(236099, 322.56607f, 777.8472f, 148.35696f, (byte) 13));
		        raksangRaid((Npc)spawn(236096, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
		        raksangRaid((Npc)spawn(236097, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
				raksangRaid((Npc)spawn(236099, 334.89322f, 801.4367f, 146.65071f, (byte) 92));
			}
		}, 120000);
	}
	
	private void raksangRaid(final Npc npc) {
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
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
}