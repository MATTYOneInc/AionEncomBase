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
 * Created by Ghostfur (Aion-Unique)
 */
public class cmd_siege extends PlayerCommand {

    public cmd_siege() {
        super("siege");
    }

    public void execute(Player player, String...param){

        if (player.isAttackMode()){
            PacketSendUtility.sendMessage(player, "You cannot go to siege while in attack mode!");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            monSiege(player);
            giveSiegeWelcomeMsg(player, "monSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            //tueSiege(player); Disabled Only One Siege map at moment
			 monSiege(player);
            giveSiegeWelcomeMsg(player, "tueSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            //wedSiege(player); Disabled Only One Siege map at moment
			 monSiege(player); 
            giveSiegeWelcomeMsg(player, "wedSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            //thurSiege(player); Disabled Only One Siege map at moment
			 monSiege(player);
            giveSiegeWelcomeMsg(player, "thurSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            //friSiege(player); Disabled Only One Siege map at moment
			 monSiege(player);
            giveSiegeWelcomeMsg(player, "friSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            //satSiege(player); Disabled Only One Siege map at moment
			 monSiege(player);
            giveSiegeWelcomeMsg(player, "satSiege");
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            //sunSiege(player); Disabled Only One Siege map at moment
			 monSiege(player);
            giveSiegeWelcomeMsg(player, "sunSiege");
        }
    }

    private void monSiege(Player player){
        checkotherEvents(player);
        if(player.getWorldId() == 600090000){ //Kaldor
            PacketSendUtility.sendMessage(player, "You cannot use the command inside the siege map!");
            return;
        }
        if (player.getRace() == Race.ASMODIANS && !player.isInPrison()) {
            goTo(player, WorldMapType.KALDOR.getId(), 236.66698f, 522.7742f, 202.09662f);
        } else if (player.getRace() == Race.ELYOS && !player.isInPrison()) {
            goTo(player, WorldMapType.KALDOR.getId(), 1368.6766f, 532.0484f, 276.54712f);
        }
    }
	
      /* Enable This For More Siege Maps on different days!
    private void tueSiege(Player player){
        checkotherEvents(player);
        if (player.getRace() == Race.ASMODIANS  && player.getWorldId() != 210050000 && !player.isInPrison()) { 
            goTo(player, WorldMapType.INGGISON.getId(), 882.2768f,1974.7095f,341.36612f);
        } else if (player.getRace() == Race.ELYOS && player.getWorldId() != 210040000 && !player.isInPrison()) {
            goTo(player, WorldMapType.INGGISON.getId(), 1729.6011f,2231.5864f,328.67572f);
        }
    }
	*/

    private void checkotherEvents(Player player){
        if (player.isAttackMode()) {
            PacketSendUtility.sendMessage(player, "You cannot use the command inside the Siege Map!");
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

    private void giveSiegeWelcomeMsg(Player player, String SiegeMap){
        String msg = "";
        if(SiegeMap.equalsIgnoreCase("monSiege")){
            if(player.getWorldId() == 600090000){ //Kaldor
                return;
            }
			
		  /* Enable This For More Siege Maps on different days!
        }else if(SiegeMap.equalsIgnoreCase("tueSiege")){
            if(player.getWorldId() == 600100000){
                return;
            }
        }else if(SiegeMap.equalsIgnoreCase("wedSiege")){
            if(player.getWorldId() == 210100000){
                return;
            }
        }else if(SiegeMap.equalsIgnoreCase("thuSiege")){
            if(player.getWorldId() == 220110000){
                return;
            }
        }else if(SiegeMap.equalsIgnoreCase("friSiege")) {
            if (player.getWorldId() == 600100000) {
                return;
            }
        }else if(SiegeMap.equalsIgnoreCase("satSiege")) {
            if (player.getWorldId() == 210100000) {
                return;
            }
        }else if(SiegeMap.equalsIgnoreCase("sunSiege")) {
            if (player.getWorldId() == 210100000) {
                return;
            }
			*/
        }

        if(player.getRace() == Race.ASMODIANS){
            msg = "all the ELYOS :]";
        }else if(player.getRace() == Race.ELYOS){
            msg = "all the ASMODIANS :]";
        }
        PacketSendUtility.sendSys3Message(player, "\uE04D", "[Siege Zone] Welcome to the Siege Zone!!");
        PacketSendUtility.sendYellowMessage(player, "\n[Siege Rules]" +
                "\n # No Camping at Spawn Area" +
                "\n # No Hacking" +
                "\n # No Bug Abusing" +
                "\n # And as always remember to kill "+ msg);
    }
}