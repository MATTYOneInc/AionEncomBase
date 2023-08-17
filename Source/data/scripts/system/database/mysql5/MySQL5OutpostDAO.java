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
package mysql5;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.*;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.outpost.OutpostLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wnkrz on 27/08/2017.
 */

public class MySQL5OutpostDAO extends OutpostDAO
{
    private static final Logger log = LoggerFactory.getLogger(MySQL5OutpostDAO.class);
	
    public static final String SELECT_QUERY = "SELECT * FROM `outpost_location`";
    public static final String UPDATE_QUERY = "UPDATE `outpost_location` SET `race` = ? WHERE `id` = ?";
    public static final String INSERT_QUERY = "INSERT INTO `outpost_location` (`id`, `race`) VALUES(?, ?)";
	
    @Override
    public boolean loadOutposLocations(Map<Integer, OutpostLocation> locations) {
        boolean success = true;
        Connection con = null;
        List<Integer> loaded = new ArrayList<Integer>();
        PreparedStatement stmt = null;
        try {
            con = DatabaseFactory.getConnection();
            stmt = con.prepareStatement(SELECT_QUERY);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                OutpostLocation loc = locations.get(resultSet.getInt("id"));
                loc.setRace(Race.valueOf(resultSet.getString("race")));
                loaded.add(loc.getId());
            }
            resultSet.close();
        } catch (Exception e) {
            log.warn("Error loading Outpost informaiton from database: " + e.getMessage(), e);
            success = false;
        } finally {
            DatabaseFactory.close(stmt, con);
        } for (Map.Entry<Integer, OutpostLocation> entry : locations.entrySet()) {
            OutpostLocation sLoc = entry.getValue();
            if (!loaded.contains(sLoc.getId())) {
                insertOutpostLocation(sLoc);
            }
        }
        return success;
    }
	
    @Override
    public boolean updateOutpostLocation(OutpostLocation location) {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = DatabaseFactory.getConnection();
            stmt = con.prepareStatement(UPDATE_QUERY);
            stmt.setString(1, location.getRace().toString());
            stmt.setInt(2, location.getId());
            stmt.execute();
        } catch (Exception e) {
            log.error("Error update Outpost Location: " + "id: " + location.getId() );
            return false;
        } finally {
            DatabaseFactory.close(stmt, con);
        }
        return true;
    }
	
    private boolean insertOutpostLocation(final OutpostLocation locations) {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = DatabaseFactory.getConnection();
            stmt = con.prepareStatement(INSERT_QUERY);
            stmt.setInt(1, locations.getId());
            stmt.setString(2, Race.NPC.toString());
            stmt.execute();
        } catch (Exception e) {
            log.error("Error insert Outpost Location: " + locations.getId(), e);
            return false;
        } finally {
            DatabaseFactory.close(stmt, con);
        }
        return true;
    }
	
    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }
}