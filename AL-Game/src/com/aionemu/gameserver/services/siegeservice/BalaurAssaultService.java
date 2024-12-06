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
package com.aionemu.gameserver.services.siegeservice;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.assemblednpc.AssembledNpc;
import com.aionemu.gameserver.model.assemblednpc.AssembledNpcPart;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.assemblednpc.AssembledNpcTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_ASSEMBLER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;
import javolution.util.FastMap;

public class BalaurAssaultService {
	private static final BalaurAssaultService instance = new BalaurAssaultService();
	private Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	private final Map<Integer, FortressAssault> fortressAssaults = new FastMap<Integer, FortressAssault>().shared();

	public static BalaurAssaultService getInstance() {
		return instance;
	}

	public void onSiegeStart(final Siege<?> siege) {
		int rvrId = siege.getSiegeLocationId();
		if (siege instanceof FortressSiege) {
			if (!calculateFortressAssault(((FortressSiege) siege).getSiegeLocation())) {
				return;
			}
			switch (rvrId) {
			case 1011:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <DIVINE FORTRESS> !");
					}
				});
				break;
			case 1131:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <SIEL'S WESTERN FORTRESS> !");
					}
				});
				break;
			case 1132:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <SIEL'S EASTERN FORTRESS> !");
					}
				});
				break;
			case 1141:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <SULFUR FORTRESS> !");
					}
				});
				break;
			case 1221:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <KROTAN REFUGE> !");
					}
				});
				break;
			case 1231:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <KYSIS FORTRESS> !");
					}
				});
				break;
			case 1241:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <MIREN FORTRESS> !");
					}
				});
				break;
			case 7011:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <WEALHTHEOW'S KEEP> !");
					}
				});
				break;
			case 10111:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <ARCADIAN FORTRESS> !");
					}
				});
				break;
			case 10211:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <UMBRAL FORTRESS> !");
					}
				});
				break;
			case 10311:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <ETERNUM FORTRESS> !");
					}
				});
				break;
			case 10411:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys4Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur launch an assault on <SKYCLASH FORTRESS> !");
					}
				});
				break;
			}
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Balaur have destroyed the Castle Gate.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DRAGON_DOOR_BROKEN,
							600000);
					// The Balaur have destroyed the Gate Guardian Stone.
					PacketSendUtility.playerSendPacketTime(player,
							SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DRAGON_REPAIR_BROKEN, 1500000);
					// The Balaur have destroyed the Aetheric Field Activation Stone.
					PacketSendUtility.playerSendPacketTime(player,
							SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DRAGON_SHIELD_BROKEN, 2100000);
				}
			});
		} else if (siege instanceof ArtifactSiege) {
			if (!calculateArtifactAssault(((ArtifactSiege) siege).getSiegeLocation())) {
				return;
			}
		} else {
			return;
		}
		newAssault(siege, Rnd.get(1, 600));
		if (LoggingConfig.LOG_SIEGE) {
			log.info("[RVR/SIEGE] Balaur Assault scheduled on Siege ID: " + siege.getSiegeLocationId() + "!");
		}
	}

	public void onSiegeFinish(Siege<?> siege) {
		int locId = siege.getSiegeLocationId();
		if (fortressAssaults.containsKey(locId)) {
			Boolean bossIsKilled = siege.isBossKilled();
			fortressAssaults.get(locId).finishAssault(bossIsKilled);
			if (bossIsKilled && siege.getSiegeLocation().getRace().equals(SiegeRace.BALAUR)) {
				log.info("[RVR/SIEGE] > [FORTRESS:" + siege.getSiegeLocationId()
						+ "] has been captured by Balaur Assault!");
				switch (locId) {
				case 1011:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <DIVINE FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1131:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <SIEL'S WESTERN FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1132:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <SIEL'S EASTERN FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1141:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <SULFUR FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1221:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <KROTAN REFUGE> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1231:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <KYSIS FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 1241:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <MIREN FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 7011:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <WEALHTHEOW'S KEEP> has been captured by Balaur Assault!");
						}
					});
					break;
				case 10111:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <ARCADIAN FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 10211:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <UMBRAL FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 10311:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <ETERNUM FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				case 10411:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							PacketSendUtility.sendSys5Message(player, "\uE005",
									"[RVR/SIEGE]: <SKYCLASH FORTRESS> has been captured by Balaur Assault!");
						}
					});
					break;
				}
			} else {
				log.info("[RVR/SIEGE] > [FORTRESS:" + siege.getSiegeLocationId()
						+ "] Balaur Assault finished without capture!");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys6Message(player, "\uE005",
								"[RVR/SIEGE]: the Balaur failed to capture fortress");
					}
				});
			}
			fortressAssaults.remove(locId);
		}
	}

	private boolean calculateFortressAssault(FortressLocation fortress) {
		boolean isBalaurea = fortress.getWorldId() != 400010000;
		int locationId = fortress.getLocationId();
		if (fortressAssaults.containsKey(locationId)) {
			return false;
		}
		if (!calcFortressInfluence(isBalaurea, fortress)) {
			return false;
		}
		int count = 0;
		for (FortressAssault fa : fortressAssaults.values()) {
			if (fa.getWorldId() == fortress.getWorldId()) {
				count++;
			}
		}
		if (count >= (isBalaurea ? 1 : 2)) {
			return false;
		}
		return true;
	}

	private boolean calculateArtifactAssault(ArtifactLocation artifact) {
		return false;
	}

	public void startAssault(Player player, int location, int delay) {
		if (fortressAssaults.containsKey(location)) {
			PacketSendUtility.sendMessage(player, "Assault on " + location + " was already started");
			return;
		}
		newAssault(SiegeService.getInstance().getSiege(location), delay);
	}

	private void newAssault(Siege<?> siege, int delay) {
		if (siege instanceof FortressSiege) {
			FortressAssault assault = new FortressAssault((FortressSiege) siege);
			assault.startAssault(delay);
			fortressAssaults.put(siege.getSiegeLocationId(), assault);
		} else if (siege instanceof ArtifactSiege) {
			ArtifactAssault assault = new ArtifactAssault((ArtifactSiege) siege);
			assault.startAssault(delay);
		}
	}

	private boolean calcFortressInfluence(boolean isBalaurea, FortressLocation fortress) {
		SiegeRace locationRace = fortress.getRace();
		if (locationRace.equals(SiegeRace.BALAUR) || !fortress.isVulnerable()) {
			return false;
		}
		int ownedForts = 0;
		float influence;
		if (isBalaurea) {
			for (FortressLocation fl : SiegeService.getInstance().getFortresses().values()) {
				if (fl.getWorldId() != 400010000 && !fortressAssaults.containsKey(fl.getLocationId())
						&& fl.getRace().equals(locationRace)) {
					ownedForts++;
				}
			}
			influence = ownedForts >= 2 ? 0.25f : 0.1f;
		} else {
			influence = locationRace.equals(SiegeRace.ASMODIANS) ? Influence.getInstance().getGlobalAsmodiansInfluence()
					: Influence.getInstance().getGlobalElyosInfluence();
		}
		return Rnd.get() < influence * SiegeConfig.BALAUR_ASSAULT_RATE;
	}

	public void spawnDredgion(int spawnId) {
		AssembledNpcTemplate template = DataManager.ASSEMBLED_NPC_DATA.getAssembledNpcTemplate(spawnId);
		FastList<AssembledNpcPart> assembledParts = new FastList<AssembledNpcPart>();
		for (AssembledNpcTemplate.AssembledNpcPartTemplate npcPart : template.getAssembledNpcPartTemplates()) {
			assembledParts.add(new AssembledNpcPart(IDFactory.getInstance().nextId(), npcPart));
		}
		AssembledNpc npc = new AssembledNpc(template.getRouteId(), template.getMapId(), template.getLiveTime(),
				assembledParts);
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		Player findedPlayer;
		while (iter.hasNext()) {
			findedPlayer = iter.next();
			PacketSendUtility.sendPacket(findedPlayer, new SM_NPC_ASSEMBLER(npc));
			// A dredgion has appeared.
			PacketSendUtility.sendPacket(findedPlayer, SM_SYSTEM_MESSAGE.STR_ABYSS_CARRIER_SPAWN);
		}
	}
}