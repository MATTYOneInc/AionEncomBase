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
package com.aionemu.gameserver.services.abyss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.model.AbyssRankingResult;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_LEGIONS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANKING_PLAYERS;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class AbyssRankingCache {
	private static final Logger log = LoggerFactory.getLogger(AbyssRankingCache.class);
	private int lastUpdate;
	private final FastMap<Race, List<SM_ABYSS_RANKING_PLAYERS>> players = new FastMap<Race, List<SM_ABYSS_RANKING_PLAYERS>>();
	private final FastMap<Race, SM_ABYSS_RANKING_LEGIONS> legions = new FastMap<Race, SM_ABYSS_RANKING_LEGIONS>();

	public void reloadRankings() {
		log.info("Updating abyss ranking cache");
		this.lastUpdate = (int) (System.currentTimeMillis() / 1000);
		getDAO().updateRankList();
		renewPlayerRanking(Race.ASMODIANS);
		renewPlayerRanking(Race.ELYOS);
		renewLegionRanking();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				player.resetAbyssRankListUpdated();
			}
		});
	}

	private void renewLegionRanking() {
		Map<Integer, Integer> newLegionRankingCache = new HashMap<Integer, Integer>();
		ArrayList<AbyssRankingResult> elyosRanking = getDAO().getAbyssRankingLegions(Race.ELYOS);
		ArrayList<AbyssRankingResult> asmoRanking = getDAO().getAbyssRankingLegions(Race.ASMODIANS);
		legions.clear();
		legions.put(Race.ASMODIANS, new SM_ABYSS_RANKING_LEGIONS(lastUpdate, asmoRanking, Race.ASMODIANS));
		legions.put(Race.ELYOS, new SM_ABYSS_RANKING_LEGIONS(lastUpdate, elyosRanking, Race.ELYOS));
		for (AbyssRankingResult result : elyosRanking) {
			newLegionRankingCache.put(Integer.valueOf(result.getLegionId()), result.getRankPos());
		}
		for (AbyssRankingResult result : asmoRanking) {
			newLegionRankingCache.put(Integer.valueOf(result.getLegionId()), result.getRankPos());
		}
		LegionService.getInstance().performRankingUpdate(newLegionRankingCache);
	}

	private void renewPlayerRanking(Race race) {
		List<SM_ABYSS_RANKING_PLAYERS> newlyCalculated;
		newlyCalculated = generatePacketsForRace(race);
		players.remove(race);
		players.put(race, newlyCalculated);
	}

	private List<SM_ABYSS_RANKING_PLAYERS> generatePacketsForRace(Race race) {
		ArrayList<AbyssRankingResult> list = getDAO().getAbyssRankingPlayers(race);
		int page = 1;
		List<SM_ABYSS_RANKING_PLAYERS> playerPackets = new ArrayList<SM_ABYSS_RANKING_PLAYERS>();
		for (int i = 0; i < list.size(); i += 44) {
			if (list.size() > i + 44) {
				playerPackets.add(new SM_ABYSS_RANKING_PLAYERS(lastUpdate, list.subList(i, i + 44), race, page, false));
			} else {
				playerPackets
						.add(new SM_ABYSS_RANKING_PLAYERS(lastUpdate, list.subList(i, list.size()), race, page, true));
			}
			page++;
		}
		return playerPackets;
	}

	public List<SM_ABYSS_RANKING_PLAYERS> getPlayers(Race race) {
		return players.get(race);
	}

	public SM_ABYSS_RANKING_LEGIONS getLegions(Race race) {
		return legions.get(race);
	}

	public int getLastUpdate() {
		return lastUpdate;
	}

	private AbyssRankDAO getDAO() {
		return DAOManager.getDAO(AbyssRankDAO.class);
	}

	public static final AbyssRankingCache getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		protected static final AbyssRankingCache INSTANCE = new AbyssRankingCache();
	}
}