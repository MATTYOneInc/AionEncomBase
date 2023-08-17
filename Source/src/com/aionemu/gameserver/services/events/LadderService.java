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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.LadderDAO;
import com.aionemu.gameserver.eventEngine.Event;
import com.aionemu.gameserver.eventEngine.events.BattlegroundEvent;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.LegionEmblem;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.services.events.bg.*;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Rinzler (Encom)
 */
public class LadderService
{
    private static Logger log = LoggerFactory.getLogger(LadderService.class);
    private List<AionObject> eventQueueList = new ArrayList<AionObject>();
    private List<AionObject> normalQueueList = new ArrayList<AionObject>();
    private List<AionObject> soloQueueList = new ArrayList<AionObject>();
    private Map<Integer, Battleground> bgMap = Collections.synchronizedMap(new FastMap<Integer, Battleground>());
    private Map<Integer, Event> normalBgMap = Collections.synchronizedMap(new FastMap<Integer, Event>());
    private Battleground eventBg = null;
    private ScheduledFuture<?> eventTask = null;
    private ScheduledFuture<?> normalTask = null;
    boolean normalReady = false;
    boolean eventReady = false;
    boolean normalTeamBased = false;
    boolean eventTeamBased = false;
    private int rankUpdateInterval = 2;

    private LadderService() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                UpdateRanks();
            }
        }, rankUpdateInterval * 60 * 1000, rankUpdateInterval * 60 * 1000);
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                HandleSoloQueue();
            }
        }, 60 * 1000, 60 * 1000);
        log.info("[LadderService] is initialized...");
    }

    public void UpdateRanks() {
        getLadderDAO().updateRanks();
    }

    public boolean registerForNormal(Player player) {
        if (!isNormalReady() || player.getBattleground() != null) {
            return false;
		} if (player.isInGroup2() && isInQueue(player.getPlayerGroup2())) {
            return false;
		} if (isInQueue(player)) {
            unregisterFromQueue(player);
		}
        PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(1, 301550000, 0));
        PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(301550000, false));
        return normalQueueList.add(player);
    }

    public boolean registerForEvent(Player player) {
        if (!isEventReady() || player.getBattleground() != null) {
            return false;
		} if (player.isInGroup2() && isInQueue(player.getPlayerGroup2())) {
            return false;
		} if (isInQueue(player)) {
            unregisterFromQueue(player);
		}
        PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(1, 300350000, 0));
        PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(300350000, false));
        return eventQueueList.add(player);
    }

    public boolean registerForSolo(Player player) {
        if (player.isInGroup2() && isInQueue(player.getPlayerGroup2())) {
            return false;
		} if (isInQueue(player)) {
            unregisterFromQueue(player);
		}
        PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(1, 320150000, 0));
        return soloQueueList.add(player);
    }

    public void unregisterForNormal(Player player) {
        normalQueueList.remove(player);
    }

    public void unregisterForEvent(Player player) {
        eventQueueList.remove(player);
    }

    public void unregisterForSolo(Player player) {
        soloQueueList.remove(player);
    }

    public boolean isInQueue(Player player) {
        if (normalQueueList.contains(player) || eventQueueList.contains(player) || soloQueueList.contains(player)) {
            return true;
		}
        return false;
    }

    public void unregisterFromQueue(Player player) {
        if (normalQueueList.contains(player)) {
            unregisterForNormal(player);
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(2, 301550000, 0));
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(301550000, true));
        } if (eventQueueList.contains(player)) {
            unregisterForEvent(player);
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(2, 300350000, 0));
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(300350000, true));
        } if (soloQueueList.contains(player)) {
            unregisterForSolo(player);
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(2, 320150000, 0));
        }
    }

    public boolean registerForNormal(PlayerGroup group) {
        if (!isNormalReady()) {
            return false;
		} for (Player pl : group.getMembers()) {
            if (pl.getBattleground() != null) {
                return false;
			} else if (isInQueue(pl)) {
                unregisterFromQueue(pl);
			}
        } if (isInQueue(group)) {
            unregisterFromQueue(group);
		} for (Player pl: group.getMembers()) {
            PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(1, 301550000, 2));
            PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(301550000, false));
        }
        return normalQueueList.add(group);
    }

    public boolean registerForEvent(PlayerGroup group) {
        if (!isEventReady()) {
            return false;
		} for (Player pl: group.getMembers()) {
            if (pl.getBattleground() != null) {
                return false;
			}
		} if (isInQueue(group)) {
            unregisterFromQueue(group);
		} for (Player pl: group.getMembers()) {
            PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(1, 300350000, 2));
            PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(300350000, false));
        }
        return eventQueueList.add(group);
    }

    public void unregisterForNormal(PlayerGroup group) {
        normalQueueList.remove(group);
    }

    public void unregisterForEvent(PlayerGroup group) {
        eventQueueList.remove(group);
    }

    public boolean isInQueue(PlayerGroup group) {
        if (normalQueueList.contains(group) || eventQueueList.contains(group)) {
            return true;
		}
        return false;
    }

    public void unregisterFromQueue(PlayerGroup group) {
        if (normalQueueList.contains(group)) {
            unregisterForNormal(group);
            for (Player pl: group.getMembers()) {
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 301550000, 0));
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(301550000, true));
            }
        } if (eventQueueList.contains(group)) {
            unregisterForEvent(group);
            for (Player pl: group.getMembers()) {
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 301550000, 0));
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(301550000, true));
            }
        }
    }

    public int getEventQueueSize() {
        return eventQueueList.size();
    }

    public int getEventQueuePlayers() {
        int players = 0;
        for (AionObject ao : eventQueueList) {
            if (ao instanceof Player) {
                players++;
			} else if (ao instanceof PlayerGroup) {
                players += ((PlayerGroup) ao).getMembers().size();
			}
        }
        return players;
    }

    private Battleground getRandomBg(boolean teamOnly) {
        Battleground bg = null;
        Class<?>[] bgs = new Class<?>[] { TwoTeamBg.class, TwoTeamSmallBg.class};
        if (!teamOnly) {
            bgs = new Class<?>[] {SoloSurvivorBg.class, SoloSurvivorBg.class, SoloSurvivorBg.class, SoloSurvivorBg.class, DeathmatchBg.class, TwoTeamSmallBg.class};
		} try {
            bg = (Battleground) bgs[Rnd.get(bgs.length)].newInstance();
        } catch (Exception e) {
            //log.error("getRandomBg() failed!", e);
        }
        return bg;
    }

    public boolean createNormalBgs(final BattlegroundEvent event) {
        if (normalTask != null) {
            normalTask.cancel(false);
		}
        normalReady = true;
        normalTeamBased = Rnd.get(3) != 0;
        normalQueueList.clear();
        announceAll("[BG Open] Register with the button located on the right of your skill bar. You have <2 Minutes> to register!!!");
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player pl) {
                if (pl.getBattleground() == null && !isInQueue(pl) && !FFAService.getInstance().isInArena(pl)) {
                    PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(301550000, true));
                }
            }
        });
        normalTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                HandleNormalQueue(event);
                normalReady = false;
                normalTask = null;
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(301550000, false));
                    }
                });
            }
        }, (normalTeamBased ? 60 : 30) * 1000);
        return true;
    }

    public boolean createEventBg(Battleground bg, boolean teamBased) {
        if (eventBg != null) {
            return false;
		} if (eventTask != null) {
            eventTask.cancel(true);
		}
        eventReady = true;
        eventBg = bg;
        eventTeamBased = teamBased;
        eventQueueList.clear();
        if (eventTeamBased) {
            announceAll("WARNING!!! " + bg.getName() + "The event start in 1 minute!!! Register you by using the right button on your skill bars!!!");
		} else {
            announceAll("WARNING!!! " + bg.getName() + "The event start in 30 seconds ! Register you by using the right button on your skill bars!!!");
		}
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player pl) {
                if (pl.getBattleground() == null && !isInQueue(pl)) {
                    PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(300350000, true));
                }
            }
        });
        eventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(300350000, false));
                    }
                });
                HandleEventQueue();
                eventTask.cancel(false);
                eventTask = null;
            }
        }, (eventTeamBased ? 60 : 30) * 1000);
        return true;
    }

    private void HandleNormalQueue(BattlegroundEvent event) {
        List<List<Player>> validGroups = new ArrayList<List<Player>>();
        List<Integer> validParticipants = new ArrayList<Integer>();
        for (AionObject ao: normalQueueList) {
            if (ao == null) {
                continue;
			} if (ao instanceof Player) {
                Player pl = (Player) ao;
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 301550000, 0));
                if (!pl.isOnline() || pl.getBattleground() != null) {
                    continue;
				}
                validGroups.add(Arrays.asList(pl));
                validParticipants.add(pl.getObjectId());
            } else if (ao instanceof PlayerGroup) {
                final PlayerGroup group = (PlayerGroup) ao;
                boolean add = true;
                for (Player pl : group.getMembers()) {
                    PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 301550000, 0));
                    if (!pl.isOnline() || pl.getBattleground() != null) {
                        add = false;
					}
                } if (add && normalTeamBased) {
                    validGroups.add(new ArrayList<Player>() {
						{
                            addAll(group.getMembers());
                        }
                    });
                }
            }
        } if (normalTeamBased) {
            Collections.shuffle(validGroups);
            Collections.sort(validGroups, new Comparator<List<Player>>() {
                @Override
                public int compare(List<Player> o1, List<Player> o2) {
                    return -Integer.valueOf(o1.size()).compareTo(Integer.valueOf(o2.size()));
                }
            });
        } else {
            Collections.shuffle(validParticipants);
            SortParticipantList(validParticipants);
        }
        normalQueueList.clear();
        int iterations = 0;
        while (iterations++ < 40) {
            Battleground bg = getRandomBg(normalTeamBased);
            bg.setTeamBased(normalTeamBased);
            if (normalTeamBased) {
                List<List<Player>> usedGroups = new ArrayList<List<Player>>();
                List<List<Player>> groups = new ArrayList<List<Player>>();
                List<Integer> participants = new ArrayList<Integer>();
                if (validGroups.size() < bg.getTeamCount()) {
                    continue;
				} for (int i = 0; i < bg.getTeamCount(); i++) {
                    groups.add(new ArrayList<Player>());
                    for (List<Player> group: validGroups) {
                        if (!usedGroups.contains(group) && group.size() <= bg.getMaxSize()) {
                            groups.set(i, cloneGroup(group));
                            usedGroups.add(group);
                            break;
                        }
                    }
                } if (groups.get(0).size() < bg.getMinSize()) {
                    for (List<Player> group: validGroups) {
                        if (!usedGroups.contains(group) && group.size() + groups.get(0).size() <= bg.getMaxSize()) {
                            groups.get(0).addAll(group);
                            usedGroups.add(group);
                            if (groups.get(0).size() >= bg.getMinSize()) {
                                break;
							}
                        }
                    }
                } if (groups.get(0).size() < bg.getMinSize() || groups.get(0).size() > bg.getMaxSize()) {
                    continue;
				}
                boolean satisfied = true;
                for (int i = 1; i < bg.getTeamCount(); i++) {
                    if (groups.get(i).size() < groups.get(0).size()) {
                        for (List<Player> group: validGroups) {
                            if (!usedGroups.contains(group) && group.size() + groups.get(i).size() <= groups.get(0).size()) {
                                groups.get(i).addAll(group);
                                usedGroups.add(group);
                            }
                        }
                    } if (groups.get(i).size() < groups.get(0).size()) {
                        satisfied = false;
					}
                } if (!satisfied) {
                    continue;
				} for (List<Player> group : groups) {
                    for (Player pl : group) {
                        participants.add(pl.getObjectId());
					}
				}
                validGroups.removeAll(usedGroups);
                bg.createMatch(participants);
            } else {
                bg.createMatch(validParticipants);
            } if (bg.hasPlayers()) {
                Integer bgId = registerBg(bg);
                normalBgMap.put(bgId, event);
                event.onCreate(bgId);
            }
        } if (event.getBgCount() == 0) {
            event.onEnd();
		} if (normalTeamBased) {
            for (List<Player> group: validGroups) {
                for (Player pl: group) {
                    scheduleAnnouncement(pl, "No opponents found!!! Please wait for the next registration.", 0);
                }
            }
        } else {
            for (Integer objectId : validParticipants) {
                AionObject ao = World.getInstance().findVisibleObject(objectId);
                if (ao != null && ao instanceof Player) {
                    scheduleAnnouncement((Player) ao, "No opponents found!!! Please wait for the next registration.", 0);
                }
            }
        }
    }

    private void HandleEventQueue() {
        List<List<Player>> validGroups = new ArrayList<List<Player>>();
        List<Integer> validParticipants = new ArrayList<Integer>();
        for (AionObject ao : eventQueueList) {
            if (ao != null && ao instanceof Player) {
                Player pl = (Player) ao;
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 300350000, 0));
                if (!pl.isOnline() || pl.getBattleground() != null) {
                    continue;
				}
                validGroups.add(Arrays.asList(pl));
                validParticipants.add(ao.getObjectId());
            } else if (ao instanceof PlayerGroup) {
                final PlayerGroup group = (PlayerGroup) ao;
                boolean add = true;
                for (Player pl : group.getMembers()) {
                    PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 300350000, 0));
                    if (!pl.isOnline() || pl.getBattleground() != null) {
                        add = false;
					}
                } if (add && eventTeamBased) {
                    validGroups.add(new ArrayList<Player>() {
                        {
                            addAll(group.getMembers());
                        }
                    });
                }
            }
        } if (eventTeamBased) {
            Collections.shuffle(validGroups);
            Collections.sort(validGroups, new Comparator<List<Player>>() {
                @Override
                public int compare(List<Player> o1, List<Player> o2) {
                    return -Integer.valueOf(o1.size()).compareTo(Integer.valueOf(o2.size()));
                }
            });
        } else {
            Collections.shuffle(validParticipants);
            SortParticipantList(validParticipants);
        }
        Class<?> eventBgClass = eventBg.getClass();
        eventQueueList.clear();
        int iterations = 0;
        while (iterations++ < 40) {
            Battleground bg;
            try {
                bg = (Battleground) eventBgClass.newInstance();
            }
            catch (Exception e) {
                continue;
            }
            bg.setTeamBased(eventTeamBased);
            bg.setIsEvent(true);
            if (eventTeamBased) {
                List<List<Player>> usedGroups = new ArrayList<List<Player>>();
                List<List<Player>> groups = new ArrayList<List<Player>>();
                List<Integer> participants = new ArrayList<Integer>();
                if (validGroups.size() < bg.getTeamCount()) {
                    continue;
				} for (int i = 0; i < bg.getTeamCount(); i++) {
                    groups.add(new ArrayList<Player>());
                    for (List<Player> group: validGroups) {
                        if (!usedGroups.contains(group) && group.size() <= bg.getMaxSize()) {
                            groups.set(i, cloneGroup(group));
                            usedGroups.add(group);
                            break;
                        }
                    }
                } if (groups.get(0).size() < bg.getMinSize()) {
                    for (List<Player> group: validGroups) {
                        if (!usedGroups.contains(group) && group.size() + groups.get(0).size() <= bg.getMaxSize()) {
                            groups.get(0).addAll(group);
                            usedGroups.add(group);
                            if (groups.get(0).size() >= bg.getMinSize()) {
                                break;
							}
                        }
                    }
                } if (groups.get(0).size() < bg.getMinSize() || groups.get(0).size() > bg.getMaxSize()) {
                    continue;
				}
                boolean satisfied = true;
                for (int i = 1; i < bg.getTeamCount(); i++) {
                    if (groups.get(i).size() < groups.get(0).size()) {
                        for (List<Player> group: validGroups) {
                            if (!usedGroups.contains(group) && group.size() + groups.get(i).size() <= groups.get(0).size()) {
                                groups.get(i).addAll(group);
                                usedGroups.add(group);
                            }
                        }
                    } if (groups.get(i).size() < groups.get(0).size()) {
                        satisfied = false;
					}
                } if (!satisfied) {
                    continue;
				} for (List<Player> group: groups) {
                    for (Player pl: group) {
                        participants.add(pl.getObjectId());
					}
				}
                validGroups.removeAll(usedGroups);
                bg.createMatch(participants);
            } else {
                bg.createMatch(validParticipants);
            } if (bg.hasPlayers()) {
                registerBg(bg);
			}
        } if (eventTeamBased) {
            for (List<Player> group: validGroups) {
                for (Player pl: group) {
                    scheduleAnnouncement(pl, "There are no more place!!!, You will more luck next time!", 0);
                }
            }
        } else {
            for (Integer objectId : validParticipants) {
                AionObject ao = World.getInstance().findVisibleObject(objectId);
                if (ao != null && ao instanceof Player) {
                    scheduleAnnouncement((Player) ao, "\uE05C", "There are no more place!!!, You will more luck next time!", 0);
                }
            }
        }
        eventReady = false;
        eventBg = null;
        eventTeamBased = false;
    }

    private void HandleSoloQueue() {
        if (soloQueueList.size() < 1) {
            return;
		}
        List<Player> validPlayers = new ArrayList<Player>();
        for (AionObject ao: soloQueueList) {
            if (ao instanceof Player) {
                Player pl = (Player) ao;
                if (!pl.isOnline() || pl.getBattleground() != null) {
                    continue;
				}
                validPlayers.add(pl);
            }
        }
        soloQueueList.clear();
        Collections.shuffle(validPlayers);
        int iterations = 0;
        while (validPlayers.size() >= 2 && iterations++ < 5) {
            Battleground bg = new SoloSurvivorBg();
            List<Integer> participants = new ArrayList<Integer>(2);
            PacketSendUtility.sendPacket(validPlayers.get(0), new SM_AUTO_GROUP(2, 320150000, 0));
            PacketSendUtility.sendPacket(validPlayers.get(1), new SM_AUTO_GROUP(2, 320150000, 0));
            participants.add(validPlayers.remove(0).getObjectId());
            participants.add(validPlayers.remove(0).getObjectId());
            bg.setIs1v1(true);
            bg.setMatchLength(135);
            bg.createMatch(participants);
            if (bg.hasPlayers()) {
                registerBg(bg);
			}
        } if (validPlayers.size() > 0) {
            for (Player pl: validPlayers) {
                PacketSendUtility.sendSys3Message(pl, "\uE05C", "No opponents found!!! Search for a new opponent...");
                PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(2, 320150000, 0));
                registerForSolo(pl);
            }
        }
    }

    public boolean isNormalReady() {
        return normalReady;
    }

    public boolean isEventReady() {
        return eventReady;
    }

    public void cancelEvent() {
        if (eventTask != null) {
            eventTask.cancel(false);
            eventBg = null;
            eventQueueList.clear();
            eventTask = null;
            eventReady = false;
            World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendPacket(pl, new SM_AUTO_GROUP(300350000, false));
                }
            });
            announceAll("The event was canceled!!!");
        }
    }

    private void announceAll(final String msg) {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (player.getBattleground() == null && !FFAService.getInstance().isInArena(player)) {
                    PacketSendUtility.sendSys3Message(player, "\uE05C", msg);
                }
            }
        });
    }

    private Integer getNextAvailableBgId() {
        for (Integer i = 1; i < 10000; i++) {
            if (!bgMap.containsKey(i)) {
                return i;
			}
        }
        return -1;
    }

    public void onBgEnd(Battleground bg) {
        bgMap.remove(bg.getBgId());
        if (normalBgMap.containsKey(bg.getBgId())) {
            Event event = normalBgMap.remove(bg.getBgId());
            if (event instanceof BattlegroundEvent) {
                ((BattlegroundEvent) event).onEnd(bg.getBgId());
			}
        }
    }

    public Integer registerBg(Battleground bg) {
        Integer bgId = getNextAvailableBgId();
        bg.setBgId(bgId);
        bgMap.put(bgId, bg);
        return bgId;
    }

    public Map<Integer, Battleground> getBattlegrounds() {
        return bgMap;
    }

    public Battleground getActiveBattleground(Player player) {
        for (Battleground bg: getBattlegrounds().values()) {
            if (bg.getSecondsLeft() > 1) {
                if (bg.getLeavers().containsKey(player.getObjectId())) {
                    return bg;
				}
			}
        }
        return null;
    }

    public int getBgCloak(Player player, int bgIndex) {
        int template;
        switch(bgIndex + 1) {
            case 1:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202617;
				} else {
                    template = 202618;
				}
            break;
            case 2:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202603;
				} else {
                    template = 202604;
				}
            break;
            case 3:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202539;
				} else {
                    template = 202540;
				}
            break;
            case 4:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202583;
				} else {
                    template = 202584;
				}
            break;
            case 5:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202529;
				} else {
                    template = 202530;
				}
            break;
            case 6:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202531;
				} else {
                    template = 202532;
				}
            break;
            case 7:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202537;
				} else {
                    template = 202538;
				}
            break;
            case 8:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202526;
				} else {
                    template = 202527;
				}
            break;
            case 9:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202541;
				} else {
                    template = 202542;
				}
            break;
            case 10:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202543;
				} else {
                    template = 202544;
				}
            break;
            case 11:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202551;
				} else {
                    template = 202552;
				}
            break;
            case 12:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202561;
				} else {
                    template = 202562;
				}
            break;
            case 13:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202571;
				} else {
                    template = 202572;
				}
            break;
            case 14:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202573;
				} else {
                    template = 202574;
				}
            break;
            case 15:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202575;
				} else {
                    template = 202576;
				}
            break;
            case 16:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202579;
				} else {
                    template = 202580;
				}
            break;
            case 17:
                if (player.getCommonData().getRace() == Race.ELYOS) {
                    template = 202524;
				} else {
                    template = 202525;
				}
            break;
            default:
                template = 0;
            break;
        }
        return template;
    }

    public String getName(Player player, Player target) {
        if (player.isSpectating() || (player.getBattleground() != null && player.getBattleground().isTournament())) {
            return target.getName();
        } if (player.getAccessLevel() > 0 || (player.getBattleground() != null && player.getBattleground().is1v1())) {
            return target.getName();
        } if (player.isInGroup2() && target.isInGroup2() && player.getPlayerGroup2().getMembers().contains(target)) {
            return target.getName();
        }
        String playerName = "Contestant";
        if (!player.isInGroup2() && !player.isInAlliance2()) {
            playerName += " " + (target.getBgIndex() + 1);
        }
        return playerName;
    }

    public String getNameByIndex(int bgIndex) {
        String name;
        switch (bgIndex + 1) {
            case 1:
                name = "Daeva Of Chaos";
            break;
            case 2:
                name = "Until Death";
            break;
            case 3:
                name = "Happy Tiger's";
            break;
            case 4:
                name = "Abyssal Inquin's";
            break;
            case 5:
                name = "Puffy Bear's";
            break;
            case 6:
                name = "Mossy Treant's";
            break;
            case 7:
                name = "Cursed Pirate's";
            break;
            case 8:
                name = "Naughty Kerub's";
            break;
            case 9:
                name = "Tundra Tiger's";
            break;
            case 10:
                name = "Tiger Lover's";
            break;
            case 11:
                name = "Screamer's";
            break;
            case 12:
                name = "Ninja Balaur's";
            break;
            case 13:
                name = "Summer Inquin's";
            break;
            case 14:
                name = "Volcano Inquin's";
            break;
            case 15:
                name = "Savana Inquin's";
            break;
            case 16:
                name = "Tropical Inquin's";
            break;
            case 17:
                name = "Walking Dead";
            break;
            default:
                name = "Invalid";
            break;
        }
        name += "Team";
        return name;
    }

    public LegionEmblem getCapeEmblemByIndex(int bgIndex) {
        LegionEmblem emblem = new LegionEmblem();
        byte[] uploadData = {0, 0};
        switch (bgIndex + 1) {
            case 1:
                emblem.setEmblem(22, 0, 0, 255, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 2:
                emblem.setEmblem(22, 255, 0, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 3:
                emblem.setEmblem(22, 255, 255, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 4:
                emblem.setEmblem(22, 0, 255, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 5:
                emblem.setEmblem(22, 255, 0, 255, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 6:
                emblem.setEmblem(22, 0, 255, 255, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 7:
                emblem.setEmblem(22, 255, 128, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 8:
                emblem.setEmblem(22, 255, 100, 180, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 9:
                emblem.setEmblem(22, 115, 255, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 10:
                emblem.setEmblem(22, 0, 255, 212, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 11:
                emblem.setEmblem(22, 255, 255, 255, LegionEmblemType.DEFAULT, uploadData);
            break;
            case 12:
                emblem.setEmblem(22, 0, 0, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
            default:
                emblem.setEmblem(22, 0, 0, 0, LegionEmblemType.DEFAULT, uploadData);
            break;
        }
        return emblem;
    }

    public void handleWindow(Player player, int windowId, int dialogId) {
        if (soloQueueList.contains(player) && windowId == 101) {
            unregisterFromQueue(player);
        } else if (isEventReady()) {
            switch (windowId) {
                case 100:
                    switch (dialogId) {
                        case 0:
                        case 1:
                            if (registerForEvent(player)) {
                                PacketSendUtility.sendSys3Message(player, "\uE05C", "You are now registered for event!!!");
							} else {
                                PacketSendUtility.sendSys3Message(player, "\uE05C", "Failed to register!!! If you are in a group, you have already registered.");
							}
                        break;
                        case 2:
                            if (eventTeamBased && player.isInGroup2()) {
                                if (player.getPlayerGroup2().getLeaderObject() == player) {
                                    if (registerForEvent(player.getPlayerGroup2())) {
                                        for (Player pl: player.getPlayerGroup2().getMembers()) {
                                            PacketSendUtility.sendSys3Message(pl, "\uE05C", "Your group is now registered for event.");
										}
									} else {
                                        PacketSendUtility.sendSys3Message(player, "\uE05C", "Failed to register your group!!!");
									}
                                } else {
                                    PacketSendUtility.sendSys3Message(player, "\uE05C", "You must be the leader of your group for registration!!!");
								}
                            }
                        break;
                    }
                break;
                case 101:
                    if (isInQueue(player)) {
                        unregisterFromQueue(player);
                        PacketSendUtility.sendSys3Message(player, "\uE05C", "You must cancel your registration for the event!!!");
                    } else if (player.isInGroup2() && isInQueue(player.getPlayerGroup2())) {
                        unregisterFromQueue(player.getPlayerGroup2());
                        for (Player pl: player.getPlayerGroup2().getMembers()) {
                            PacketSendUtility.sendSys3Message(pl, "\uE05C", "Your group to withdraw its registration for the event!!!");
						}
                    }
                break;
                case 104:
                    PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(300350000, eventTeamBased, 903479));
                break;
            }
        } else if (isNormalReady()) {
            switch (windowId) {
                case 100:
                    switch (dialogId) {
                        case 0:
                        case 1:
                            if (registerForNormal(player)) {
                                PacketSendUtility.sendSys3Message(player, "\uE05C", "You are now registered for the next battlefield!!!");
                            } else {
                                PacketSendUtility.sendSys3Message(player, "\uE05C", "Failed to register!!! If you are in a group, you have already registered.");
							}
                        break;
                        case 2:
                            if (normalTeamBased && player.isInGroup2()) {
                                if (player.getPlayerGroup2().getLeaderObject() == player) {
                                    if (registerForNormal(player.getPlayerGroup2())) {
                                        for (Player pl : player.getPlayerGroup2().getMembers()) {
                                            PacketSendUtility.sendSys3Message(pl, "\uE05C", "Your group is now registered for battlefield.");
										}
									} else {
                                        PacketSendUtility.sendSys3Message(player, "\uE05C", "Failed to register your group!!!");
									}
                                } else {
                                    PacketSendUtility.sendSys3Message(player, "\uE05C", "You must be the leader of your group for registration!!!");
								}
                            }
                        break;
                    }
                break;
                case 101:
                    if (isInQueue(player)) {
                        unregisterFromQueue(player);
                        PacketSendUtility.sendSys3Message(player, "\uE05C", "You must cancel your registration for the battlefield!");
                    } else if (player.isInGroup2() && isInQueue(player.getPlayerGroup2())) {
                        unregisterFromQueue(player.getPlayerGroup2());
                        for (Player pl: player.getPlayerGroup2().getMembers()) {
                            PacketSendUtility.sendSys3Message(pl, "\uE05C", "Your group to withdraw its registration for the battlefield!!!");
						}
                    }
                break;
                case 104:
                    PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(301550000, normalTeamBased, 903479));
                break;
            }
        }
    }

    public void onPlayerLogin(Player player) {
        if (isNormalReady() && player.getBattleground() != null && !isInQueue(player)) {
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(301550000, true));
        }
    }

    private void SortParticipantList(List<Integer> participants) {
        List<Integer> warrior = new ArrayList<Integer>();
        List<Integer> scout = new ArrayList<Integer>();
        List<Integer> mage = new ArrayList<Integer>();
        List<Integer> cleric = new ArrayList<Integer>();
		List<Integer> technist = new ArrayList<Integer>();
		List<Integer> muse = new ArrayList<Integer>();
        for (Integer objectId : participants) {
            Player pl = World.getInstance().findPlayer(objectId);
            if (pl == null) {
                continue;
			} switch (PlayerClass.getStartingClassFor(pl.getPlayerClass())) {
                case WARRIOR:
                    warrior.add(objectId);
                break;
                case SCOUT:
                    scout.add(objectId);
                break;
                case MAGE:
                    mage.add(objectId);
                break;
                case PRIEST:
                    cleric.add(objectId);
                break;
				case TECHNIST:
                    technist.add(objectId);
                break;
				case MUSE:
                    muse.add(objectId);
                break;
            }
        }
        participants = new ArrayList<Integer>();
        while (!warrior.isEmpty() && !scout.isEmpty() && !mage.isEmpty() && !cleric.isEmpty() && !technist.isEmpty() && !muse.isEmpty()) {
            int total = warrior.size() + scout.size() + mage.size() + cleric.size() + technist.size() + muse.size();
            float ratioW = (warrior.size() * 100) / total;
            float ratioS = (scout.size() * 100) / total;
            float ratioM = (mage.size() * 100) / total;
            float ratioP = (cleric.size() * 100) / total;
			float ratioT = (technist.size() * 100) / total;
			float ratioMu = (muse.size() * 100) / total;
            if (!warrior.isEmpty()) {
                participants.add(warrior.remove(0));
			} if (!scout.isEmpty()) {
                participants.add(scout.remove(0));
			} if (!mage.isEmpty()) {
                participants.add(mage.remove(0));
			} if (!cleric.isEmpty()) {
                participants.add(cleric.remove(0));
			} if (!technist.isEmpty()) {
                participants.add(technist.remove(0));
			} if (!muse.isEmpty()) {
                participants.add(muse.remove(0));
			} if (!warrior.isEmpty() && ratioW > 30) {
                participants.add(warrior.remove(0));
			} if (!scout.isEmpty() && ratioS > 30) {
                participants.add(scout.remove(0));
			} if (!mage.isEmpty() && ratioM > 30) {
                participants.add(mage.remove(0));
			} if (!cleric.isEmpty() && ratioP > 30) {
                participants.add(cleric.remove(0));
			} if (!technist.isEmpty() && ratioT > 30) {
                participants.add(technist.remove(0));
			} if (!muse.isEmpty() && ratioMu > 30) {
                participants.add(muse.remove(0));
			}
        }
    }

    private List<Player> cloneGroup(List<Player> group) {
        List<Player> clone = new ArrayList<Player>();
        clone.addAll(group);
        return clone;
    }

    private void scheduleAnnouncement(final Player player, final String msg, int delay) {
        this.scheduleAnnouncement(player, "\uE05C", msg, delay);
    }

    private void scheduleAnnouncement(final Player player, final String sender, final String msg, int delay) {
        if (delay > 0) {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    PacketSendUtility.sendSys3Message(player, sender, msg);
                }
            }, delay);
        } else {
            PacketSendUtility.sendSys3Message(player, sender, msg);
        }
    }

    private LadderDAO getLadderDAO() {
        return DAOManager.getDAO(LadderDAO.class);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final LadderService instance = new LadderService();
    }

    public static final LadderService getInstance() {
        return SingletonHolder.instance;
    }
}