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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RESURRECT;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Kill3r
 */
public class EventCaller extends AdminCommand {

    public EventCaller(){
        super("eventcaller");
    }

    private static final Logger log = LoggerFactory.getLogger("GM_MONITOR_LOG");

    public void execute(Player player, String...params){
        if(params.length == 0){
            onFail(player, "" +
                    "--Syntax--" +
                    "\n//eventcaller show - shows registered players." +
                    "\n//eventcaller start - starts the event to calling to players." +
                    "\n//eventcaller start (number of players want) - if u want a limit write the number there. or just leave it blank for no limit" +
                    "\n//eventcaller stop - stops/ends the registeration queue and ports the registered players to you." +
                    "\n//eventcaller cancel - cancels an on-going registration , without porting the players.");
            return;
        }

        if(params[0].equals("show")){
            int count = 0;
            PacketSendUtility.sendMessage(player, "\nRegistered Players for the following Event!");
            PacketSendUtility.sendMessage(player, "==================================");
              Collection<Player> players = World.getInstance().getAllPlayers();

            for (Player p : players){
                if(p.isRegedEvent()){
                    PacketSendUtility.sendMessage(player, "# " + p.getName() + " - " + p.getRace() + " - " + p.getPlayerClass());
                    count = count + 1;
                }
            }
            PacketSendUtility.sendMessage(player, "=================( " + count + " )===============");
        }else if(params[0].equals("start")){
            if (params.length == 1) { //eventcaller start
                player.setEventStarted(true);
                player.setCountPlayers(500);
                Iterator<Player> iter = World.getInstance().getPlayersIterator();
                while(iter.hasNext()){
                    Player p1 = iter.next();
                    PacketSendUtility.sendSys3Message(p1, player.getName(), "[EVENT] Registering for Event has Started! Type .queue to register to the event!");
                }
                log.info("[eventcaller-start{unlim]] GM : " + player.getName() + " started an Unlimited Slot Event in mapId '" + player.getWorldId() + "'");
            }

            if (params.length == 2) {//eventcaller start {number of player}
                int countP = Integer.parseInt(params[1]);
                player.setEventStarted(true);
                player.setCountPlayers(countP);

                Iterator<Player> ita = World.getInstance().getPlayersIterator();
                while(ita.hasNext()){
                    Player p1 = ita.next();
                    PacketSendUtility.sendSys3Message(p1, player.getName(), "[EVENT] Registering for Event has Started! Type .queue to register to the event! ( '"+countP+"' Slots Available)");
                }
                log.info("[eventcaller-start{limited]] GM : " + player.getName() + " started an limited Slot of [" + countP + "] Event in mapId '" + player.getWorldId() + "'");
            }
        }else if(params[0].equals("stop")){
            AdminCommand test = new MoveToMe();
            int count = 0;
            player.setEventStarted(false);
            Iterator<Player> iter = World.getInstance().getPlayersIterator();
            while(iter.hasNext()){
                Player p1 = iter.next();

                if(p1.isRegedEvent()){
                    if(p1.getLifeStats().isAlreadyDead()){
                        p1.setPlayerResActivate(true);
                        PacketSendUtility.sendPacket(p1, new SM_RESURRECT(player));
                        PlayerReviveService.skillRevive(p1);
                    }
                    test.execute(player, p1.getName());
                    PacketSendUtility.sendMessage(player, "Player : "+p1.getName()+" has been ported and added to reward list!");
                    player.setQueuedPlayers(p1);
                    count = count + 1;
                }
                PacketSendUtility.sendSys3Message(p1, player.getName(), "[EVENT] Event is Closed! Better luck next time!!");
                p1.setRegedEvent(false);
            }
            log.info("[eventcaller-stop{ported}] GM : " + player.getName() + " stopped the queue process and ported total of [" + count + "] players  in mapId '" + player.getWorldId() + "'");
            PacketSendUtility.sendMessage(player, "All the players have been added to you're queue list for rewarding!");
            PacketSendUtility.sendMessage(player, "=========================\n" +
                    "Total Players : " +count+ "\n" +
                    "=========================");
            //reseting all configs
            count = 0;
            player.setCountPlayers(0);
        }else if (params[0].equals("cancel")){
            player.setEventStarted(false);
            Iterator<Player> ita = World.getInstance().getPlayersIterator();
            while(ita.hasNext()){
                Player p1 = ita.next();

                if(p1.isRegedEvent()){
                    PacketSendUtility.sendMessage(player, "Player : " + p1.getName() + " removed from Event Registration!");
                }
                p1.setRegedEvent(false);
                PacketSendUtility.sendSys3Message(p1, player.getName(), "[EVENT] Event has been Canceled!");
            }
            log.info("[eventcaller-cancel] GM : " + player.getName() + " event has been canceled for some reasons in mapId '" + player.getWorldId() + "'");
            player.setCountPlayers(0);
        }
    }
}