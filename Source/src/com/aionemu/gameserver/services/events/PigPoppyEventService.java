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
public class PigPoppyEventService {
    private static final Logger log = LoggerFactory.getLogger(PigPoppyEventService.class);
    private static List<float[]> floatArray = new ArrayList<float[]>();
    private static final String PIG_POPPY_EVENT_SCHEDULE = EventsConfig.PIG_POPPY_EVENT_SCHEDULE;
    private static int WORLD_ELY = 110010000; // Sanctum
    private static int WORLD_ASMO = 120010000; // Pandaemonium
    private static int NPC_ID = 217385; // Poppy

    /**
     * Rewards !
     */
    private static int[] rewards = pigReward();//cannot get directly u must call an method

    private static int[] pigReward() {
        //initialize an new list
        int[] returnArray;
        //get an list of strings (all configs come from strings)
        String[] list = EventsConfig.PIG_POPPY_REWARDS.split(",");
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
                startEvent(); //To change body of generated methods, choose Tools | Templates.
            }

        },PIG_POPPY_EVENT_SCHEDULE);
        log.info("Scheduled <Pig Poppy Event> based on cron expression: " + EventsConfig.PIG_POPPY_EVENT_SCHEDULE + " duration 30 min");
    }

    /**
     * Start Pig Poppy Event
     */
    public static void startEvent(){
        if (EventsConfig.ENABLE_PIG_POPPY_EVENT) {
            AsmoCoordinates();
            ElyCoordinates();
            announceAll("[Event] Insane Poppy has escaped in Sanctum & Pandaemonium go find him and win the reward!");
        }
        initPigAsmo();
        initPigEly();
        ThreadPoolManager.getInstance().schedule(new Runnable(){

            @Override
            public void run() {
                endEvent(); //To change body of generated methods, choose Tools | Templates.
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
                PacketSendUtility.sendSys3Message(player, "\uE058", msg);
            }
        });
    }

    /**
     * Init World ID's Asmo
     */
    private static void initPigAsmo() {
        float[] coords = floatArray.get(Rnd.get(floatArray.size()));
        SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(WORLD_ASMO, NPC_ID, coords[0], coords[1], coords[2], (byte) coords[3]);
        VisibleObject mainObject = SpawnEngine.spawnObject(spawn, 1);

        if(mainObject instanceof Npc) {
            mainN = (Npc) mainObject;
        }
    }

    /**
     * Init World ID's Ely/Asmo
     */
    private static void initPigEly() {
        float[] coords = floatArray.get(Rnd.get(floatArray.size()));
        SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(WORLD_ELY, NPC_ID, coords[0], coords[1], coords[2], (byte) coords[3]);
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
                    ItemService.addItem(player, id, EventsConfig.PIG_POPPY_EVENT_COUNT_REWARD);
                    PacketSendUtility.sendSys3Message(player, "\uE09B", "Found Insane poppy and got a nice surprise!");
                }
                mainN.getObserveController().removeObserver(this);
                mainN.getController().onDelete();
                initPigAsmo();
                initPigEly();
            }
        };
        if(mainN != null) {
            mainN.getObserveController().attach(observer);
        }
    }

    /**
     * End The Event
     */
    public static void endEvent(){
        announceAll("[Event] Insane Poppy has been escaped thanks for your participation!");
        mainN.getController().onDelete();
    }

    /**
     * Init Coordinates Asmo's & Elyo's
     */
    private static void AsmoCoordinates(){
        floatArray.add(new float[] { 1632.9166f, 1424.3572f, 193.127f, 0f } );
        floatArray.add(new float[] { 1486.164f, 1336.1799f, 176.9295f, 0f } );
        floatArray.add(new float[] { 1449.3718f, 1314.1283f, 195.35262f, 0f } );
        floatArray.add(new float[] { 1367.562f, 1217.9772f, 208.37625f, 0f } );
        floatArray.add(new float[] { 1336.7334f, 1052.437f, 206.088f, 0f } );
        floatArray.add(new float[] { 1377.034f, 1065.1808f, 214.92575f, 0f } );
        floatArray.add(new float[] { 1224.6565f, 1105.8811f, 208.88635f, 0f } );
        floatArray.add(new float[] { 1161.7792f, 1260.4316f, 209.67445f, 0f } );
        floatArray.add(new float[] { 1151.4117f, 1348.3867f, 209.6744f, 0f } );
        floatArray.add(new float[] { 1215.5375f, 1444.1869f, 209.33066f, 0f } );
        floatArray.add(new float[] { 1197.442f, 1575.0511f, 214.03578f, 0f } );
        floatArray.add(new float[] { 11238.8937f, 1487.0414f, 214.13577f, 0f } );
        floatArray.add(new float[] { 1343.2433f, 1533.0582f, 209.80017f, 0f } );
        floatArray.add(new float[] { 1363.108f, 1464.0858f, 209.09084f, 0f } );
        floatArray.add(new float[] { 1394.5568f, 1382.9987f, 208.08516f, 0f } );
        floatArray.add(new float[] { 1516.5026f, 1407.1633f, 201.70367f, 0f } );
        floatArray.add(new float[] { 1493.8002f, 1460.6648f, 176.9295f, 0f } );
        floatArray.add(new float[] { 1275.4224f, 1352.994f, 204.4608f, 0f } );
        floatArray.add(new float[] { 1312.3104f, 1234.2994f, 214.66357f, 0f } );
        floatArray.add(new float[] { 1235.8165f, 1170.0706f, 215.13603f, 0f } );
        floatArray.add(new float[] { 1075.684f, 1065.3511f, 201.52081f, 0f } );
        floatArray.add(new float[] { 992.7121f, 1044.0405f, 201.52101f, 0f } );
        floatArray.add(new float[] { 942.4062f, 1131.7365f, 206.84773f, 0f } );
        floatArray.add(new float[] { 956.87994f, 1188.8582f, 201.4773f, 0f } );//
        floatArray.add(new float[] { 1226.273f, 1306.5815f, 208.125f, 0f } );
        floatArray.add(new float[] { 1016.5605f, 1518.2861f, 220.50787f, 0f } );
    }
    private static void ElyCoordinates(){
        floatArray.add(new float[] { 1375.3103f, 1480.2904f, 570.00366f, 0f } );
        floatArray.add(new float[] { 1317.2561f, 1522.925f, 567.9356f, 0f } );
        floatArray.add(new float[] { 1429.9823f, 1652.2386f, 573.19714f, 0f } );
        floatArray.add(new float[] { 1484.9032f, 1651.9423f, 573.2296f, 0f } );
        floatArray.add(new float[] { 1642.8931f, 1615.0457f, 548.74054f, 0f } );
        floatArray.add(new float[] { 1603.292f, 1490.8684f, 549.5507f, 0f } );
        floatArray.add(new float[] { 1667.1558f, 1361.4783f, 557.2329f, 0f } );
        floatArray.add(new float[] { 1750.8691f, 1402.9253f, 574.8727f, 0f } );
        floatArray.add(new float[] { 1722.307f, 1515.548f, 587.0522f, 0f } );
        floatArray.add(new float[] { 1889.2927f, 1762.3921f, 576.82635f, 0f } );
        floatArray.add(new float[] { 1982.785f, 1751.1995f, 576.7586f, 0f } );
        floatArray.add(new float[] { 1898.7987f, 1621.6406f, 590.35803f, 0f } );
        floatArray.add(new float[] { 2051.538f, 1527.4951f, 581.1387f, 0f } );
        floatArray.add(new float[] { 1938.7406f, 1458.2668f, 590.36285f, 0f } );
        floatArray.add(new float[] { 1838.0339f, 1465.5217f, 590.1739f, 0f } );
        floatArray.add(new float[] { 1896.2542f, 1580.0668f, 590.10864f, 0f } );
        floatArray.add(new float[] { 1812.2086f, 2113.3728f, 527.92975f, 0f } );
        floatArray.add(new float[] { 1851.6115f, 2197.7754f, 528.51184f, 0f } );
        floatArray.add(new float[] { 1755.1313f, 2009.3539f, 517.90717f, 0f } );
        floatArray.add(new float[] { 1909.5095f, 2143.6946f, 524.3187f, 0f } );
        floatArray.add(new float[] { 1337.0829f, 1399.552f, 575.2538f, 0f } );
        floatArray.add(new float[] { 1584.59f, 1511.4647f, 572.50555f, 0f } );
        floatArray.add(new float[] { 1688.3009f, 1615.3771f, 566.76227f, 0f } );
        floatArray.add(new float[] { 1549.2594f, 1380.8866f, 563.80085f, 0f } );
        floatArray.add(new float[] { 1588.8115f, 1425.2795f, 573.0344f, 0f } );
        floatArray.add(new float[] { 1536.8796f, 1599.3599f, 572.9935f, 0f } );
    }
}