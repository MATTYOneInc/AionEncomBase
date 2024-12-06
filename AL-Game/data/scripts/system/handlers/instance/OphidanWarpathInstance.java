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

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.EngulfedOphidanBridgeReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.EngulfedOphidanBridgePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;
import javolution.util.FastList;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=dPpM-kCTrOU
/****/

@InstanceID(301670000)
public class OphidanWarpathInstance extends GeneralInstanceHandler
{
	private long instanceTime;
	private int powerGenerator;
	private Map<Integer, StaticDoor> doors;
    protected EngulfedOphidanBridgeReward engulfedOphidanBridgeReward;
    private float loosingGroupMultiplier = 1;
    private boolean isInstanceDestroyed = false;
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> warpathTask = FastList.newInstance();
	
    protected EngulfedOphidanBridgePlayerReward getPlayerReward(Player player) {
        engulfedOphidanBridgeReward.regPlayerReward(player);
        return (EngulfedOphidanBridgePlayerReward) engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return engulfedOphidanBridgeReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(0, storage.getItemCountByItemId(0));
	}
	
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
        engulfedOphidanBridgeReward.setInstanceStartTime();
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!engulfedOphidanBridgeReward.isRewarded()) {
				    openFirstDoors();
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
					//The Beritra Power Generator is almost completely charged.
				    sendMsgByRace(1403624, Race.PC_ALL, 20000);
                    engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    engulfedOphidanBridgeReward.sendPacket(4, null);
					sp(806391, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 3); //North Idle Power Generator.
					sp(806392, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 42); //South Idle Power Generator.
					sp(833935, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 3); //Beritra Army Power Generator.
					sp(833936, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 42); //Beritra Army Power Generator.
					sp(806272, 758.85846f, 566.28235f, 577.43921f, (byte) 0, 2); //Southern Cave Teleporter.
					sp(806273, 314.84390f, 489.72495f, 597.13184f, (byte) 0, 32); //Northern Cave Teleporter.
					sp(806274, 586.42255f, 477.52847f, 620.75189f, (byte) 0, 155); //Cave Teleport Statue.
					sp(806275, 617.93579f, 508.27386f, 592.09863f, (byte) 0, 156); //Cave Teleport Statue.
				}
            }
        }, 90000)); //...1 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 150000)); //...2 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 210000)); //...3 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 270000)); //...4 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 330000)); //...5 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 390000)); //...6 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 450000)); //...7 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 510000)); //...8 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 570000)); //...9 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 630000)); //...10 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 690000)); //...11 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 750000)); //...12 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 810000)); //...13 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 870000)); //...14 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 930000)); //...15 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 990000)); //...16 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 1050000)); //...17 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Elyos now own the Sealed Reian Relic.
				sendMsgByRace(1403561, Race.PC_ALL, 0);
				spawnChestPartElyos();
				spawnMechanicalElyos();
            }
        }, 1110000)); //...18 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//The Asmodians now control the Sealed Reian Relic.
				sendMsgByRace(1403560, Race.PC_ALL, 0);
				spawnChestPartAsmodians();
				spawnMechanicalAsmodians();
            }
        }, 1170000)); //...19 Minutes 30s
		warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!engulfedOphidanBridgeReward.isRewarded()) {
					Race winnerRace = engulfedOphidanBridgeReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
            }
        }, 1200000));
    }
	
   /**
	* Elyos
	*/
	private void spawnMechanicalElyos() {
	    spawn(833950, 600.0f, 423.0f, 609.1875f, (byte) 68);
		spawn(833950, 677.0f, 485.0f, 599.625f, (byte) 7);
		spawn(833950, 549.0f, 461.0f, 615.9375f, (byte) 40);
		spawn(833950, 634.0f, 539.0f, 589.1875f, (byte) 3);
		spawn(833950, 678.0f, 449.0f, 599.98193f, (byte) 56);
		spawn(833950, 612.0f, 522.0f, 591.48346f, (byte) 81);
		spawn(833950, 472.0f, 530.0f, 604.875f, (byte) 55);
		spawn(833950, 493.0f, 463.0f, 606.5625f, (byte) 27);
		spawn(833950, 605.0f, 513.0f, 591.6789f, (byte) 114);
		spawn(833950, 651.0f, 424.0f, 605.6841f, (byte) 25);
		spawn(833950, 578.0f, 460.0f, 620.15216f, (byte) 111);
		spawn(833950, 694.0f, 479.0f, 599.9584f, (byte) 86);
		spawn(833950, 524.0f, 426.0f, 613.0f, (byte) 23);
		spawn(833950, 573.7f, 482.3f, 620.81024f, (byte) 31);
		spawn(833950, 643.0f, 440.0f, 605.625f, (byte) 23);
		spawn(833950, 562.0f, 534.0f, 599.875f, (byte) 74);
		spawn(833950, 595.0f, 379.0f, 609.57855f, (byte) 108);
		spawn(833950, 573.0f, 397.0f, 609.1875f, (byte) 56);
		spawn(833950, 668.0f, 454.0f, 599.75f, (byte) 106);
		spawn(833950, 670.0f, 521.0f, 595.875f, (byte) 17);
		spawn(833950, 579.0f, 421.0f, 609.7527f, (byte) 110);
		spawn(833950, 680.0f, 468.0f, 599.75f, (byte) 48);
		spawn(833950, 628.0f, 430.0f, 607.125f, (byte) 72);
		spawn(833950, 597.0f, 395.0f, 609.25104f, (byte) 14);
	}
	private void spawnChestPartElyos() {
	    spawn(833951, 614.75696f, 508.87222f, 592.0906f, (byte) 32);
		spawn(833951, 626.0f, 519.0f, 592.29364f, (byte) 67);
		spawn(833951, 570.29816f, 425.61432f, 611.41455f, (byte) 99);
		spawn(833951, 618.9085f, 522.03625f, 591.74426f, (byte) 71);
		spawn(833951, 624.8603f, 515.6118f, 592.44324f, (byte) 52);
		spawn(833951, 686.7708f, 490.0117f, 599.86646f, (byte) 59);
		spawn(833951, 570.1571f, 480.35446f, 620.5303f, (byte) 3);
		spawn(833951, 570.1781f, 468.12268f, 620.2185f, (byte) 11);
		spawn(833951, 611.78326f, 407.5259f, 608.51807f, (byte) 43);
		spawn(833951, 668.7937f, 463.2208f, 599.5267f, (byte) 97);
		spawn(833951, 608.2965f, 509.28085f, 591.5489f, (byte) 15);
		spawn(833951, 454.84802f, 506.42538f, 604.50684f, (byte) 107);
		spawn(833951, 615.49066f, 523.0673f, 591.66815f, (byte) 86);
		spawn(833951, 604.1456f, 547.00757f, 590.5f, (byte) 90);
		spawn(833951, 606.0356f, 423.58078f, 607.99335f, (byte) 88);
		spawn(833951, 611.50806f, 566.4567f, 590.71704f, (byte) 83);
		spawn(833951, 598.4454f, 381.3276f, 609.57855f, (byte) 53);
		spawn(833951, 672.76544f, 469.15033f, 599.32715f, (byte) 6);
		spawn(833951, 490.71436f, 482.9249f, 605.1118f, (byte) 63);
		spawn(833951, 656.13776f, 466.9984f, 600.2328f, (byte) 119);
		spawn(833951, 489.4269f, 509.61548f, 605.0651f, (byte) 74);
		spawn(833951, 591.1686f, 427.19696f, 610.3221f, (byte) 90);
		spawn(833951, 591.27454f, 382.10886f, 609.125f, (byte) 9);
		spawn(833951, 473.67172f, 502.37726f, 605.25f, (byte) 43);
		spawn(833951, 474.93942f, 475.15845f, 604.25f, (byte) 6);
		spawn(833951, 589.9174f, 545.2889f, 590.67975f, (byte) 5);
		spawn(833951, 689.20935f, 453.58746f, 599.9777f, (byte) 38);
		spawn(833951, 659.10974f, 458.06223f, 601.0303f, (byte) 18);
		spawn(833951, 466.38913f, 482.13803f, 604.44025f, (byte) 17);
		spawn(833951, 577.8969f, 388.76083f, 609.20245f, (byte) 20);
		spawn(833951, 583.5908f, 461.02527f, 620.1254f, (byte) 44);
		spawn(833951, 609.1258f, 384.5054f, 609.57855f, (byte) 20);
		spawn(833951, 671.76294f, 489.84125f, 599.75f, (byte) 98);
		spawn(833951, 612.0762f, 395.11655f, 609.19476f, (byte) 48);
		spawn(833951, 479.1732f, 499.45947f, 605.0f, (byte) 4);
		spawn(833951, 594.2503f, 569.0778f, 590.91034f, (byte) 95);
		spawn(833951, 459.89664f, 483.47577f, 604.4813f, (byte) 31);
		spawn(833951, 568.2626f, 478.05176f, 620.2774f, (byte) 92);
		spawn(833951, 620.1892f, 567.508f, 591.2927f, (byte) 66);
		spawn(833951, 681.99725f, 457.24365f, 599.9777f, (byte) 39);
		spawn(833951, 665.2871f, 478.41504f, 599.21014f, (byte) 39);
		spawn(833951, 662.5696f, 474.53754f, 599.1606f, (byte) 53);
		spawn(833951, 489.15237f, 489.4455f, 605.0f, (byte) 62);
		spawn(833951, 620.2688f, 543.25995f, 590.75f, (byte) 40);
		spawn(833951, 574.73724f, 454.57498f, 620.34515f, (byte) 49);
		spawn(833951, 480.36932f, 516.76117f, 604.70245f, (byte) 58);
		spawn(833951, 582.88556f, 481.7509f, 620.74567f, (byte) 84);
	}
	
   /**
	* Asmodians
	*/
	private void spawnMechanicalAsmodians() {
	    spawn(833960, 600.0f, 423.0f, 609.1875f, (byte) 68);
		spawn(833960, 677.0f, 485.0f, 599.625f, (byte) 7);
		spawn(833960, 549.0f, 461.0f, 615.9375f, (byte) 40);
		spawn(833960, 634.0f, 539.0f, 589.1875f, (byte) 3);
		spawn(833960, 678.0f, 449.0f, 599.98193f, (byte) 56);
		spawn(833960, 612.0f, 522.0f, 591.48346f, (byte) 81);
		spawn(833960, 472.0f, 530.0f, 604.875f, (byte) 55);
		spawn(833960, 493.0f, 463.0f, 606.5625f, (byte) 27);
		spawn(833960, 605.0f, 513.0f, 591.6789f, (byte) 114);
		spawn(833960, 651.0f, 424.0f, 605.6841f, (byte) 25);
		spawn(833960, 578.0f, 460.0f, 620.15216f, (byte) 111);
		spawn(833960, 694.0f, 479.0f, 599.9584f, (byte) 86);
		spawn(833960, 524.0f, 426.0f, 613.0f, (byte) 23);
		spawn(833960, 573.7f, 482.3f, 620.81024f, (byte) 31);
		spawn(833960, 643.0f, 440.0f, 605.625f, (byte) 23);
		spawn(833960, 562.0f, 534.0f, 599.875f, (byte) 74);
		spawn(833960, 595.0f, 379.0f, 609.57855f, (byte) 108);
		spawn(833960, 573.0f, 397.0f, 609.1875f, (byte) 56);
		spawn(833960, 668.0f, 454.0f, 599.75f, (byte) 106);
		spawn(833960, 670.0f, 521.0f, 595.875f, (byte) 17);
		spawn(833960, 579.0f, 421.0f, 609.7527f, (byte) 110);
		spawn(833960, 680.0f, 468.0f, 599.75f, (byte) 48);
		spawn(833960, 628.0f, 430.0f, 607.125f, (byte) 72);
		spawn(833960, 597.0f, 395.0f, 609.25104f, (byte) 14);
	}
	private void spawnChestPartAsmodians() {
	    spawn(833961, 614.75696f, 508.87222f, 592.0906f, (byte) 32);
		spawn(833961, 626.0f, 519.0f, 592.29364f, (byte) 67);
		spawn(833961, 570.29816f, 425.61432f, 611.41455f, (byte) 99);
		spawn(833961, 618.9085f, 522.03625f, 591.74426f, (byte) 71);
		spawn(833961, 624.8603f, 515.6118f, 592.44324f, (byte) 52);
		spawn(833961, 686.7708f, 490.0117f, 599.86646f, (byte) 59);
		spawn(833961, 570.1571f, 480.35446f, 620.5303f, (byte) 3);
		spawn(833961, 570.1781f, 468.12268f, 620.2185f, (byte) 11);
		spawn(833961, 611.78326f, 407.5259f, 608.51807f, (byte) 43);
		spawn(833961, 668.7937f, 463.2208f, 599.5267f, (byte) 97);
		spawn(833961, 608.2965f, 509.28085f, 591.5489f, (byte) 15);
		spawn(833961, 454.84802f, 506.42538f, 604.50684f, (byte) 107);
		spawn(833961, 615.49066f, 523.0673f, 591.66815f, (byte) 86);
		spawn(833961, 604.1456f, 547.00757f, 590.5f, (byte) 90);
		spawn(833961, 606.0356f, 423.58078f, 607.99335f, (byte) 88);
		spawn(833961, 611.50806f, 566.4567f, 590.71704f, (byte) 83);
		spawn(833961, 598.4454f, 381.3276f, 609.57855f, (byte) 53);
		spawn(833961, 672.76544f, 469.15033f, 599.32715f, (byte) 6);
		spawn(833961, 490.71436f, 482.9249f, 605.1118f, (byte) 63);
		spawn(833961, 656.13776f, 466.9984f, 600.2328f, (byte) 119);
		spawn(833961, 489.4269f, 509.61548f, 605.0651f, (byte) 74);
		spawn(833961, 591.1686f, 427.19696f, 610.3221f, (byte) 90);
		spawn(833961, 591.27454f, 382.10886f, 609.125f, (byte) 9);
		spawn(833961, 473.67172f, 502.37726f, 605.25f, (byte) 43);
		spawn(833961, 474.93942f, 475.15845f, 604.25f, (byte) 6);
		spawn(833961, 589.9174f, 545.2889f, 590.67975f, (byte) 5);
		spawn(833961, 689.20935f, 453.58746f, 599.9777f, (byte) 38);
		spawn(833961, 659.10974f, 458.06223f, 601.0303f, (byte) 18);
		spawn(833961, 466.38913f, 482.13803f, 604.44025f, (byte) 17);
		spawn(833961, 577.8969f, 388.76083f, 609.20245f, (byte) 20);
		spawn(833961, 583.5908f, 461.02527f, 620.1254f, (byte) 44);
		spawn(833961, 609.1258f, 384.5054f, 609.57855f, (byte) 20);
		spawn(833961, 671.76294f, 489.84125f, 599.75f, (byte) 98);
		spawn(833961, 612.0762f, 395.11655f, 609.19476f, (byte) 48);
		spawn(833961, 479.1732f, 499.45947f, 605.0f, (byte) 4);
		spawn(833961, 594.2503f, 569.0778f, 590.91034f, (byte) 95);
		spawn(833961, 459.89664f, 483.47577f, 604.4813f, (byte) 31);
		spawn(833961, 568.2626f, 478.05176f, 620.2774f, (byte) 92);
		spawn(833961, 620.1892f, 567.508f, 591.2927f, (byte) 66);
		spawn(833961, 681.99725f, 457.24365f, 599.9777f, (byte) 39);
		spawn(833961, 665.2871f, 478.41504f, 599.21014f, (byte) 39);
		spawn(833961, 662.5696f, 474.53754f, 599.1606f, (byte) 53);
		spawn(833961, 489.15237f, 489.4455f, 605.0f, (byte) 62);
		spawn(833961, 620.2688f, 543.25995f, 590.75f, (byte) 40);
		spawn(833961, 574.73724f, 454.57498f, 620.34515f, (byte) 49);
		spawn(833961, 480.36932f, 516.76117f, 604.70245f, (byte) 58);
		spawn(833961, 582.88556f, 481.7509f, 620.74567f, (byte) 84);
	}
	
    protected void stopInstance(Race race) {
        stopInstanceTask();
        engulfedOphidanBridgeReward.setWinnerRace(race);
        engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        engulfedOphidanBridgeReward.sendPacket(5, null);
    }
	
    @Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            engulfedOphidanBridgeReward.regPlayerReward(player);
        }
        sendEnterPacket(player);
    }
	
    private void sendEnterPacket(final Player player) {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player opponent) {
                if (player.getRace() != opponent.getRace()) {
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime2(), getInstanceReward(), player.getObjectId()));
                    PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime2(), getInstanceReward(), opponent.getObjectId()));
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime2(), getInstanceReward(),  player.getObjectId()));
                } else {
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime2(), getInstanceReward(), opponent.getObjectId()));
                    if (player.getObjectId() != opponent.getObjectId()) {
                        PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime2(), getInstanceReward(), player.getObjectId(), 20, 0));
                    }
                }
            }
        });
    	sendPacket(true);
    	sendPacket(false);
        PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, getTime2(), getInstanceReward(), player.getObjectId(), 20, 0));
    }
	
    private void startInstancePacket() {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime2(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime2(), engulfedOphidanBridgeReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime2(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime2(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime2(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime2(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        engulfedOphidanBridgeReward = new EngulfedOphidanBridgeReward(mapId, instanceId, instance);
        engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.PREPARING);
        doors = instance.getDoors();
        startInstanceTask();
    }
	
	protected void reward() {
        int ElyosPvPKills = getPvpKillsByRace(Race.ELYOS).intValue();
        int ElyosPoints = getPointsByRace(Race.ELYOS).intValue();
        int AsmoPvPKills = getPvpKillsByRace(Race.ASMODIANS).intValue();
        int AsmoPoints = getPointsByRace(Race.ASMODIANS).intValue();
        for (Player player : instance.getPlayersInside()) {
            if (PlayerActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			EngulfedOphidanBridgePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(engulfedOphidanBridgeReward.getWinnerRace())) {
                abyssPoint += engulfedOphidanBridgeReward.AbyssReward(true, true);
                gloryPoint += engulfedOphidanBridgeReward.GloryReward(true, true);
				expPoint += engulfedOphidanBridgeReward.ExpReward(true, true);
                playerReward.setBonusAp(engulfedOphidanBridgeReward.AbyssReward(true, true));
                playerReward.setBonusGp(engulfedOphidanBridgeReward.GloryReward(true, true));
				playerReward.setBonusExp(engulfedOphidanBridgeReward.ExpReward(true, true));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
				playerReward.setAdditionalReward(188055394);
			} else {
                abyssPoint += engulfedOphidanBridgeReward.AbyssReward(false, false);
                gloryPoint += engulfedOphidanBridgeReward.GloryReward(false, false);
				expPoint += engulfedOphidanBridgeReward.ExpReward(false, false);
				playerReward.setRewardAp(engulfedOphidanBridgeReward.AbyssReward(false, false));
                playerReward.setRewardGp(engulfedOphidanBridgeReward.GloryReward(false, false));
				playerReward.setRewardExp(engulfedOphidanBridgeReward.ExpReward(false, false));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
            }
			ItemService.addItem(player, 188055394, 1);
            ItemService.addItem(player, 188100391, 750); //5.5
			ItemService.addItem(player, 186000243, 1);
            AbyssPointsService.addAp(player, (int) abyssPoint);
            AbyssPointsService.addGp(player, (int) gloryPoint);
            player.getCommonData().addExp(expPoint, RewardType.HUNTING);
        }
        for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
        ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 60000);
    }
	
    private int getTime2() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 90000) {
            return (int) (90000 - result);
        } else if (result < 1200000) { //20-Mins
            return (int) (1200000 - (result - 90000));
        }
        return 0;
    }
	
    @Override
    public boolean onReviveEvent(Player player) {
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        PlayerReviveService.revive(player, 100, 100, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        engulfedOphidanBridgeReward.portToPosition(player);
        return true;
    }
	
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
		EngulfedOphidanBridgePlayerReward ownerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0) {
                    points *= loosingGroupMultiplier;
                } else if (loosingGroupMultiplier == 10 || playerReward.getPoints() == 0) {
                    points = 0;
                }
                updateScore((Player) lastAttacker, player, points, true);
            }
        }
        updateScore(player, player, -points, false);
        return true;
    }
	
	private MutableInt getPvpKillsByRace(Race race) {
        return engulfedOphidanBridgeReward.getPvpKillsByRace(race);
    }
	
    private MutableInt getPointsByRace(Race race) {
        return engulfedOphidanBridgeReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        engulfedOphidanBridgeReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        engulfedOphidanBridgeReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
    }
	
    protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
        if (points == 0) {
            return;
        }
        addPointsByRace(player.getRace(), points);
        List<Player> playersToGainScore = new ArrayList<Player>();
        if (target != null && player.isInGroup2()) {
            for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
                if (member.getLifeStats().isAlreadyDead()) {
                    continue;
                } if (MathUtil.isIn3dRange(member, target, GroupConfig.GROUP_MAX_DISTANCE)) {
                    playersToGainScore.add(member);
                }
            }
        } else {
            playersToGainScore.add(player);
        }
        for (Player playerToGainScore : playersToGainScore) {
            addPointToPlayer(playerToGainScore, points / playersToGainScore.size());
            if (target instanceof Npc) {
                PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(((Npc) target).getObjectTemplate().getNameId() * 2 + 1), points));
            } else if (target instanceof Player) {
                PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, target.getName(), points));
            }
        }
        int pointDifference = getPointsByRace(Race.ASMODIANS).intValue() - (getPointsByRace(Race.ELYOS)).intValue();
        if (pointDifference < 0) {
            pointDifference *= -1;
        } if (pointDifference >= 3000) {
            loosingGroupMultiplier = 10;
        } else if (pointDifference >= 1000) {
            loosingGroupMultiplier = 1.5f;
        } else {
            loosingGroupMultiplier = 1;
        } if (pvpKill && points > 0) {
            addPvpKillsByRace(player.getRace(), 1);
            addPvPKillToPlayer(player);
        }
        engulfedOphidanBridgeReward.sendPacket(11, player.getObjectId());
        if (engulfedOphidanBridgeReward.hasCapPoints()) {
            stopInstance(engulfedOphidanBridgeReward.getWinnerRaceByScore());
        }
    }
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("NORTH_POST_301670000")) {
            powerGenerator = 1;
	    } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SOUTH_POST_301670000")) {
			powerGenerator = 2;
		}
    }
	
    @Override
	public void onDie(Npc npc) {
        int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        }
		Race race = mostPlayerDamage.getRace();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 243962: //눈길 고개 페슬롯.
			case 243963: //눈길 고개 만�?리.
			case 243964: //난�?�한 눈길 고개 만�?리.
			    point = 25;
				despawnNpc(npc);
            break;
        }
        updateScore(mostPlayerDamage, npc, point, false);
    }
	
    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
		int point = 0;
		switch (npc.getNpcId()) {
			case 701947: //Elyos Field Gun.
			case 701949: //Elyos Field Gun.
                SkillEngine.getInstance().getSkill(npc, 21065, 1, player).useNoAnimationSkill();
            break;
			case 701948: //Asmodians Field Gun.
			case 701950: //Asmodians Field Gun.
                SkillEngine.getInstance().getSkill(npc, 21066, 1, player).useNoAnimationSkill();
            break;
			case 833935: //브리트�?�군 �?�력 장치.
				point = 1000;
				despawnNpc(npc);
				deleteNpc(806425);
				deleteNpc(806391);
				if (powerGenerator == 1) {
					switch (player.getRace()) {
						case ELYOS:
						    //The Elyos have activated the Beritra Power Generator.
							sendMsgByRace(1403449, Race.PC_ALL, 0);
						    sp(802036, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 0); //North Post Flag.
							sp(806391, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 3); //North Power Generator.
							SkillEngine.getInstance().getSkill(npc, 21336, 1, player).useNoAnimationSkill(); //Shugo Alchemical Enhancement Device.
						break;
					    case ASMODIANS:
						    //The Asmodians have activated the Beritra Power Generator.
							sendMsgByRace(1403450, Race.PC_ALL, 0);
						    sp(802037, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 0); //North Post Flag.
						    sp(806391, 589.974180f, 407.85278f, 610.20313f, (byte) 0, 3); //North Power Generator.
							SkillEngine.getInstance().getSkill(npc, 21337, 1, player).useNoAnimationSkill(); //Shugo Alchemical Enhancement Device.
						break;
					}
				}
				//The Beritra Power Generator is completely charged and can be used.
				//The device is close to being overloaded and cannot be charged anymore.
				sendMsgByRace(1403453, Race.PC_ALL, 5000);
			break;
			case 833936: //브리트�?�군 �?�력 장치.
				point = 1000;
				despawnNpc(npc);
				deleteNpc(806425);
				deleteNpc(806392);
				if (powerGenerator == 2) {
					switch (player.getRace()) {
						case ELYOS:
						    //The Elyos have activated the Beritra Power Generator.
							sendMsgByRace(1403449, Race.PC_ALL, 0);
						    sp(802039, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 0); //South Post Flag.
						    sp(806392, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 42); //South Power Generator.
							SkillEngine.getInstance().getSkill(npc, 21336, 1, player).useNoAnimationSkill(); //Shugo Alchemical Enhancement Device.
						break;
					    case ASMODIANS:
						    //The Asmodians have activated the Beritra Power Generator.
							sendMsgByRace(1403450, Race.PC_ALL, 0);
						    sp(802040, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 0); //South Post Flag.
						    sp(806392, 605.049130f, 553.60150f, 591.49310f, (byte) 0, 42); //South Idle Power Generator.
							SkillEngine.getInstance().getSkill(npc, 21337, 1, player).useNoAnimationSkill(); //Shugo Alchemical Enhancement Device.
						break;
					}
				}
				//The Beritra Power Generator is completely charged and can be used.
				//The device is close to being overloaded and cannot be charged anymore.
				sendMsgByRace(1403453, Race.PC_ALL, 5000);
			break;
			case 833950: //Mechanical Weapon Test Part.
			case 833960: //Mechanical Weapon Test Part.
                point = 200;
				despawnNpc(npc);
				//You’ve retrieved the Mechanical Weapon Test Parts from the Odd Ophidan Advanced Route.
				sendMsgByRace(1403555, Race.PC_ALL, 0);
            break;
			case 833951: //Mechanical Weapon Test Part Box.
			case 833961: //Mechanical Weapon Test Part Box.
                point = 2000;
				despawnNpc(npc);
            break;
        }
		updateScore(player, npc, point, false);
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
	
    @Override
    public void onInstanceDestroy() {
        engulfedOphidanBridgeReward.clear();
        isInstanceDestroyed = true;
        stopInstanceTask();
        doors.clear();
    }
	
    protected void openFirstDoors() {
        openDoor(176);
		openDoor(177);
    }
	
    protected void openDoor(int doorId) {
        StaticDoor door = doors.get(doorId);
        if (door != null) {
            door.setOpen(true);
        }
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
    protected void sendMsgByRace(final int msg, final Race race, int time) {
        warpathTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
    private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = warpathTask.head(), end = warpathTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    @Override
    public InstanceReward<?> getInstanceReward() {
        return engulfedOphidanBridgeReward;
    }
	
    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
    @Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		EngulfedOphidanBridgePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
		removeItems(player);
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
    @Override
    public void onPlayerLogin(Player player) {
        engulfedOphidanBridgeReward.sendPacket(10, player.getObjectId());
    }
}