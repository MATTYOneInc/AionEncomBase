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

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Kill3r
 */
public class Repairkit extends AdminCommand {

    public Repairkit(){
        super("repairkit");
    }

    private Logger log = LoggerFactory.getLogger("GM_MONITOR_LOG");

    public void execute(Player admin, String...params){
        if(params.length == 0){
            PacketSendUtility.sendMessage(admin, "Syntax\n" +
                    " //repairkit [operation] [playername] [playerRace | itemID]\n" +
                    "##### NOTE ####\n" +
                    "# You will need playerRace to use the 'RETURN' Operation.\n" +
                    "# You will need itemID to use the 'REMOVE' Operation.\n" +
                    "# NEVER USE @LINK OF ITEMS.. ONLY ITEM ID!!!!.\n" +
                    "# PLEASE STRICTLY FOLLOW THESE RULES , BECAUSE IF WRITTEN WRONG, THIS CAN CAUSE ALOT OF DATABASE LOSE!!!!!.\n" +
                    "# Player Name Also MUST be used as Exactly case sensitive, (example: Extreme is correct , and extreme is wrong... First letter should be capital)" +
                    "#### NOTE END ####\n " +
                    "# [operation] : return | remove | terminate | wipe\n" +
                    "# [playername] : name of the player to initialize the operation\n" +
                    "# [playerRace | itemID] : race should be given in correct spelling and case ( ELYOS | ASMODIANS ) case Sensitive!! OR use itemID if you want to remove an Item from the Player!!(NO LINKS ALLOWED, ONLY ITEM ID!!)\n" +
                    "#######################");
            return;
        }

        if(params[0].equalsIgnoreCase("return")){
            String playerToreturn = params[1];
            String raceOfPlayer = params[2];

            returnPlayer(admin, playerToreturn, raceOfPlayer.toUpperCase());
        }else if (params[0].equalsIgnoreCase("remove")){
            String playerToRemove = params[1];
            int itemId = Integer.parseInt(params[2]);

            removeItemByID(admin, playerToRemove, itemId);
        } else if (params[0].equalsIgnoreCase("terminate")){
            if(admin.getAccessLevel() <= 3){
                PacketSendUtility.sendMessage(admin, "You are not Insane Enough to use this Command! ;)");
                return;
            }
            int itemId = Integer.parseInt(params[1]);

            removeItemFromDatabase(admin, itemId);
        } else if (params[0].equalsIgnoreCase("wipe")){
            if(admin.getAccessLevel() <= 3){
                PacketSendUtility.sendMessage(admin, "You are not Insane Enough to use this Command! ;)");
                return;
            }
            String playerToWipe = params[1];

            wipeInventoryExceptEquiped(admin, playerToWipe);
        }

    }

    private void removeItemFromDatabase(final Player admin, final int itemId){
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM inventory WHERE item_id=?");
            stmt.setInt(1, itemId);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("ERROR while deleting ALL item through Repairkit Command!!");
        } finally {
            DatabaseFactory.close(con);
        }
        PacketSendUtility.sendMessage(admin, "[item:"+itemId+"] ("+itemId+") has been successfully removed from all players!");
    }

    private void wipeInventoryExceptEquiped(final Player admin, final String playerToWipe){
        Connection con = null;
        Player checkPOnline = World.getInstance().findPlayer(playerToWipe);
        if(checkPOnline != null){
            PacketSendUtility.sendMessage(admin, "You cannot proceed if the player is Online!");
            return;
        }
        if (playerToWipe == admin.getName()){
            PacketSendUtility.sendMessage(admin, "Noob! you can't remove item from you're self!");
            return;
        }

        int playerID = 0;

        try {
            playerID = DAOManager.getDAO(PlayerDAO.class).getPlayerIdByName(playerToWipe);
        } catch (Exception e) {
            log.info("[repairkit-wipe] GM : [" + admin.getName() + "] couldn't Find Name in Database ( RepairKIT )");
        }

        if (playerID == 0){
            PacketSendUtility.sendMessage(admin, "Couldn't find that name in the database!");
            return;
        }

        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM inventory WHERE item_owner=? AND is_equiped = 0");
            stmt.setInt(1, playerID);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("[repairkit-wipe] GM : [" + admin.getName() + "] Error While wiping item inventory through RepairKit Command!");
        } finally {
            DatabaseFactory.close(con);
        }
        PacketSendUtility.sendMessage(admin, "All items from the player has been removed except the Equiped ones!");
        log.error("[repairkit-wipe] GM : [" + admin.getName() + "] succesfully wiped the player's Inventory of [" + playerToWipe + "]");
    }

    private void removeItemByID(final Player admin, final String playerToRemoveFrom, final int itemID){
        Connection con = null;
        Player checkPlayerOnline = World.getInstance().findPlayer(playerToRemoveFrom);
        if(checkPlayerOnline != null){
            PacketSendUtility.sendMessage(admin, "You can only remove items from Offline Player using this Command, If you want to remove item from Online Players, use '//remove'!!");
            return;
        }
        if (playerToRemoveFrom == admin.getName()){
            PacketSendUtility.sendMessage(admin, "Noob! you can't remove item from you're self!");
            return;
        }

        int playerID = 0;

        try {
            playerID = DAOManager.getDAO(PlayerDAO.class).getPlayerIdByName(playerToRemoveFrom);
        } catch (Exception e) {
            log.info("[repairkit-removeItem] GM : [" + admin.getName() + "] Couldn't Find Name in Database ( RepairKIT )");
        }

        if (playerID == 0){
            PacketSendUtility.sendMessage(admin, "Couldn't find that name in the database!");
            return;
        }

        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM inventory WHERE item_id=? AND item_owner=?");
            stmt.setInt(1, itemID);
            stmt.setInt(2, playerID);
            stmt.execute();
            stmt.close();

        } catch (Exception e){
            log.error("[repairkit-removeItem] GM : [" + admin.getName() + "] ERROR while deleting item through Repairkit Command!!");
        } finally {
            DatabaseFactory.close(con);
        }
        PacketSendUtility.sendMessage(admin, "[item:"+itemID+"] ("+itemID+") successfully removed from '" + playerToRemoveFrom + "'");
        log.error("[repairkit-wipe] GM : [" + admin.getName() + "] succesfully removed an Item [" + itemID + "] from player [" + playerToRemoveFrom + "]");
    }

    private void returnPlayer(final Player admin, final String playerToReturn, final String race){
        Connection con = null;
        Player checkPlayerOnline = World.getInstance().findPlayer(Util.convertName(playerToReturn));
        if(checkPlayerOnline != null){
            PacketSendUtility.sendMessage(admin, "You cannot return a player while he's Online!");
            return;
        }
        if (playerToReturn == admin.getName()){
            PacketSendUtility.sendMessage(admin, "Noob! you can't return you're self!");
            return;
        }

        float x,y,z;
        int heading,world_id;

        if(race.equalsIgnoreCase("ELYOS") || race.equalsIgnoreCase("ASMODIANS")){
            PacketSendUtility.sendMessage(admin, "GIVEN RACE : CORRECT!");
        }else{
            PacketSendUtility.sendMessage(admin, "The Given RACE is not correct!.. Please use 'ELYOS' or 'ASMODIANS'");
            return;
        }

        if (race.equalsIgnoreCase("ELYOS")){
            x = 1443.6982f;
            y = 1574.4116f;
            z = 572.87537f;
            world_id = 110010000;
            heading = 0;
        }else if (race.equalsIgnoreCase("ASMODIANS")){
            x = 1316.4557f;
            y = 1425.6559f;
            z = 209.09084f;
            world_id = 120010000;
            heading = 65;
        } else {
            x = admin.getX();
            y = admin.getY();
            z = admin.getZ();
            world_id = admin.getWorldId();
            heading = admin.getHeading();
        }

        int playerID = 0;

        try {
            playerID = DAOManager.getDAO(PlayerDAO.class).getPlayerIdByName(playerToReturn);
        } catch (Exception e) {
            log.info("[repairkit-return] GM : [" + admin.getName() + "] Couldn't Find Name in Database ( RepairKIT )");
        }

        if (playerID == 0){
            PacketSendUtility.sendMessage(admin, "Couldn't find that name in the database!");
            return;
        }

        try{
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE players SET x=?, y=?, z=?, heading=?, world_id=? WHERE id=?");

            stmt.setFloat(1, x);
            stmt.setFloat(2, y);
            stmt.setFloat(3, z);
            stmt.setInt(4, heading);
            stmt.setInt(5, world_id);
            stmt.setInt(6, playerID);
            stmt.execute();
            stmt.close();

        } catch (Exception e) {
            log.error("[repairkit-return] GM : [" + admin.getName() + "] Error Returning Player through Command!");
        } finally {
            DatabaseFactory.close(con);
        }
        PacketSendUtility.sendMessage(admin, playerToReturn+" has been successfully moved to main city of '"+race+"'.");
        log.error("[repairkit-return] GM : [" + admin.getName() + "] returned the player [" + playerToReturn + "] to main city of [" + race + "]");
    }

    public void onFail(Player player, String msg){
        // TODO Auto-Generated Message
    }
}