/*

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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import java.sql.Timestamp;

/**
 * Created by wanke on 12/02/2017.
 */

public abstract class LadderDAO implements DAO
{
    @Override
    public final String getClassName() {
        return LadderDAO.class.getName();
    }
	
    public static class PlayerLadderData {
        private Player player;
        private int rating;
        private int rank;
        private int wins;
        private int losses;
        private int leaves;
        private Timestamp lastUpdate;
		
        public PlayerLadderData(Player player, int rating, int rank, int wins, int losses, int leaves, Timestamp lastUpdate) {
            this.player = player;
            this.rating = rating;
            this.rank = rank;
            this.wins = wins;
            this.losses = losses;
            this.leaves = leaves;
            this.lastUpdate = lastUpdate;
        }
		
        public void setRating(int rating) {
            this.rating = rating;
        }
        public int getRating() {
            return rating;
        }
        public void setRank(int rank) {
            this.rank = rank;
        }
        public int getRank() {
            return rank;
        }
        public void setWins(int wins) {
            this.wins = wins;
        }
        public int getWins() {
            return wins;
        }
        public void setLosses(int losses) {
            this.losses = losses;
        }
        public int getLosses() {
            return losses;
        }
        public void setLeaves(int leaves) {
            this.leaves = leaves;
        }
        public int getLeaves() {
            return leaves;
        }
        public void setLastUpdate(Timestamp lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
        public Timestamp getLastUpdate() {
            return lastUpdate;
        }
        public void setPlayer(Player player) {
            this.player = player;
        }
        public Player getPlayer() {
            return player;
        }
    }
	
    public abstract void updateRanks();
    public abstract int getRank(Player player);
    public abstract void addWin(Player player);
    public abstract void addLoss(Player player);
    public abstract void addLeave(Player player);
    public abstract void addRating(Player player, int rating);
    public abstract int getWins(Player player);
    public abstract int getLosses(Player player);
    public abstract int getLeaves(Player player);
    public abstract int getRating(Player player);
    public abstract void setWins(Player player, int wins);
    public abstract void setLosses(Player player, int losses);
    public abstract void setLeaves(Player player, int leaves);
    public abstract void setRating(Player player, int rating);
    public abstract PlayerLadderData getPlayerLadderData(Player player);
}