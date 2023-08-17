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
package com.aionemu.gameserver.services;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.RvrSchedule;
import com.aionemu.gameserver.configs.schedule.RvrSchedule.Rvr;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.rvr.RvrStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.rvrservice.DirectPortal;
import com.aionemu.gameserver.services.rvrservice.RvrStartRunnable;
import com.aionemu.gameserver.services.rvrservice.Rvrlf3df3;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Rinzler (Encom)
 */
public class RvrService
{
	private RvrSchedule rvrSchedule;
	private Map<Integer, RvrLocation> rvr;
	private static final int duration = CustomConfig.RVR_DURATION;
	private static Logger log = LoggerFactory.getLogger(SvsService.class);
	
	//Brigade General's Urgent Order 4.9.1
	private final Map<Integer, Rvrlf3df3<?>> activeRvr = new FastMap<Integer, Rvrlf3df3<?>>().shared();
	//Heavy Tetran/Kenovikan 5.6
	private FastMap<Integer, VisibleObject> adventPortal = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEffect = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventControl = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventDirecting = new FastMap<Integer, VisibleObject>();
	
	public void initRvrLocations() {
		if (CustomConfig.RVR_ENABLED) {
			rvr = DataManager.RVR_DATA.getRvrLocations();
			for (RvrLocation loc: getRvrLocations().values()) {
				spawn(loc, RvrStateType.PEACE);
			}
			log.info("[RvRService] Loaded " + rvr.size() + " rvr Locations.");
		} else {
			rvr = Collections.emptyMap();
		}
	}
	
	public void initRvr() {
		if (CustomConfig.RVR_ENABLED) {
			log.info("[RvRService] is initialized...");
		    rvrSchedule = RvrSchedule.load();
		    for (Rvr rvr: rvrSchedule.getRvrsList()) {
			    for (String rvrTime: rvr.getRvrTimes()) {
				    CronService.getInstance().schedule(new RvrStartRunnable(rvr.getId()), rvrTime);
			    }
			}
		}
	}
	
	public void startRvr(final int id) {
		final Rvrlf3df3<?> directPortal;
		synchronized (this) {
			if (activeRvr.containsKey(id)) {
				return;
			}
			directPortal = new DirectPortal(rvr.get(id));
			activeRvr.put(id, directPortal);
		}
		directPortal.start();
		rvrCountdownMsg(id);
		LF6RvrCountdownMsg(id);
		DF6RvrCountdownMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopRvr(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopRvr(int id) {
		if (!isRvrInProgress(id)) {
			return;
		}
		Rvrlf3df3<?> directPortal;
		synchronized (this) {
			directPortal = activeRvr.remove(id);
		} if (directPortal == null || directPortal.isFinished()) {
			return;
		}
		directPortal.stop();
	}
	
	public void spawn(RvrLocation loc, RvrStateType rstate) {
		if (rstate.equals(RvrStateType.RVR)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getRvrSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				RvrSpawnTemplate rvrtemplate = (RvrSpawnTemplate) st;
				if (rvrtemplate.getRStateType().equals(rstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(rvrtemplate, 1));
				}
			}
		}
	}
	
   /**
	* Rvr Countdown.
	*/
	public boolean rvrCountdownMsg(int id) {
        switch (id) {
            case 1:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Brigade General's Urgent Order.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL, 0);
						//The Legion's Corridor has opened.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_OPEN, 20000);
						//When the Legion's Corridor closes, you will automatically return to the entrance where you came from.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_DIRECT_PORTAL_CLOSE_COMPULSION_TELEPORT, 60000);
						//The Legion's Corridor will close in 45 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_01, 900000);
						//The Legion's Corridor will close in 30 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_02, 1800000);
						//The Legion's Corridor will close in 15 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_03, 2700000);
						//The Legion's Corridor will close in 10 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_04, 3000000);
						//The Legion's Corridor will close in 5 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_05, 3300000);
						//The Legion's Corridor will close in 1 minutes. Once the corridor is closed, the Alliance is automatically disbanded and members are automatically returned.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_TIMER_NOTICE_06, 3540000);
						//Seraphim Defender Merchant Wirinerk has appeared at Heiron Fortress.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_L_WIN, 3558000);
						//Shedim Defender Merchant Girunerk has appeared at Beluslan Fortress.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_RVR_D_WIN, 3600000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	//Iluma.
	public boolean LF6G1Spawn01Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Asmodian warship will invade in 10 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_01);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6G1Spawn02Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Asmodian warship will invade in 5 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_02);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6G1Spawn03Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Asmodian warship will invade in 3 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_03);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6G1Spawn04Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Asmodian warship will invade in 1 minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_04, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6G1Spawn05Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Asmodian warship Invasion.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Spawn_05, 0);
						if (player.getRace() == Race.ELYOS) {
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Valley of the Lost.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_B_G2_Spawn_Chat_MSG, 10000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Coast of the Light-Deprived.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_B2_G2_Spawn_Chat_MSG, 20000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Five-colored Marshland.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_C_G2_Spawn_Chat_MSG, 30000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of Black Wind Valley.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_D_G2_Spawn_Chat_MSG, 40000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Serene Forest of Spirits.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_E_G2_Spawn_Chat_MSG, 50000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Forest of Dormant Life.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_F_G2_Spawn_Chat_MSG, 60000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Ancient Temple of Life.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_F2_G2_Spawn_Chat_MSG, 70000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Plateau of Zephyr.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G_G2_Spawn_Chat_MSG, 80000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of the Krall Aether Mine.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_H_G2_Spawn_Chat_MSG, 90000);
						    //The Archon Assault Frigate will soon arrive at the Sky Island of Red Mushroom Valley.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_I_G2_Spawn_Chat_MSG, 100000);
							//The Asmodian Frigate Commander has arrived.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Boss_Spawn_01, 110000);
						}
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6EventG2Start02Msg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Asmodian Troopers are retreating after the defeat of their officers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G2_Start_01, 1800000);
						//The Asmodian Troopers will shortly return after completing reconnaissance.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G2_Start_03, 1820000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean LF6RvrCountdownMsg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Elyos frigate invasion will end in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_01, 3000000);
						//The Elyos frigate invasion is about to end.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_02, 3300000);
						//The Elyos frigate invasion has ended.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Evett_G1_Time_End_03, 3540000);
						//The defense against the Elyos warship failed. The Asmodians have attacked Ariel's Sanctuary.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_LF6_Event_G1_Defence_Failed, 3600000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	//Norsvold.
	public boolean DF6G1Spawn01Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Elyos warship will invade in 10 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_01);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6G1Spawn02Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Elyos warship will invade in 5 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_02);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6G1Spawn03Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Elyos warship will invade in 3 minutes.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_03);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6G1Spawn04Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//An Elyos warship will invade in 1 minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_04, 10000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6G1Spawn05Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Elyos warship Invasion.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Spawn_05, 0);
						if (player.getRace() == Race.ASMODIANS) {
							//The Guardian Assault Frigate will soon arrive at the Sky Island of the Feather Bough Forest.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_B_G2_Spawn_Chat_MSG, 10000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Territory of Spiritus.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_B2_G2_Spawn_Chat_MSG, 20000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Cursed Canyon.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_C_G2_Spawn_Chat_MSG, 30000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of Kalidag Canyon.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_D_G2_Spawn_Chat_MSG, 40000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Plateau of Judgment.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_E_G2_Spawn_Chat_MSG, 50000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Blue Illusion Forest.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_F_G2_Spawn_Chat_MSG, 60000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Ruins of Lost Time.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_F2_G2_Spawn_Chat_MSG, 70000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of the Lake of Life.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G_G2_Spawn_Chat_MSG, 80000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of Black Mane Mountains.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_H_G2_Spawn_Chat_MSG, 90000);
						    //The Guardian Assault Frigate will soon arrive at the Sky Island of Saphora Forest.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_I_G2_Spawn_Chat_MSG, 100000);
							//The Elyos Frigate Commander has arrived.
						    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_G1_Boss_Spawn_01, 110000);
						}
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6EventG2Start02Msg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Aetos are retreating after the defeat of their officers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G2_Start_01, 1900000);
						//The Elyos Troopers will shortly return after completing reconnaissance.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G2_Start_03, 1920000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean DF6RvrCountdownMsg(int id) {
        switch (id) {
            case 4:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Asmodian frigate invasion will end in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_01, 3050000);
						//The Asmodian frigate invasion is about to end.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_02, 3290000);
						//The Asmodian frigate invasion has ended.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Evett_G1_Time_End_03, 3530000);
						//The defense against the Asmodian warship failed. The Elyos have attacked Azphel's Sanctuary.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_DF6_Event_G1_Defence_Failed, 3590000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
   /**
	* Heavy Tetran/Kenovikan Msg.
	*/
	public boolean F6RaidStart(int id) {
        switch (id) {
            case 5:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Archon's Weapon Invasion.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Start_LF6);
						//Ancient's Weapon Invasion.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Start_DF6);
						//Norsvold Tower Fragment Retrieval Operation.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_InvasionStart_Light);
						//Iluma Tower Fragment Retrieval Operation.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_InvasionStart_Dark);
						//An intruder has appeared, strengthening via the fragment's energy. Destroy all fragments before you return.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_BossSpawn__MSG, 20000);
						//Kenovikan has entered the region.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Spawn_Start_MSG, 30000);
						//Tetran has entered the region.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Spawn_Start_Dark_MSG, 40000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean F6RaidStart5Minute(int id) {
        switch (id) {
            case 5:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Space has been distorted... You should look into that.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Warning_MSG, 0);
						//The infiltration operation entrance for the opposing faction will open soon. Please participate in this operation.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Spawn_DF6_Attack_MSG, 20000);
						//Tetran's intrusion was detected.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Spawn_DF6_5minute_MSG, 40000);
						//Kenovikan's intrusion was detected.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_Spawn_LF6_5minute_MSG, 50000);
						//Kenovikan will arrive soon. Stop the Asmodian invasion!
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_ST_BossSpawn_MSG, 70000);
						//Tetran will arrive soon. Stop the Elyos invasion!
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_F6_Raid_ST_Dark_BossSpawn_MSG, 80000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public boolean adventControlSP(int id) {
        switch (id) {
            case 5:
                adventControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210100000, 702529, 2722.799f, 1424.293f, 227.375f, (byte) 53), 1));
				adventControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220110000, 702529, 2478.824f, 1804.861f, 216.271f, (byte) 56), 1));
			    return true;
            default:
                return false;
        }
    }
	public boolean adventEffectSP(int id) {
        switch (id) {
            case 5:
                adventEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210100000, 702549, 2722.799f, 1424.293f, 227.375f, (byte) 53), 1));
				adventEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220110000, 702549, 2478.824f, 1804.861f, 216.271f, (byte) 56), 1));
			    return true;
            default:
                return false;
        }
	}
	public boolean adventPortalSP(int id) {
        switch (id) {
            case 5:
                adventPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210100000, 702550, 2722.799f, 1424.293f, 227.375f, (byte) 53), 1));
				adventPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220110000, 702550, 2478.824f, 1804.861f, 216.271f, (byte) 56), 1));
			    return true;
            default:
                return false;
        }
    }
	public boolean adventDirectingSP(int id) {
        switch (id) {
            case 5:
                adventDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210100000, 855231, 2722.799f, 1424.293f, 227.375f, (byte) 53), 1));
				adventDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220110000, 855231, 2478.824f, 1804.861f, 216.271f, (byte) 56), 1));
			    return true;
            default:
                return false;
        }
	}
	
	public void despawn(RvrLocation loc) {
		if (loc.getSpawned() == null) {
        	return;
		} for (VisibleObject obj: loc.getSpawned()) {
            Npc spawned = (Npc) obj;
            spawned.setDespawnDelayed(true);
            if (spawned.getAggroList().getList().isEmpty()) {
                spawned.getController().cancelTask(TaskId.RESPAWN);
                obj.getController().onDelete();
            }
        }
        loc.getSpawned().clear();
	}
	
	public boolean isRvrInProgress(int id) {
		return activeRvr.containsKey(id);
	}
	
	public Map<Integer, Rvrlf3df3<?>> getActiveRvr() {
		return activeRvr;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public RvrLocation getRvrLocation(int id) {
		return rvr.get(id);
	}
	
	public Map<Integer, RvrLocation> getRvrLocations() {
		return rvr;
	}
	
	public static RvrService getInstance() {
		return RvrServiceHolder.INSTANCE;
	}
	
	private static class RvrServiceHolder {
		private static final RvrService INSTANCE = new RvrService();
	}
}