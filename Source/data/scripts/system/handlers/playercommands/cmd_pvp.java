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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;

import java.util.Calendar;

/**
 * Created by Ghostfur/Nimwey
 */
public class cmd_pvp extends PlayerCommand {

    public cmd_pvp() {
        super("pvp");
    }

    public void execute(Player player, String...param){

        if (player.isAttackMode()){
            PacketSendUtility.sendMessage(player, "You cannot Go to Insane PvP while in Attack Mode!");
            return;
        }

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            monPvP(player);
            givePvPWelcomeMsg(player, "monPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            wedPvP(player);
            givePvPWelcomeMsg(player, "wedPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            monPvP(player);
            givePvPWelcomeMsg(player, "monPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            wedPvP(player);
            givePvPWelcomeMsg(player, "wedPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            monPvP(player);
            givePvPWelcomeMsg(player, "monPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            wedPvP(player);
            givePvPWelcomeMsg(player, "wedPvP");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            monPvP(player);
            givePvPWelcomeMsg(player, "monPvP");
        }
    }
		
	  private void monPvP(Player player){
        checkotherEvents(player);
        if(player.getWorldId() == 600050000){ //Beluslan
            PacketSendUtility.sendMessage(player, "You cannot use the command inside the PvP Map!");
            return;
        }
        if (player.getRace() == Race.ASMODIANS && !player.isInPrison()) {
            goTo(player, WorldMapType.KATALAM.getId(), 322.85242f, 387.63394f, 274.15488f);
        } else if (player.getRace() == Race.ELYOS && !player.isInPrison()) {
            goTo(player, WorldMapType.KATALAM.getId(), 450.45847f, 2628.0674f, 146.0f);
        }
    } 				

	
    private void wedPvP(Player player){
        checkotherEvents(player);
        if (player.getRace() == Race.ASMODIANS  && player.getWorldId() != 210040000 && !player.isInPrison()) { //Heiron
            goTo(player, WorldMapType.HEIRON.getId(), 1933.721f, 2482.6672f, 311.38864f);
        } else if (player.getRace() == Race.ELYOS && player.getWorldId() != 210040000 && !player.isInPrison()) {
            goTo(player, WorldMapType.HEIRON.getId(), 1262.2222f, 2283.0647f, 239.9606f);
        }
    }					

    private void checkotherEvents(Player player){
        if (player.isAttackMode()) {
            PacketSendUtility.sendMessage(player, "You can not use this command during the fight!");
            return;
        }
	}
    private static void goTo(final Player player, int worldId, float x, float y, float z) {
        WorldMap destinationMap = World.getInstance().getWorldMap(worldId);
        if (destinationMap.isInstanceType()) {
            TeleportService2.teleportTo(player, worldId, getInstanceId(worldId, player), x, y, z);
        } else {
            TeleportService2.teleportTo(player, worldId, x, y, z);
        }
    }

    private static int getInstanceId(int worldId, Player player) {
        if (player.getWorldId() == worldId) {
            WorldMapInstance registeredInstance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
            if (registeredInstance != null) {
                return registeredInstance.getInstanceId();
            }
        }
        WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(worldId);
        InstanceService.registerPlayerWithInstance(newInstance, player);
        return newInstance.getInstanceId();
    }

    private void givePvPWelcomeMsg(Player player, String PvPMap){
        String msg = "";
        if(PvPMap.equalsIgnoreCase("monPvP")){
            if(player.getWorldId() == 600050000){
                return;
            }
        }else if(PvPMap.equalsIgnoreCase("tuePvP")){
            if(player.getWorldId() == 210040000){
                return;
            }
        }else if(PvPMap.equalsIgnoreCase("wedPvP")){
            if(player.getWorldId() == 600050000){
                return;
            }
        }else if(PvPMap.equalsIgnoreCase("monPvP")){
            if(player.getWorldId() == 210040000){
                return;
            }
        }else if(PvPMap.equalsIgnoreCase("tuePvP")) {
            if (player.getWorldId() == 600050000) {
                return;
            }
	    }else if(PvPMap.equalsIgnoreCase("wedPvP")) {
            if (player.getWorldId() == 210040000) {
                return;
            }
        }
		 
        if(player.getRace() == Race.ASMODIANS){
            msg = "all the ELYOS :]";
        }else if(player.getRace() == Race.ELYOS){
            msg = "all the ASMODIANS :]";
        }
		PacketSendUtility.sendSys3Message(player, "\uE059", "[PvP Zone] Welcome to the PvP Zone!!");
        PacketSendUtility.sendYellowMessage(player, "\n[PvP Rules]" +
                "\n # No Camping at Spawn Area" +
                "\n # No Hacking" +
                "\n # No Bug Abusing" +
                "\n # And as always remember to kill "+ msg);
    }
}