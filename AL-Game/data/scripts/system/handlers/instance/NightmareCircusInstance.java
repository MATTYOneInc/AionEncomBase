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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(301200000)
public class NightmareCircusInstance extends GeneralInstanceHandler {

	private int freneticNightmareKilled;
	private List<Integer> movies = new ArrayList<Integer>();
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		SpawnTemplate ClosedCage = SpawnEngine.addNewSingleTimeSpawn(301200000, 831627, 522.39825f, 564.69006f, 199.03371f, (byte) 0);
		ClosedCage.setEntityId(142);
		objects.put(831627, SpawnEngine.spawnObject(ClosedCage, instanceId));
		SpawnTemplate SolidIronChain = SpawnEngine.addNewSingleTimeSpawn(301200000, 831572, 522.04095f, 565.48944f, 199.34138f, (byte) 0);
		SolidIronChain.setEntityId(17);
		objects.put(831572, SpawnEngine.spawnObject(SolidIronChain, instanceId));
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 233161: //Nightmare Lord Heiramune.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000163, 1)); //Nightmare Lord's Key.
		    break;
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		switch (player.getRace()) {
			case ELYOS:
				switch (Rnd.get(1, 2)) {
					case 1:
					    SkillEngine.getInstance().applyEffectDirectly(21469, player, player, 3600000 * 1); //Embrace The Dream.
					break;
					case 2:
					    SkillEngine.getInstance().applyEffectDirectly(21470, player, player, 3600000 * 1); //Embrace The Nightmare.
					break;
				}
			break;
			case ASMODIANS:
				switch (Rnd.get(1, 2)) {
					case 1:
					    SkillEngine.getInstance().applyEffectDirectly(21471, player, player, 3600000 * 1); //Embrace The Dream.
					break;
					case 2:
					    SkillEngine.getInstance().applyEffectDirectly(21472, player, player, 3600000 * 1); //Embrace The Nightmare.
					break;
				}
			break;
		}
	}
	
	private void attackEvent(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void startNightmareWave() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 1000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 11000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 21000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 31000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 41000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 51000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 61000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 71000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 81000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 91000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 101000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 111000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 121000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 131000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 141000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 151000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 161000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Violent Nightmare.
				attackEvent((Npc)spawn(233149, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233149, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 171000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Frenetic Nightmare.
				attackEvent((Npc)spawn(233144, 520.3353f, 494.21973f, 198.34286f, (byte) 28), 519.6476f, 540.5993f, 198.83754f, false);
				attackEvent((Npc)spawn(233144, 523.84314f, 494.02798f, 198.37112f, (byte) 30), 524.2756f, 540.6662f, 198.94113f, false);
			}
		}, 181000);
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 233144: //Frenetic Nightmare.
			    freneticNightmareKilled++;
				if (freneticNightmareKilled == 26) {
					//Bovariki has appeared!.
					sendMsgByRace(1401797, Race.PC_ALL, 0);
					spawn(233153, 529.8865f, 550.8018f, 198.70699f, (byte) 78); //Mistress Viloa.
				}
				despawnNpc(npc);
			break;
			case 233153: //Mistress Viloa.
			    despawnNpc(npc);
			    spawnBoxOfTerrors();
				//Harlequin Lord Reshka has appeared!
			    sendMsgByRace(1401798, Race.PC_ALL, 0);
			    spawn(233147, 551.98303f, 567.18024f, 198.82376f, (byte) 67); //Harlequin Lord Reshka.
			break;
			case 233147: //Harlequin Lord Reshka.
			    despawnNpc(npc);
				deleteNpc(831348); //Box Of Terrors.
			    //Nightmare Lord Heiramune will appear in 5 seconds!
			    sendMsgByRace(1401798, Race.PC_ALL, 0);
			    spawn(233161, 552.595f, 567.2736f, 198.79242f, (byte) 68); //Nightmare Lord Heiramune.
				spawn(233162, 553.6005f, 562.4973f, 198.93172f, (byte) 65); //Nightmare Lord Heiramune.
			break;
			case 233161:
			/*Nightmare Lord Heiramune.
			<Nightmare Lord>*/
			    spawnIUFriendDance();
				sendMovie(player, 982);
				deleteNpc(831572); //Bird Cage Chain.
				deleteNpc(831627); //Bird Cage Closed.
				deleteNpc(831573); //IU In The Cage.
				spawnNightmareCrate();
				spawnGreaterNightmareCrate();
				//sendMsg("[SUCCES]: You have finished <Nightmare Circus>");
				SpawnTemplate OpenCage = SpawnEngine.addNewSingleTimeSpawn(301200000, 831598, 522.39825f, 564.69006f, 199.03371f, (byte) 0);
				OpenCage.setEntityId(14);
				objects.put(831598, SpawnEngine.spawnObject(OpenCage, instanceId));
				spawn(831576, 483.582581f, 567.211487f, 201.734894f, (byte) 0); //Nightmare Circus Exit.
			break;
			case 233162:
			/*Nightmare Lord Heiramune.
			<Phantom Of Nightmare Lord Heiramune>*/
			    despawnNpc(npc);
			break;
			case 831572: //Solid Iron Chain.
				startNightmareWave();
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						sendMovie(player, 981);
				    }
			    });
			break;
		}
	}
	
	private void spawnBoxOfTerrors() {
	    spawn(831348, 534.0532f, 198.64136f, 556.31506f, (byte) 45);
        spawn(831348, 525.08844f, 552.17865f, 198.75f, (byte) 30);
        spawn(831348, 515.5271f, 555.8954f, 198.75f, (byte) 15);
        spawn(831348, 511.9015f, 565.3504f, 198.75f, (byte) 1);
        spawn(831348, 515.68115f, 574.70087f, 198.75f, (byte) 105);
        spawn(831348, 525.0347f, 577.6604f, 198.55933f, (byte) 90);
        spawn(831348, 533.94104f, 574.7093f, 198.75f, (byte) 74);
        spawn(831348, 537.88513f, 565.4929f, 198.71112f, (byte) 61);
	}
	private void spawnGreaterNightmareCrate() {
        spawn(831575, 505.28232f, 560.26874f, 198.875f, (byte) 0);
        spawn(831575, 500.8052f, 560.1265f, 198.875f, (byte) 0);
        spawn(831575, 500.71606f, 564.9373f, 198.875f, (byte) 0);
        spawn(831575, 500.82724f, 569.7114f, 198.875f, (byte) 0);
        spawn(831575, 500.7664f, 574.143f, 199.0f, (byte) 0);
        spawn(831575, 505.12305f, 573.96344f, 198.8071f, (byte) 119);
        spawn(831575, 504.6248f, 567.0404f, 198.875f, (byte) 0);
	}
	private void spawnNightmareCrate() {
        spawn(831745, 544.54767f, 569.4929f, 198.875f, (byte) 60);
        spawn(831745, 544.49084f, 566.5837f, 198.96352f, (byte) 60);
        spawn(831745, 541.7616f, 566.55865f, 198.95529f, (byte) 60);
        spawn(831745, 538.9772f, 550.2749f, 198.875f, (byte) 45);
        spawn(831745, 540.71594f, 552.16797f, 198.875f, (byte) 45);
        spawn(831745, 537.3058f, 551.9778f, 198.875f, (byte) 45);
        spawn(831745, 529.0003f, 583.8923f, 198.86154f, (byte) 87);
        spawn(831745, 526.29016f, 584.32465f, 198.91559f, (byte) 87);
        spawn(831745, 528.6637f, 581.28894f, 198.69373f, (byte) 87);
        spawn(831745, 517.5683f, 547.7183f, 198.875f, (byte) 25);
        spawn(831745, 520.36786f, 546.94763f, 198.875f, (byte) 25);
        spawn(831745, 518.48267f, 550.7468f, 198.75f, (byte) 25);
	}
	
	private void spawnIUFriendDance() {
		spawn(831559, 517.783997f, 562.049500f, 200.161789f, (byte) 0);
		spawn(831560, 520.415588f, 563.304688f, 200.161789f, (byte) 0);
		spawn(831561, 517.843872f, 568.376221f, 200.161789f, (byte) 0);
		spawn(831562, 520.566406f, 567.832764f, 200.161789f, (byte) 0);
		spawn(831568, 522.113159f, 575.088928f, 200.161789f, (byte) 0);
		spawn(831569, 522.604614f, 568.458313f, 200.161789f, (byte) 0);
		spawn(831570, 523.138611f, 556.660278f, 200.161789f, (byte) 0);
		spawn(831571, 523.020996f, 570.701294f, 200.161789f, (byte) 0);
		spawn(831574, 519.297729f, 565.522888f, 199.720016f, (byte) 0); //IU Free.
		spawn(831577, 522.389526f, 571.335693f, 199.720016f, (byte) 0);
		spawn(831578, 515.614807f, 565.294922f, 202.947632f, (byte) 0);
		spawn(831578, 515.558350f, 565.320740f, 200.676620f, (byte) 0);
		spawn(831579, 518.391052f, 565.527527f, 200.676620f, (byte) 0);
		spawn(831580, 518.447510f, 565.501709f, 202.947632f, (byte) 0);
		spawn(831598, 523.536987f, 564.654968f, 200.527283f, (byte) 0);
		spawn(831601, 525.472229f, 553.109924f, 200.641571f, (byte) 0);
		spawn(831602, 520.488220f, 571.165710f, 199.954300f, (byte) 0);
		spawn(831603, 525.643860f, 557.815735f, 199.954300f, (byte) 0);
		spawn(831604, 522.645996f, 572.826294f, 200.161789f, (byte) 0);
		spawn(831605, 523.658447f, 562.224121f, 199.954300f, (byte) 0);
		spawn(831606, 526.925049f, 571.162537f, 200.641571f, (byte) 0);
		spawn(831607, 516.781921f, 571.597046f, 200.161789f, (byte) 0);
		spawn(831607, 516.010071f, 568.456116f, 200.161789f, (byte) 0);
		spawn(831607, 517.606262f, 572.883240f, 200.161789f, (byte) 0);
		spawn(831607, 518.7628f, 571.9297f, 198.906f, (byte) 7);
		spawn(831607, 518.867188f, 573.099792f, 200.161789f, (byte) 0);
		spawn(831607, 520.135071f, 573.331116f, 200.161789f, (byte) 0);
		spawn(831609, 526.119080f, 555.544189f, 199.954300f, (byte) 0);
		spawn(831610, 526.454834f, 575.113403f, 199.954300f, (byte) 0);
		spawn(831611, 520.735901f, 554.773315f, 200.641571f, (byte) 0);
		spawn(831612, 523.154785f, 554.475586f, 200.641571f, (byte) 0);
		spawn(831613, 523.126770f, 559.204712f, 200.016571f, (byte) 0);
		spawn(831614, 520.618286f, 559.273376f, 200.641571f, (byte) 0);
		spawn(831615, 520.780396f, 556.974060f, 200.641571f, (byte) 0);
		spawn(831627, 523.600586f, 566.472473f, 200.527283f, (byte) 0);
		spawn(831630, 524.708008f, 565.484009f, 199.907257f, (byte) 0);
		spawn(831658, 518.406921f, 564.972046f, 200.161789f, (byte) 0);
		spawn(831658, 520.031921f, 561.347046f, 200.161789f, (byte) 0);
		spawn(831658, 517.281921f, 563.847046f, 200.161789f, (byte) 0);
		spawn(831659, 518.001343f, 570.414368f, 200.161789f, (byte) 0);
		spawn(831659, 518.626343f, 563.789368f, 200.161789f, (byte) 0);
		spawn(831660, 516.376343f, 569.914368f, 200.161789f, (byte) 0);
		spawn(831661, 515.531921f, 564.972046f, 200.161789f, (byte) 0);
		spawn(831662, 520.251343f, 569.539368f, 200.161789f, (byte) 0);
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21469);
		effectController.removeEffect(21470);
		effectController.removeEffect(21471);
		effectController.removeEffect(21472);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
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
	
	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			npc.getController().onDelete();
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
	
	@Override
    public boolean onReviveEvent(Player player) {
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		switch (player.getRace()) {
			case ELYOS:
				switch (Rnd.get(1, 2)) {
					case 1:
					    SkillEngine.getInstance().applyEffectDirectly(21469, player, player, 3600000 * 1); //Embrace The Dream.
					break;
					case 2:
					    SkillEngine.getInstance().applyEffectDirectly(21470, player, player, 3600000 * 1); //Embrace The Nightmare.
					break;
				}
			break;
			case ASMODIANS:
				switch (Rnd.get(1, 2)) {
					case 1:
					    SkillEngine.getInstance().applyEffectDirectly(21471, player, player, 3600000 * 1); //Embrace The Dream.
					break;
					case 2:
					    SkillEngine.getInstance().applyEffectDirectly(21472, player, player, 3600000 * 1); //Embrace The Nightmare.
					break;
				}
			break;
		}
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
        return TeleportService2.teleportTo(player, mapId, instanceId, 469.65033f, 567.8404f, 201.74283f, (byte) 113);
    }
	
	@Override
    public void onInstanceDestroy() {
		movies.clear();
    }
}