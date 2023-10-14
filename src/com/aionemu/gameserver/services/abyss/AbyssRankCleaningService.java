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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.model.AbyssRankingResult;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;

public class AbyssRankCleaningService {

    private Logger log = LoggerFactory.getLogger(AbyssRankCleaningService.class);
	
	private final int SECURITY_MINIMUM_PERIOD = 30;

	private static AbyssRankCleaningService instance = new AbyssRankCleaningService();
	
	private long startTime;
	
	private AbyssRankCleaningService() {
		if (CleaningConfig.ABYSS_CLEANING_ENABLE) {
			runCleaning();
		}
	}
	
    private void runCleaning() {
		log.info("AbyssRankCleaningService: Executing abyss cleaning");
		startTime = System.currentTimeMillis();

		int periodInDays = CleaningConfig.ABYSS_CLEANING_PERIOD;
		
		if (periodInDays > SECURITY_MINIMUM_PERIOD) {
			runAbyssRankingCleaning();
		}
		else {
			log.warn("The configured days for database cleaning is to low. For security reasons the service will only execute with periods over 30 days!");
		}
	}

    private void runAbyssRankingCleaning() {
        ArrayList<AbyssRankingResult> rankingsElyos = DAOManager.getDAO(AbyssRankDAO.class).getAbyssRankingPlayers(Race.ELYOS);
        ArrayList<AbyssRankingResult> rankingsAsmos = DAOManager.getDAO(AbyssRankDAO.class).getAbyssRankingPlayers(Race.ASMODIANS);
        List<Player> ToArray = new ArrayList<Player>();
        for (AbyssRankingResult result : rankingsElyos) {
            Player p = World.getInstance().findPlayer(result.getPlayerName());
            if (p == null) {
                return;
            }
            Timestamp t = p.getCommonData().getLastOnline();
            boolean isOutOfDate = (t.getTime() / 1000) >= 43200 ? true : false; // Every 30 Days
            if (isOutOfDate) {
                ToArray.add(p);
            }
        }

        for (AbyssRankingResult result : rankingsAsmos) {
            Player p = World.getInstance().findPlayer(result.getPlayerName());
            if (p == null) {
                return;
            }

            Timestamp t = p.getCommonData().getLastOnline();
            boolean isOutOfDate = (t.getTime() / 1000) >= 43200 ? true : false;
            if (isOutOfDate) {
                ToArray.add(p);
            }
        }

        if (ToArray.size() > 0) {
            DAOManager.getDAO(AbyssRankDAO.class).removePlayer(ToArray);
            AbyssRankingCache.getInstance().reloadRankings();
            log.info("Cleaned  " + ToArray.size() + " Abyss Ranking Rows in" + (System.currentTimeMillis() - startTime) / 1000L + " seconds!");
        } else {
            log.info("None of Abyss Rankings is Out of Date.");
        }
    }
	
	public static AbyssRankCleaningService getInstance() {
		return instance;
	}
}