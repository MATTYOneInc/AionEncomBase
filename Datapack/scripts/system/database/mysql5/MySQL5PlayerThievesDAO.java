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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerThievesListDAO;
import com.aionemu.gameserver.services.events.thievesguildservice.ThievesStatusList;
import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;

/**
 * @author Dision
 */
public class MySQL5PlayerThievesDAO extends PlayerThievesListDAO {
	
	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerThievesDAO.class);
	private static final String UPDATE_THIEVES_QUERY = "UPDATE player_thieves SET rank=?, thieves_count=?, prison_count=?, last_kinah=?, revenge_name=?, revenge_count=?, revenge_date=? WHERE player_id=?";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ThievesStatusList loadThieves(int playerId) {
		ThievesStatusList thieves = null;
		PreparedStatement st = DB.prepareStatement("SELECT * FROM player_thieves WHERE `player_id`=?");
		
		try {
			st.setInt(1, playerId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				thieves = new ThievesStatusList();
				thieves.setPlayerId(playerId);
				thieves.setRankId(rs.getInt("rank"));
				thieves.setThievesCount(rs.getInt("thieves_count"));
				thieves.setPrisonCount(rs.getInt("prison_count"));
				thieves.setLastThievesKinah(rs.getLong("last_kinah"));
				thieves.setRevengeName(rs.getString("revenge_name"));
				thieves.setRevengeCount(rs.getInt("revenge_count"));
				thieves.setRevengeDate(rs.getTimestamp("revenge_date"));
			}
		}
		catch (Exception e) {
			log.error("Error in MySQL5PlayerThievesDAO.loadThieves, playerId: "+playerId, e);
		}
		finally {
			DB.close(st);
		}
		return thieves;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveNewThieves(ThievesStatusList thieves) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO player_thieves (`player_id`, rank, thieves_count, prison_count, last_kinah, `revenge_name`, revenge_count, revenge_date) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, thieves.getPlayerId()); // Default
			preparedStatement.setInt(2, thieves.getRankId());
			preparedStatement.setInt(3, thieves.getThievesCount());
			preparedStatement.setInt(4, thieves.getPrisonCount());
			preparedStatement.setLong(5, thieves.getLastThievesKinah());
			preparedStatement.setString(6, thieves.getRevengeName());
			preparedStatement.setInt(7, thieves.getRevengeCount());
			preparedStatement.setTimestamp(8, thieves.getRevengeDate());
			preparedStatement.execute();
			preparedStatement.close();
		}
		catch (Exception e) {
			log.error("Error in MySQL5PlayerThievesDAO.saveNewThieves", e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void storeThieves(final ThievesStatusList thieves) {
		DB.insertUpdate(UPDATE_THIEVES_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, thieves.getRankId());
				stmt.setInt(2, thieves.getThievesCount());
				stmt.setInt(3, thieves.getPrisonCount());
				stmt.setLong(4, thieves.getLastThievesKinah());
				stmt.setString(5, thieves.getRevengeName());
				stmt.setInt(6, thieves.getRevengeCount());
				stmt.setString(7, thieves.getRevengeName());
				stmt.setTimestamp(8, thieves.getRevengeDate());
				stmt.setInt(9, thieves.getPlayerId()); // Default
				stmt.execute();
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String database, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
	}
}