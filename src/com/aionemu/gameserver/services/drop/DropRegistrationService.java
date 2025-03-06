/**
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
package com.aionemu.gameserver.services.drop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.GlobalDropData;
import com.aionemu.gameserver.dataholders.NpcDropData;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropGroup;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.drop.NpcDrop;
import com.aionemu.gameserver.model.gameobjects.DropNpc;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.templates.event.EventDrop;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropItem;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropMap;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropRace;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropRating;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropTribe;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropWorld;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalDropZone;
import com.aionemu.gameserver.model.templates.globaldrops.GlobalRule;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.npc.AbyssNpcType;
import com.aionemu.gameserver.model.templates.npc.NpcRating;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOT_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.DropRewardEnum;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastList;
import javolution.util.FastMap;

public class DropRegistrationService {

	private Map<Integer, Set<DropItem>> currentDropMap = new FastMap<Integer, Set<DropItem>>().shared();
	private Map<Integer, DropNpc> dropRegistrationMap = new FastMap<Integer, DropNpc>().shared();
	private FastList<Integer> noReductionMaps;

	Logger log = LoggerFactory.getLogger(DropRegistrationService.class);

	public void registerDrop(Npc npc, Player player, Collection<Player> groupMembers) {
		registerDrop(npc, player, player.getLevel(), groupMembers);
	}

	private DropRegistrationService() {
		init();
		noReductionMaps = new FastList<Integer>();
		for (String zone : DropConfig.DISABLE_DROP_REDUCTION_IN_ZONES.split(",")) {
			noReductionMaps.add(Integer.parseInt(zone));
		}
	}

   public final void init() {
        NpcDropData npcDrop = DataManager.NPC_DROP_DATA;
        for (NpcDrop drop : npcDrop.getNpcDrop()) {
            NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(drop.getNpcId());
            if (npcTemplate == null) {
                continue;
            }
            if (npcTemplate.getNpcDrop() != null) {
                NpcDrop currentDrop = npcTemplate.getNpcDrop();
                for (DropGroup dg : currentDrop.getDropGroup()) {
                    List<Drop> list = new ArrayList<Drop>();
                    for (Drop d : dg.getDrop()) {
                        for (DropGroup dg2 : drop.getDropGroup()) {
                            for (Drop d2 : dg2.getDrop()) {
                                if (d.getItemId() == d2.getItemId()) {
                                    list.add(d);
                                    break;
                                }
                            }
                        }
                    }
                    dg.getDrop().remove(list);
                }
                List<DropGroup> list = new ArrayList<DropGroup>();
                for (DropGroup dg : drop.getDropGroup()) {
                    boolean added = false;
                    for (DropGroup dg2 : currentDrop.getDropGroup()) {
                        if (dg2.getGroupName().equals(dg.getGroupName())) {
                            dg2.getDrop().addAll(dg.getDrop());
                            added = true;
                        }
                    }
                    if (!added) {
                        list.add(dg);
                    }
                }
                if (!list.isEmpty()) {
                    currentDrop.getDropGroup().addAll(list);
                }
            } else {
                npcTemplate.setNpcDrop(drop);
            }
        }
    }

	/**
	 * After NPC dies, it can register arbitrary drop
	 */
	public void registerDrop(Npc npc, Player player, int heighestLevel, Collection<Player> groupMembers) {
		boolean stepCheck = false;
		if (player == null) {
			return;
		}
		int npcObjId = npc.getObjectId();

		// Getting all possible drops for this Npc
		NpcDrop npcDrop = npc.getNpcDrop();
		Set<DropItem> droppedItems = new HashSet<DropItem>();
		int index = 1;
		int dropChance = 100;
		int npcLevel = npc.getLevel();
		boolean isChest = npc.getAi2().getName().equals("chest");
		if (!DropConfig.DISABLE_DROP_REDUCTION && ((isChest && npcLevel != 1 || !isChest))
				&& !noReductionMaps.contains(npc.getWorldId())) {
			dropChance = DropRewardEnum.dropRewardFrom(npcLevel - heighestLevel); // reduce chance depending on level
		}

		Player genesis = player;
		Integer winnerObj = 0;

		// Distributing drops to players
		Collection<Player> dropPlayers = new ArrayList<Player>();
		Collection<Player> winningPlayers = new ArrayList<Player>();
		if (player.isInGroup2() || player.isInAlliance2()) {
			List<Integer> dropMembers = new ArrayList<Integer>();
			LootGroupRules lootGrouRules = player.getLootGroupRules();

			switch (lootGrouRules.getLootRule()) {
			case ROUNDROBIN:
				int size = groupMembers.size();
				if (size > lootGrouRules.getNrRoundRobin()) {
					lootGrouRules.setNrRoundRobin(lootGrouRules.getNrRoundRobin() + 1);
				} else {
					lootGrouRules.setNrRoundRobin(1);
				}
				int i = 0;
				for (Player p : groupMembers) {
					i++;
					if (i == lootGrouRules.getNrRoundRobin()) {
						winningPlayers.add(p);
						winnerObj = p.getObjectId();
						setItemsToWinner(droppedItems, winnerObj);
						genesis = p;
						break;
					}
				}
				break;
			case FREEFORALL:
				winningPlayers = groupMembers;
				break;
			case LEADER:
				Player leader = player.isInGroup2() ? player.getPlayerGroup2().getLeaderObject()
						: player.getPlayerAlliance2().getLeaderObject();
				winningPlayers.add(leader);
				winnerObj = leader.getObjectId();
				setItemsToWinner(droppedItems, winnerObj);
				genesis = leader;
				break;
			}

			for (Player member : winningPlayers) {
				dropMembers.add(member.getObjectId());
				dropPlayers.add(member);
			}
			DropNpc dropNpc = new DropNpc(npcObjId);
			dropRegistrationMap.put(npcObjId, dropNpc);
			dropNpc.setPlayersObjectId(dropMembers);
			dropNpc.setInRangePlayers(groupMembers);
			dropNpc.setGroupSize(groupMembers.size());
		} else {
			List<Integer> singlePlayer = new ArrayList<Integer>();
			singlePlayer.add(player.getObjectId());
			dropPlayers.add(player);
			dropRegistrationMap.put(npcObjId, new DropNpc(npcObjId));
			dropRegistrationMap.get(npcObjId).setPlayersObjectId(singlePlayer);
		}
		float boostDropRate = npc.getGameStats().getStat(StatEnum.BOOST_DROP_RATE, 100).getCurrent() / 100f;
		boostDropRate += genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f;
		boostDropRate += genesis.getCommonData().getCurrentReposteEnergy() > 0 ? 0.05f : 0;
		boostDropRate += genesis.getCommonData().getCurrentSalvationPercent() > 0 ? 0.05f : 0;
		boostDropRate += genesis.getActiveHouse() != null
				? genesis.getActiveHouse().getHouseType().equals(HouseType.PALACE) ? 0.05f : 0
				: 0;
		boostDropRate += genesis.getGameStats().getStat(StatEnum.BOOST_DROP_RATE, 100).getCurrent() / 100f - 1;
		boostDropRate += genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f - 1;
		// ?????????? / Comments for drop rate calculation
		// ??NPC???? / Add NPC rating modification
		float ratingModifier = getRatingModifier(npc);
		// ??????? = ????? * ???? * ????? * ???? / 100
		// Final drop rate = base drop rate * boost rate * level difference modifier * rating modifier / 100
		float dropRate = genesis.getRates().getDropRate() * boostDropRate * dropChance * ratingModifier / 100f;
		
		if (npcDrop != null) {
			index = npcDrop.dropCalculator(droppedItems, index, dropRate, genesis.getRace(), groupMembers);
		}
		currentDropMap.put(npcObjId, droppedItems);
		index = QuestService.getQuestDrop(droppedItems, index, npc, groupMembers, genesis);
		if (EventsConfig.ENABLE_EVENT_SERVICE) {
			List<EventTemplate> activeEvents = EventService.getInstance().getActiveEvents();
			for (EventTemplate eventTemplate : activeEvents) {
				if (eventTemplate.EventDrop() == null) {
					continue;
				}
				List<EventDrop> eventDrops = eventTemplate.EventDrop().getEventDrops();
				for (EventDrop eventDrop : eventDrops) {
					int diff = npc.getLevel() - eventDrop.getItemTemplate().getLevel();
					int minDiff = eventDrop.getMinDiff();
					int maxDiff = eventDrop.getMaxDiff();
					if (minDiff != 0) {
						if (diff < eventDrop.getMinDiff()) {
							continue;
						}
					}
					if (maxDiff != 0) {
						if (diff > eventDrop.getMaxDiff()) {
							continue;
						}
					}
					float percent = eventDrop.getChance();
					percent *= dropRate;
					if (Rnd.get() * 100 > percent) {
						continue;
					}
					droppedItems.add(
							regDropItem(index++, winnerObj, npcObjId, eventDrop.getItemId(), eventDrop.getCount()));
				}
			}
		}
		if (DropConfig.ENABLE_GLOBAL_DROPS) {
			boolean isNpcChest = npc.getAi2().getName().equals("chest");
			if ((!isNpcChest && npc.getLevel() > 1) || isNpcChest) {
				GlobalDropData globalDrops = DataManager.GLOBAL_DROP_DATA;
				List<GlobalRule> globalrules = globalDrops.getAllRules();
				for (GlobalRule rule : globalrules) {
					if (rule.getGlobalRuleItems() == null) {
						continue;
					}
					boostDropRate += genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f;
					boostDropRate += genesis.getCommonData().getCurrentReposteEnergy() > 0 ? 0.05f : 0;
					boostDropRate += genesis.getCommonData().getCurrentSalvationPercent() > 0 ? 0.05f : 0;
					boostDropRate += genesis.getActiveHouse() != null
							? genesis.getActiveHouse().getHouseType().equals(HouseType.PALACE) ? 0.05f : 0
							: 0;
					boostDropRate += genesis.getGameStats().getStat(StatEnum.BOOST_DROP_RATE, 100).getCurrent() / 100f
							- 1;
					boostDropRate += genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f - 1;
					float gDropRate = genesis.getRates().getGlobalDropRate() * boostDropRate * dropChance / 100f;
					float percent = rule.getChance() * gDropRate;
					if (Rnd.get() * 100 > percent) {
						continue;
					}
					if (!DropConfig.DISABLE_DROP_REDUCTION && ((isChest && npc.getLevel() != 1 || !isChest))
							&& !noReductionMaps.contains(npc.getWorldId())) {
						if ((player.getLevel() - npc.getLevel()) >= 1 && !rule.getNoReduction()) {
							continue;
						}
					}
					if (rule.getRestrictionRace() != null) {
						if (player.getRace() == Race.ASMODIANS && rule.getRestrictionRace().equals("ELYOS")) {
							continue;
						}
						if (player.getRace() == Race.ELYOS && rule.getRestrictionRace().equals("ASMODIANS")) {
							continue;
						}
					}
					if (rule.getGlobalRuleMaps() != null) {
						stepCheck = false;
						for (GlobalDropMap gdMap : rule.getGlobalRuleMaps().getGlobalDropMaps()) {
							if (gdMap.getMapId() == npc.getPosition().getMapId()) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					if (rule.getGlobalRuleWorlds() != null) {
						stepCheck = false;
						for (GlobalDropWorld gdWorld : rule.getGlobalRuleWorlds().getGlobalDropWorlds()) {
							if (gdWorld.getWorldDropType().equals(npc.getWorldDropType())) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					if (rule.getGlobalRuleRatings() != null) {
						stepCheck = false;
						for (GlobalDropRating gdRating : rule.getGlobalRuleRatings().getGlobalDropRatings()) {
							if (gdRating.getRating().equals(npc.getRating())) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					if (rule.getGlobalRuleRaces() != null) {
						stepCheck = false;
						for (GlobalDropRace gdRace : rule.getGlobalRuleRaces().getGlobalDropRaces()) {
							if (gdRace.getRace().equals(npc.getRace())) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					if (rule.getGlobalRuleTribes() != null) {
						stepCheck = false;
						for (GlobalDropTribe gdTribe : rule.getGlobalRuleTribes().getGlobalDropTribes()) {
							if (gdTribe.getTribe().equals(npc.getTribe())) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					if (rule.getGlobalRuleZones() != null) {
						stepCheck = false;
						for (GlobalDropZone gdZone : rule.getGlobalRuleZones().getGlobalDropZones()) {
							if (npc.isInsideZone(ZoneName.get(gdZone.getZone()))) {
								stepCheck = true;
								break;
							}
						}
						if (!stepCheck) {
							continue;
						}
					}
					List<Integer> alloweditems = new ArrayList<Integer>();
					for (GlobalDropItem globalItem : rule.getGlobalRuleItems().getGlobalDropItems()) {
						int diff = npc.getLevel() - globalItem.getItemTemplate().getLevel();
						if (diff >= rule.getMinDiff() && diff <= rule.getMaxDiff()) {
							alloweditems.add(globalItem.getId());
						}
					}
					if (alloweditems.size() == 0) {
						continue;
					}
					int rndItemId = alloweditems.size() > 1 ? alloweditems.get(Rnd.get(0, alloweditems.size() - 1))
							: alloweditems.get(0);
					long count = 1;
					if (rndItemId == 182400001) {
						count = rule.getMaxCount() > 1
								? Rnd.get((int) rule.getMinCount(), (int) rule.getMaxCount())
								: rule.getMinCount();

					} else {
						count = rule.getMaxCount() > 1 ? Rnd.get((int) rule.getMinCount(), (int) rule.getMaxCount())
								: rule.getMinCount();
					}
					droppedItems.add(regDropItem(index++, winnerObj, npcObjId, rndItemId, count));
				}
			}
		}
		if (npc.getPosition().isInstanceMap()) {
			npc.getPosition().getWorldMapInstance().getInstanceHandler().onDropRegistered(npc);
		}
		npc.getAi2().onGeneralEvent(AIEventType.DROP_REGISTERED);
		for (Player p : dropPlayers) {
			PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npcObjId, 0));
		}
		if (player.getPet() != null && player.getPet().getPetTemplate().getPetFunction(PetFunctionType.LOOT) != null
				&& player.getPet().getCommonData().isLooting()) {
			PacketSendUtility.sendPacket(player, new SM_PET(true, npcObjId));
			Set<DropItem> drops = getCurrentDropMap().get(npcObjId);
			if (drops == null || drops.size() == 0) {
				npc.getController().onDelete();
			} else {
				DropItem[] dropItems = drops.toArray(new DropItem[0]);
				for (int i = 0; i < dropItems.length; i++) {
					DropService.getInstance().requestDropItem(player, npcObjId, dropItems[i].getIndex(), true);
				}
			}
			PacketSendUtility.sendPacket(player, new SM_PET(false, npcObjId));
			if (drops == null || drops.size() == 0) {
				return;
			}
		}
		if (player.getMinion() != null && player.getMinion().getCommonData().isLooting()) {
			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, 1, npcObjId, true));
			Set<DropItem> drops = getCurrentDropMap().get(npcObjId);
			if (drops == null || drops.size() == 0) {
				npc.getController().onDelete();
			} else {
				DropItem[] dropItems = drops.toArray(new DropItem[drops.size()]);
				for (int i = 0; i < dropItems.length; i++) {
					DropService.getInstance().requestDropItem(player, npcObjId, dropItems[i].getIndex(), true);
				}
			}
			PacketSendUtility.sendPacket(player, new SM_MINIONS(8, 1, npcObjId, true));
			if (drops == null || drops.size() == 0) {
				return;
			}
		}
		DropService.getInstance().scheduleFreeForAll(npcObjId);
	}

	public void setItemsToWinner(Set<DropItem> droppedItems, Integer obj) {
		for (DropItem dropItem : droppedItems) {
			if (!dropItem.getDropTemplate().isEachMember()) {
				dropItem.setPlayerObjId(obj);
			}
		}
	}

	public DropItem regDropItem(int index, int playerObjId, int objId, int itemId, long count) {
		DropItem item = new DropItem(new Drop(itemId, 1, 1, 100, false, false));
		item.setPlayerObjId(playerObjId);
		item.setNpcObj(objId);
		item.setCount(count);
		item.setIndex(index);
		return item;
	}

	private float getRatingModifier(Npc npc) {
		float ratingModifier = 1f; // ?????? / Default modifier
		
		switch (npc.getRating()) { // ??NPC???????????? / Set modifier based on NPC rating
			case NORMAL:
				ratingModifier = 1f;   // ???????? / Normal monster modifier
				break;
			case ELITE:
				ratingModifier = 3f;   // ???????? / Elite monster modifier
				break;
			case HERO:
				ratingModifier = 5f;   // ???????? / Hero monster modifier
				break;
			case LEGENDARY:
				ratingModifier = 7f;   // ???????? / Legendary monster modifier
				break;
			default:
				ratingModifier = 1f;   // ????????? / Use default value for unknown rating
				break;
		}
		return ratingModifier;
	}

	/**
	 * @return dropRegistrationMap
	 */
	public Map<Integer, DropNpc> getDropRegistrationMap() {
		return dropRegistrationMap;
	}

	/**
	 * @return currentDropMap
	 */
	public Map<Integer, Set<DropItem>> getCurrentDropMap() {
		return currentDropMap;
	}

	public static DropRegistrationService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final DropRegistrationService instance = new DropRegistrationService();
	}
}