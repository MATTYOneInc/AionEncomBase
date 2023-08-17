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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class EventsConfig
{
	@Property(key = "gameserver.event.enable", defaultValue = "false")
	public static boolean EVENT_ENABLED;
	@Property(key = "gameserver.enable.decor", defaultValue = "0")
    public static int ENABLE_DECOR;
	@Property(key = "gameserver.events.give.juice", defaultValue = "160009017")
	public static int EVENT_GIVE_JUICE;
	@Property(key = "gameserver.events.give.cake", defaultValue = "160010073")
	public static int EVENT_GIVE_CAKE;
	@Property(key = "gameserver.event.service.enable", defaultValue = "false")
	public static boolean ENABLE_EVENT_SERVICE;
	
	//VIP Tickets.
	@Property(key = "gameserver.vip.tickets.enable", defaultValue = "false")
	public static boolean ENABLE_VIP_TICKETS;
	@Property(key = "gameserver.vip.tickets.time", defaultValue = "60")
	public static int VIP_TICKETS_PERIOD;
	
	//Event Awake [Event JAP]
	@Property(key = "gameserver.event.awake.enable", defaultValue = "false")
	public static boolean ENABLE_AWAKE_EVENT;
	@Property(key = "gameserver.event.seed.transformation.time", defaultValue = "60")
	public static int SEED_TRANSFORMATION_PERIOD;
	
	//EVENT
	//Shugo Imperial Tomb 4.3
	@Property(key = "gameserver.shugo.imperial.tomb.enable", defaultValue = "true")
	public static boolean IMPERIAL_TOMB_ENABLE;
	@Property(key = "gameserver.shugo.imperial.tomb.timer.from.start.to.end", defaultValue = "10")
	public static long IMPERIAL_TOMB_TIMER;
	@Property(key = "gameserver.shugo.imperial.tomb.time.to.start", defaultValue = "0 0 0,12,20,0 ? * *")
	public static String IMPERIAL_TOMB_TIMES;
	
	//Crazy Daeva.
	@Property(key = "gameserver.crazy.daeva.enable", defaultValue = "false")
	public static boolean ENABLE_CRAZY;
	@Property(key = "gameserver.crazy.daeva.tag", defaultValue = "<Crazy>")
	public static String CRAZY_TAG;
	@Property(key = "gameserver.crazy.daeva.lowest.rnd", defaultValue = "10")
	public static int CRAZY_LOWEST_RND;
	@Property(key = "gameserver.crazy.daeva.time.to.start", defaultValue = "0 0 0,12,20,0 ? * *")
	public static String CRAZY_TIMES;
	@Property(key = "gameserver.crazy.daeva.endtime", defaultValue = "5")
	public static int CRAZY_ENDTIME;
	
	//Upgrade Arcade 4.7
	@Property(key = "gameserver.event.arcade.enable", defaultValue = "false")
	public static boolean ENABLE_EVENT_ARCADE;
	@Property(key="gameserver.upgrade.arcade.chance", defaultValue = "50")
	public static int EVENT_ARCADE_CHANCE;

	/**
	 * Treasure Abyss
	 */
	@Property(key = "gameserver.event.abyss.treasure.enable", defaultValue = "false")
	public static boolean ENABLE_ABYSS_EVENT;
	@Property(key = "gameserver.event.abyss.treasure.time", defaultValue = "0 0 15 ? * SUN")
	public static String ABYSS_EVENT_SCHEDULE;
	@Property(key = "gameserver.event.abyss.rewards ", defaultValue = "0")
	public static String ABYSS_EVENT_REWARDS;

	/**
	 * Pig Poppy Event
	 */
	@Property(key = "gameserver.event.pig.poppy.enable", defaultValue = "false")
	public static boolean ENABLE_PIG_POPPY_EVENT;
	@Property(key = "gameserver.event.pig.poppy", defaultValue = "0 0 20 ? * SAT")
	public static String PIG_POPPY_EVENT_SCHEDULE;
	@Property(key = "gameserver.event.pig.poppy.reward.count", defaultValue = "5")
	public static int PIG_POPPY_EVENT_COUNT_REWARD;
	@Property(key = "gameserver.pig.poppy.rewards", defaultValue = "0")
	public static String PIG_POPPY_REWARDS;

	/**
	 * Shugo Sweep
	 */
	@Property(key = "gameserver.shugoSweep.board", defaultValue = "1")
	public static int EVENT_SHUGOSWEEP_BOARD;
	@Property(key = "gameserver.shugoSweep.freeDice", defaultValue = "5")
	public static int EVENT_SHUGOSWEEP_FREEDICE;

	/**
	 * Youtube Video!
	 */
	@Property(key = "gameserver.event.youtube_video", defaultValue = "https://www.youtube.com/embed/zZ7OhMY5mYg")
	public static String EVENT_YOUTUBE_VIDEO;
}