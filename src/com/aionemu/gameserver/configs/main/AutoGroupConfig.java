/*

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

public class AutoGroupConfig
{
	@Property(key = "gameserver.autogroup.enable", defaultValue = "true")
	public static boolean AUTO_GROUP_ENABLED;
	
	//DREDGION
	@Property(key = "gameserver.dredgion.timer", defaultValue = "60")
	public static long DREDGION_TIMER;

	@Property(key = "gameserver.dredgion.enable", defaultValue = "true")
	public static boolean DREDGION_ENABLED;
	@Property(key = "gameserver.dredgion.schedule.midday", defaultValue = "0 0 12 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.dredgion.schedule.evening", defaultValue = "0 0 20 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_EVENING;
	@Property(key = "gameserver.dredgion.schedule.midnight", defaultValue = "0 0 23 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_MIDNIGHT;
	
	//KAMAR BATTLEFIELD 4.3
	@Property(key = "gameserver.kamar.timer", defaultValue = "60")
	public static long KAMAR_TIMER;
	@Property(key = "gameserver.kamar.enable", defaultValue = "true")
	public static boolean KAMAR_ENABLED;
	@Property(key = "gameserver.kamar.schedule.midnight", defaultValue = "0 0 23 ? * FRI *")
	public static String KAMAR_SCHEDULE_MIDNIGHT;
	
	//ENGULFED OPHIDAN BRIDGE 4.5
	@Property(key = "gameserver.ophidan.timer", defaultValue = "60")
	public static long OPHIDAN_TIMER;
	@Property(key = "gameserver.ophidan.enable", defaultValue = "true")
	public static boolean OPHIDAN_ENABLED;
	@Property(key = "gameserver.ophidan.schedule.midday", defaultValue = "0 0 12 ? * TUE,THU,SAT *")
	public static String OPHIDAN_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.ophidan.schedule.midnight", defaultValue = "0 0 23 ? * TUE,THU,SAT *")
	public static String OPHIDAN_SCHEDULE_MIDNIGHT;
	
	//IRON WALL WARFRONT 4.5
	@Property(key = "gameserver.bastion.timer", defaultValue = "60")
	public static long BASTION_TIMER;
	@Property(key = "gameserver.bastion.enable", defaultValue = "true")
	public static boolean BASTION_ENABLED;
	@Property(key = "gameserver.bastion.schedule.midnight", defaultValue = "0 0 23 ? * FRI *")
	public static String BASTION_SCHEDULE_MIDNIGHT;
	
	//IDGEL DOME 4.7
	@Property(key = "gameserver.idgel.dome.timer", defaultValue = "60")
	public static long IDGEL_TIMER;
	@Property(key = "gameserver.idgel.dome.enable", defaultValue = "true")
	public static boolean IDGEL_ENABLED;
	@Property(key = "gameserver.idgel.dome.schedule.midday", defaultValue = "0 0 12 ? * MON,WED,FRI *")
	public static String IDGEL_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.idgel.dome.schedule.midnight", defaultValue = "0 0 23 ? * MON,WED,FRI *")
	public static String IDGEL_SCHEDULE_MIDNIGHT;
	
	//ASHUNATAL DREDGION 5.1
	@Property(key = "gameserver.ashunatal.timer", defaultValue = "60")
	public static long ASHUNATAL_TIMER;
	@Property(key = "gameserver.ashunatal.enable", defaultValue = "true")
	public static boolean ASHUNATAL_ENABLED;
	@Property(key = "gameserver.ashunatal.schedule.midday", defaultValue = "0 0 12 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String ASHUNATAL_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.ashunatal.schedule.evening", defaultValue = "0 0 20 ? * MON,TUE,WED,THU,FRI *")
	public static String ASHUNATAL_SCHEDULE_EVENING;
	@Property(key = "gameserver.ashunatal.schedule.midnight", defaultValue = "0 0 23 ? * SAT,SUN *")
	public static String ASHUNATAL_SCHEDULE_MIDNIGHT;
	
	//OPHIDAN WARPATH 5.1
	@Property(key = "gameserver.ophidan.warpath.timer", defaultValue = "60")
	public static long OPHIDAN_WARPATH_TIMER;
	@Property(key = "gameserver.ophidan.warpath.enable", defaultValue = "true")
	public static boolean OPHIDAN_WARPATH_ENABLED;
	@Property(key = "gameserver.ophidan.warpath.schedule.midnight", defaultValue = "0 0 23 ? * TUE,THU *")
	public static String OPHIDAN_WARPATH_SCHEDULE_MIDNIGHT;
	
	//IDGEL DOME LANDMARK 5.1
	@Property(key = "gameserver.idgel.dome.landmark.timer", defaultValue = "60")
	public static long IDGEL_DOME_LANDMARK_TIMER;
	@Property(key = "gameserver.idgel.dome.landmark.enable", defaultValue = "true")
	public static boolean IDGEL_DOME_LANDMARK_ENABLED;
	@Property(key = "gameserver.idgel.dome.landmark.schedule.midnight", defaultValue = "0 0 23 ? * MON,WED *")
	public static String IDGEL_DOME_LANDMARK_SCHEDULE_MIDNIGHT;
	
	//HALL OF TENACITY 5.3
	@Property(key = "gameserver.hall.of.tenacity.timer", defaultValue = "360")
	public static long HALL_OF_TENACITY_TIMER;
	@Property(key = "gameserver.hall.of.tenacity.enable", defaultValue = "true")
	public static boolean HALL_OF_TENACITY_ENABLED;
	@Property(key = "gameserver.hall.of.tenacity.schedule.morning", defaultValue = "0 0 9 ? * SAT,SUN *")
	public static String HALL_OF_TENACITY_SCHEDULE_MORNING;
	@Property(key = "gameserver.hall.of.tenacity.schedule.evening", defaultValue = "0 0 18 ? * MON,WED,TUE,THU,FRI, *")
	public static String HALL_OF_TENACITY_SCHEDULE_EVENING;
	
	//Grand Arena Training Camp 5.6
	@Property(key = "gameserver.grand.arena.training.camp.timer", defaultValue = "360")
	public static long GRAND_ARENA_TRAINING_CAMP_TIMER;
	@Property(key = "gameserver.grand.arena.training.camp.enable", defaultValue = "true")
	public static boolean GRAND_ARENA_TRAINING_CAMP_ENABLED;
	@Property(key = "gameserver.grand.arena.training.camp.schedule.evening", defaultValue = "0 0 18 ? * SAT,SUN *")
	public static String GRAND_ARENA_TRAINING_CAMP_SCHEDULE_EVENING;
	
	//IDTM_Lobby_P02 5.6
	@Property(key = "gameserver.IDTM_Lobby_P02.timer", defaultValue = "360")
	public static long IDTM_LOBBY_P02_TIMER;
	@Property(key = "gameserver.IDTM_Lobby_P02.enable", defaultValue = "true")
	public static boolean IDTM_LOBBY_P02_ENABLED;
	@Property(key = "gameserver.IDTM_Lobby_P02.schedule.evening", defaultValue = "0 0 18 ? * SAT,SUN *")
	public static String IDTM_LOBBY_P02_SCHEDULE_EVENING;
	
	//IDTM_Lobby_E01 5.6
	@Property(key = "gameserver.IDTM_Lobby_E01.timer", defaultValue = "360")
	public static long IDTM_LOBBY_E01_TIMER;
	@Property(key = "gameserver.IDTM_Lobby_E01.enable", defaultValue = "true")
	public static boolean IDTM_LOBBY_E01_ENABLED;
	@Property(key = "gameserver.IDTM_Lobby_E01.schedule.evening", defaultValue = "0 0 18 ? * SAT,SUN *")
	public static String IDTM_LOBBY_E01_SCHEDULE_EVENING;
	
	//IDRun 5.8
	@Property(key = "gameserver.IDRun.timer", defaultValue = "60")
	public static long IDRUN_TIMER;
	@Property(key = "gameserver.IDRun.enable", defaultValue = "true")
	public static boolean IDRUN_ENABLED;
	@Property(key = "gameserver.IDRun.schedule", defaultValue = "0 0 0/2 1/1 * ? *")
	public static String IDRUN_SCHEDULE;
}