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
package com.aionemu.gameserver.services.base;

import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.base.BaseNpc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler
 */

public class Base<BL extends BaseLocation>
{
	private Npc boss, flag;
	private boolean started;
	private final BL baseLocation;
	private Future<?> startAssault, stopAssault;
	private List<Race> list = new ArrayList<Race>();
	private List<Npc> spawned = new ArrayList<Npc>();
	private List<Npc> attackers = new ArrayList<Npc>();
	private final AtomicBoolean finished = new AtomicBoolean();
	private final BaseBossDeathListener baseBossDeathListener = new BaseBossDeathListener(this);
	
	public Base(BL baseLocation) {
		list.add(Race.ASMODIANS);
		list.add(Race.ELYOS);
		list.add(Race.NPC);
		this.baseLocation = baseLocation;
	}
	
	public final void start() {
		boolean doubleStart = false;
		synchronized (this) {
			if (started) {
				doubleStart = true;
			} else {
				started = true;
			}
		} if (doubleStart) {
			return;
		}
		spawn();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			if (getBoss() != null) {
				rmvBaseBossListener();
			}
			despawn(getId());
		}
	}
	
	private List<SpawnGroup2> getBaseSpawns() {
		List<SpawnGroup2> spawns = DataManager.SPAWNS_DATA2.getBaseSpawnsByLocId(getId());
		if (spawns == null) {
		}
		return spawns;
	}
	
	protected void spawn() {
        for (SpawnGroup2 group : getBaseSpawns()) {
            for (SpawnTemplate spawn : group.getSpawnTemplates()) {
                final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
                if (template.getBaseRace().equals(getBaseLocation().getRace())) {
                    if (template.getHandlerType() == null) {
                        Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
                        NpcTemplate npcTemplate = npc.getObjectTemplate();
						if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG)) {
							setFlag(npc);
						}
						getSpawned().add(npc);
					}
				}
			}
		}
		delayedAssault();
		delayedSpawn(getRace());
	}
	
	private void delayedAssault() {
		startAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				chooseAttackersRace();
				sendMsgKiller(getId());
			}
		}, Rnd.get(120, 180) * 60000);
	}
	
	public boolean sendMsgKiller(int id) {
        switch (id) {
            case 90:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v13);
					}
				});
			    return true;
            case 91:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v04);
					}
				});
			    return true;
            case 92:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v12);
					}
				});
			    return true;
			case 93:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v03);
					}
				});
			    return true;
			case 94:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v06);
					}
				});
			    return true;
			case 95:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v05);
					}
				});
			    return true;
			case 96:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v01);
					}
				});
			    return true;
			case 97:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v09);
					}
				});
			    return true;
			case 98:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v11);
					}
				});
			    return true;
			case 99:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v10);
					}
				});
			    return true;
			case 100:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v07);
					}
				});
			    return true;
			case 101:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v02);
					}
				});
			    return true;
			case 102:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v08);
					}
				});
			    return true;
			//Shugo Negotiator 5.3
			case 105: //Oharung At The Sulfur Archipelago.
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Sulfur Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_01);
							//The Asmodians have rescued the Oharung at the Sulfur Tree Archipelago. As a reward, the ship supports the Asmodians.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_01, 5000);
							//The Steel Rose Mercenaries hired by the Oharung were dispatched to the Sulfur Tree Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_01, 10000);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Steel Rose Mercenaries hired by the Elyos have arrived at the Sulfur Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_01);
							//The Elyos have rescued the Oharung at the Sulfur Tree Archipelago. As a reward, the ship supports the Elyos.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_01, 5000);
							//The Steel Rose Mercenaries hired by the Oharung were dispatched to the Sulfur Tree Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_01, 10000);
						}
					}
				});
				return true;
			case 106: //Joarin At Zephyr Island.
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Asmodians have rescued the Joarin at Zephyr Island. As a reward, the ship supports the Asmodians.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_02);
							//With the support of the Joarin, your attacks against the Balaur have been bolstered.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_02, 5000);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Elyos have rescued the Joarin at Zephyr Island. As reward, the ship supports the Elyos.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_02);
							//With the support of the Joarin, your attacks against the Balaur have been bolstered.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_02, 5000);
						}
					}
				});
				return true;
			case 107: //Temirun At Leibo Island.
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Asmodians have rescued the Temirun at Leibo Island. As a reward, the ship supports the Asmodians.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_03);
							//With the support of the Temirun, your attacks against the Balaur have been bolstered.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_03, 5000);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Elyos have rescued the Temirun at Leibo Island. As reward, the ship supports the Elyos.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_03);
							//With the support of the Temirun, your attacks against the Balaur have been bolstered.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_03, 5000);
						}
					}
				});
				return true;
			case 108: //Shairing At Carpus Isle.
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Asmodians have rescued the Shairing at Storm Island. As a reward, the ship supports the Asmodians.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_04);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Elyos have rescued the Shairing at Storm Island. As reward, the ship will support the Elyos.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_04);
						}
					}
				});
				return true;
			case 109: //Bomishung At Siel's Left Wing.
			    World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Siel's Western Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_02);
							//The Asmodians have rescued the Bomishung at the Siel's Left Wing. As a reward, the ship supports the Asmodians.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_05, 5000);
							//The Steel Rose Mercenaries hired by the Bomishung were dispatched to Siel's Western Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_05, 10000);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Steel Rose Mercenaries hired by the Elyos have arrived at the Siel's Western Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_02);
							//The Elyos have rescued the Bomishung at the Siel's Left Wing. As a reward, the ship supports the Elyos.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_05, 5000);
							//The Steel Rose Mercenaries hired by the Bomishung were dispatched to Siel's Western Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_05, 10000);
						}
					}
				});
				return true;
			case 110: //Sasming At Siel's Right Wing.
			    World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getCommonData().getRace() == Race.ELYOS) {
							//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Siel's Eastern Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_03);
							//The Asmodians have rescued the Sasming at the Siel's Right Wing. As a reward, the ship supports the Asmodians.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuD_06, 5000);
							//The Steel Rose Mercenaries hired by the Sasming were dispatched to Siel's Eastern Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_06, 10000);
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							//The Steel Rose Mercenaries hired by the Elyos have arrived at the Siel's Eastern Fortress.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_03);
							//The Elyos have rescued the Sasming at the Siel's Right Wing. As a reward, the ship supports the Elyos.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuL_06, 5000);
							//The Steel Rose Mercenaries hired by the Sasming were dispatched to Siel's Eastern Fortress.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_Buff_06, 10000);
						}
					}
				});
				return true;
            default:
                return false;
        }
    }
	
	private void delayedSpawn(final Race race) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (getRace().equals(race) && getBoss() == null) {
					spawnBoss();
				}
			}
		}, Rnd.get(5, 10) * 60000);
	}
	
	protected void spawnBoss() {
		for (SpawnGroup2 group : getBaseSpawns()) {
			for (SpawnTemplate spawn : group.getSpawnTemplates()) {
				final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
				if (template.getBaseRace().equals(getBaseLocation().getRace())) {
					if (template.getHandlerType() != null && template.getHandlerType().equals(SpawnHandlerType.CHIEF)) {
						Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
						setBoss(npc);
						addBaseBossListeners();
						getSpawned().add(npc);
					}
				}
			}
		}
	}
	
	protected void chooseAttackersRace() {
		AtomicBoolean next = new AtomicBoolean(Math.random() < 0.5);
		for (Race race : list) {
			if (!race.equals(getRace())) {
				if (next.compareAndSet(true, false)) {
					continue;
				}
				spawnAttackers(race);
				if (baseLocation.getWorldId() == 400010000) {
					//Rattlefrost Outpost.
					if (getBaseLocation().getId() == 53) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Sliversleet Outpost.
					if (getBaseLocation().getId() == 54) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Coldforge Outpost.
					if (getBaseLocation().getId() == 55) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Shimmerfrost Outpost.
					if (getBaseLocation().getId() == 56) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Icehowl Outpost.
					if (getBaseLocation().getId() == 57) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Chillhaunt Outpost.
					if (getBaseLocation().getId() == 58) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Sootguzzle Outpost.
					if (getBaseLocation().getId() == 59) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Flameruin Outpost.
					if (getBaseLocation().getId() == 60) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Stokebellow Outpost.
					if (getBaseLocation().getId() == 61) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Blazerack Outpost.
					if (getBaseLocation().getId() == 62) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Smoldergeist Outpost.
					if (getBaseLocation().getId() == 63) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					//Moltenspike Outpost.
					if (getBaseLocation().getId() == 64) {
						if (race == Race.ASMODIANS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						} else if (race == Race.ELYOS) {
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
				}
				break;
			}
		}
	}
	
	public void spawnAttackers(Race race) {
		if (getFlag() == null) {
		} else if (!getFlag().getPosition().getMapRegion().isMapRegionActive()) {
			if (Math.random() < 0.5) {
				BaseService.getInstance().capture(getId(), race);
			} else {
				delayedAssault();
			}
			return;
		} if (!isAttacked()) {
			despawnAttackers();
			for (SpawnGroup2 group : getBaseSpawns()) {
				for (SpawnTemplate spawn : group.getSpawnTemplates()) {
					final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
					if (template.getBaseRace().equals(race)) {
						if (template.getHandlerType() != null && template.getHandlerType().equals(SpawnHandlerType.SLAYER)) {
							Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
							getAttackers().add(npc);
						}
					}
				}
			} if (getAttackers().isEmpty()) {
			} else {
				stopAssault = ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnAttackers();
						delayedAssault();
					}
				}, 5 * 60000);
			}
		}
	}
	
	public boolean isAttacked() {
		for (Npc attacker : getAttackers()) {
			if (!attacker.getLifeStats().isAlreadyDead()) {
				return true;
			}
		}
		return false;
	}
	
	protected void despawn(int baseLocationId) {
        setFlag(null);
        Collection<BaseNpc> baseNpcs = World.getInstance().getLocalBaseNpcs(baseLocationId);
        for (BaseNpc npc : baseNpcs) {
            npc.getController().onDelete();
        } if (startAssault != null) {
            startAssault.cancel(true);
        } if (stopAssault != null) {
            stopAssault.cancel(true);
            despawnAttackers();
        }
    }
	
	protected void despawnAttackers() {
		for (Npc attacker : getAttackers()) {
			attacker.getController().onDelete();
		}
		getAttackers().clear();
	}
	
	protected void addBaseBossListeners() {
		AbstractAI ai = (AbstractAI) getBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.addCallback(getBaseBossDeathListener());
	}
	
	protected void rmvBaseBossListener() {
		AbstractAI ai = (AbstractAI) getBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.removeCallback(getBaseBossDeathListener());
	}
	
	public Npc getFlag() {
		return flag;
	}
	
	public void setFlag(Npc flag) {
		this.flag = flag;
	}
	
	public Npc getBoss() {
		return boss;
	}
	
	public void setBoss(Npc boss) {
		this.boss = boss;
	}
	
	public BaseBossDeathListener getBaseBossDeathListener() {
		return baseBossDeathListener;
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public BL getBaseLocation() {
		return baseLocation;
	}
	
	public int getId() {
		return baseLocation.getId();
	}
	
	public Race getRace() {
		return baseLocation.getRace();
	}
	
	public void setRace(Race race) {
		baseLocation.setRace(race);
	}
	
	public List<Npc> getAttackers() {
		return attackers;
	}
	
	public List<Npc> getSpawned() {
		return spawned;
	}
}