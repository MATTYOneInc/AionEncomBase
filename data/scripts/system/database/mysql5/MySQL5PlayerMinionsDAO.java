/*
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;

/**
 * @author Falke_34
 */
public class MySQL5PlayerMinionsDAO extends PlayerMinionsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerMinionsDAO.class);

	@Override
	public void insertPlayerMinions(MinionCommonData minionCommonData, String name) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO player_minions(player_id, minion_id, name, birthday, growth_point, despawn_time, expire_time) VALUES(?,?,?,?,?,?,?)");
			stmt.setInt(1, minionCommonData.getMasterObjectId());
			stmt.setInt(2, minionCommonData.getMinionId());
			stmt.setString(3, name);
			stmt.setTimestamp(4, minionCommonData.getBirthdayTimestamp());
			stmt.setInt(5, minionCommonData.getGrowthPoint());
			stmt.setTimestamp(6, minionCommonData.getDespawnTime());
			stmt.setInt(7, minionCommonData.getExpireTime());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error inserting new minion #" + minionCommonData.getMinionId() + "[" + minionCommonData.getName() + "]", e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void removePlayerMinions(Player player, int minionId, String minionName) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM player_minions WHERE player_id = ? AND minion_id = ? AND name=?");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, minionId);
			stmt.setString(3, minionName);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error removing minion #" + minionId, e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public List<MinionCommonData> getPlayerMinions(Player player) {
		List<MinionCommonData> minions = new ArrayList<MinionCommonData>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_minions WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				MinionCommonData minionCommonData = new MinionCommonData(rs.getInt("minion_id"), player.getObjectId(), rs.getInt("expire_time"));
				minionCommonData.setName(rs.getString("name"));
				minionCommonData.setBirthday(rs.getTimestamp("birthday"));
				minionCommonData.setGrowthPoint(rs.getInt("growth_point"));
				Timestamp ts = null;
				try {
					ts = rs.getTimestamp("despawn_time");
				} catch (Exception e) {
				}
				if (ts == null) {
					ts = new Timestamp(System.currentTimeMillis());
				}
				if (minionCommonData.getDopingBag() != null) {
					String dopings = rs.getString("buff_item");
					if (dopings != null && dopings.length() > 6) {
						String[] ids = dopings.split(",");
						for (int i = 0; i < ids.length; i++) {
							minionCommonData.getDopingBag().setItem(Integer.parseInt(ids[i]), i);
						}
					}
				}
				minionCommonData.setDespawnTime(ts);
				minions.add(minionCommonData);
			}
			stmt.close();
		} catch (Exception e) {
			log.error("Error getting minion for " + player.getObjectId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
		return minions;
	}

	@Override
	public void updateMinionsName(MinionCommonData petCommonData, String OldName) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("UPDATE player_minions SET name = ? WHERE player_id = ? AND minion_id = ? AND name=?");
			stmt.setString(1, petCommonData.getName());
			stmt.setInt(2, petCommonData.getMasterObjectId());
			stmt.setInt(3, petCommonData.getMinionId());
			stmt.setString(4, OldName);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error update minion #" + petCommonData.getMinionId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void lockMinions(Player player, int minionId, int isLocked) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("UPDATE player_minions SET is_locked = ? WHERE player_id = ? AND minion_id = ?");
			stmt.setInt(1, isLocked);
			stmt.setInt(2, player.getObjectId());
			stmt.setInt(3, minionId);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error update minionId #" + minionId, e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void evolutionMinion(Player player, int oldMinionId, int newMinionId, MinionCommonData petCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("UPDATE player_minions SET minion_id = ? WHERE player_id = ? AND minion_id = ? AND name=?");
			stmt.setInt(1, newMinionId);
			stmt.setInt(2, player.getObjectId());
			stmt.setInt(3, oldMinionId);
			stmt.setString(4, petCommonData.getRealName());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error update minion #" + petCommonData.getMinionId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void saveDopingBag(Player player, MinionCommonData petCommonData, MinionDopingBag bag) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE player_minions SET buff_item = ? WHERE player_id = ? AND minion_id = ? AND name=?");
			String itemIds = bag.getFoodItem() + "," + bag.getDrinkItem();
			for (int itemId : bag.getScrollsUsed()) {
				itemIds += "," + Integer.toString(itemId);
			}
			stmt.setString(1, itemIds);
			stmt.setInt(2, player.getObjectId());
			stmt.setInt(3, petCommonData.getMinionId());
			stmt.setString(3, petCommonData.getRealName());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error update doping for minion #" + petCommonData.getMinionId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void updateMinionsGrowthPoint(MinionCommonData petCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("UPDATE player_minions SET growth_point = ? WHERE player_id = ? AND minion_id = ? AND name=?");
			stmt.setInt(1, petCommonData.getGrowthPoint());
			stmt.setInt(2, petCommonData.getMasterObjectId());
			stmt.setInt(3, petCommonData.getMinionId());
			stmt.setString(4, petCommonData.getRealName());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error update minion #" + petCommonData.getMinionId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}