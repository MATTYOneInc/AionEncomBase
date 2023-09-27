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
import com.aionemu.gameserver.model.gameobjects.player.ranking.Arena6V6Ranking;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfTenacityRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.GoldArenaRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.TowerOfChallengeRank;
import com.aionemu.gameserver.model.ranking.SeasonRankingResult;

import java.util.ArrayList;

/**
 * Created by Wnkrz on 24/07/2017.
 */
public abstract class SeasonRankingDAO implements DAO {

    @Override
    public final String getClassName() {
        return SeasonRankingDAO.class.getName();
    }
    public abstract ArrayList<SeasonRankingResult> getCompetitionRankingPlayers(int tableId);
    public abstract GoldArenaRank loadGoldArenaRank(int playerId, int tableId);
    public abstract ArenaOfTenacityRank loadArenaOfTenacityRank(int playerId, int tableId);
    public abstract TowerOfChallengeRank loadTowerOfChallengeRank(int playerId, int tableId);
    public abstract Arena6V6Ranking loadArena6v6Rank(int playerId, int tableId);
    public abstract boolean storeGoldArenaRank(Player player);
    public abstract boolean storeTowerRank(Player player);
    public abstract boolean storeTenacityRank(Player player);
    public abstract boolean store6v6Rank(Player player);
}