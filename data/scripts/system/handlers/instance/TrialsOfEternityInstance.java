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

import javolution.util.FastList;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=V8Rf-d6GnVc
/** Final Boss: https://www.youtube.com/watch?v=LpEJVS5gyqk
/****/

@InstanceID(301560000)
public class TrialsOfEternityInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private Race videoRace;
	private long startTime;
	private long instanceTime;
	private Future<?> fakeBook2Task;
	private Future<?> instanceTimer;
	private boolean isInstanceDestroyed;
	private int protectorOfResurrectionAgaldima;
	private Map<Integer, StaticDoor> doors;
	//Boss Wave.
	private Future<?> trialsOfEternityTaskA1;
	private Future<?> trialsOfEternityTaskA2;
	private Future<?> trialsOfEternityTaskA3;
	private Future<?> trialsOfEternityTaskA4;
	private Future<?> trialsOfEternityTaskA5;
	private Future<?> trialsOfEternityTaskA6;
	private Future<?> trialsOfEternityTaskA7;
	private Future<?> trialsOfEternityTaskA8;
	private List<Integer> movies = new ArrayList<Integer>();
	private List<Npc> ScatteredEnergyBook = new ArrayList<Npc>();
	private final FastList<Future<?>> trialsOfEternityTask = FastList.newInstance();
	private FastMap<Integer, VisibleObject> trialsShield = new FastMap<Integer, VisibleObject>();
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 246410: //Essence Guardian Fleshgolem.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000297, 1));
				    }
				}
		    break;
			case 731751: //Real Trials Of Eternity Book 2.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						switch (Rnd.get(1, 4)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000298, 1));
				            break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000299, 1));
							break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000300, 1));
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000301, 1));
							break;
						}
				    }
				}
			break;
			case 835409: //Nakaring Treasure Chest.
			    //https://aionpowerbook.com/powerbook/Elegant_Peacock_Feathers_Set
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						switch (Rnd.get(1, 6)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 110000064, 1)); //우아한 공작 깃털 �?�?�.
				            break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 111000105, 1)); //우아한 공작 깃털 장갑.
							break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 112000066, 1)); //우아한 공작 깃털 견갑.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 113000069, 1)); //우아한 공작 깃털 하�?�.
							break;
							case 5:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 114000107, 1)); //우아한 공작 깃털 신발.
							break;
							case 6:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 125005216, 1)); //우아한 공작 깃털 �?건.
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
		doors = instance.getDoors();
		scatteredYellowEnergyBook();
		spawnTrialsOfEternityRings();
		SpawnTemplate IDEternity03Shield1 = SpawnEngine.addNewSingleTimeSpawn(301560000, 700998, 1186.9253f, 1026.0826f, 761.41602f, (byte) 0);
		IDEternity03Shield1.setEntityId(133);
		trialsShield.put(700998, SpawnEngine.spawnObject(IDEternity03Shield1, instanceId));
	}
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("TRIALS_OF_ETERNITY")) {
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
						startTrialsOfEternityTimer();
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 600)); //10 Minutes.
					}
				}
			});
		}
		return false;
	}
	
	private void spawnTrialsOfEternityRings() {
        FlyRing f1 = new FlyRing(new FlyRingTemplate("TRIALS_OF_ETERNITY", mapId,
        new Point3D(810.5852, 1323.6439, 735.4228),
		new Point3D(810.11584, 1328.6671, 743.01385),
        new Point3D(810.4953, 1333.4171, 735.4228), 60), instanceId);
        f1.spawn();
    }
	
	private void sendPacket(Player player, final String variable, final int value) {
		instance.doOnAllPlayers(new Visitor<Player>() {
		    @Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, new SM_CONDITION_VARIABLE(player, variable, value));
				}
			}
		});
	}
	
	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		//Netherworld Magic.
		sendPacket(player, "UI_Gauge_01", 0 + 1);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
		    instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					deleteNpc(700998);
					//Boliag is channeling dark magic to cause an eruption!
					sendMsgByRace(1404379, Race.PC_ALL, 120000);
					//The ground is shaking... You should hurry!
					sendMsgByRace(1404148, Race.PC_ALL, 240000);
				}
			}, 60000);
		} if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnTrialsOfEternityRace();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				fallingRock();
			}
		}, 300000); //...5Min
	}
	
	//Scattered Yellow Energy Book.
	private void scatteredYellowEnergyBook() {
		ScatteredEnergyBook.add((Npc) spawn(731752, 712.32526f, 1270.5907f, 735.4228f, (byte) 9));
		ScatteredEnergyBook.add((Npc) spawn(731752, 775.016f, 1290.2628f, 735.4228f, (byte) 58));
		ScatteredEnergyBook.add((Npc) spawn(731752, 668.7447f, 1327.0892f, 735.4228f, (byte) 109));
		ScatteredEnergyBook.add((Npc) spawn(731752, 741.6452f, 1391.0905f, 735.4228f, (byte) 88));
		ScatteredEnergyBook.add((Npc) spawn(731752, 723.69275f, 1389.2625f, 735.4228f, (byte) 96));
		ScatteredEnergyBook.add((Npc) spawn(731752, 668.0599f, 1297.0428f, 735.4228f, (byte) 0));
		ScatteredEnergyBook.add((Npc) spawn(731752, 712.63116f, 1391.3739f, 735.4228f, (byte) 100));
		ScatteredEnergyBook.add((Npc) spawn(731752, 781.0233f, 1359.4529f, 735.4228f, (byte) 59));
		ScatteredEnergyBook.add((Npc) spawn(731752, 776.95374f, 1295.8823f, 735.4228f, (byte) 60));
		ScatteredEnergyBook.add((Npc) spawn(731752, 674.5508f, 1285.0021f, 735.4228f, (byte) 20));
		ScatteredEnergyBook.add((Npc) spawn(731752, 739.11975f, 1266.0924f, 735.4228f, (byte) 41));
		ScatteredEnergyBook.add((Npc) spawn(731752, 706.4169f, 1269.0189f, 735.4228f, (byte) 23));
		ScatteredEnergyBook.add((Npc) spawn(731752, 735.7693f, 1387.3572f, 735.4228f, (byte) 92));
		ScatteredEnergyBook.add((Npc) spawn(731752, 780.49054f, 1340.2495f, 735.4228f, (byte) 114));
		ScatteredEnergyBook.add((Npc) spawn(731752, 734.4574f, 1318.4962f, 733.9775f, (byte) 15));
		ScatteredEnergyBook.add((Npc) spawn(731752, 715.1528f, 1318.7916f, 733.9775f, (byte) 3));
		ScatteredEnergyBook.add((Npc) spawn(731752, 748.0488f, 1337.4724f, 733.9775f, (byte) 117));
		ScatteredEnergyBook.add((Npc) spawn(731752, 695.9409f, 1333.3064f, 733.9775f, (byte) 3));
		ScatteredEnergyBook.add((Npc) spawn(731752, 742.9203f, 1285.7396f, 735.4228f, (byte) 30));
		ScatteredEnergyBook.add((Npc) spawn(731752, 683.3152f, 1322.9395f, 735.4228f, (byte) 111));
		ScatteredEnergyBook.add((Npc) spawn(731752, 770.1084f, 1365.091f, 735.4228f, (byte) 73));
		ScatteredEnergyBook.add((Npc) spawn(731752, 672.815f, 1368.174f, 735.4228f, (byte) 111));
		ScatteredEnergyBook.add((Npc) spawn(731752, 718.09375f, 1355.8845f, 733.9775f, (byte) 33));
		ScatteredEnergyBook.add((Npc) spawn(731752, 696.08844f, 1317.2058f, 733.9775f, (byte) 82));
		ScatteredEnergyBook.add((Npc) spawn(731752, 664.81006f, 1356.33f, 735.4228f, (byte) 90));
	}
	
	private void startTrialsOfEternityTimer() {
		//The book was destroyed.
		this.sendMessage(1404208, 10 * 60 * 1000);
		fakeBook2Task = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Fake Book.
				ScatteredEnergyBook.get(0).getController().onDelete();
				ScatteredEnergyBook.get(1).getController().onDelete();
				ScatteredEnergyBook.get(2).getController().onDelete();
				ScatteredEnergyBook.get(3).getController().onDelete();
				ScatteredEnergyBook.get(4).getController().onDelete();
				ScatteredEnergyBook.get(5).getController().onDelete();
				ScatteredEnergyBook.get(6).getController().onDelete();
				ScatteredEnergyBook.get(7).getController().onDelete();
				ScatteredEnergyBook.get(8).getController().onDelete();
				ScatteredEnergyBook.get(9).getController().onDelete();
				ScatteredEnergyBook.get(10).getController().onDelete();
				ScatteredEnergyBook.get(11).getController().onDelete();
				ScatteredEnergyBook.get(12).getController().onDelete();
				ScatteredEnergyBook.get(13).getController().onDelete();
				ScatteredEnergyBook.get(14).getController().onDelete();
				ScatteredEnergyBook.get(15).getController().onDelete();
				ScatteredEnergyBook.get(16).getController().onDelete();
				ScatteredEnergyBook.get(17).getController().onDelete();
				ScatteredEnergyBook.get(18).getController().onDelete();
				ScatteredEnergyBook.get(19).getController().onDelete();
				ScatteredEnergyBook.get(20).getController().onDelete();
				ScatteredEnergyBook.get(21).getController().onDelete();
				ScatteredEnergyBook.get(22).getController().onDelete();
				ScatteredEnergyBook.get(23).getController().onDelete();
			}
		}, 600000); //10 Minutes.
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 246402: //Protector of Resurrection Agaldima.
			    protectorOfResurrectionAgaldima++;
				if (protectorOfResurrectionAgaldima == 2) {
					deleteNpc(731766); //Book Shield.
					//Kisk Point A
					spawn(835392, 1195.3578f, 1034.3889f, 761.4341f, (byte) 0);
					//Kisk Point B
					spawn(835393, 933.1394f, 1060.0201f, 751.92633f, (byte) 75);
					//Kisk Point C
					spawn(835394, 915.826f, 1339.96f, 747.7376f, (byte) 90);
					//Kisk Point D
					spawn(835395, 566.4959f, 1317.885f, 721.9022f, (byte) 15);
					//Kisk Point E
					spawn(835396, 549.6273f, 1127.4685f, 710.9765f, (byte) 75);
				}
			break;
			case 246416: //Unyielding Guardian Shardgolem.
			    deleteNpc(731761);
				doors.get(846).setOpen(true);
			    doors.get(962).setOpen(true);
			    final int Zarik_Ostia2 = spawnRace == Race.ASMODIANS ? 806575 : 806566;
                spawn(Zarik_Ostia2, 533.7936f, 1007.8767f, 711.9952f, (byte) 22);
			break;
			case 246418: //Ganesh.
			    deleteNpc(731762);
				//Ganesh's Gateway.
			    final int Peregrine_Viola2 = spawnRace == Race.ASMODIANS ? 806576 : 806567;
                spawn(Peregrine_Viola2, 392.01886f, 1017.03314f, 711.8558f, (byte) 11);
				final int Gampt_Kontesius2 = spawnRace == Race.ASMODIANS ? 806577 : 806568;
                spawn(Gampt_Kontesius2, 390.57996f, 1019.0326f, 711.857f, (byte) 11);
                //Library Of Insight.
				final int Kontesius_Gampt = spawnRace == Race.ASMODIANS ? 246425 : 246431;
				final int Ostia_Zarik = spawnRace == Race.ASMODIANS ? 246426 : 246432;
				final int SlaughtererGuard = spawnRace == Race.ASMODIANS ? 246412 : 246414;
				spawn(Kontesius_Gampt, 216.47931f, 1025.3909f, 706.75494f, (byte) 0);
				spawn(Ostia_Zarik, 216.47931f, 1025.3909f, 706.75494f, (byte) 0);
				spawn(SlaughtererGuard, 218.27975f, 1022.4601f, 706.75494f, (byte) 0);
				spawn(SlaughtererGuard, 218.33261f, 1028.3926f, 706.75494f, (byte) 0);
				spawn(SlaughtererGuard, 215.11415f, 1030.633f, 706.75494f, (byte) 0);
				spawn(SlaughtererGuard, 215.0843f, 1020.15955f, 706.75494f, (byte) 0);
		        final int SageGuard = spawnRace == Race.ASMODIANS ? 247032 : 247034;
				spawn(SageGuard, 333.16205f, 1025.4404f, 710.8191f, (byte) 0);
				spawn(SageGuard, 289.8541f, 1025.3204f, 710.87256f, (byte) 0);
			break;
			case 246425: //Kontesius.
			case 246426: //Ostia.
			    despawnNpc(npc);
				sendMovie(player, 955);
				startInstanceTask();
				//The Elyos Royal Guard Captain is retreating.
				sendMsgByRace(1404258, Race.PC_ALL, 0);
			break;
			case 246431: //Gampt.
			case 246432: //Zarik.
			    despawnNpc(npc);
				sendMovie(player, 952);
				startInstanceTask();
				//The Asmodian Royal Guard Captain is retreating.
				sendMsgByRace(1404257, Race.PC_ALL, 0);
			break;
			case 246440: //Boliag.
			    final int dimensionBossVideo = videoRace == Race.ASMODIANS ? 956 : 954;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, dimensionBossVideo));
				final int Peregrine_Viola3 = spawnRace == Race.ASMODIANS ? 806578 : 806569;
                spawn(Peregrine_Viola3, 234.02586f, 1025.4587f, 706.75494f, (byte) 60);
				final int Zarik_Ostia3 = spawnRace == Race.ASMODIANS ? 806579 : 806570;
                spawn(Zarik_Ostia3, 231.80095f, 1022.01776f, 706.75494f, (byte) 53);
				final int Gampt_Kontesius3 = spawnRace == Race.ASMODIANS ? 806580 : 806571;
                spawn(Gampt_Kontesius3, 231.91643f, 1028.9543f, 706.75494f, (byte) 67);
				spawn(806581, 397.44950f, 1025.2417f, 711.88477f, (byte) 0); //Ganesh.
				spawn(731744, 236.54419f, 1025.4613f, 706.75494f, (byte) 60); //Exit.
				spawn(835408, 222.55490f, 1031.6270f, 706.75494f, (byte) 15); //Treasure Chest A.
				spawn(835409, 222.53116f, 1019.2116f, 706.75494f, (byte) 105); //Treasure Chest B.
				sendMsg("[SUCCES]: You have finished <Trials Of Eternity>");
			break;
		   /**
			* You will now receive "1,200 GP" instead of "200 Gp" for killing "Heart Of Boliag" in the Trials of Eternity instance.
			* http://aionpowerbook.com/powerbook/KR_-_Update_January_18th_2017
			*/
			case 246441: //Heart Of Boliag.
			    AbyssPointsService.addGp(player, 1200);
			break;
		}
	}
	
	//============================//
	//** Wave Before Boss Fight **//
	//============================//
	protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 15000);
				sp(246724, 238.16855f, 1047.1934f, 707.12958f, (byte) 0, 1018, 0, 0, null);
				sp(246724, 204.77652f, 1044.5760f, 706.77118f, (byte) 0, 991, 3000, 0, null);
				sp(246724, 238.52361f, 1004.6866f, 707.12933f, (byte) 0, 1016, 6000, 0, null);
            }
        }, 60000)); //...1Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA1();
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				sendMsg("[START]: Wave <1/8>");
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 1020)); //17 Minutes.
						}
					}
				});
            }
        }, 90000)); //...1-30Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA2();
				sendMsg("[START]: Wave <2/8>");
				trialsOfEternityTaskA1.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 180000)); //...3Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA3();
				sendMsg("[START]: Wave <3/8>");
				trialsOfEternityTaskA2.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 300000)); //...5Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA4();
				sendMsg("[START]: Wave <4/8>");
				trialsOfEternityTaskA3.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 420000)); //...7Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA5();
				sendMsg("[START]: Wave <5/8>");
				trialsOfEternityTaskA4.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 540000)); //...9Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA6();
				sendMsg("[START]: Wave <6/8>");
				trialsOfEternityTaskA5.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 660000)); //...11Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA7();
				sendMsg("[START]: Wave <7/8>");
				trialsOfEternityTaskA6.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 780000)); //...13Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startTrialsOfEternityA8();
				sendMsg("[START]: Wave <8/8>");
				trialsOfEternityTaskA7.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 900000)); //...15Min
		trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						stopInstance(player);
						dimensionBoss01(player);
						trialsOfEternityTaskA8.cancel(true);
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
						}
				    }
			    });
            }
        }, 1020000)); //...17Min
    }
	
	protected void stopInstance(Player player) {
		stopInstanceTask();
		sendMsg("[SUCCES]: You survived !!! :) ");
	}
	
	protected void dimensionBoss01(Player player) {
		//Boliag.
		sp(246440, 189.53326f, 1025.3595f, 707.59015f, (byte) 0, 0, 40000, 0, null);
		final int endVideo = videoRace == Race.ASMODIANS ? 964 : 953;
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, endVideo));
	}
	
	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000) {
			return (int) (60000 - result);
		} else if (result < 1020000) { //...17Min
			return (int) (1020000 - (result - 60000));
		}
		return 0;
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 731736: //Restricted Library Entrance.
			    if (player.getInventory().decreaseByItemId(185000297, 1)) {
				    TrialsOfEternityTeleporter(player, 522.7508f, 1217.3593f, 724.3436f, (byte) 61);
				} else {
					//You need a restricted library admission ticket.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404075));
				}
			break;
			case 731750: //Scattered Blue Energy Book.
			case 731752: //Scattered Yellow Energy Book.
			    switch (Rnd.get(1, 4)) {
				    case 1:
				        ItemService.addItem(player, 185000298, 1); //Blue Tome.
					break;
					case 2:
				        ItemService.addItem(player, 185000299, 1); //Red Tome.
					break;
					case 3:
				        ItemService.addItem(player, 185000300, 1); //Yellow Tome.
					break;
					case 4:
				        ItemService.addItem(player, 185000301, 1); //Black Tome.
					break;
				}
				despawnNpc(npc);
			break;
		}
	}
	
	protected void TrialsOfEternityTeleporter(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	
	private void SpawnTrialsOfEternityRace() {
		//Npc.
		final int Peregrine_Viola1 = spawnRace == Race.ASMODIANS ? 806572 : 806563;
		final int Gampt_Kontesius1 = spawnRace == Race.ASMODIANS ? 806573 : 806564;
		final int Zarik_Ostia1 = spawnRace == Race.ASMODIANS ? 806574 : 806565;
		//Quest Book.
		final int QuestBook = spawnRace == Race.ASMODIANS ? 703455 : 703454;
		//Guard Start.
		final int IDEternity03StartGuardKn = spawnRace == Race.ASMODIANS ? 246390 : 246388;
		final int IDEternity03StartGuardWi = spawnRace == Race.ASMODIANS ? 246391 : 246389;
		//Event Guard.
		final int IDEternity03Guard01 = spawnRace == Race.ASMODIANS ? 246715 : 246719;
		final int IDEternity03Guard02 = spawnRace == Race.ASMODIANS ? 246716 : 246720;
		final int IDEternity03Guard03 = spawnRace == Race.ASMODIANS ? 246717 : 246721;
		final int IDEternity03AmbushGuardKn = spawnRace == Race.ASMODIANS ? 246738 : 246742;
		final int IDEternity03EventGuardKn = spawnRace == Race.ASMODIANS ? 246412 : 246414;
		final int IDEternity03EventGuardWi = spawnRace == Race.ASMODIANS ? 246413 : 246415;
		//Npc.
		spawn(Peregrine_Viola1, 1195.9297f, 1017.4283f, 761.1656f, (byte) 0);
		spawn(Gampt_Kontesius1, 1195.9012f, 1019.9607f, 761.1656f, (byte) 0);
		spawn(Zarik_Ostia1, 1195.9429f, 1014.75854f, 761.1656f, (byte) 0);
		//Quest Book.
		spawn(QuestBook, 552.68518f, 883.55646f, 702.53864f, (byte) 0, 23);
		//Guard Start.
		spawn(IDEternity03StartGuardKn, 1206.8582f, 1014.1617f, 761.1656f, (byte) 119);
		spawn(IDEternity03StartGuardKn, 1204.0542f, 1014.14825f, 761.1656f, (byte) 0);
		spawn(IDEternity03StartGuardKn, 1204.0986f, 1011.7326f, 761.1656f, (byte) 0);
		spawn(IDEternity03StartGuardKn, 1206.8433f, 1011.7308f, 761.1656f, (byte) 119);
		spawn(IDEternity03StartGuardWi, 1200.9108f, 1014.10596f, 761.1656f, (byte) 0);
		spawn(IDEternity03StartGuardWi, 1200.8986f, 1011.8001f, 761.1656f, (byte) 2);
		spawn(IDEternity03StartGuardWi, 1209.6967f, 1011.7956f, 761.1656f, (byte) 0);
		spawn(IDEternity03StartGuardWi, 1209.6384f, 1014.19434f, 761.1656f, (byte) 0);
		//Event Guard.
		spawn(IDEternity03Guard01, 837.4585f, 1328.4395f, 735.832f, (byte) 60);
		spawn(IDEternity03Guard02, 566.9962f, 1328.5254f, 721.5761f, (byte) 60);
		spawn(IDEternity03Guard03, 539.8887f, 1107.7954f, 710.5968f, (byte) 90);
		spawn(IDEternity03AmbushGuardKn, 929.2876f, 1058.0618f, 751.6501f, (byte) 74);
		spawn(IDEternity03AmbushGuardKn, 936.4944f, 1058.0548f, 751.6501f, (byte) 105);
		spawn(IDEternity03AmbushGuardKn, 877.19055f, 1331.6013f, 737.24585f, (byte) 60);
        spawn(IDEternity03AmbushGuardKn, 877.21265f, 1325.3414f, 737.26855f, (byte) 60);
		spawn(IDEternity03AmbushGuardKn, 605.7254f, 1331.877f, 726.72296f, (byte) 60);
        spawn(IDEternity03AmbushGuardKn, 605.8276f, 1325.6444f, 726.7673f, (byte) 60);
		spawn(IDEternity03EventGuardKn, 842.239f, 1324.3356f, 735.835f, (byte) 60);
		spawn(IDEternity03EventGuardKn, 577.5211f, 1324.2075f, 721.57935f, (byte) 60);
		spawn(IDEternity03EventGuardKn, 577.25824f, 1332.9463f, 721.5794f, (byte) 61);
		spawn(IDEternity03EventGuardKn, 544.49036f, 1109.382f, 710.65924f, (byte) 90);
		spawn(IDEternity03EventGuardKn, 542.3867f, 1105.893f, 710.59845f, (byte) 90);
		spawn(IDEternity03EventGuardWi, 842.09216f, 1332.7976f, 735.8352f, (byte) 60);
		spawn(IDEternity03EventGuardWi, 573.8743f, 1328.6174f, 721.5761f, (byte) 60);
		spawn(IDEternity03EventGuardWi, 535.5834f, 1108.8912f, 710.60004f, (byte) 90);
		spawn(IDEternity03EventGuardWi, 537.58624f, 1105.864f, 710.5986f, (byte) 90);
	}
	
	private void rushTrialsOfEternity(final Npc npc) {
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
	
	private void startTrialsOfEternityA1() {
		trialsOfEternityTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246439, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246439, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246439, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
				rushTrialsOfEternity((Npc)spawn(246439, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246439, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246439, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA2() {
		trialsOfEternityTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246437, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246437, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246437, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
				rushTrialsOfEternity((Npc)spawn(246439, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246439, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246439, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA3() {
		trialsOfEternityTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246437, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246437, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246437, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
				rushTrialsOfEternity((Npc)spawn(246437, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246437, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246437, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
				//Portal Right.
				rushTrialsOfEternity((Npc)spawn(246437, 235.17462f, 1046.58740f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246437, 238.82994f, 1043.69760f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246437, 235.73709f, 1043.49000f, 706.75494f, (byte) 77));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA4() {
		trialsOfEternityTaskA4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246437, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246437, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246437, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
				rushTrialsOfEternity((Npc)spawn(246437, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246437, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246437, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA5() {
		trialsOfEternityTaskA5 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246439, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246439, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246439, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
                rushTrialsOfEternity((Npc)spawn(246438, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
				//Portal Right.
				rushTrialsOfEternity((Npc)spawn(246437, 235.17462f, 1046.58740f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246437, 238.82994f, 1043.69760f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246437, 235.73709f, 1043.49000f, 706.75494f, (byte) 77));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA6() {
		trialsOfEternityTaskA6 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246439, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246439, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246439, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
                rushTrialsOfEternity((Npc)spawn(246438, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
				//Portal Right.
				rushTrialsOfEternity((Npc)spawn(246439, 235.17462f, 1046.58740f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246439, 238.82994f, 1043.69760f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246439, 235.73709f, 1043.49000f, 706.75494f, (byte) 77));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA7() {
		trialsOfEternityTaskA7 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246439, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246439, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246439, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
                rushTrialsOfEternity((Npc)spawn(246437, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246437, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246437, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
				//Portal Right.
				rushTrialsOfEternity((Npc)spawn(246439, 235.17462f, 1046.58740f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246438, 238.82994f, 1043.69760f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246439, 235.73709f, 1043.49000f, 706.75494f, (byte) 77));
			}
		}, 1000);
	}
	private void startTrialsOfEternityA8() {
		trialsOfEternityTaskA8 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Portal Left.
				rushTrialsOfEternity((Npc)spawn(246437, 239.36716f, 1007.58734f, 706.75494f, (byte) 43));
                rushTrialsOfEternity((Npc)spawn(246437, 235.87540f, 1004.97577f, 706.75494f, (byte) 42));
                rushTrialsOfEternity((Npc)spawn(246437, 236.04263f, 1008.56990f, 706.75494f, (byte) 43));
				//Portal Middle.
                rushTrialsOfEternity((Npc)spawn(246437, 203.97627f, 1041.26380f, 706.75494f, (byte) 105));
                rushTrialsOfEternity((Npc)spawn(246438, 207.45520f, 1044.34670f, 706.75494f, (byte) 103));
                rushTrialsOfEternity((Npc)spawn(246437, 207.32959f, 1040.83100f, 706.75494f, (byte) 104));
				//Portal Right.
				rushTrialsOfEternity((Npc)spawn(246437, 235.17462f, 1046.58740f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246438, 238.82994f, 1043.69760f, 706.75494f, (byte) 78));
                rushTrialsOfEternity((Npc)spawn(246437, 235.73709f, 1043.49000f, 706.75494f, (byte) 77));
			}
		}, 1000);
	}
	
	private void fallingRock() {
		spawn(246452, 1025.4661f, 1015.3299f, 750.54456f, (byte) 44);
        spawn(246452, 1016.78424f, 1025.8098f, 750.57355f, (byte) 103);
        spawn(246452, 1026.4714f, 1038.438f, 750.1689f, (byte) 75);
        spawn(246452, 1028.5117f, 1044.0419f, 750.1689f, (byte) 26);
        spawn(246452, 1002.37286f, 1043.6503f, 750.3926f, (byte) 50);
        spawn(246452, 1013.1662f, 1040.4281f, 750.29016f, (byte) 1);
        spawn(246452, 1001.4438f, 1005.8842f, 750.04895f, (byte) 92);
        spawn(246452, 999.8123f, 1019.4888f, 749.8897f, (byte) 92);
        spawn(246452, 979.3912f, 1012.49664f, 750.8312f, (byte) 101);
        spawn(246452, 979.1976f, 1024.3999f, 750.5997f, (byte) 28);
        spawn(246452, 960.4141f, 1031.7275f, 749.82764f, (byte) 38);
        spawn(246452, 962.66327f, 1045.5558f, 749.9464f, (byte) 5);
        spawn(246452, 952.68506f, 1041.9098f, 749.9464f, (byte) 74);
        spawn(246452, 955.19763f, 1017.42053f, 749.2399f, (byte) 14);
        spawn(246452, 943.045f, 1009.04126f, 749.12537f, (byte) 4);
        spawn(246452, 930.24396f, 1022.2551f, 749.4728f, (byte) 36);
        spawn(246452, 927.5648f, 1012.25476f, 749.66425f, (byte) 4);
        spawn(246452, 912.5281f, 1014.4573f, 750.0069f, (byte) 116);
        spawn(246452, 908.76807f, 1021.7782f, 750.1894f, (byte) 15);
        spawn(246452, 899.9093f, 1013.19543f, 750.02423f, (byte) 65);
        spawn(246452, 887.37384f, 1036.8839f, 751.03186f, (byte) 35);
        spawn(246452, 889.4028f, 1031.9984f, 751.0289f, (byte) 98);
        spawn(246452, 884.1124f, 1016.5624f, 751.2453f, (byte) 0);
        spawn(246452, 871.3897f, 1018.73535f, 751.3101f, (byte) 63);
        spawn(246452, 872.3457f, 1026.4879f, 751.1764f, (byte) 45);
        spawn(246452, 980.85156f, 1040.8524f, 750.4177f, (byte) 79);
        spawn(246452, 986.8397f, 1050.9484f, 750.418f, (byte) 20);
        spawn(246452, 939.2889f, 1040.133f, 750.68445f, (byte) 119);
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = trialsOfEternityTask.head(), end = trialsOfEternityTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        trialsOfEternityTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
    }
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
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
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000297, storage.getItemCountByItemId(185000297));
		storage.decreaseByItemId(185000298, storage.getItemCountByItemId(185000298));
		storage.decreaseByItemId(185000299, storage.getItemCountByItemId(185000299));
		storage.decreaseByItemId(185000300, storage.getItemCountByItemId(185000300));
		storage.decreaseByItemId(185000301, storage.getItemCountByItemId(185000301));
	}
	
	@Override
    public void onPlayerLogOut(Player player) {
        removeItems(player);
    }
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		stopInstanceTask();
		movies.clear();
		doors.clear();
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
}