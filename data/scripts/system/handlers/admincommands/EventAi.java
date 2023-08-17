/*
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
package com.aionemu.gameserver;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RESURRECT;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * Created by Ataba
 */
public class EventAi extends AdminCommand {
    public EventAi() {
        super("eventai");
    }

    private static final Logger log = LoggerFactory.getLogger("GM_MONITOR_LOG");

    public void execute(final Player admin, String...params){
        if(params.length < 1){
            onFail(admin,null);
            return;
        }

        if(params[0].equals("help")){
            onFail(admin, "");
            return;
        }

        //TODO IN DISTANCE 100
        if(params[0].equals("rewardall_inzone")){
            int rewardAmount = Integer.parseInt(params[2]);

            Iterator<Player> ita = World.getInstance().getPlayersIterator();
            if(params[1].equals("gp")){

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId() && !(player.getName() == admin.getName() && GeoService.getInstance().canSee(admin, player))){
                        AbyssPointsService.addGp(player, rewardAmount);
                        PacketSendUtility.sendMessage(player, "You've rewarded "+rewardAmount+" GP from an Event!");
                        PacketSendUtility.sendMessage(admin, "Player : "+player.getName()+" has been rewarded!");
                    }
                }

                log.info("[eventai-rewardall_inzone] GM : " + admin.getName() + " gave GP : " + rewardAmount + " in mapId '" + admin.getWorldId() + "'");
                PacketSendUtility.sendMessage(admin, "Every player on the map has been sucessfully rewarded with " + rewardAmount + " GP !");

            }else if(params[1].equals("ap")){
                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId() && !(player.getName() == admin.getName() && GeoService.getInstance().canSee(admin, player))){
                    	AbyssPointsService.addAp(player, rewardAmount);
                        PacketSendUtility.sendMessage(player, "You've rewarded "+rewardAmount+" AP from an Event!");
                    }
                }
                log.info("[eventai-rewardall_inzone] GM : " + admin.getName() + " gave AP : " + rewardAmount + " in mapId '" + admin.getWorldId() + "'");
                PacketSendUtility.sendMessage(admin, "Every player on the map has been sucessfully rewarded with "+rewardAmount+" AP !");
            }
        } else if (params[0].equalsIgnoreCase("reward_range")) {
            //eventai reward_range [TS] [Omega] [gp] [toll] [range]
            final int TS = Integer.parseInt(params[1]);
            final int Omega = Integer.parseInt(params[2]);
            final int GP = Integer.parseInt(params[3]);
            final int toll = Integer.parseInt(params[4]);
            final int range = Integer.parseInt(params[5]);

            RequestResponseHandler requestHim = new RequestResponseHandler(admin) {
                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                        @Override
                        public void visit(Player player) {
                            if (MathUtil.isInRange(admin, player, range)){
                                if(admin != player){
                                    if(TS != 0){
                                        ItemService.addItem(player, 166030005, TS);
                                    }
                                    if(Omega != 0){
                                        ItemService.addItem(player, 166020000, Omega);
										
									}
									if(GP != 0){
										AbyssPointsService.addGp(player, GP);
										PacketSendUtility.sendMessage(player, "You Received " + GP + " Glory Point(s) from Event!");
									}
                                    if(toll != 0){
                                        InGameShopEn.getInstance().addToll(player, toll);
                                        PacketSendUtility.sendMessage(player, "You Received " + toll + " Toll Point(s) from Event!");
                                    }
                                    PacketSendUtility.sendWhiteMessage(admin, "Player: \uE020" + player.getName() + "\uE020 Has Been Successfully Rewarded!");
									PacketSendUtility.sendWhiteMessage(player, "\uE020Thanks for Registering To "+ admin.getName() +"'s Event, Have a Great Time Playing InsaneAion \uE020 ");
                                }
                            }
                        }
                    });
                    log.info("[eventai-reward_range] GM : " + admin.getName() + " gave TS : " + TS + " Omega : " + Omega + " GP : " + GP + " Toll : " + toll + " in range of " + range + "m in mapId '" + admin.getWorldId() + "'");
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    PacketSendUtility.sendMessage(admin, "Well, Better Recheck Again ^^");
                }
            };
            boolean areyousure = admin.getResponseRequester().putRequest(1300564, requestHim);
            if (areyousure){
                PacketSendUtility.sendPacket(admin, new SM_QUESTION_WINDOW(1300564, 0, 0, "You Are About To Give\n" +
                        "x" + TS + " Tempering Solution and\n" +
                        "x" + Omega + " Omega Enchantment Stone and\n" +
                        "" + GP + " Glory Points and \n" +
                        "" + toll + " Toll Points In Range Of \n" +
                        "" + range + " Meters\n" +
                        "Are You Sure You wanna Procced?"));
            }

        }else if(params[0].equalsIgnoreCase("rewardall_all")){
            int rewardAmount = Integer.parseInt(params[2]);

            Iterator<Player> ita = World.getInstance().getPlayersIterator();
            if(params[1].equals("gp")){

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(!(player.getName() == admin.getName() && GeoService.getInstance().canSee(admin, player))){
                    	AbyssPointsService.addGp(player, rewardAmount);
                        PacketSendUtility.sendMessage(player, "You've rewarded "+rewardAmount+" GP from an Event!");
                        PacketSendUtility.sendMessage(admin, "Player : "+player.getName()+" has been rewarded!");
                    }
                }

                log.info("[eventai-rewardall_all] GM : " + admin.getName() + " gave GP : " + rewardAmount + " in mapId '" + admin.getWorldId() + "'");
                PacketSendUtility.sendMessage(admin, "Every player on the map has been sucessfully rewarded with "+rewardAmount+" GP !");

            }else if(params[1].equals("ap")){
                while(ita.hasNext()){
                    Player player = ita.next();
                    if(!(player.getName() == admin.getName() && GeoService.getInstance().canSee(admin, player))){
                    	AbyssPointsService.addAp(player, rewardAmount);
                        PacketSendUtility.sendMessage(player, "You've rewarded "+rewardAmount+" AP from an Event!");
                    }
                }

                log.info("[eventai-rewardall_inzone] GM : " + admin.getName() + " gave AP : " + rewardAmount + " in mapId '" + admin.getWorldId() + "'");
                PacketSendUtility.sendMessage(admin, "Every player on the map has been sucessfully rewarded with "+rewardAmount+" AP !");
            }
        }else if(params[0].equals("rewardall_queue")){
            int rewardAmount = Integer.parseInt(params[2]);
            if(admin.getQueuedPlayers().isEmpty()){
                PacketSendUtility.sendMessage(admin, "There is no one in the queued reward list!");
                return;
            }

            Iterator<Player> ita = World.getInstance().getPlayersIterator();
            if(params[1].equals("gp")){

                while(ita.hasNext()){
                    Player player = ita.next();

                    if(admin.QueuedPlayers.contains(player)){
                        PacketSendUtility.sendMessage(admin, "Player : "+player.getName()+" has been rewarded!");
                        AbyssPointsService.addGp(player, rewardAmount);
                    }
                }
                log.info("[eventai-rewardall_queue] GM : " + admin.getName() + " gave GP : " + rewardAmount + " in mapId '" + admin.getWorldId() + "'");
                admin.QueuedPlayers.clear();
            }

        }else if(params[0].equals("movetomeall_inzone")){
           Iterator<Player> ita = World.getInstance().getPlayersIterator();
           while(ita.hasNext()){
               Player player = ita.next();
               if(player.getWorldId() == admin.getWorldId() && !(player.getName() == admin.getName())){
                   TeleportService2.teleportTo(player, admin.getWorldId(),admin.getInstanceId(), admin.getX(),admin.getY(),admin.getZ());
                   PacketSendUtility.sendMessage(player, "You have been ported by a GM");
               }
           }
            log.info("[eventai-movetomeall_inzone] GM : " + admin.getName() + " teleported everyone to him in mapId '" + admin.getWorldId() + "'");
            PacketSendUtility.sendMessage(admin, "Every player in this Map as been gathered to Your location!");

        }else if(params[0].equals("resall_inzone")){
            Iterator<Player> ita = World.getInstance().getPlayersIterator();
            while(ita.hasNext()){
                Player player = ita.next();
                if(player.getWorldId() == admin.getWorldId() && player.getLifeStats().isAlreadyDead()){
                    player.setPlayerResActivate(true);
                    PacketSendUtility.sendPacket(player, new SM_RESURRECT(admin));
                    return;
                }
            }
            log.info("[eventai-resall_inzone] GM : " + admin.getName() + " resurrected everyone inzone in mapId '" + admin.getWorldId() + "'");
        }else if(params[0].equals("announce_inzone")){
            String Message;
            String type;
            Message = " ";
            type = params[1];


            for (int i = 2; i < params.length - 1; i++) {
                Message += params[i] + " ";
            }
            // Add the last without the end space
            Message += params[params.length - 1];

            String actual;
            actual = "[ " +type+" ] : " + Message + " ";

            Iterator<Player> ita = World.getInstance().getPlayersIterator();

            while(ita.hasNext()){
                Player player = ita.next();
                if(player.getWorldId() == admin.getWorldId()){
                    PacketSendUtility.sendYellowMessageOnCenter(player, actual);
                }
            }
            log.info("[eventai-announce_inzone] GM : " + admin.getName() + " announced inzone saying [" + actual + "] in mapId '" + admin.getWorldId() + "'");
        }else if(params[0].equals("announce_all")){
            String Message;
            String type;
            Message = " ";
            type = params[1];

            for (int i = 2; i < params.length - 1; i++) {
                Message += params[i] + " ";
            }

            // Add the last without the end space
            Message += params[params.length - 1];


            String actual;
            actual = "[ " +type+" ] : " + Message + " ";

            Iterator<Player> ita = World.getInstance().getPlayersIterator();

            while(ita.hasNext()){
                Player player = ita.next();

                PacketSendUtility.sendYellowMessageOnCenter(player, actual);

            }
            log.info("[eventai-announce_all] GM : " + admin.getName() + " announced all saying [" + actual + "] in mapId '" + admin.getWorldId() + "'");
        }else if(params[0].equalsIgnoreCase("stop")){
            if(params[1].equalsIgnoreCase("all")){

                Iterator<Player> ita = World.getInstance().getPlayersIterator();

                while(ita.hasNext()){
                    Player player = ita.next();

                    if(player.getWorldId() == admin.getWorldId() && player.getAccessLevel() < 1 && GeoService.getInstance().canSee(admin,player)){
                        SkillEngine.getInstance().applyEffectDirectly(8256, admin, player, (10 * 1000));
                    }
                }
                log.info("[eventai-stop{all}] GM : " + admin.getName() + " paralyzed everyone in mapId '" + admin.getWorldId() + "'");

            }else{

            VisibleObject target = admin.getTarget();
            VisibleObject targetsTarget = target.getTarget();

            if (target == null || !(target instanceof Creature)) {
                PacketSendUtility.sendMessage(admin, "You must select a target!");
                return;
            }

            SkillEngine.getInstance().applyEffectDirectly(8256, admin, (Creature) target, (10 * 1000));
            SkillEngine.getInstance().applyEffectDirectly(8256, admin, (Creature) targetsTarget, (10 * 1000));
            log.info("[eventai-stop{target}] GM : " + admin.getName() + " paralyzed both [" + target.getName() + "][" + targetsTarget.getName() +"] in mapId '" + admin.getWorldId() + "'");
            }
        }else if(params[0].equalsIgnoreCase("returnall_inzone")){
            Iterator<Player> ita = World.getInstance().getPlayersIterator();

            while(ita.hasNext()){
                Player player = ita.next();

                if(admin.getWorldId() == player.getWorldId() && player.getAccessLevel() < 1 && GeoService.getInstance().canSee(admin,player)){
                    TeleportService2.moveToBindLocation(player, true);
                }

            }
            log.info("[eventai-returnall_inzone] GM : " + admin.getName() + " returned everyone in mapId '" + admin.getWorldId() + "'");
        }else if(params[0].equalsIgnoreCase("port2jumping")){
            Player player = World.getInstance().findPlayer(Util.convertName(params[1]));

            if (player == null) {
                PacketSendUtility.sendMessage(player, "The specified player is not online.");
                return;
            }

            TeleportService2.teleportTo(player, 300260000, 468.792f, 423.079f, 233.494f);
            PacketSendUtility.sendMessage(admin, "Player : "+player.getName()+" Ported to Jumping Instance StartPoint!");

            log.info("[eventai-port2jumping] GM : " + admin.getName() + " tried to port2jumping player [" + player.getName() + "] mapId '" + admin.getWorldId() + "'");
        }else if(params[0].equalsIgnoreCase("test")){


        }
        else{
            onFail(admin,null);

        }
    }

    public void onFail(Player admin, String Msg){
        PacketSendUtility.sendMessage(admin, "=====EVENT AI====\n//eventai [type]\n" +
                "[type1] : rewardall_inzone - Gives reward to all in map\n" +
                "[type2] : movetomeall_inzone - Moves everyone in map to you\n" +
                "[type3] : resall_inzone - Ressurect all in map\n" +
                "[type4] : announce_inzone  - Announce to all in map Only\n" +
                "[type5] : announce_all - Announce to all in Server\n" +
                "[type6] : returnall_inzone - Moves Everyone back to their BindPoints\n" +
                "[type7] : stop - Stops Rage Players :D ( Extra parameters are  //eventai stop < all | 0 >\n" +
                "[type8] : port2jumping - Moves a player to jumping instance ( //eventai port2jumping [playername]\n" +
                "[type9] : rewardall_queue - rewards everyone that was queued on your event! //eventai rewardall_queue gp 2000\n" +
                "[type10] : resall_inzone - ressurects everyone in the map! //eventai resall_inzone\n");
        PacketSendUtility.sendMessage(admin, "\n[Type1 Info]\n[info] You need to type the reward type you're giving, GP or AP and after that you need to type the value amount\n" +
                "[type1 example] //eventai rewardall_inzone gp 100\n" +
                "[type7 example] //eventai stop 0 <--- will paralyze the player you're targeting and the targets target.\n" +
                "[type7 example] //eventai stop all <--- will paralyze everyone in the map!\n" +
                "[type4 and type5 info]\n" +
                "[info] You need to give the announce type and the message. \n" +
                "[example] //eventai announce_inzone 1-on-1 This will be the message.");
        PacketSendUtility.sendMessage(admin, "==== New ===\n" +
                "[type8] //eventai reward_range [TS] [Omega] [GP] [TOLL] [Range]\n " +
                "[type8] [TS] = Value of Tempering Solution (DONT NEED ID JUST HOW MANY U WANT TO GIVE)\n" +
                "[type8] [Omega] = Value of Omega Stone\n" +
                "[type8] [GP] = how many GP u want to give\n" +
                "[type8] [TOLL] = Number of Toll.\n" +
                "[type8] [Range] = The range the people gets the reward");
    }
}