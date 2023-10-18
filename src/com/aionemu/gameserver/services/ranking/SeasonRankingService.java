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
package com.aionemu.gameserver.services.ranking;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.SeasonRankingDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.ranking.Arena6V6Ranking;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfTenacityRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.GoldArenaRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.TowerOfChallengeRank;
import com.aionemu.gameserver.model.ranking.SeasonRankingEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MY_HISTORY;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Wnkrz on 24/07/2017.
 */

public class SeasonRankingService {
	public void loadPacketPlayer(Player player, int tableid) {
		if (tableid == 1) {
			loadGoldArenaScore(player);
		} else if (tableid == 2) {
			loadTowerScore(player);
		} else if (tableid == 3) {
			loadArena6v6Score(player);
		} else if (tableid == 541) {
			loadArenaOfTenacityScore(player);
		} else {
			return;
		}
	}

	public void loadGoldArenaScore(Player player) {
		GoldArenaRank rank = getDAO().loadGoldArenaRank(player.getObjectId(),
				SeasonRankingEnum.HALL_OF_TENACITY.getId());
		player.setArenaGoldRank(rank);
		PacketSendUtility.sendPacket(player,
				new SM_MY_HISTORY(SeasonRankingEnum.HALL_OF_TENACITY.getId(), player.getArenaGoldRank()));
	}

	public void loadTowerScore(Player player) {
		TowerOfChallengeRank rank = getDAO().loadTowerOfChallengeRank(player.getObjectId(),
				SeasonRankingEnum.TOWER_OF_CHALLENGE.getId());
		player.setTowerRank(rank);
		PacketSendUtility.sendPacket(player,
				new SM_MY_HISTORY(SeasonRankingEnum.TOWER_OF_CHALLENGE.getId(), player.getTowerRank()));
	}

	public void loadArena6v6Score(Player player) {
		Arena6V6Ranking rank = getDAO().loadArena6v6Rank(player.getObjectId(), SeasonRankingEnum.ARENA_6V6.getId());
		player.set6v6Rank(rank);
		PacketSendUtility.sendPacket(player,
				new SM_MY_HISTORY(SeasonRankingEnum.ARENA_6V6.getId(), player.get6v6Rank()));
	}

	public void loadArenaOfTenacityScore(Player player) {
		ArenaOfTenacityRank rank = getDAO().loadArenaOfTenacityRank(player.getObjectId(),
				SeasonRankingEnum.ARENA_OF_TENACITY.getId());
		player.setTenacityRank(rank);
		PacketSendUtility.sendPacket(player,
				new SM_MY_HISTORY(SeasonRankingEnum.ARENA_OF_TENACITY.getId(), player.getTenacityRank()));
	}

	public void saveCrusibleSpireTime(Player player, int newTime) {
		TowerOfChallengeRank rank = player.getTowerRank();
		// update best time if new time is supp
		if (rank.getBestTime() == 0) {
			rank.setCurrentTime(newTime);
		} else if (rank.getBestTime() > newTime) {
			rank.setBestTime(newTime);
		}
		// add last time
		if (rank.getLastTime() == 0) {
			rank.setLastTime(newTime);
		} else {
			rank.setLastTime(rank.getCurrentTime());
		}
		// add curren time
		rank.setCurrentTime(newTime);
		// save to database
		DAOManager.getDAO(SeasonRankingDAO.class).storeTowerRank(player);
	}

	private SeasonRankingDAO getDAO() {
		return DAOManager.getDAO(SeasonRankingDAO.class);
	}

	public static final SeasonRankingService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final SeasonRankingService INSTANCE = new SeasonRankingService();
	}
}