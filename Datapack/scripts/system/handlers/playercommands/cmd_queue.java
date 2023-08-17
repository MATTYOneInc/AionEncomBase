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
package playercommands;

import java.util.Collection;
import java.util.Iterator;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;

/**
 * Created by Kill3r
 */
public class cmd_queue extends PlayerCommand {

    public cmd_queue(){
        super("queue");
    }

    public void execute(Player player,String...params){
        boolean anyEventfound = false;
        if(player.isRegedEvent()){
            PacketSendUtility.sendMessage(player, "You've already registered to the event!");
            return;
        }
        if(player.isInPrison()){
            PacketSendUtility.sendMessage(player, "You cant register inside Prison!");
            return;
        }
        Iterator<Player> ita = World.getInstance().getPlayersIterator();

        while(ita.hasNext()){
            Player player1 = ita.next();

            if(player1.getAccessLevel() >= 2 && player1.isEventStarted()){
                int playerCounter = player1.getCountPlayers();
                if (player1.getCountPlayers() != checkRegedPlayers()){
                    PacketSendUtility.sendMessage(player, "Found an Event! Registering to " + player1.getName() + "'s Event!");
                    anyEventfound = true;
                    if (player1.getCountPlayers() != 500){
                        checkPosition(player1, player, playerCounter);
                    }
                }else{
                    PacketSendUtility.sendMessage(player, "Sorry all slots are taken now! Better luck next time <3");
                }
            }
        }

        // Find a way to fix the problem when u type .queue when the gm has enabled the registration for unlimited players..
        // since the getCountplayers are set to a value when .queue is working.. and it ends when its 0.. and by default its by 0..

        if(anyEventfound == true){
            PacketSendUtility.sendMessage(player, "You've registered to the upcoming event!");
            player.setRegedEvent(true);
        }else{
            PacketSendUtility.sendMessage(player, "Currently there are no event running!");
        }
    }

    private void checkPosition(Player admin, Player oneWhoExecute, int countPosition){
        int count = admin.getCountPlayers();
        count = count - checkRegedPlayers();

        PacketSendUtility.sendMessage(oneWhoExecute, "There are " + count + " slots remaining!");
    }

    private int checkRegedPlayers(){
        int count = 0;
        Collection<Player> players = World.getInstance().getAllPlayers();
        for(Player p : players){
            if(p.isRegedEvent()){
                count = count + 1;
            }
        }
        return count;
    }
}