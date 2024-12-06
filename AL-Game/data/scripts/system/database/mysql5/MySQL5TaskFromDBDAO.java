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
import com.aionemu.gameserver.dao.TaskFromDBDAO;
import com.aionemu.gameserver.model.tasks.TaskFromDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Divinity
 */
public class MySQL5TaskFromDBDAO extends TaskFromDBDAO {

	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(MySQL5TaskFromDBDAO.class);
	private static final String SELECT_ALL_QUERY = "SELECT * FROM tasks ORDER BY id";
	private static final String UPDATE_QUERY = "UPDATE tasks SET last_activation = ? WHERE id = ?";

	@Override
	public ArrayList<TaskFromDB> getAllTasks() {
		final ArrayList<TaskFromDB> result = new ArrayList<TaskFromDB>();

		Connection con = null;

		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_ALL_QUERY);
			ResultSet rset = stmt.executeQuery();

			while (rset.next()) {
				result.add(new TaskFromDB(rset.getInt("id"), rset.getString("task"), rset.getString("type"), rset.getTimestamp("last_activation"), rset.getString("start_time"), rset.getInt("delay"), rset.getString("param")));
            }
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			log.error("getAllTasks", e);
		} finally {
			DatabaseFactory.close(stmt, con);
		}
		return result;
	}

	@Override
	public void setLastActivation(final int id) {
		Connection con = null;

		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);

			stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			stmt.setInt(2, id);
			stmt.execute();
		} catch (SQLException e) {
			log.error("setLastActivation", e);
		} finally {
			DatabaseFactory.close(stmt, con);
		}
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}