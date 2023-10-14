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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CONQUEROR_PROTECTOR;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.conquerors.Conqueror;
import com.aionemu.gameserver.services.conquerors.ConquerorBuffs;
import com.aionemu.gameserver.services.protectors.Protector;
import com.aionemu.gameserver.services.protectors.ProtectorBuffs;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;
import javolution.util.FastMap;

public class ProtectorConquerorService
{
	private static final Logger log = LoggerFactory.getLogger(ProtectorConquerorService.class);
	
	private FastMap<Integer, Protector> protectors = new FastMap<Integer, Protector>();
	private FastMap<Integer, Conqueror> conquerors = new FastMap<Integer, Conqueror>();
	
	private FastMap<Integer, FastMap<Integer, Player>> worldConqueror = new FastMap<Integer, FastMap<Integer, Player>>();
	private FastMap<Integer, FastMap<Integer, Player>> worldProtectors = new FastMap<Integer, FastMap<Integer, Player>>();
	
	private static final FastMap<Integer, WorldType> handledWorlds = new FastMap<Integer, WorldType>();
	private int refresh = CustomConfig.PROTECTOR_CONQUEROR_REFRESH;
	private int levelDiff = CustomConfig.PROTECTOR_CONQUEROR_LEVEL_DIFF;
	private ProtectorBuffs protectorBuff;
	private ConquerorBuffs conquerorBuff;;
	
	public enum WorldType {
		ASMODIANS,
		ELYOS,
		USEALL;
	}
	
	public void initSystem() {
		log.info("[ProtectorConquerorService] is initialized...");
		if (!CustomConfig.PROTECTOR_CONQUEROR_ENABLE) {
			return;
		} for (String world : CustomConfig.PROTECTOR_CONQUEROR_WORLDS.split(",")) {
			if ("".equals(world))
				break;
			int worldId = Integer.parseInt(world);
			int worldType = Integer.parseInt(String.valueOf(world.charAt(1)));
			protectorBuff = new ProtectorBuffs();
			conquerorBuff = new ConquerorBuffs();
			WorldType type = worldType > 0 ? worldType > 1 ? WorldType.ASMODIANS : WorldType.ELYOS : WorldType.USEALL;
			handledWorlds.put(worldId, type);
		} ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				for (Protector info : protectors.values()) {
					if (info.victims > 0 && !isEnemyWorld(info.getOwner())) {
						info.victims -= CustomConfig.PROTECTOR_CONQUEROR_DECREASE;
						int newRank = getRanks(info.victims);
						if (info.getRank() != newRank) {
							info.setRank(newRank);
							PacketSendUtility.sendPacket(info.getOwner(), new SM_CONQUEROR_PROTECTOR(true, info.getRank()));
						} if (info.victims < 1) {
							info.victims = 0;
							protectors.remove(info.getOwner().getObjectId());
						}
					}
				} for (Conqueror info : conquerors.values()) {
					if (info.victims > 0 && !isEnemyWorld(info.getOwner())) {
						info.victims -= CustomConfig.PROTECTOR_CONQUEROR_DECREASE;
						int newRank = getRanks(info.victims);
						if (info.getRank() != newRank) {
							info.setRank(newRank);
							PacketSendUtility.sendPacket(info.getOwner(), new SM_CONQUEROR_PROTECTOR(true, info.getRank()));
						} if (info.victims < 1) {
							info.victims = 0;
							conquerors.remove(info.getOwner().getObjectId());
						}
					}
				}
			}
		}, refresh * 60000, refresh * 60000);
	}
	
	public FastMap<Integer, Player> getWorldProtector(int worldId) {
		if (worldProtectors.containsKey(worldId)) {
			return worldProtectors.get(worldId);
		} else {
			FastMap<Integer, Player> protectors = new FastMap<Integer, Player>();
			worldProtectors.putEntry(worldId, protectors);
			return protectors;
		}
	}
	
	public FastMap<Integer, Player> getWorldConqueror(int worldId) {
		if (worldConqueror.containsKey(worldId)) {
			return worldConqueror.get(worldId);
		} else {
			FastMap<Integer, Player> killers = new FastMap<Integer, Player>();
			worldConqueror.putEntry(worldId, killers);
			return killers;
		}
	}
	
	public void onProtectorConquerorLogin(Player player) {
		if (!CustomConfig.PROTECTOR_CONQUEROR_ENABLE) {
			return;
		} if (protectors.containsKey(player.getObjectId())) {
			player.setProtectorInfo(protectors.get(player.getObjectId()));
			player.getProtectorInfo().refreshOwner(player);
		} if (conquerors.containsKey(player.getObjectId())) {
			player.setConquerorInfo(conquerors.get(player.getObjectId()));
			player.getConquerorInfo().refreshOwner(player);
		}
	}
	
	public void onLogout(Player player) {
		if (!CustomConfig.PROTECTOR_CONQUEROR_ENABLE) {
			return;
		}
		onLeaveMap(player);
	}
	
	public void onEnterMap(final Player player) {
		if (!CustomConfig.PROTECTOR_CONQUEROR_ENABLE) {
			return;
		}
		int worldId = player.getWorldId();
		Protector info = player.getProtectorInfo();
		Conqueror infoConqueror = player.getConquerorInfo();
		if (!isHandledWorld(worldId)) {
			return;
		} if (!isEnemyWorld(player)) { //Protector.
			int objId = player.getObjectId();
			info.setRank(1);
			if (info.getRank() >= 1) {
				//You are now a Protector.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_1LEVEL);
			} if (info.getRank() >= 2) {
				//You are now an Indomitable Protector.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_2LEVEL);
			} if (info.getRank() >= 3) {
				//You are now a Valiant Protector.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_3LEVEL);
			}
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(false, info.getRank()));
			final FastMap<Integer, Player> world = getWorldProtector(worldId);
			if (!world.containsKey(objId)) {
				world.putEntry(objId, player);
			}
			protectorBuff.applyRankEffect(player, info.getRank());
			World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(player.getInstanceId()).doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player victim) {
					if (!player.getRace().equals(victim.getRace())) {
						PacketSendUtility.sendPacket(victim, new SM_CONQUEROR_PROTECTOR(world.values()));
					}
				}
			});
		} else if (isEnemyWorld(player)) { //Conqueror.
			int objId = player.getObjectId();
			infoConqueror.setRank(1);
			if (infoConqueror.getRank() >= 1) {
				//You are now a Conqueror.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_1LEVEL);
			} if (infoConqueror.getRank() >= 2) {
				//You are now an Furious Conqueror.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_2LEVEL);
			} if (infoConqueror.getRank() >= 3) {
				//You are now a Berserk Conqueror.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_3LEVEL);
			}
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(false, infoConqueror.getRank()));
			final FastMap<Integer, Player> world = getWorldConqueror(worldId);
			if (!world.containsKey(objId)) {
				world.putEntry(objId, player);
			}
			conquerorBuff.applyEffect(player, infoConqueror.getRank());
			World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(player.getInstanceId()).doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player victim) {
					if (!player.getRace().equals(victim.getRace())) {
						PacketSendUtility.sendPacket(victim, new SM_CONQUEROR_PROTECTOR(world.values()));
					}
				}
			});
		} else {
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(getWorldProtector(worldId).values()));
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(getWorldConqueror(worldId).values()));
		}
		player.clearKnownlist();
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_PLAYER_INFO(player, false));
		player.updateKnownlist();
	}
	
	public void onLeaveMap(Player player) {
		int worldId = player.getWorldId();
		if (!isHandledWorld(worldId)) {
			return;
		} if (!isEnemyWorld(player)) { //Protector.
			Protector info = player.getProtectorInfo();
			FastList<Player> kill = new FastList<Player>();
			FastMap<Integer, Player> guards = getWorldProtector(worldId);
			kill.addAll(guards.values());
			guards.remove(player.getObjectId());
			if (info.getRank() > 0) {
				info.setRank(0);
				protectorBuff.endEffect(player);
				for (Player victim: World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(player.getInstanceId()).getPlayersInside()) {
					if (!player.getRace().equals(victim.getRace())) {
						PacketSendUtility.sendPacket(victim, new SM_CONQUEROR_PROTECTOR(kill));
					}
				}
			}
		} else if (isEnemyWorld(player)) { //Conqueror.
			Conqueror info = player.getConquerorInfo();
			FastList<Player> kill = new FastList<Player>();
			FastMap<Integer, Player> killers = getWorldConqueror(worldId);
			kill.addAll(killers.values());
			killers.remove(player.getObjectId());
			if (info.getRank() > 0) {
				info.setRank(0);
				conquerorBuff.endEffect(player);
				for (Player victim : World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(player.getInstanceId()).getPlayersInside()) {
					if (!player.getRace().equals(victim.getRace())) {
						PacketSendUtility.sendPacket(victim, new SM_CONQUEROR_PROTECTOR(kill));
					}
				}
			}
		}
	}
	
	public void updateIcons(Player player) {
		if (!isEnemyWorld(player)) {
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(getWorldProtector(player.getWorldId()).values()));
		} else if (isEnemyWorld(player)) {
			PacketSendUtility.sendPacket(player, new SM_CONQUEROR_PROTECTOR(getWorldConqueror(player.getWorldId()).values()));
		}
	}
	
	public void updateRanks(final Player killer, Player victim) {
		if (!isEnemyWorld(killer)) { //Protector.
			Protector info = killer.getProtectorInfo();
			if (killer.getLevel() >= victim.getLevel() + levelDiff) {
				int rank = getRanks(++info.victims);
				if (info.getRank() >= 1) {
				    //You are now a Protector.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_1LEVEL);
				} if (info.getRank() >= 2) {
				    //You are now an Indomitable Protector.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_2LEVEL);
				} if (info.getRank() >= 3) {
				    //You are now a Valiant Protector.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_GUARD_UP_3LEVEL);
				} if (info.getRank() != rank) {
					info.setRank(rank);
					protectorBuff.applyRankEffect(killer, rank);
					final FastMap<Integer, Player> guards = getWorldProtector(killer.getWorldId());
					PacketSendUtility.sendPacket(killer, new SM_CONQUEROR_PROTECTOR(true, info.getRank()));
					World.getInstance().getWorldMap(killer.getWorldId()).getWorldMapInstanceById(killer.getInstanceId()).doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player observed) {
							if (!killer.getRace().equals(observed.getRace())) {
								PacketSendUtility.sendPacket(observed, new SM_CONQUEROR_PROTECTOR(guards.values()));
							}
						}
					});
				} if (!protectors.containsKey(killer.getObjectId())) {
					protectors.put(killer.getObjectId(), info);
				}
			}
		} else if (isEnemyWorld(killer)) { //Conqueror.
			Conqueror info = killer.getConquerorInfo();
			if (killer.getLevel() >= victim.getLevel() + levelDiff) {
				int rank = getRanks(++info.victims);
				if (info.getRank() >= 1) {
				    //You are now a Conqueror.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_1LEVEL);
				} if (info.getRank() >= 2) {
				    //You are now an Furious Conqueror.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_2LEVEL);
				} if (info.getRank() >= 3) {
				    //You are now a Berserk Conqueror.
				    PacketSendUtility.sendPacket(killer, SM_SYSTEM_MESSAGE.STR_MSG_SLAYER_UP_3LEVEL);
				} if (info.getRank() != rank) {
					info.setRank(rank);
					conquerorBuff.applyEffect(killer, rank);
					final FastMap<Integer, Player> killers = getWorldConqueror(killer.getWorldId());
					PacketSendUtility.sendPacket(killer, new SM_CONQUEROR_PROTECTOR(true, info.getRank()));
					World.getInstance().getWorldMap(killer.getWorldId()).getWorldMapInstanceById(killer.getInstanceId()).doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player observed) {
							if (!killer.getRace().equals(observed.getRace())) {
								PacketSendUtility.sendPacket(observed, new SM_CONQUEROR_PROTECTOR(killers.values()));
							}
						}
					});
				} if (!conquerors.containsKey(killer.getObjectId())) {
					conquerors.put(killer.getObjectId(), info);
				}
			}
		}
	}
	
	private int getRanks(int kills) {
		return kills > CustomConfig.PROTECTOR_CONQUEROR_2ND_RANK_KILLS ? 2 : kills > CustomConfig.PROTECTOR_CONQUEROR_1ST_RANK_KILLS ? 1 : 0;
	}
	
	public void onKillProtectorConqueror(final Player killer, final Player victim) {
		if (!isEnemyWorld(victim)) {
			final Protector info = victim.getProtectorInfo();
			victim.getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					if (killer.getRace().equals(player.getRace()) && MathUtil.isIn3dRange(victim, player, 30)) {
					    SkillEngine.getInstance().applyEffectDirectly(buffId(killer, info), player, player, 0);
					}
				}
			});
		} else if (isEnemyWorld(victim)) {
			final Conqueror conqueror = victim.getConquerorInfo();
			victim.getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					if (killer.getRace().equals(player.getRace()) && MathUtil.isIn3dRange(victim, player, 30)) {
						SkillEngine.getInstance().applyEffectDirectly(buffId(killer, conqueror), player, player, 0);
					}
				}
			});
		}
	}
	
	public boolean isHandledWorld(int worldId) {
		return handledWorlds.containsKey(worldId);
	}
	
	public boolean isEnemyWorld(Player player) {
		if (handledWorlds.containsKey(player.getWorldId())) {
			WorldType homeType = player.getRace().equals(Race.ASMODIANS) ? WorldType.ASMODIANS : WorldType.ELYOS;
			return !handledWorlds.get(player.getWorldId()).equals(homeType);
		}
		return false;
	}
	
	private int buffId(Player player, Protector info) {
		if (info.getRank() > 0) {
			return player.getRace() == Race.ELYOS ? 8610 : 8611;
		}
		return 0;
	}
	
	private int buffId(Player player, Conqueror info) {
		if (info.getRank() > 0) {
			return player.getRace() == Race.ELYOS ? 8610 : 8611;
		}
		return 0;
	}
	
	public static ProtectorConquerorService getInstance() {
		return ProtectorConquerorService.SingletonHolder.instance;
	}
	
	private static class SingletonHolder {
		protected static final ProtectorConquerorService instance = new ProtectorConquerorService();
	}
}