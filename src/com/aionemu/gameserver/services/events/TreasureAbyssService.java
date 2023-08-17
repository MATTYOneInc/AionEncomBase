/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services.events;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rinzler (Encom)
 */
public class TreasureAbyssService {
	private static final Logger log = LoggerFactory.getLogger(TreasureAbyssService.class);
	private static List<float[]> floatArray = new ArrayList<float[]>();
	private static final String ABYSS_EVENT_SCHEDULE = EventsConfig.ABYSS_EVENT_SCHEDULE;
	private static int WORLD_ID = 400010000;
	private static int NPC_ID = 210596;

    /**
     * Rewards !
     */
    private static int[] rewards = TreasureReward();//cannot get directly u must call an method

    private static int[] TreasureReward() {
        //initialize an new list
        int[] returnArray;
        //get an list of strings (all configs come from strings)
        String[] list = EventsConfig.ABYSS_EVENT_REWARDS.split(",");
        returnArray = new int[list.length];
        //run all the itens and put in the int array
        for (int i =0; i < list.length; i++ ) {
            returnArray[i] = Integer.parseInt(list[i]);

        }
        //return the int array
        return returnArray;
    }

    private static Npc mainN;

    /**
     * Schedule
     */
    public static void ScheduleCron(){
            CronService.getInstance().schedule(new Runnable(){

                @Override
                public void run() {
                    startEvent();
                }

            },ABYSS_EVENT_SCHEDULE);
            log.info("Scheduled <Treasure Abyss Event> based on cron expression: " + EventsConfig.ABYSS_EVENT_SCHEDULE + " duration 30 min");
        }

    public static void startEvent(){
        if (EventsConfig.ENABLE_ABYSS_EVENT) {
            initCoordinates();
        }
        announceAll("[Event] Balaur treasure chest start location quickly follow to take the prize!");
        initPig();
        ThreadPoolManager.getInstance().schedule(new Runnable(){

            @Override
            public void run() {
                endEvent();
            }
        }, 30 * 60 * 1000);
    }

    /**
     * Announce All
     */
    private static void announceAll(final String msg) {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                PacketSendUtility.sendSys3Message(player, "\uE056", msg);
            }
        });
    }

    /**
     * Init World ID's Abyss
     */
    private static void initPig() {
        float[] coords = floatArray.get(Rnd.get(floatArray.size()));
        SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(WORLD_ID, NPC_ID, coords[0], coords[1], coords[2], (byte) coords[3]);
        VisibleObject mainObject = SpawnEngine.spawnObject(spawn, 1);
        if(mainObject instanceof Npc) {
            mainN = (Npc) mainObject;
        }
        ActionObserver observer = new ActionObserver(ObserverType.ATTACKED){

            @Override
            public void attacked(Creature creature) {
                if(creature instanceof Player) {
                    final Player player = (Player) creature;
                    final int id = rewards[Rnd.get(rewards.length)];
                    ItemService.addItem(player, id, 1);
                    PacketSendUtility.sendSys3Message(player, "\uE09B", "Founds the balaur treasure chest!");
                }
                mainN.getObserveController().removeObserver(this);
                mainN.getController().onDelete();
                initPig();
            }
        };
        if(mainN != null) {
            mainN.getObserveController().attach(observer);
        }
    }

    /**
     * End Event
     */
    public static void endEvent(){
        announceAll("[Event] The balaur treasure chest event has been ended thanks for your participation!");
        mainN.getController().onDelete();
    }

    /**
     * Init Coordinates Abyss
     */
    private static void initCoordinates(){
		floatArray.add(new float[] { 927.2632f, 1347.1643f, 2887.8228f, 0f } );
		floatArray.add(new float[] { 1318.6154f, 1272.6053f, 2950.5398f, 0f } );
		floatArray.add(new float[] { 1646.3756f, 1285.964f, 3027.5366f, 0f } );
		floatArray.add(new float[] { 1879.9838f, 1811.3093f, 2997.9136f, 0f } );
		floatArray.add(new float[] { 1469.5518f, 1145.2373f, 3001.3567f, 0f } );
		floatArray.add(new float[] { 360.03555f, 1725.2064f, 2856.7979f, 0f } );
		floatArray.add(new float[] { 575.1284f, 2417.2117f, 3123.2341f, 0f } );
		floatArray.add(new float[] { 890.849f, 2530.2727f, 2950.2358f, 0f } );
    }
}