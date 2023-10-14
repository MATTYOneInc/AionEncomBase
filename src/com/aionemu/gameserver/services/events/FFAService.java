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
package com.aionemu.gameserver.services.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.FFAConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
public class FFAService
{
    private static final Logger log = LoggerFactory.getLogger(FFAService.class);
    private Map<Integer, WorldPosition> previousLocations = new FastMap<Integer, WorldPosition>();
    private WorldMapInstance activeInstance;
    private List<ArenaMap> maps = new ArrayList<ArenaMap>();
    private ArenaMap activeMap = null;
    private int incrementCounter = 0;
    @SuppressWarnings("unused")
    private Map<Integer, StaticDoor> doors;
    private Object UnsummonType;
    private static boolean isAvailable;

    public FFAService() {
        log.info("[FFAService] is initialized...");
        //Nochsana Training Camp.
        maps.add(new ArenaMap(300030000, 99, Arrays.asList(new Float[]{331f, 272f, 384f}, new Float[]{314f, 325f, 380f}, new Float[]{366f, 320f, 380f},
                new Float[]{308f, 280f, 392f}, new Float[]{356f, 271f, 392f}, new Float[]{343f, 339f, 379f})));
        //Carpus Isle Storeroom.
        maps.add(new ArenaMap(300050000, 99, Arrays.asList(new Float[]{484f, 566f, 201f}, new Float[]{524f, 591f, 199f}, new Float[]{556f, 564f, 198f},
                new Float[]{521f, 536f, 199f}, new Float[]{514f, 564f, 197f}, new Float[]{530f, 563f, 197f})));
        //Sulfur Tree Nest.
        maps.add(new ArenaMap(300060000, 99, Arrays.asList(new Float[]{476f, 418f, 163f}, new Float[]{435f, 440f, 162f}, new Float[]{430f, 486f, 162f},
                new Float[]{480f, 504f, 162f}, new Float[]{485f, 460f, 162f}, new Float[]{453f, 472f, 163f})));
        //Hamate Isle Storeroom.
        maps.add(new ArenaMap(300070000, 99, Arrays.asList( new Float[]{504f, 423f, 91f}, new Float[]{503f, 460f, 86f}, new Float[]{481f, 483f, 87f},
                new Float[]{528f, 483f, 87f}, new Float[]{504f, 503f, 88f}, new Float[]{503f, 479f, 87f})));
        //Left Wing Chamber.
        maps.add(new ArenaMap(300080000, 99, Arrays.asList(new Float[]{488f, 512f, 352f}, new Float[]{495f, 548f, 354f}, new Float[]{458f, 530f, 352f},
                new Float[]{485f, 585f, 355f}, new Float[]{495f, 623f, 354f}, new Float[]{451f, 618f, 352f})));
        //Steel Rake.
        maps.add(new ArenaMap(300100000, 99, Arrays.asList(new Float[]{568f, 489f, 1023f}, new Float[]{568f, 528f, 1023f}, new Float[]{544f, 527f, 1023f},
                new Float[]{545f, 489f, 1023f}, new Float[]{592f, 489f, 1023f}, new Float[]{592f, 528f, 1023f})));
        //Baranath Dredgion.
        maps.add(new ArenaMap(300110000, 99, Arrays.asList(new Float[]{485f, 857f, 417f}, new Float[]{485f, 877f, 405f}, new Float[]{513f, 889f, 405f},
                new Float[]{457f, 889f, 405f}, new Float[]{485f, 909f, 405f}, new Float[]{485f, 814f, 416f})));
        //Grave Of Steel Storeroom.
        maps.add(new ArenaMap(300120000, 99, Arrays.asList(new Float[]{496f, 826f, 199f}, new Float[]{492f, 851f, 199f}, new Float[]{504f, 873f, 199f},
                new Float[]{528f, 881f, 199f}, new Float[]{552f, 873f, 199f}, new Float[]{564f, 851f, 199f})));
        //Twilight Battlefield Storeroom.
        maps.add(new ArenaMap(300130000, 99, Arrays.asList(new Float[]{496f, 826f, 199f}, new Float[]{492f, 851f, 199f}, new Float[]{504f, 873f, 199f},
                new Float[]{528f, 881f, 199f}, new Float[]{552f, 873f, 199f}, new Float[]{564f, 851f, 199f})));
        //Isle Of Roots Storeroom.
        maps.add(new ArenaMap(300140000, 99, Arrays.asList(new Float[]{496f, 826f, 199f}, new Float[]{492f, 851f, 199f}, new Float[]{504f, 873f, 199f},
                new Float[]{528f, 881f, 199f}, new Float[]{552f, 873f, 199f}, new Float[]{564f, 851f, 199f})));
        //Lower Udas Temple.
        maps.add(new ArenaMap(300160000, 99, Arrays.asList(new Float[]{571f, 1297f, 187f}, new Float[]{566f, 1242f, 188f}, new Float[]{572f, 1344f, 188f},
                new Float[]{636f, 1385f, 186f}, new Float[]{658f, 1297f, 186f}, new Float[]{640f, 1215f, 186f})));
        //Beshmundir Temple.
        maps.add(new ArenaMap(300170000, 99, Arrays.asList(new Float[]{1505f, 1463f, 304f}, new Float[]{1441f, 1378f, 305f}, new Float[]{1511f, 1385f, 307f},
                new Float[]{1428f, 1448f, 307f}, new Float[]{1533f, 1433f, 300f}, new Float[]{1468f, 1483f, 300f})));
        //Taloc's Hollow.
        maps.add(new ArenaMap(300190000, 99, Arrays.asList(new Float[]{392f, 897f, 1266f}, new Float[]{442f, 919f, 1274f}, new Float[]{434f, 878f, 1276f},
                new Float[]{387f, 862f, 1264f}, new Float[]{429f, 934f, 1266f}, new Float[]{382f, 842f, 1271f})));
        //Haramel.
        maps.add(new ArenaMap(300200000, 99, Arrays.asList(new Float[]{387f, 315f, 88f}, new Float[]{376f, 285f, 89f}, new Float[]{347f, 287f, 90f},
                new Float[]{344f, 331f, 87f}, new Float[]{356f, 367f, 90f}, new Float[]{327f, 380f, 89f})));
        //Chantra Dredgion.
        maps.add(new ArenaMap(300210000, 99, Arrays.asList(new Float[]{458f, 493f, 397f}, new Float[]{514f, 493f, 397f}, new Float[]{486f, 455f, 398f},
                new Float[]{484f, 527f, 396f}, new Float[]{483f, 496f, 397f}, new Float[]{484f, 420f, 398f})));
        //Kromede Trial.
        maps.add(new ArenaMap(300230000, 99, Arrays.asList(new Float[]{528f, 640f, 201f}, new Float[]{493f, 640f, 201f}, new Float[]{513f, 610f, 201f},
                new Float[]{512f, 670f, 201f}, new Float[]{557f, 640f, 206f}, new Float[]{531f, 612f, 201f})));
        //Esoterrace.
        maps.add(new ArenaMap(300250000, 99, Arrays.asList(new Float[]{1254f, 624f, 296f}, new Float[]{1217f, 620f, 295f}, new Float[]{1230f, 664f, 298f},
                new Float[]{1249f, 695f, 299f}, new Float[]{1286f, 675f, 296f}, new Float[]{1294f, 623f, 297f})));
        //Terath Dredgion.
        maps.add(new ArenaMap(300440000, 99, Arrays.asList(new Float[]{443f, 321f, 403f}, new Float[]{484f, 342f, 403f}, new Float[]{485f, 297f, 402f},
                new Float[]{529f, 323f, 403f}, new Float[]{485f, 314f, 403f}, new Float[]{424f, 300f, 409f})));
        //Sealed Danuar Mysticarium.
        maps.add(new ArenaMap(300480000, 99, Arrays.asList(new Float[]{189f, 180f, 239f}, new Float[]{152f, 194f, 239f}, new Float[]{154f, 219f, 239f},
                new Float[]{190f, 207f, 238f}, new Float[]{188f, 244f, 240f}, new Float[]{230f, 208f, 239f})));
        //Eternal Bastion.
        //maps.add(new ArenaMap(300540000, 99, Arrays.asList(new Float[]{740f, 255f, 253f}, new Float[]{778f, 288f, 253f}, new Float[]{754f, 336f, 253f},
        //        new Float[]{717f, 321f, 252f}, new Float[]{698f, 287f, 253f}, new Float[]{766f, 266f, 233f})));
        //The Hexway.
        maps.add(new ArenaMap(300700000, 99, Arrays.asList(new Float[]{488f, 512f, 352f}, new Float[]{495f, 548f, 354f}, new Float[]{458f, 530f, 352f},
                new Float[]{485f, 585f, 355f}, new Float[]{495f, 623f, 354f}, new Float[]{451f, 618f, 352f})));
        //Kamar Battlefield.
        maps.add(new ArenaMap(301120000, 99, Arrays.asList(new Float[]{1344f, 1528f, 595f}, new Float[]{1313f, 1510f, 597f}, new Float[]{1313f, 1460f, 597f},
                new Float[]{1387f, 1513f, 597f}, new Float[]{1370f, 1460f, 599f}, new Float[]{1396f, 1423f, 600f})));
        //Engulfed Ophidan Bridge.
        maps.add(new ArenaMap(301210000, 99, Arrays.asList(new Float[]{499f, 523f, 597f}, new Float[]{527f, 541f, 604f}, new Float[]{494f, 550f, 597f},
                new Float[]{434f, 495f, 600f}, new Float[]{474f, 490f, 597f}, new Float[]{448f, 537f, 599f})));
        //Iron Wall Warfront.
        maps.add(new ArenaMap(301220000, 99, Arrays.asList(new Float[]{491f, 765f, 200f}, new Float[]{552f, 744f, 197f}, new Float[]{591f, 777f, 187f},
                new Float[]{565f, 807f, 188f}, new Float[]{599f, 823f, 187f}, new Float[]{612f, 776f, 185f})));
        //Idgel Dome.
        maps.add(new ArenaMap(301310000, 99, Arrays.asList(new Float[]{252f, 246f, 92f}, new Float[]{276f, 272f, 92f}, new Float[]{226f, 258f, 89f},
                new Float[]{302f, 258f, 89f}, new Float[]{248f, 289f, 89f}, new Float[]{277f, 225f, 89f})));
        //Drakenspire Depths.
        maps.add(new ArenaMap(301390000, 99, Arrays.asList(new Float[]{208f, 542f, 1754f}, new Float[]{176f, 579f, 1760f}, new Float[]{127f, 575f, 1754f},
                new Float[]{128f, 461f, 1754f}, new Float[]{177f, 458f, 1759f}, new Float[]{208f, 496f, 1754f})));
        //Cradle Of Eternity.
        maps.add(new ArenaMap(301550000, 99, Arrays.asList(new Float[]{464f, 1398f, 827f}, new Float[]{474f, 1418f, 827f}, new Float[]{510f, 1387f, 823f},
                new Float[]{430f, 1429f, 823f}, new Float[]{449f, 1374f, 823f}, new Float[]{491f, 1445f, 823f})));
        //Cradle Of Eternity [Memorial Path]
        maps.add(new ArenaMap(301550000, 99, Arrays.asList(new Float[]{602f, 806f, 565f}, new Float[]{626f, 768f, 561f}, new Float[]{629f, 717f, 555f},
                new Float[]{738f, 727f, 546f}, new Float[]{685f, 721f, 548f}, new Float[]{709f, 772f, 547f})));
        //Drakenseer's Lair.
        maps.add(new ArenaMap(301620000, 99, Arrays.asList(new Float[]{276f, 342f, 336f}, new Float[]{328f, 309f, 318f}, new Float[]{350f, 266f, 318f},
                new Float[]{330f, 204f, 319f}, new Float[]{266f, 197f, 319f}, new Float[]{237f, 292f, 318f})));
        //Fallen Poeta.
        maps.add(new ArenaMap(301660000, 99, Arrays.asList(new Float[]{216f, 348f, 130f}, new Float[]{235f, 382f, 124f}, new Float[]{183f, 334f, 123f},
                new Float[]{175f, 379f, 120f}, new Float[]{221f, 400f, 118f}, new Float[]{193f, 393f, 119f})));
        //Ophidan Warpath.
        maps.add(new ArenaMap(301670000, 99, Arrays.asList(new Float[]{697f, 466f, 599f}, new Float[]{676f, 495f, 599f}, new Float[]{665f, 449f, 600f},
                new Float[]{570f, 412f, 610f}, new Float[]{599f, 395f, 609f}, new Float[]{620f, 423f, 607f})));
        //Fissure Of Oblivion.
        maps.add(new ArenaMap(302100000, 99, Arrays.asList(new Float[]{326f, 512f, 352f}, new Float[]{278f, 513f, 351f}, new Float[]{300f, 531f, 350f},
                new Float[]{301f, 496f, 350f}, new Float[]{290f, 527f, 350f}, new Float[]{312f, 499f, 350f})));
        //Indratu Fortress.
        maps.add(new ArenaMap(310090000, 99, Arrays.asList(new Float[]{604f, 466f, 1019f}, new Float[]{617f, 516f, 1019f}, new Float[]{575f, 540f, 1013f},
                new Float[]{566f, 507f, 1012f}, new Float[]{552f, 479f, 1011f}, new Float[]{615f, 562f, 1018f})));
        //Azoturan Fortress.
        maps.add(new ArenaMap(310100000, 99, Arrays.asList(new Float[]{462f, 442f, 993f}, new Float[]{417f, 402f, 1004f}, new Float[]{425f, 398f, 991f},
                new Float[]{459f, 392f, 991f}, new Float[]{413f, 426f, 991f}, new Float[]{443f, 419f, 991f})));
        //Fire Temple.
        maps.add(new ArenaMap(320100000, 99, Arrays.asList(new Float[]{414f, 97f, 117f}, new Float[]{392f, 88f, 117f}, new Float[]{411f, 120f, 117f},
                new Float[]{392f, 128f, 117f}, new Float[]{377f, 99f, 117f}, new Float[]{361f, 126f, 116f})));
        //Padmarashka's Cave.
        maps.add(new ArenaMap(320150000, 99, Arrays.asList(new Float[]{576f, 279f, 66f}, new Float[]{605f, 235f, 66f}, new Float[]{578f, 206f, 66f},
                new Float[]{537f, 209f, 66f}, new Float[]{524f, 239f, 66f}, new Float[]{535f, 279f, 66f})));
        //Transidium Annex.
        //maps.add(new ArenaMap(400030000, 99, Arrays.asList(new Float[]{481f, 500f, 674f}, new Float[]{480f, 524f, 674f}, new Float[]{497f, 541f, 674f},
        //        new Float[]{521f, 542f, 674f}, new Float[]{538f, 524f, 674f}, new Float[]{538f, 500f, 674f}, new Float[]{521f, 483f, 674f}, new Float[]{497f, 483f, 674f})));
        pickArenaMap();
        activeInstance = getWorldMap().getMainWorldMapInstance();
        doors = activeInstance.getDoors();
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                incrementCounter++;
                if ((incrementCounter % 300) == 0) {
                    announcePlayerCount();
                } if ((incrementCounter % 180) == 0) {
                    announcePlayerCount();
                } if ((incrementCounter % 900) == 0) {
                    final int players = activeInstance.getPlayersInside().size();
                    if (players > 0) {
                        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                            @Override
                            public void visit(Player pl) {
                                if (!isInArena(pl) && pl.getBattleground() == null) {
                                    PacketSendUtility.sendSys3Message(pl, "\uE00B", "<FFA> Join the <FFA> map in writing: .ffa and play with " + players + " other players right now!!!");
                                }
                            }
                        });
                    }
                } if ((incrementCounter % 600) == 0) { //Change map every 10 Min.
                    pickArenaMap();
                } if ((incrementCounter % 3600) == 0) {
                    incrementCounter = 0;
                    World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                        @Override
                        public void visit(Player pl) {
                            if (!isInArena(pl) && pl.getBattleground() == null) {
                                PacketSendUtility.sendSys3Message(pl, "\uE00B", "<FFA> Join the <FFA> area, and try to win AP/GP. Write: .ffa!!!");
                            }
                        }
                    });
                }
            }
        }, 1 * 1000, 1 * 1000);
    }

    public boolean pickArenaMap() {
        if (maps.size() == 0) {
            return false;
        } if (maps.size() == 1) {
            activeMap = maps.get(0);
            analyseInstanceBalance();
            return false;
        }
        List<ArenaMap> mapsWithoutActive = new ArrayList<ArenaMap>(maps.size());
        mapsWithoutActive.addAll(maps);
        if (activeMap != null) {
            mapsWithoutActive.remove(activeMap);
            for (WorldMapInstance instance : getWorldMap().getInstances()) {
                final String msg = "Map loading, please wait...";
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendMessage(pl, msg);
                        enterArena(pl, true);
                    }
                });
            }
        }
        activeMap = mapsWithoutActive.get(Rnd.get(mapsWithoutActive.size()));
        activeInstance = getWorldMap().getMainWorldMapInstance();
        return true;
    }

    private WorldMap getWorldMap() {
        return World.getInstance().getWorldMap(activeMap.getMapId());
    }

    private void announcePlayerCount() {
        for (WorldMapInstance instance: getWorldMap().getInstances()) {
            final String msg = "[FFA] There are currently: " + instance.getPlayersInside().size() + " player's on the map.";
            instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendMessage(pl, msg);
                }
            });
        }
    }

    public void announceKill(Player victim, Player killer) {
        for (WorldMapInstance instance : getWorldMap().getInstances()) {
            final String msg = killer.getPlayerClass() + " has killed " + victim.getPlayerClass() + "!";
            instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendSys3Message(pl, "\uE00B", msg);
                }
            });
        }
    }

    public void onDie(final Player player, Creature lastAttacker) {
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DEATH_MESSAGE_ME);
        player.getMoveController().abortMove();
        player.setState(CreatureState.DEAD);
        player.getObserveController().notifyDeathObservers(player);
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player.getEffectController().removeEffectByDispelCat(DispelCategoryType.ALL, SkillTargetSlot.DEBUFF, 100, 2, 100, false);
        player.setTarget(null);
        PacketSendUtility.sendPacket(player, new SM_TARGET_SELECTED(player));
        if (lastAttacker instanceof Player) {
            rewardKiller(player, (Player)lastAttacker);
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (isInArena(player) && player.isFFA()) {
                    if (player.getLifeStats().isAlreadyDead()) {
                        PlayerReviveService.ffaRevive(player);
                    }
                    Float[] spawn = getRandomSpawn();
                    TeleportService2.teleportTo(player, getWorldMap().getMapId(), player.getInstanceId(), spawn[0], spawn[1], spawn[2]);
                }
            }
        }, 6000);
    }

    public void rewardKiller(Player player, Player killer) {
        if (killer == null || killer == player) {
            return;
        }
        killer.setKillStreak(killer.getKillStreak() + 1);
        checkKillerLevel(killer);
        //Reward AP.
        AbyssPointsService.addAp(player, 0);
        AbyssPointsService.addAp(killer, 5000);
        //Reward GP.
        AbyssPointsService.addGp(killer, 10);
        player.setKillStreak(0);
        killer.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 1500);
        killer.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, 1500);
        killer.getCommonData().setDp(500 + killer.getCommonData().getDp());
        for (WorldMapInstance instance: getWorldMap().getInstances()) {
            final String msg = killer.getName() + " has killed " + player.getName() + "!";
            instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player pl) {
                    PacketSendUtility.sendSys3Message(pl, "\uE005", msg);
                }
            });
        }
    }

    public void checkKillerLevel (Player player) {
        if (player.getKillStreak() == 5) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_1;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 10) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_2;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 15) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_3;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 20) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_4;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 25) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_5;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 30) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_6;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 35) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_7;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 40) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_8;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() == 45) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_9;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        } if (player.getKillStreak() >= 50 && player.getKillStreak() <= 999) {
            for (WorldMapInstance instance: getWorldMap().getInstances()) {
                ItemService.addItem(player, FFAConfig.FFA_SPREE_REWARD_ITEM, 1);
                InGameShopEn.getInstance().addToll(player, FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY);
                PacketSendUtility.sendMessage(player, "You've received " + FFAConfig.FFA_SPREE_REWARD_TOLL_QUANTITY + " tolls from FFA!");
                final String msg = player.getName() + FFAConfig.FFA_SPREE_10;
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player pl) {
                        PacketSendUtility.sendSys3Message(pl, "\uE07e", msg);
                    }
                });
            }
        }
    }

    public boolean isInArena(Player player) {
        for (ArenaMap arenaMap : maps) {
            if (arenaMap.getMapId() == player.getWorldId()) {
                return true;
            }
        }
        return false;
    }

    public void enterArena(final Player player, final boolean isMapRotation) {
        player.getEffectController().setAbnormal(AbnormalState.SLEEP.getId());
        player.getEffectController().updatePlayerEffectIcons();
        player.getEffectController().broadCastEffects();
        if (!isMapRotation) {
            previousLocations.put(player.getObjectId(), player.getPosition().clone());
        }
        final ActionObserver observer = new ActionObserver(ObserverType.ATTACKED) {
            @Override
            public void attacked(Creature creature) {
                if (player.getController().hasTask(TaskId.FFA)) {
                    player.getController().cancelTask(TaskId.FFA);
                    player.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
                    player.getEffectController().updatePlayerEffectIcons();
                    player.getEffectController().broadCastEffects();
                }
            }
        };
        player.getObserveController().attach(observer);
        player.getController().addTask(TaskId.FFA,
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        player.getObserveController().removeObserver(observer);
                        if (player.getLifeStats().isAlreadyDead()) {
                            PlayerReviveService.skillRevive(player);
                        } if (player.isInGroup2()) {
                            PlayerGroupService.removePlayer(player);
                        } if (player.isInAlliance2()) {
                            PlayerAllianceService.removePlayer(player);
                        }
                        player.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
                        player.getEffectController().updatePlayerEffectIcons();
                        player.getEffectController().broadCastEffects();
                        player.getCommonData().setDp(0);
                        player.setFFA(true);
                        analyseInstanceBalance();
                        Float[] spawn = getRandomSpawn();
                        //sendEventPacket(StageType.PVP_STAGE_1, 0);
                        TeleportService2.teleportTo(player, getWorldMap().getMapId(), activeInstance.getInstanceId(), spawn[0], spawn[1], spawn[2]);
                    }
                },10 * 1000));
    }

    public void leaveArena(final Player player) {
        final WorldPosition pos = previousLocations.remove(player.getObjectId());
        player.getEffectController().setAbnormal(AbnormalState.SLEEP.getId());
        player.getEffectController().updatePlayerEffectIcons();
        player.getEffectController().broadCastEffects();
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (player.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.skillRevive(player);
                }
                player.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
                player.getEffectController().updatePlayerEffectIcons();
                player.getEffectController().broadCastEffects();
                player.setFFA(false);
                if (pos != null) {
                    TeleportService2.teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ());
                } else {
                    TeleportService2.moveToBindLocation(player, true);
                }
            }
        }, 10 * 1000);
    }

    private Float[] getRandomSpawn() {
        return activeMap.getSpawns().get(Rnd.get(activeMap.getSpawns().size()));
    }

    private void analyseInstanceBalance() {
        for (WorldMapInstance instance : getWorldMap().getInstances()) {
            if (instance.getPlayersInside().size() < activeMap.getPlayerCap()) {
                activeInstance = instance;
            }
        } if (activeInstance == null || activeInstance.getPlayersInside().size() >= activeMap.getPlayerCap()) {
            activeInstance = InstanceService.getNextBgInstance(getWorldMap().getMapId());
        }
    }

    public boolean isActiveInstance(WorldMapInstance instance) {
        return instance.getInstanceId() == activeInstance.getInstanceId();
    }

    public boolean isActiveWorld(WorldMapInstance instance) {
        return instance.getMapId() == activeInstance.getMapId();
    }

    public String getName(Player player, Player target) {
        String FFAplayerName = target.getPlayerClass().name();
        return FFAplayerName;
    }

    public static final FFAService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    public static class SingletonHolder {
        protected static final FFAService instance = new FFAService();
    }

    public static class ArenaMap {
        private int mapId;
        private List<Float[]> spawns;
        private int playerCap;
        private List<Integer> staticDoors = null;
        public ArenaMap(int mapId, int playerCap, List<Float[]> spawns) {
            this.mapId = mapId;
            this.playerCap = playerCap;
            this.spawns = spawns;
        }
        public int getMapId() {
            return mapId;
        }
        public int getPlayerCap() {
            return playerCap;
        }
        public List<Float[]> getSpawns() {
            return spawns;
        }
    }

    public static boolean isAvailable() {
        return isAvailable;
    }
}