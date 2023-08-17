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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.StonespearReachReward;
import com.aionemu.gameserver.model.instance.playerreward.StonespearReachPlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(301500000)
public class StonespearReachInstance extends GeneralInstanceHandler
{
	private int rank;
	private Race spawnRace;
	private long startTime;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private Future<?> stoneSpearTaskA1;
	private Future<?> stoneSpearTaskA2;
	private Future<?> stoneSpearTaskA3;
	///////////////////////////////////
	private Future<?> stoneSpearTaskB1;
	private Future<?> stoneSpearTaskB2;
	private Future<?> stoneSpearTaskB3;
	///////////////////////////////////
	private Future<?> stoneSpearTaskC1;
	private Future<?> stoneSpearTaskC2;
	private Future<?> stoneSpearTaskC3;
	///////////////////////////////////
	private Future<?> stoneSpearTaskD1;
	private Future<?> stoneSpearTaskD2;
	private Future<?> stoneSpearTaskD3;
	private boolean isInstanceDestroyed;
	//Preparation Time.
	private int prepareTimerSeconds = 60000; //...1Min
	//Duration Instance Time.
	private int instanceTimerSeconds = 1800000; //...30Min
	private StonespearReachReward instanceReward;
	private final FastList<Future<?>> stonespearTask1 = FastList.newInstance();
	private final FastList<Future<?>> stonespearTask2 = FastList.newInstance();
	private final FastList<Future<?>> stonespearTask3 = FastList.newInstance();
	private final FastList<Future<?>> stonespearTask4 = FastList.newInstance();
	private final FastList<Future<?>> stonespearTask5 = FastList.newInstance();
	
	protected StonespearReachPlayerReward getPlayerReward(Integer object) {
		return (StonespearReachPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	@SuppressWarnings("unchecked")
	protected void addPlayerReward(Player player) {
		instanceReward.addPlayerReward(new StonespearReachPlayerReward(player.getObjectId()));
	}
	
	private boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}
	
	@Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
		}
	}
	
	private void spawnTerritoryManager() {
		final int territoryManager = spawnRace == Race.ASMODIANS ? 833489 : 833488; //Legion Territory Manager.
		spawn(territoryManager, 165.91524f, 264.50375f, 97.454155f, (byte) 0);
    }
	private void spawnGuardianStone() {
		final int guardianStone = spawnRace == Race.ASMODIANS ? 856466 : 855763; //Guardian Stone.
		spawn(guardianStone, 231.26677f, 264.4961f, 95.7781f, (byte) 60);
    }
	
	@Override
	public void onDie(Npc npc) {
		int points = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 856303: //Agitated Kebbit.
			    points = 1500;
			break;
			case 856305: //Macadamic Jester.
			    points = 12000;
			break;
			//** ROUND 1 **//
			case 855765: //Shulack Outrider.
			case 855766: //Rubblespout Spirit.
			case 855767: //Owllau Outrider.
			case 855768: //Shulack Watcher.
			case 855769: //Vilerock Spirit.
			case 855770: //Owllau Watcher.
			case 855771: //Shulack Bladesman.
			case 855772: //Malistone Spirit.
			case 855773: //Owllau Bladesman.
			    points = 100;
			break;
			case 855764: //Aetheric Field Blaststone.
			    points = 500;
			break;
			case 855774: //Vision Of Hamerun.
			case 855775: //Vision Of Kromede.
			case 855776: //Vision Of Kaliga.
			    points = 12000;
				stopInstanceTask1();
				//The second battle will begin in 2 minutes.
				sendMsgByRace(1402868, Race.PC_ALL, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						startInstanceTask2();
					}
				}, 60000);
			break;
			//** ROUND 2 **//
			case 855788: //Shulack Gladiator.
			case 855789: //Cinderspout Spirit.
			case 855790: //Owllau Gladiator.
			case 855791: //Shulack Fencer.
			case 855792: //Vileflame Spirit.
			case 855793: //Owllau Fencer.
			case 855794: //Shulack Swordsman.
			case 855795: //Malistoke Spirit.
			case 855796: //Owllau Swordsman.
			    points = 200;
			break;
			case 855787: //Aetheric Field Blaststone.
			    points = 1000;
			break;
			case 855797: //Apparition Of Bakarma.
			case 855798: //Apparition Of Triroan.
			case 855799: //Apparition Of Lanmark.
			    points = 21000;
				stopInstanceTask2();
				//The third battle will begin in 3 minutes.
				sendMsgByRace(1402869, Race.PC_ALL, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						startInstanceTask3();
					}
				}, 60000);
			break;
			//** ROUND 3 **//
			case 855811: //Shulack Reconnoiterer.
			case 855812: //Waterspout Spirit.
			case 855813: //Owllau Healer.
			case 855814: //Shulack Scout.
			case 855815: //Vilewash Spirit.
			case 855816: //Owllau Priest.
			case 855817: //Shulack Guardsman.
			case 855818: //Malisalt Spirit.
			case 855819: //Owllau Mender.
			    points = 300;
			break;
			case 855810: //Aetheric Field Blaststone.
			    points = 1500;
			break;
			case 855820: //Vision Of Calindi.
			case 855821: //Vision Of Tahabata.
			case 855822: //Vision Of Rudra.
			    points = 30000;
				stopInstanceTask3();
				//The fourth battle will begin in 4 minutes.
				sendMsgByRace(1402870, Race.PC_ALL, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						startInstanceTask4();
					}
				}, 60000);
			break;
			//** ROUND 4 **//
			case 855834: //Shulack Warlock.
			case 855835: //Galespout Spirit.
			case 855836: //Owllau Warlock.
			case 855837: //Shulack Mage.
			case 855838: //Vilegust Spirit.
			case 855839: //Owllau Mage.
			case 855840: //Shulack Dark Warlock.
			case 855841: //Malistorm Spirit.
			case 855842: //Owllau Dark Warlock.
			    points = 400;
			break;
			case 855833: //Aetheric Field Blaststone.
			    points = 2000;
			break;
			case 855843: //Vision Of Guardian General.
			    points = 42000;
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								stopInstance(player);
							}
						});
					}
				}, 5000);
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
	}
	
	protected void startInstanceTask1() {
    	stonespearTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//The player has 1 min to prepare !!! [Timer Red]
				if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
					//Start the instance time !!! [Timer White]
					startCountDown();
					startMainInstanceTimer();
				}
				deleteNpc(833284);
				spawnGuardianStone();
				//The Aetheric Field is deactivated. The battle will now begin!
				sendMsgByRace(1402867, Race.PC_ALL, 0);
				//Protect the Guardian Stone for 2 minutes.
				sendMsgByRace(1402924, Race.PC_ALL, 2000);
				spawn(856305, 206.64789f, 263.70578f, 96.25f, (byte) 94); //Macadamic Jester.
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackOutriderRaid();
				    break;
					case 2:
					    startRubblespoutSpiritRaid();
				    break;
					case 3:
					    startOwllauOutriderRaid();
				    break;
				}
            }
        }, 60000)); //...1Min
		stonespearTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				deleteNpc(855763);
				deleteNpc(856466);
				//You have successfully protected the Guardian Stone and the stone has disappeared.
				sendMsgByRace(1402925, Race.PC_ALL, 0);
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackWatcherRaid();
				    break;
					case 2:
					    startVilerockSpiritRaid();
				    break;
					case 3:
					    startOwllauWatcherRaid();
				    break;
				}
				stoneSpearTaskA1.cancel(true);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 120000)); //...2Min
		stonespearTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 180000)); //...3Min
		stonespearTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackBladesmanRaid();
				    break;
					case 2:
					    startMalistoneSpiritRaid();
				    break;
					case 3:
					    startOwllauBladesmanRaid();
				    break;
				}
				stoneSpearTaskA2.cancel(true);
				//Aetheric Field Blaststone.
				spawn(855764, 251.47273f, 264.46713f, 96.30522f, (byte) 61);
				spawn(855764, 230.85971f, 285.67032f, 96.41852f, (byte) 90);
				spawn(855764, 211.20746f, 264.05276f, 96.53291f, (byte) 0);
				spawn(855764, 231.29951f, 243.66095f, 96.36497f, (byte) 29);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 240000)); //...4Min
		stonespearTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
					case 1:
					    spawn(855774, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Hamerun.
					break;
					case 2:
					    spawn(855775, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Kromede.
					break;
					case 3:
					    spawn(855776, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Kaliga.
					break;
				}
				deleteNpc(856305);
				stoneSpearTaskA3.cancel(true);
				//The Guardian Stone and its attackers have all disappeared!
				sendMsgByRace(1402901, Race.PC_ALL, 0);
            }
        }, 300000)); //...5Min
	}
	
	protected void startInstanceTask2() {
    	stonespearTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawnGuardianStone();
				//Protect the Guardian Stone for 2 minutes.
				sendMsgByRace(1402924, Race.PC_ALL, 2000);
				spawn(856305, 206.64789f, 263.70578f, 96.25f, (byte) 94); //Macadamic Jester.
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackGladiatorRaid();
				    break;
					case 2:
					    startCinderspoutSpiritRaid();
				    break;
					case 3:
					    startOwllauGladiatorRaid();
				    break;
				}
            }
        }, 60000)); //...1Min
		stonespearTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				deleteNpc(855763);
				deleteNpc(856466);
				//You have successfully protected the Guardian Stone and the stone has disappeared.
				sendMsgByRace(1402925, Race.PC_ALL, 0);
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackFencerRaid();
				    break;
					case 2:
					    startVileflameSpiritRaid();
				    break;
					case 3:
					    startOwllauFencerRaid();
				    break;
				}
				stoneSpearTaskB1.cancel(true);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 120000)); //...2Min
		stonespearTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 180000)); //...3Min
		stonespearTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackSwordsmanRaid();
				    break;
					case 2:
					    startMalistokeSpiritRaid();
				    break;
					case 3:
					    startOwllauSwordsmanRaid();
				    break;
				}
				stoneSpearTaskB2.cancel(true);
				//Aetheric Field Blaststone.
				spawn(855787, 251.47273f, 264.46713f, 96.30522f, (byte) 61);
				spawn(855787, 230.85971f, 285.67032f, 96.41852f, (byte) 90);
				spawn(855787, 211.20746f, 264.05276f, 96.53291f, (byte) 0);
				spawn(855787, 231.29951f, 243.66095f, 96.36497f, (byte) 29);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 240000)); //...4Min
		stonespearTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
					case 1:
					    spawn(855797, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Apparition Of Bakarma.
					break;
				    case 2:
					    spawn(855798, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Apparition Of Triroan.
					break;
					case 3:
					    spawn(855799, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Apparition Of Lanmark.
					break;
				}
				deleteNpc(856305);
				stoneSpearTaskB3.cancel(true);
				//The Guardian Stone and its attackers have all disappeared!
				sendMsgByRace(1402901, Race.PC_ALL, 0);
            }
        }, 300000)); //...5Min
	}
	
	protected void startInstanceTask3() {
    	stonespearTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawnGuardianStone();
				//Protect the Guardian Stone for 2 minutes.
				sendMsgByRace(1402924, Race.PC_ALL, 2000);
				spawn(856305, 206.64789f, 263.70578f, 96.25f, (byte) 94); //Macadamic Jester.
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackReconnoitererRaid();
				    break;
					case 2:
					    startWaterspoutSpiritRaid();
				    break;
					case 3:
					    startOwllauHealerRaid();
				    break;
				}
            }
        }, 60000)); //...1Min
		stonespearTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				deleteNpc(855763);
				deleteNpc(856466);
				//You have successfully protected the Guardian Stone and the stone has disappeared.
				sendMsgByRace(1402925, Race.PC_ALL, 0);
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackScoutRaid();
				    break;
					case 2:
					    startVilewashSpiritRaid();
				    break;
					case 3:
					    startOwllauPriestRaid();
				    break;
				}
				stoneSpearTaskC1.cancel(true);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 120000)); //...2Min
		stonespearTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 180000)); //...3Min
		stonespearTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackGuardsmanRaid();
				    break;
					case 2:
					    startMalisaltSpiritRaid();
				    break;
					case 3:
					    startOwllauMenderRaid();
				    break;
				}
				stoneSpearTaskC2.cancel(true);
				//Aetheric Field Blaststone.
				spawn(855810, 251.47273f, 264.46713f, 96.30522f, (byte) 61);
				spawn(855810, 230.85971f, 285.67032f, 96.41852f, (byte) 90);
				spawn(855810, 211.20746f, 264.05276f, 96.53291f, (byte) 0);
				spawn(855810, 231.29951f, 243.66095f, 96.36497f, (byte) 29);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 240000)); //...4Min
		stonespearTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
					case 1:
					    spawn(855820, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Calindi.
					break;
					case 2:
					    spawn(855821, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Tahabata.
					break;
					case 3:
					    spawn(855822, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Rudra.
					break;
				}
				deleteNpc(856305);
				stoneSpearTaskC3.cancel(true);
				//The Guardian Stone and its attackers have all disappeared!
				sendMsgByRace(1402901, Race.PC_ALL, 0);
            }
        }, 300000)); //...5Min
	}
	
	protected void startInstanceTask4() {
    	stonespearTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawnGuardianStone();
				//Protect the Guardian Stone for 2 minutes.
				sendMsgByRace(1402924, Race.PC_ALL, 2000);
				spawn(856305, 206.64789f, 263.70578f, 96.25f, (byte) 94); //Macadamic Jester.
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackWarlockRaid();
				    break;
					case 2:
					    startGalespoutSpiritRaid();
				    break;
					case 3:
					    startOwllauWarlockRaid();
				    break;
				}
            }
        }, 60000)); //...1Min
		stonespearTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				deleteNpc(855763);
				deleteNpc(856466);
				//You have successfully protected the Guardian Stone and the stone has disappeared.
				sendMsgByRace(1402925, Race.PC_ALL, 0);
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackMageRaid();
				    break;
					case 2:
					    startVilegustSpiritRaid();
				    break;
					case 3:
					    startOwllauMageRaid();
				    break;
				}
				stoneSpearTaskD1.cancel(true);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 120000)); //...2Min
		stonespearTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 180000)); //...3Min
		stonespearTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				switch (Rnd.get(1, 3)) {
				    case 1:
					    startShulackDarkWarlockRaid();
				    break;
					case 2:
					    startMalistormSpiritRaid();
				    break;
					case 3:
					    startOwllauDarkWarlockRaid();
				    break;
				}
				stoneSpearTaskD2.cancel(true);
				//Aetheric Field Blaststone.
				spawn(855833, 251.47273f, 264.46713f, 96.30522f, (byte) 61);
				spawn(855833, 230.85971f, 285.67032f, 96.41852f, (byte) 90);
				spawn(855833, 211.20746f, 264.05276f, 96.53291f, (byte) 0);
				spawn(855833, 231.29951f, 243.66095f, 96.36497f, (byte) 29);
				//Agitated Kebbit.
				spawn(856303, 208.48062f, 256.79190f, 96.25000f, (byte) 5);
                spawn(856303, 253.01332f, 275.73624f, 96.23518f, (byte) 69);
                spawn(856303, 223.37283f, 286.79090f, 96.25000f, (byte) 96);
                spawn(856303, 236.64775f, 241.84962f, 95.93428f, (byte) 30);
            }
        }, 240000)); //...4Min
		stonespearTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				deleteNpc(856305);
				stoneSpearTaskD3.cancel(true);
				//The Guardian Stone and its attackers have all disappeared!
				sendMsgByRace(1402901, Race.PC_ALL, 0);
				spawn(855843, 231.35631f, 264.5710f, 95.77810f, (byte) 58); //Vision Of Guardian General.
            }
        }, 300000)); //...5Min
	}
	
	protected void startCountDown() {
		stonespearTask5.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
					    stopInstance(player);
				    }
			    });
            }
        }, 1800000));
    }
	
   /**
	* RAID A1
	*/
	private void startShulackOutriderRaid() {
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Outrider.
				stoneSpearRaid((Npc)spawn(855765, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855765, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855765, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855765, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855765, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855765, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855765, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855765, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startRubblespoutSpiritRaid() {
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Rubblespout Spirit.
				stoneSpearRaid((Npc)spawn(855766, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855766, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855766, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855766, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855766, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855766, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855766, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855766, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauOutriderRaid() {
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Outrider.
				stoneSpearRaid((Npc)spawn(855767, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855767, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855767, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855767, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855767, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855767, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855767, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855767, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID A2
	*/
	private void startShulackWatcherRaid() {
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Watcher.
				stoneSpearRaid((Npc)spawn(855768, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855768, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855768, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855768, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855768, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855768, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855768, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855768, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startVilerockSpiritRaid() {
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilerock Spirit.
				stoneSpearRaid((Npc)spawn(855769, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855769, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855769, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855769, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855769, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855769, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855769, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855769, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauWatcherRaid() {
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Watcher.
				stoneSpearRaid((Npc)spawn(855770, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855770, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855770, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855770, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855770, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855770, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855770, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855770, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID A3
	*/
	private void startShulackBladesmanRaid() {
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Bladesman.
				stoneSpearRaid((Npc)spawn(855771, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855771, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855771, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855771, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855771, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855771, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855771, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855771, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startMalistoneSpiritRaid() {
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistone Spirit.
				stoneSpearRaid((Npc)spawn(855772, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855772, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855772, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855772, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855772, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855772, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855772, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855772, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauBladesmanRaid() {
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Bladesman.
				stoneSpearRaid((Npc)spawn(855773, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855773, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855773, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855773, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855773, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855773, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855773, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855773, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID B1
	*/
	private void startShulackGladiatorRaid() {
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Gladiator.
				stoneSpearRaid((Npc)spawn(855788, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855788, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855788, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855788, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855788, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855788, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855788, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855788, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startCinderspoutSpiritRaid() {
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Cinderspout Spirit.
				stoneSpearRaid((Npc)spawn(855789, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855789, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855789, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855789, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855789, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855789, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855789, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855789, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauGladiatorRaid() {
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Gladiator.
				stoneSpearRaid((Npc)spawn(855790, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855790, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855790, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855790, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855790, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855790, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855790, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855790, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID B2
	*/
	private void startShulackFencerRaid() {
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Fencer.
				stoneSpearRaid((Npc)spawn(855791, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855791, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855791, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855791, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855791, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855791, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855791, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855791, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startVileflameSpiritRaid() {
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vileflame Spirit.
				stoneSpearRaid((Npc)spawn(855792, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855792, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855792, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855792, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855792, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855792, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855792, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855792, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauFencerRaid() {
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Fencer.
				stoneSpearRaid((Npc)spawn(855793, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855793, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855793, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855793, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855793, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855793, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855793, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855793, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID B3
	*/
	private void startShulackSwordsmanRaid() {
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855794, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855794, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855794, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855794, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855794, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855794, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855794, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855794, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startMalistokeSpiritRaid() {
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistoke Spirit.
				stoneSpearRaid((Npc)spawn(855795, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855795, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855795, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855795, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855795, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855795, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855795, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855795, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauSwordsmanRaid() {
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskB3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Swordsman.
				stoneSpearRaid((Npc)spawn(855796, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855796, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855796, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855796, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855796, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855796, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855796, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855796, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID C1
	*/
	private void startShulackReconnoitererRaid() {
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Swordsman.
				stoneSpearRaid((Npc)spawn(855811, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855811, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855811, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855811, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855811, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855811, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855811, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855811, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startWaterspoutSpiritRaid() {
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Waterspout Spirit.
				stoneSpearRaid((Npc)spawn(855812, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855812, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855812, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855812, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855812, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855812, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855812, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855812, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauHealerRaid() {
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Healer.
				stoneSpearRaid((Npc)spawn(855813, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855813, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855813, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855813, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855813, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855813, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855813, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855813, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID C2
	*/
	private void startShulackScoutRaid() {
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Scout.
				stoneSpearRaid((Npc)spawn(855814, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855814, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855814, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855814, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855814, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855814, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855814, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855814, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startVilewashSpiritRaid() {
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilewash Spirit.
				stoneSpearRaid((Npc)spawn(855815, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855815, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855815, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855815, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855815, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855815, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855815, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855815, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauPriestRaid() {
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Priest.
				stoneSpearRaid((Npc)spawn(855816, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855816, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855816, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855816, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855816, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855816, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855816, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855816, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID C3
	*/
	private void startShulackGuardsmanRaid() {
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Guardsman.
				stoneSpearRaid((Npc)spawn(855817, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855817, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855817, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855817, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855817, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855817, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855817, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855817, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startMalisaltSpiritRaid() {
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malisalt Spirit.
				stoneSpearRaid((Npc)spawn(855818, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855818, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855818, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855818, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855818, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855818, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855818, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855818, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauMenderRaid() {
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskC3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mender.
				stoneSpearRaid((Npc)spawn(855819, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855819, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855819, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855819, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855819, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855819, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855819, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855819, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID D1
	*/
	private void startShulackWarlockRaid() {
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Warlock.
				stoneSpearRaid((Npc)spawn(855834, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855834, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855834, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855834, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855834, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855834, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855834, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855834, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startGalespoutSpiritRaid() {
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Galespout Spirit.
				stoneSpearRaid((Npc)spawn(855835, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855835, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855835, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855835, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855835, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855835, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855835, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855835, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauWarlockRaid() {
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Warlock.
				stoneSpearRaid((Npc)spawn(855836, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855836, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855836, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855836, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855836, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855836, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855836, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855836, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID D2
	*/
	private void startShulackMageRaid() {
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Mage.
				stoneSpearRaid((Npc)spawn(855837, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855837, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855837, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855837, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855837, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855837, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855837, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855837, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startVilegustSpiritRaid() {
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Vilegust Spirit.
				stoneSpearRaid((Npc)spawn(855838, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855838, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855838, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855838, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855838, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855838, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855838, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855838, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauMageRaid() {
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Mage.
				stoneSpearRaid((Npc)spawn(855839, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855839, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855839, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855839, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855839, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855839, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855839, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855839, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
   /**
	* RAID D3
	*/
	private void startShulackDarkWarlockRaid() {
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Shulack Dark Warlock.
				stoneSpearRaid((Npc)spawn(855840, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855840, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855840, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855840, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855840, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855840, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855840, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855840, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startMalistormSpiritRaid() {
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Malistorm Spirit.
				stoneSpearRaid((Npc)spawn(855841, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855841, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855841, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855841, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855841, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855841, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855841, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855841, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	private void startOwllauDarkWarlockRaid() {
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 1000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 10000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 20000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 30000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 40000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 50000);
		stoneSpearTaskD3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Owllau Dark Warlock.
				stoneSpearRaid((Npc)spawn(855842, 211.05080f, 264.03802f, 96.53291f, (byte) 0));
				stoneSpearRaid((Npc)spawn(855842, 217.06422f, 248.22205f, 96.25f, (byte) 17));
				stoneSpearRaid((Npc)spawn(855842, 231.39449f, 243.60184f, 96.36497f, (byte) 31));
				stoneSpearRaid((Npc)spawn(855842, 245.20996f, 250.43109f, 96.07562f, (byte) 44));
				stoneSpearRaid((Npc)spawn(855842, 251.58972f, 264.37146f, 96.30522f, (byte) 59));
				stoneSpearRaid((Npc)spawn(855842, 243.75105f, 279.34222f, 96.25f, (byte) 77));
				stoneSpearRaid((Npc)spawn(855842, 230.97932f, 285.57825f, 96.418526f, (byte) 89));
				stoneSpearRaid((Npc)spawn(855842, 217.75461f, 277.61115f, 96.02431f, (byte) 104));
			}
		}, 60000);
	}
	
	private void stoneSpearRaid(final Npc npc) {
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
	
	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);
		return instanceTimerSeconds - (int) result;
	}
	
	private void sendPacket(final int nameId, final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (nameId != 0) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}
	
	private int checkRank(int totalPoints) {
		if (totalPoints >= 71600) { //Rank S.
			rank = 1;
		} else if (totalPoints >= 41000) { //Rank A.
			rank = 2;
		} else if (totalPoints >= 26000) { //Rank B.
			rank = 3;
		} else if (totalPoints >= 14000) { //Rank C.
			rank = 4;
		} else if (totalPoints >= 8800) { //Rank D.
			rank = 5;
		} else {
			rank = 6;
		}
		return rank;
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onEnterInstance(player);
		if (!instanceReward.containPlayer(player.getObjectId())) {
			addPlayerReward(player);
		}
		StonespearReachPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded()) {
			doReward(player);
		} if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnTerritoryManager();
		}
		startPrepareTimer();
	}
	
	private void startPrepareTimer() {
		if (timerPrepare == null) {
			timerPrepare = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startMainInstanceTimer();
				}
			}, prepareTimerSeconds);
		}
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(prepareTimerSeconds, instanceReward, null));
			}
		});
	}
	
	private void startMainInstanceTimer() {
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}
		startTime = System.currentTimeMillis();
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player) {
		stopInstanceTask1();
		stopInstanceTask2();
		stopInstanceTask3();
		stopInstanceTask4();
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[SUCCES]: You have finished <Stonespear Reach>");
		sendPacket(0, 0);
	}
	
	private void rewardGroup() {
		for (Player p: instance.getPlayersInside()) {
			doReward(p);
		}
	}
	
	@Override
	public void doReward(Player player) {
		StonespearReachPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			int reachRank = instanceReward.getRank();
			switch (reachRank) {
				case 1: //Rank S
				break;
				case 2: //Rank A
				break;
				case 3: //Rank B
				break;
				case 4: //Rank C
				break;
				case 5: //Rank D
				break;
			}
		}
	}
	
	@Override
	public void onExitInstance(Player player) {
		if (player.isInGroup2()) {
            PlayerGroupService.removePlayer(player);
        }
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void stopInstanceTask1() {
        for (FastList.Node<Future<?>> n = stonespearTask1.head(), end = stonespearTask1.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask2() {
        for (FastList.Node<Future<?>> n = stonespearTask2.head(), end = stonespearTask2.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask3() {
        for (FastList.Node<Future<?>> n = stonespearTask3.head(), end = stonespearTask3.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask4() {
        for (FastList.Node<Future<?>> n = stonespearTask4.head(), end = stonespearTask4.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask5() {
        for (FastList.Node<Future<?>> n = stonespearTask5.head(), end = stonespearTask5.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new StonespearReachReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		startInstanceTask1();
	}
	
	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		} if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
		stopInstanceTask1();
		stopInstanceTask2();
		stopInstanceTask3();
		stopInstanceTask4();
		stopInstanceTask5();
		isInstanceDestroyed = true;
		instanceReward.clear();
	}
	
	private void despawnNpc(Npc npc) {
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
	
	@Override
	public boolean onReviveEvent(Player player) {
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.getObjectId() == player.getObjectId()) {
					//You were killed during the Stonespear Seige. You will be moved to the waiting area.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402910));
				} else {
					//"Player Name" has been killed and will be moved to the waiting area.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402911, player.getName()));
				}
			}
		});
		return TeleportService2.teleportTo(player, mapId, instanceId, 196.80058f, 264.41388f, 97.461075f, (byte) 0);
	}
}