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
package instance.luna;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.ContaminatedUnderpathReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.ContaminatedUnderpathPlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
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
/** Source: https://www.youtube.com/watch?v=pkRzAfyLJ2g
/****/

@InstanceID(301630000)
public class ContaminatedUnderpathInstance extends GeneralInstanceHandler
{
	private int rank;
	private long startTime;
	private Race skillRace;
	private Race spawnRace;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private Future<?> underpathTaskA1;
	private Future<?> underpathTaskA2;
	private Future<?> underpathTaskA3;
	private Future<?> underpathTaskA4;
	private Future<?> underpathTaskA5;
	private Future<?> underpathTaskA6;
	private Future<?> underpathTaskA7;
	private Future<?> underpathTaskA8;
	private Future<?> underpathTaskA9;
	private Future<?> underpathTaskA10;
	private Future<?> underpathTaskA11;
	private Future<?> underpathTaskA12;
	private Future<?> underpathTaskA13;
	private Future<?> underpathTaskA14;
	private Future<?> underpathTaskA15;
	private Future<?> underpathTaskA16;
	private Future<?> underpathTaskA17;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	//Preparation Time.
	private int prepareTimerSeconds = 180000; //...3Min
	//Duration Instance Time.
	private int instanceTimerSeconds = 3600000; //...1Hr
	private ContaminatedUnderpathReward instanceReward;
	private final FastList<Future<?>> contaminedTask = FastList.newInstance();
	
	protected ContaminatedUnderpathPlayerReward getPlayerReward(Integer object) {
		return (ContaminatedUnderpathPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	@SuppressWarnings("unchecked")
	protected void addPlayerReward(Player player) {
		instanceReward.addPlayerReward(new ContaminatedUnderpathPlayerReward(player.getObjectId()));
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
			case 703384: //Infected Bone Mound.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182216109, 1)); //Maad-S Molar.
			break;
			case 703385: //Infected Flesh Lump.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182216110, 1)); //Maad-S Skin Tissue.
			break;
			case 833866: //Unstable Aether Energy.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182007405, 3)); //Bright Aether.
			break;
			case 834253: //Maedrunerk Legion Treasures.
			break;
		}
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(182007405, storage.getItemCountByItemId(182007405)); //Bright Aether.
	}
	
	@Override
    public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
		    case 833812: //Flame Switch.
			    despawnNpc(npc);
				spawn(833813, 232.31128f, 239.1524f, 160.36285f, (byte) 90); //Contaminated Underpath Fire.
                spawn(833813, 225.83708f, 239.09781f, 160.36285f, (byte) 90); //Contaminated Underpath Fire.
			break;
        }
    }
	
	@Override
	public void onDie(Npc npc) {
		int points = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 243647: //MAD-74C.
			    points = 50;
			break;
			case 245543: //Experimental Daeva M.
			case 245544: //Experimental Daeva F.
			case 245545: //Transfectant Crawler.
			case 245549: //Cursed Brave Warrior.
			case 245550: //Crazy Priest.
			case 245551: //Infected Zaif.
			case 245552: //Transfectant Flyer.
			case 245553: //Transfectant Flyer.
			case 245554: //Infected Saam King.
			case 245555: //Transfectant Crawler.
			case 245559: //Cursed Brave Warrior.
			case 245560: //Crazy Priest.
			case 245561: //Infected Zaif.
			case 245562: //Transfectant Flyer.
			case 245563: //Transfectant Flyer.
			case 245564: //Infected Saam King.
			case 245565: //Transfectant Crawler.
			case 245569: //Cursed Brave Warrior.
			case 245570: //Crazy Priest.
			case 246175: //Aetherflame.
			    points = 150;
			break;
			case 245547: //Experimental Reian.
			case 245548: //Experimental Reian.
			case 245557: //Experimental Reian.
			case 245558: //Experimental Reian.
			case 245567: //Experimental Reian.
			case 245568: //Experimental Reian.
			    points = 600;
			break;
			case 245546: //Gringol The Devourer.
			    points = 2500;
			break;
			case 245575: //MAAD-S.
				points = 500000;
				spawn(703384, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Infected Bone Mound.
				spawn(703385, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Infected Flesh Lump.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
					    instance.doOnAllPlayers(new Visitor<Player>() {
						    @Override
						    public void visit(Player player) {
							    stopInstance(player);
								underpathTaskA17.cancel(true);
						    }
					    });
					}
				}, 5000);
			break;
			case 246352: //MAD-99S' Core.
			    //Give 50.000 Exp to player by "Core" kill.
			    player.getCommonData().addExp(50000, RewardType.QUEST);
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		} switch (npcId) {
			case 833863: //Frontal Barricade.
			    //The barricade in the front was destroyed.
				sendMsgByRace(1403609, Race.PC_ALL, 0);
			    instanceReward.addPoints(-500);
			break;
			case 833864: //Rear Barricade.
			    //The barricade in the back was destroyed. Atreia is in danger.
				sendMsgByRace(1403610, Race.PC_ALL, 0);
			    instanceReward.addPoints(-25000);
			break;
		}
	}
	
	private void startContaminedUnderPath1() {
		//Experimental Daeva M.
		underpathTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245543, 222.78767f, 276.12140f, 160.4131f, (byte) 89, 1000, "ContaminedUnderpath1");
				sp(245543, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245543, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245543, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245543, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245543, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Experimental Daeva F.
		underpathTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245544, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245544, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245544, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245544, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245544, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245544, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath2() {
		//Transfectant Crawler.
		underpathTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245545, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245545, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245545, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245545, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245545, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245545, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Experimental Reian.
		underpathTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245547, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245547, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245547, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245547, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245547, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245547, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath3() {
		//Experimental Reian.
		underpathTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245548, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245548, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245548, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245548, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245548, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245548, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Cursed Brave Warrior.
		underpathTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245549, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245549, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245549, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245549, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245549, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245549, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath4() {
		//Crazy Priest.
		underpathTaskA4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245550, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245550, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245550, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245550, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245550, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245550, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Infected Zaif.
		underpathTaskA4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245551, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245551, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245551, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245551, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245551, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245551, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath5() {
		//MAD-74C.
		underpathTaskA5 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(243647, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 1000, "ContaminedUnderpath4");
			}
		}, 1000);
	}
	private void startContaminedUnderPath6() {
		//Gringol The Devourer.
		underpathTaskA6 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245546, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1000, "ContaminedUnderpath2");
				sp(245546, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath4");
				sp(245546, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath6");
			}
		}, 1000);
	}
	private void startContaminedUnderPath7() {
		//Gringol The Devourer.
		underpathTaskA7 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245546, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1000, "ContaminedUnderpath2");
				sp(245546, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath4");
				sp(245546, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath6");
			}
		}, 1000);
	}
	private void startContaminedUnderPath8() {
		//Transfectant Flyer.
		underpathTaskA8 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245552, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245552, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245552, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245552, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245552, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245552, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Transfectant Flyer.
		underpathTaskA8 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245553, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245553, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245553, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245553, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245553, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245553, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath9() {
		//Infected Saam King.
		underpathTaskA9 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245554, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245554, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245554, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245554, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245554, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245554, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Transfectant Crawler.
		underpathTaskA9 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245555, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245555, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245555, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245555, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245555, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245555, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath10() {
		//Experimental Reian.
		underpathTaskA10 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245557, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245557, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245557, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245557, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245557, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245557, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Experimental Reian.
		underpathTaskA10 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245558, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245558, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245558, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245558, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245558, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245558, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath11() {
		//Cursed Brave Warrior.
		underpathTaskA11 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245559, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245559, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245559, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245559, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245559, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245559, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Crazy Priest.
		underpathTaskA11 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245560, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245560, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245560, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245560, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245560, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245560, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath12() {
		//Infected Zaif.
		underpathTaskA12 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245561, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245561, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245561, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245561, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245561, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245561, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Transfectant Flyer.
		underpathTaskA12 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245562, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245562, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245562, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245562, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245562, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245562, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath13() {
		//Transfectant Flyer.
		underpathTaskA13 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245563, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245563, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245563, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245563, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245563, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245563, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Infected Saam King.
		underpathTaskA13 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245564, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245564, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245564, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245564, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245564, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245564, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath14() {
		//Transfectant Crawler.
		underpathTaskA14 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245565, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245565, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245565, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245565, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245565, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245565, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Experimental Reian.
		underpathTaskA14 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245567, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245567, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245567, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245567, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245567, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245567, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath15() {
		//Experimental Reian.
		underpathTaskA15 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245568, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245568, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245568, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245568, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245568, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245568, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Cursed Brave Warrior.
		underpathTaskA15 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245569, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245569, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245569, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245569, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245569, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245569, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath16() {
		//Crazy Priest.
		underpathTaskA16 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245570, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(245570, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(245570, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(245570, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(245570, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(245570, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 1000);
		//Aetherflame.
		underpathTaskA16 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(246175, 222.78767f, 276.12140f, 160.4131f, (byte) 90, 1000, "ContaminedUnderpath1");
				sp(246175, 225.05133f, 275.86157f, 160.3114f, (byte) 89, 1500, "ContaminedUnderpath2");
				sp(246175, 227.54712f, 275.85287f, 160.3114f, (byte) 89, 2000, "ContaminedUnderpath3");
				sp(246175, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 2500, "ContaminedUnderpath4");
				sp(246175, 232.00526f, 275.83752f, 160.3114f, (byte) 89, 3000, "ContaminedUnderpath5");
				sp(246175, 234.10661f, 275.83023f, 160.3114f, (byte) 89, 3500, "ContaminedUnderpath6");
			}
		}, 60000);
	}
	private void startContaminedUnderPath17() {
		//MAAD-S.
		underpathTaskA17 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(245575, 229.59123f, 275.84586f, 160.3114f, (byte) 89, 1000, "ContaminedUnderpath4");
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
		if (totalPoints >= 549000) { //Rank S.
			rank = 1;
		} else if (totalPoints >= 544000) { //Rank A.
			rank = 2;
		} else if (totalPoints >= 50) { //Rank B.
			rank = 3;
		} else if (totalPoints >= 50) { //Rank C.
			rank = 4;
		} else if (totalPoints >= 50) { //Rank D.
			rank = 5;
		} else {
			rank = 6;
		}
		return rank;
	}
	
	protected void startInstanceTask() {
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//The player has 3 min to prepare !!! [Timer Red]
				if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
					//Start the instance time !!! [Timer White]
					startMainInstanceTimer();
				}
				//Start wave !!!
				startContaminedUnderPath1();
				sendMsg("[START]: Wave <1/17>");
				//The Zombies are coming. You have to save Atreia.
				sendMsgByRace(1403628, Race.PC_ALL, 0);
            }
        }, 180000)); //...3Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath2();
				underpathTaskA1.cancel(true);
				sendMsg("[START]: Wave <2/17>");
				//You’re hearing a sharp yell.
				sendMsgByRace(1403657, Race.PC_ALL, 0);
            }
        }, 300000)); //...5Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath3();
				underpathTaskA2.cancel(true);
				sendMsg("[START]: Wave <3/17>");
				//You’re hearing lots of footsteps.
				sendMsgByRace(1403655, Race.PC_ALL, 0);
            }
        }, 420000)); //...7Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath4();
				underpathTaskA3.cancel(true);
				sendMsg("[START]: Wave <4/17>");
				//A dark energy is spreading.
				sendMsgByRace(1403662, Race.PC_ALL, 0);
            }
        }, 540000)); //...9Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath5();
				underpathTaskA4.cancel(true);
				sendMsg("[START]: Wave <5/17>");
				//You’re hearing heavy breathing.
				sendMsgByRace(1403656, Race.PC_ALL, 0);
            }
        }, 660000)); //...11Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath6();
				underpathTaskA5.cancel(true);
				sendMsg("[START]: Wave <6/17>");
				//You hear chattering voices.
				sendMsgByRace(1403661, Race.PC_ALL, 0);
            }
        }, 780000)); //...13Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath7();
				underpathTaskA6.cancel(true);
				sendMsg("[START]: Wave <7/17>");
				//You’re hearing terrible animal screams.
				sendMsgByRace(1403659, Race.PC_ALL, 0);
            }
        }, 900000)); //...15Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath8();
				underpathTaskA7.cancel(true);
				sendMsg("[START]: Wave <8/17>");
				//You feel a cold sensation.
				sendMsgByRace(1403660, Race.PC_ALL, 0);
            }
        }, 1020000)); //...17Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath9();
				underpathTaskA8.cancel(true);
				sendMsg("[START]: Wave <9/17>");
				//You’re hearing a sharp yell.
				sendMsgByRace(1403657, Race.PC_ALL, 0);
            }
        }, 1140000)); //...19Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath10();
				underpathTaskA9.cancel(true);
				sendMsg("[START]: Wave <10/17>");
				//You’re hearing lots of footsteps.
				sendMsgByRace(1403655, Race.PC_ALL, 0);
            }
        }, 1260000)); //...21Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath11();
				underpathTaskA10.cancel(true);
				sendMsg("[START]: Wave <11/17>");
				//You’re hearing heavy breathing.
				sendMsgByRace(1403656, Race.PC_ALL, 0);
            }
        }, 1380000)); //...23Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath12();
				underpathTaskA11.cancel(true);
				sendMsg("[START]: Wave <12/17>");
				//You’re hearing terrible animal screams.
				sendMsgByRace(1403659, Race.PC_ALL, 0);
            }
        }, 1500000)); //...25Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath13();
				underpathTaskA12.cancel(true);
				sendMsg("[START]: Wave <13/17>");
				//You feel a cold sensation.
				sendMsgByRace(1403660, Race.PC_ALL, 0);
            }
        }, 1620000)); //...27Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath14();
				underpathTaskA13.cancel(true);
				sendMsg("[START]: Wave <14/17>");
				//You hear chattering voices.
				sendMsgByRace(1403661, Race.PC_ALL, 0);
            }
        }, 1740000)); //...29Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath15();
				underpathTaskA14.cancel(true);
				sendMsg("[START]: Wave <15/17>");
				//A dark energy is spreading.
				sendMsgByRace(1403662, Race.PC_ALL, 0);
            }
        }, 1860000)); //...31Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath16();
				underpathTaskA15.cancel(true);
				sendMsg("[START]: Wave <16/17>");
				//You’re hearing lots of footsteps.
				sendMsgByRace(1403655, Race.PC_ALL, 0);
            }
        }, 1980000)); //...33Min
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startContaminedUnderPath17();
				underpathTaskA16.cancel(true);
				sendMsg("[START]: Wave <17/17>");
				//You’re hearing a heavy, coarse voice.
				sendMsgByRace(1403658, Race.PC_ALL, 0);
            }
        }, 2100000)); //...35Min
    }
	
	@Override
	public void onOpenDoor(Player player, int doorId) {
		if (doorId == 28) {
			startInstanceTask();
			doors.get(28).setOpen(true);
			//The defense turret platform has appeared.
			//You can use Bright Aether to transform it for 15 seconds.
			sendMsgByRace(1403696, Race.PC_ALL, 0);
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		if (!instanceReward.containPlayer(player.getObjectId())) {
			addPlayerReward(player);
		}
		ContaminatedUnderpathPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded()) {
			doReward(player);
		}
		startPrepareTimer();
		final int lunaDetachement = skillRace == Race.ASMODIANS ? 21346 : 21345;
		SkillEngine.getInstance().applyEffectDirectly(lunaDetachement, player, player, 3000000 * 1);
		final int lunaHeal = spawnRace == Race.ASMODIANS ? 703477 : 703477;
		spawn(lunaHeal, 236.77367f, 212.99107f, 160.28148f, (byte) 60);
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
		stopInstanceTask();
        instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[SUCCES]: You survived !!! :) ");
		sendPacket(0, 0);
	}
	
	@Override
	public void doReward(Player player) {
		ContaminatedUnderpathPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			int contaminatedRank = instanceReward.getRank();
			switch (contaminatedRank) {
				case 1: //Rank S
					playerReward.setContaminatedPremiumRewardBundle(1);
					//Contaminated Premium Reward Bundle.
					ItemService.addItem(player, 188055598, 1);
				break;
				case 2: //Rank A
				    playerReward.setContaminatedHighestRewardBundle(1);
					//Contaminated Highest Reward Bundle.
					ItemService.addItem(player, 188055599, 1);
				break;
				case 3: //Rank B
				    playerReward.setContaminatedUnderpathSpecialPouch(1);
					//Contaminated Underpath Special Pouch.
					ItemService.addItem(player, 188055664, 1);
				break;
				case 4: //Rank C
				    playerReward.setContaminatedUnderpathSpecialPouch(1);
					//Contaminated Underpath Special Pouch.
					ItemService.addItem(player, 188055664, 1);
				break;
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new ContaminatedUnderpathReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
	}
	
	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		} if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
		stopInstanceTask();
		isInstanceDestroyed = true;
		instanceReward.clear();
		doors.clear();
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = contaminedTask.head(), end = contaminedTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        contaminedTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21345);
		effectController.removeEffect(21346);
		effectController.removeEffect(22741);
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