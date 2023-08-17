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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Created by Kill3r
 */
public class Outlaw extends AdminCommand {
    public Outlaw(){
        super("outlaw");
    }

    private static final Logger log = LoggerFactory.getLogger("GM_MONITOR_LOG");

    public void execute(final Player admin, String...param){
        //Fixed opposite
        if(param.length == 0){
            onFail(admin, "== SYNAX ==\n" +
                    "//Outlaw attackable 0 - changes current target to attackable (do same to turn off/on)\n" +
                    "//Outlaw neutral 0 - changes current target to neutral (do same to turn off/on)\n" +
                    "//Outlaw attackable all - changes everyone in the current map to attackable\n" +
                    "//Outlaw neutral all - changes everyone in the current map to neutral\n" +
                    "//Outlaw attackable cancel - turns the attackable state off from everyone in map (NO SKULL)\n" +
                    "//Outlaw neutral cancel - turns the neutral mode off from everyone in map(NO SHIELD)\n" +
                    "//Outlaw clear - removes both attackable and neutral mode from everyone in map.");
            return;
        }
        VisibleObject visibleObject = admin.getTarget();

        if(visibleObject == null || !(visibleObject instanceof Player)){
            PacketSendUtility.sendMessage(admin, "You need to target a player!");
            return;
        }
        final Player target = (Player) visibleObject;

        if(param[0].equalsIgnoreCase("attackable")){


            if(param[1].equalsIgnoreCase("all")){
                Iterator<Player> ita = World.getInstance().getPlayersIterator();

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId()){
                        player.setInPkMode(true);
                        refresh(player);
                        PacketSendUtility.sendMessage(player , "[Outlaw] : You've been changed to \"[color:Atta;1 0 0][color:ckab;1 0 0][color:le;1 0 0]\" Mode!");
                    }
                }
                PacketSendUtility.sendMessage(admin, "[Outlaw] : All players in map has been changed to \"[color:Atta;1 0 0][color:ckab;1 0 0][color:le;1 0 0]\" !");
                log.info("[Outlaw-attackable-all] GM : " + admin.getName() + " has changed all in map to attackable in mapId '" + admin.getWorldId() + "'");
            }else if(param[1].equalsIgnoreCase("cancel")){
                Iterator<Player> ita = World.getInstance().getPlayersIterator();

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId()){
                        player.setInPkMode(false);
                        refresh(player);
                        PacketSendUtility.sendMessage(player, "[Outlaw] : You're now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    }
                }
                PacketSendUtility.sendMessage(admin, "[Outlaw] : All players in map has been changed to \"[color:Norm;1 1 1][color:al;1 1 1]\" !");
                log.info("[Outlaw-attackable-cancel] GM : " + admin.getName() + " has changed all in map to Normal in mapId '" + admin.getWorldId() + "'");
            }else{
                if(!target.isInPkMode()){
                    target.setInPvEMode(false);
                    target.setInPkMode(true);
                    refresh(target);
                    PacketSendUtility.sendMessage(admin, "[Outlaw] : Player " + target.getName() + " is now in \"[color:Atta;1 0 0][color:ckab;1 0 0][color:le;1 0 0]\" Mode!");
                    PacketSendUtility.sendMessage(target, "[Outlaw] : You've been changed to \"[color:Atta;1 0 0][color:ckab;1 0 0][color:le;1 0 0]\" Mode!");
                    log.info("[Outlaw-attackable] GM : " + admin.getName() + " has changed the player [" + target.getName() + "] to attackable in mapId '" + admin.getWorldId() + "'");
                }else{
                    target.setInPkMode(false);
                    refresh(target);
                    PacketSendUtility.sendMessage(admin, "[Outlaw] : Player " + target.getName() + " is now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    PacketSendUtility.sendMessage(target, "[Outlaw] : You're now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    log.info("[Outlaw-attackable] GM : " + admin.getName() + " has changed the player [" + target.getName() + "] to normal in mapId '" + admin.getWorldId() + "'");
                }
            }


        }else if(param[0].equalsIgnoreCase("neutral")){

            if(param[1].equalsIgnoreCase("all")){
                Iterator<Player> ita = World.getInstance().getPlayersIterator();

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId()){
                        player.setInPvEMode(true);
                        refresh(player);
                        PacketSendUtility.sendMessage(player, "[Outlaw] : You're now in \"[color:Neut;0 1 0][color:ral;0 1 0]\" Mode!");
                    }
                }
                PacketSendUtility.sendMessage(admin, "[Outlaw] : All players in map has been changed to \"[color:Neut;0 1 0][color:ral;0 1 0]\" Mode!");
                log.info("[Outlaw-neutral-all] GM : " + admin.getName() + " has changed all in map to Neutral in mapId '" + admin.getWorldId() + "'");
            }else if(param[1].equalsIgnoreCase("cancel")){
                Iterator<Player> ita = World.getInstance().getPlayersIterator();

                while(ita.hasNext()){
                    Player player = ita.next();
                    if(player.getWorldId() == admin.getWorldId()){
                        player.setInPvEMode(false);
                        refresh(player);
                        PacketSendUtility.sendMessage(player, "[Outlaw] : You're now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    }
                }
                PacketSendUtility.sendMessage(admin, "[Outlaw] : All players in map has been changed to \"[color:Norm;1 1 1][color:al;1 1 1]\" !");
                log.info("[Outlaw-neutral-cancel] GM : " + admin.getName() + " has changed all in map to Normal in mapId '" + admin.getWorldId() + "'");
            }else{
                if(!target.isInPvEMode()){
                    target.setInPkMode(false);
                    target.setInPvEMode(true);
                    refresh(target);
                    PacketSendUtility.sendMessage(admin, "[Outlaw] : Player " + target.getName() + " is now in \"[color:Neut;0 1 0][color:ral;0 1 0]\" Mode!");
                    PacketSendUtility.sendMessage(target, "[Outlaw] : You've been changed to \"[color:Neut;0 1 0][color:ral;0 1 0]\" Mode!");
                    log.info("[Outlaw-neutral] GM : " + admin.getName() + " has changed the player [" + target.getName() + "] to Neutral in mapId '" + admin.getWorldId() + "'");
                }else{
                    target.setInPvEMode(false);
                    refresh(target);
                    PacketSendUtility.sendMessage(admin, "[Outlaw] : Player " + target.getName() + " is now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    PacketSendUtility.sendMessage(target, "[Outlaw] : You're now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                    log.info("[Outlaw-neutral] GM : " + admin.getName() + " has changed the player [" + target.getName() + "] to Normal in mapId '" + admin.getWorldId() + "'");
                }
            }

        }else if(param[0].equalsIgnoreCase("clear")){
            Iterator<Player> ita = World.getInstance().getPlayersIterator();

            while(ita.hasNext()){
                Player player = ita.next();
                if(player.getWorldId() == admin.getWorldId()){
                    player.setInPvEMode(false);
                    player.setInPkMode(false);
                    refresh(player);
                    PacketSendUtility.sendMessage(player, "[Outlaw] : You're now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
                }
            }
            PacketSendUtility.sendMessage(admin, "[Outlaw] : Player " + target.getName() + " is now in \"[color:Norm;1 1 1][color:al;1 1 1]\" Mode!");
            log.info("[Outlaw-clear] GM : " + admin.getName() + " has changed all in map to Normal in mapId '" + admin.getWorldId() + "'");
        }
    }

    public void onFail(Player admin, String msg){
        PacketSendUtility.sendMessage(admin, "== SYNAX ==\n" +
            "//Outlaw attackable 0 - changes current target to attackable (do same to turn off/on)\n" +
            "//Outlaw neutral 0 - changes current target to neutral (do same to turn off/on)\n" +
            "//Outlaw attackable all - changes everyone in the current map to attackable\n" +
            "//Outlaw neutral all - changes everyone in the current map to neutral\n" +
            "//Outlaw attackable cancel - turns the attackable state off from everyone in map (NO SKULL)\n" +
            "//Outlaw neutral cancel - turns the neutral mode off from everyone in map(NO SHIELD)\n" +
            "//Outlaw clear - removes both attackable and neutral mode from everyone in map.");

    }

    public void refresh(Player player){
        TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(),player.getY(),player.getZ());
    }
}