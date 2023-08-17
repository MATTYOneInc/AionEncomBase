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
import com.aionemu.gameserver.dao.VeteranRewardsDAO;
import com.aionemu.gameserver.model.veteranrewards.VeteranRewards;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQL5VeteranRewardsDAO extends VeteranRewardsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5VeteranRewardsDAO.class);

	public static final String SELECT_QUERY = "SELECT * FROM veteran_rewards ORDER BY id";
	public static final String DELETE_QUERY = "DELETE FROM veteran_rewards WHERE id = ?";

	@Override
	public Set<VeteranRewards> getVeteranReward() {
		final Set<VeteranRewards> result = new HashSet<VeteranRewards>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				result.add(new VeteranRewards(rset.getInt("id"), rset.getString("player"), rset.getInt("type"), rset.getInt("item"), rset.getInt("count"), rset
						.getInt("kinah"), rset.getString("sender"), rset.getString("title"), rset.getString("message")));
			}
			rset.close();
			stmt.close();
		} catch (Exception e) {
			log.error("[VETERANREWARD] getVeteranReward - > " + e);
		} finally {
			DatabaseFactory.close(con);
		}
		return result;
	}

	@Override
	public void delVeteranReward(final int id_veteran_reward) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt_2;
			stmt_2 = con.prepareStatement(DELETE_QUERY);
			stmt_2.setInt(1, id_veteran_reward);
			stmt_2.execute();
		} catch (Exception e) {
			log.error("[VETERANREWARD] - delVeteranReward > " + e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}