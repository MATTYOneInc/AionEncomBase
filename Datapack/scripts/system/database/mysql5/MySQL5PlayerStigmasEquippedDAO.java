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
import com.aionemu.commons.utils.GenericValidator;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerStigmasEquippedDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.linked_skill.EquippedStigmasEntry;
import com.aionemu.gameserver.model.skill.linked_skill.PlayerEquippedStigmaList;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySQL5PlayerStigmasEquippedDAO extends PlayerStigmasEquippedDAO
{
	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerStigmasEquippedDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_stigmas_equipped` (`player_id`, `item_id`, `item_name`) VALUES (?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE `player_stigmas_equipped` set item_id=?, item_name=? where player_id=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_stigmas_equipped` WHERE `player_id`=? AND item_id=? AND item_name=?";
	public static final String SELECT_QUERY = "SELECT `item_id`, `item_name` FROM `player_stigmas_equipped` WHERE `player_id`=?";
	
	private static final Predicate<EquippedStigmasEntry> itemsToInsertPredicate = new Predicate<EquippedStigmasEntry>() {
		@Override
		public boolean apply(@Nullable EquippedStigmasEntry input) {
			return input != null && PersistentState.NEW == input.getPersistentState();
		}
	};
	
	private static final Predicate<EquippedStigmasEntry> itemsToUpdatePredicate = new Predicate<EquippedStigmasEntry>() {
		@Override
		public boolean apply(@Nullable EquippedStigmasEntry input) {
			return input != null && PersistentState.UPDATE_REQUIRED == input.getPersistentState();
		}
	};
	
	private static final Predicate<EquippedStigmasEntry> itemsToDeletePredicate = new Predicate<EquippedStigmasEntry>() {
		@Override
		public boolean apply(@Nullable EquippedStigmasEntry input) {
			return input != null && PersistentState.DELETED == input.getPersistentState();
		}
	};
	
	@Override
	public PlayerEquippedStigmaList loadItemsList(int playerId) {
		List<EquippedStigmasEntry> items = new ArrayList<EquippedStigmasEntry>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("item_id");
				String name = rset.getString("item_name");
				items.add(new EquippedStigmasEntry(id, name, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not restore StigmasEquipped data for player: " + playerId + " from DB: " + e.getMessage(), e);
		} finally {
			DatabaseFactory.close(con);
		}
		return new PlayerEquippedStigmaList(items);
	}
	
	@Override
	public boolean storeItems(Player player) {
		List<EquippedStigmasEntry> skillsActive = Lists.newArrayList(player.getEquipedStigmaList().getAllItems());
		List<EquippedStigmasEntry> skillsDeleted = Lists.newArrayList(player.getEquipedStigmaList().getDeletedItems());
		store(player, skillsActive);
		store(player, skillsDeleted);
		return true;
	}
	
	private void store(Player player, List<EquippedStigmasEntry> skills) {
		Connection con = null;
		try{
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);
			deleteItems(con, player, skills);
			addItems(con, player, skills);
			updateItems(con, player, skills);
		} catch (SQLException e){
			log.error("Failed to open connection to database while saving SkillList for player " + player.getObjectId());
		} finally {
			DatabaseFactory.close(con);
		} for(EquippedStigmasEntry skill : skills){
			skill.setPersistentState(PersistentState.UPDATED);
		}
	}
	
	private void addItems(Connection con, Player player, List<EquippedStigmasEntry> items) {
		Collection<EquippedStigmasEntry> skillsToInsert = Collections2.filter(items, itemsToInsertPredicate);
		if(GenericValidator.isBlankOrNull(skillsToInsert)){
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(INSERT_QUERY);
			for(EquippedStigmasEntry skill : skillsToInsert){
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, skill.getItemId());
				ps.setString(3, skill.getItemName());
				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
		} finally {
			DatabaseFactory.close(ps);
		}
	}
	
	private void updateItems(Connection con, Player player, List<EquippedStigmasEntry> skills) {
		Collection<EquippedStigmasEntry> skillsToUpdate = Collections2.filter(skills, itemsToUpdatePredicate);
		if(GenericValidator.isBlankOrNull(skillsToUpdate)){
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(UPDATE_QUERY);
			for(EquippedStigmasEntry skill : skillsToUpdate){
				ps.setInt(1, skill.getItemId());
				ps.setInt(2, player.getObjectId());
				ps.setString(3, skill.getItemName());
				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
		} finally {
			DatabaseFactory.close(ps);
		}
	}

	private void deleteItems(Connection con, Player player, List<EquippedStigmasEntry> skills) {
		Collection<EquippedStigmasEntry> skillsToDelete = Collections2.filter(skills, itemsToDeletePredicate);
		if(GenericValidator.isBlankOrNull(skillsToDelete)){
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(DELETE_QUERY);
			for(EquippedStigmasEntry skill : skillsToDelete){
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, skill.getItemId());
				ps.setString(3, skill.getItemName());
				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
		} finally {
			DatabaseFactory.close(ps);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}