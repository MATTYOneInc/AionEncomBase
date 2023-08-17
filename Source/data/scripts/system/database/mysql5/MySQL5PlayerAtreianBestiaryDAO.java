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
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerABDAO;
import com.aionemu.gameserver.model.atreian_bestiary.PlayerABEntry;
import com.aionemu.gameserver.model.atreian_bestiary.PlayerABList;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranastic
 */

public class MySQL5PlayerAtreianBestiaryDAO extends PlayerABDAO
{
	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerAtreianBestiaryDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_atreian_bestiary` (`player_id`, `id`, `kill_count`, `level`, `claim_reward`) VALUES (?,?,?,?,?)";
	public static final String INSERT_OR_UPDATE = "INSERT INTO `player_atreian_bestiary` (`player_id`, `id`, `kill_count`, `level`, `claim_reward`) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE `id` = VALUES(`id`), `kill_count` = VALUES(`kill_count`), `level` = VALUES(`level`), `claim_reward` = VALUES(`claim_reward`)";
	public static final String UPDATE_QUERY = "UPDATE `player_atreian_bestiary` set kill_count=?, level=?, claim_reward=? where player_id=? AND id=?";
	public static final String SELECT_QUERY = "SELECT `id`,`kill_count`,`level`,`claim_reward` FROM `player_atreian_bestiary` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_atreian_bestiary` WHERE `player_id`=? AND `id`=?";
	
	@Override
	public PlayerABList load(Player player) {
		List<PlayerABEntry> cp = new ArrayList<PlayerABEntry>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("id");
				int kill_count = rset.getInt("kill_count");
				int level = rset.getInt("level");
				int claimReward = rset.getInt("claim_reward");
				cp.add(new PlayerABEntry(id, kill_count, level, claimReward, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not restore Atreian Bestiary for playerObjId: " +player.getObjectId() + " from DB: " + e.getMessage(), e);
		} finally {
			DatabaseFactory.close(con);
		}
		return new PlayerABList(cp);
	}
	
	@Override
	public boolean store(int objectId, int id, int kill_count, int level, int claimReward) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_OR_UPDATE);
			stmt.setInt(1, objectId);
			stmt.setInt(2, id);
			stmt.setInt(3, kill_count);
			stmt.setInt(4, level);
			stmt.setInt(5, claimReward);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not store Atreian Bestiary for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	@Override
	public boolean delete(int playerObjId, int id) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerObjId);
			stmt.setInt(2, id);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not delete Atreian Bestiary for player " + playerObjId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	@Override
	public int getKillCountById(final int playerObjId, final int id) {
		Connection con = null;
		int kill_count = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `kill_count` FROM `player_atreian_bestiary` WHERE `player_id`=? AND `id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, id);
			ResultSet rs = s.executeQuery();
			rs.next();
			kill_count = rs.getInt("kill_count");
			rs.close();
			s.close();
		} catch (Exception e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return kill_count;
	}
	
	@Override
	public int getLevelById(final int playerObjId, final int id) {
		Connection con = null;
		int level = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `level` FROM `player_atreian_bestiary` WHERE `player_id`=? AND `id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, id);
			ResultSet rs = s.executeQuery();
			rs.next();
			level = rs.getInt("level");
			rs.close();
			s.close();
		} catch (Exception e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return level;
	}
	
	@Override
	public int getClaimRewardById(int playerObjId, int id) {
		Connection con = null;
		int level = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement s = con.prepareStatement("SELECT `claim_reward` FROM `player_atreian_bestiary` WHERE `player_id`=? AND `id`=?");
			s.setInt(1, playerObjId);
			s.setInt(2, id);
			ResultSet rs = s.executeQuery();
			rs.next();
			level = rs.getInt("claim_reward");
			rs.close();
			s.close();
		} catch (Exception e) {
			return 0;
		} finally {
			DatabaseFactory.close(con);
		}
		return level;
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}