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
import com.aionemu.gameserver.dao.LadderDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wanke on 12/02/2017.
 */

public class MySQL5LadderDAO extends LadderDAO
{
    public void addWin(Player player) {
        addPlayerLadderData(player, "wins", 1);
    }
    public void addLoss(Player player) {
        addPlayerLadderData(player, "losses", 1);
    }
    public void addLeave(Player player) {
        addPlayerLadderData(player, "leaves", 1);
    }
	
    public void addRating(Player player, int rating) {
        addPlayerLadderData(player, "rating", rating);
    }
    public void setWins(Player player, int wins) {
        setPlayerLadderData(player, "wins", wins);
    }
    public void setLosses(Player player, int losses) {
        setPlayerLadderData(player, "losses", losses);
    }
    public void setLeaves(Player player, int leaves) {
        setPlayerLadderData(player, "leaves", leaves);
    }
    public void setRating(Player player, int rating) {
        setPlayerLadderData(player, "rating", rating);
    }
	
    public int getWins(Player player) {
        return getPlayerLadderData(player, "wins");
    }
    public int getLosses(Player player) {
        return getPlayerLadderData(player, "losses");
    }
    public int getLeaves(Player player) {
        return getPlayerLadderData(player, "leaves");
    }
    public int getRating(Player player) {
        return getPlayerLadderData(player, "rating");
    }
	
    public void updateRanks() {
        Connection con = null;
        List<PlayerInfo> players = new ArrayList<PlayerInfo>();
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT player_id, last_update, rating, wins, rank FROM ladder_player WHERE wins > 0 OR losses > 0 OR leaves > 0 ORDER BY rating, wins DESC");
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                PlayerInfo plInfo = new PlayerInfo(rset.getInt("player_id"), rset.getInt("rating"), rset.getTimestamp("last_update"), rset.getInt("wins"), rset.getInt("rank"));
                players.add(plInfo);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
        } finally {
            DatabaseFactory.close(con);
        }
        Collections.sort(players, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo o1, PlayerInfo o2) {
                int result = Integer.valueOf(o1.getRating()).compareTo(Integer.valueOf(o2.getRating()));
                if (result != 0) {
                    return -result;
				}
                result = Integer.valueOf(o1.getWins()).compareTo(Integer.valueOf(o2.getWins()));
                if (result != 0) {
                    return -result;
				}
                result = Integer.valueOf(o1.getPlayerId()).compareTo(Integer.valueOf(o2.getPlayerId()));
                return result;
            }
        });
        if (players.size() > 0) {
            int i = 1;
            try {
                con = DatabaseFactory.getConnection();
                PreparedStatement stmtRank = con.prepareStatement("UPDATE ladder_player SET rank = ? WHERE player_id = ?");
                PreparedStatement stmtLast = con.prepareStatement("UPDATE ladder_player SET last_rank = ?, last_update = ? WHERE player_id = ?");
                for (PlayerInfo plInfo : players) {
                    int playerId = plInfo.getPlayerId();
                    Timestamp update = plInfo.getLastUpdate();
                    if (update == null || update.equals(new Timestamp(0)) || (update.getTime() + 24 * 60 * 60 * 1000) < System.currentTimeMillis()) {
                        stmtLast.setInt(1, plInfo.getRank());
                        stmtLast.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        stmtLast.setInt(3, playerId);
                        stmtLast.addBatch();
                    }
                    stmtRank.setInt(1, i);
                    stmtRank.setInt(2, playerId);
                    stmtRank.addBatch();
                    i++;
                }
                stmtRank.executeBatch();
                stmtLast.executeBatch();
                stmtRank.close();
                stmtLast.close();
            } catch (SQLException e) {
            } finally {
                DatabaseFactory.close(con);
            }
        }
    }
	
    public int getRank(Player player) {
        return getPlayerLadderData(player, "rank");
    }
	
    public void addPlayerLadderData(Player player, String data, int value) {
        Connection con = null;
        if (this.checkExists(player)) {
            try {
                con = DatabaseFactory.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = " + data + " + ? WHERE player_id = ?");
                stmt.setInt(1, value);
                stmt.setInt(2, player.getObjectId());
                stmt.execute();
            } catch (SQLException e) {
            } finally {
                DatabaseFactory.close(con);
            }
        } else {
            try {
                con = DatabaseFactory.getConnection();
                PreparedStatement stmt = con.prepareStatement("INSERT INTO ladder_player (player_id, " + data + ") VALUES (?, ?)");
                stmt.setInt(1, player.getObjectId());
                stmt.setInt(2, "rating".equals(data) ? 1000 + value : value);
                stmt.execute();
            } catch (SQLException e) {
            } finally {
                DatabaseFactory.close(con);
            }
        }
    }
	
    public void setPlayerLadderData(Player player, String data, int value) {
        Connection con = null;
        if (this.checkExists(player)) {
            try {
                con = DatabaseFactory.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = ? WHERE player_id = ?");
                stmt.setInt(1, value);
                stmt.setInt(2, player.getObjectId());
                stmt.execute();
            } catch (SQLException e) {
            } finally {
                DatabaseFactory.close(con);
            }
        } else {
            try {
                con = DatabaseFactory.getConnection();
                PreparedStatement stmt = con.prepareStatement("INSERT INTO ladder_player (player_id, " + data + ") VALUES (?, ?)");
                stmt.setInt(1, player.getObjectId());
                stmt.setInt(2, value);
                stmt.execute();
            } catch (SQLException e) {
            } finally {
                DatabaseFactory.close(con);
            }
        }
    }
	
    public void setPlayerLadderData(Integer playerId, String data, int value) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = ? WHERE player_id = ?");
            stmt.setInt(1, value);
            stmt.setInt(2, playerId);
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            DatabaseFactory.close(con);
        }
    }
	
    public int getPlayerLadderData(Player player, String data) {
        Connection con = null;
        int value = 0;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT " + data + " FROM ladder_player WHERE player_id = ?");
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                value = rset.getInt(data);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
        } finally {
            DatabaseFactory.close(con);
        } if (data.equals("rating") && value == 0) {
            return 1000;
		}
        return value;
    }
	
    public int getPlayerLadderData(Integer playerId, String data) {
        Connection con = null;
        int value = 0;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT " + data + " FROM ladder_player WHERE player_id = ?");
            stmt.setInt(1, playerId);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                value = rset.getInt(data);
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
        } finally {
            DatabaseFactory.close(con);
        } if (data.equals("rating") && value == 0) {
            return 1000;
		}
        return value;
    }
	
    public Timestamp getPlayerLadderUpdate(Player player) {
        Connection con = null;
        Timestamp value = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT last_update FROM ladder_player WHERE player_id = ?");
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                value = rset.getTimestamp("last_update");
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
        } finally {
            DatabaseFactory.close(con);
        }
        return value;
    }
	
    public void setPlayerLadderUpdate(Player player, Timestamp value) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET last_update = ? WHERE player_id = ?");
            stmt.setTimestamp(1, value);
            stmt.setInt(2, player.getObjectId());
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            DatabaseFactory.close(con);
        }
    }
	
    public void setPlayerLadderUpdate(Integer playerId, Timestamp value) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET last_update = ? WHERE player_id = ?");
            stmt.setTimestamp(1, value);
            stmt.setInt(2, playerId);
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            DatabaseFactory.close(con);
        }
    }
	
    public PlayerLadderData getPlayerLadderData(Player player) {
        Connection con = null;
        PlayerLadderData data = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM ladder_player WHERE player_id = ?");
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                data = new PlayerLadderData(player, rset.getInt("rating"), rset.getInt("rank"), rset.getInt("wins"), rset.getInt("losses"), rset.getInt("leaves"), rset.getTimestamp("last_update"));
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
        } finally {
            DatabaseFactory.close(con);
        } if (data == null) {
            data = new PlayerLadderData(player, 1000, 0, 0, 0, 0, new Timestamp(0));
		}
        return data;
    }
	
    private boolean checkExists(Player player) {
        Connection con = null;
        boolean exists = false;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT rating FROM ladder_player WHERE player_id = ?");
            stmt.setInt(1, player.getObjectId());
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                exists = true;
            }
            rset.close();
            stmt.close();
        } catch (Exception e) {
        } finally {
            DatabaseFactory.close(con);
        }
        return exists;
    }
	
    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }
	
    private class PlayerInfo {
        private int playerId;
        private int rating;
        private Timestamp lastUpdate;
        private int wins;
        private int rank;
		
        public PlayerInfo(int playerId, int rating, Timestamp lastUpdate, int wins, int rank) {
            this.playerId = playerId;
            this.rating = rating;
            this.lastUpdate = lastUpdate;
            this.wins = wins;
            this.rank = rank;
        }
		
        public int getPlayerId() {
            return playerId;
        }
        public int getRating() {
            return rating;
        }
        public Timestamp getLastUpdate() {
            return lastUpdate;
        }
        public int getWins() {
            return wins;
        }
        public int getRank() {
            return rank;
        }
    }
}