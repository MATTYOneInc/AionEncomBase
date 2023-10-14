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
package com.aionemu.gameserver.services.events;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.bonus_service.BoostEventBonus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.templates.event.BoostEvents;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BOOST_EVENTS;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class BoostEventService implements StatOwner
{
    private static BoostEventBonus bonus;
	
    private static final Logger log = LoggerFactory.getLogger(BoostEventService.class);
	
    public Map<Integer, BoostEvents> data = new HashMap<Integer, BoostEvents>(1);
	
    public void onStart() {
        Map<Integer, BoostEvents> raw = DataManager.BOOST_EVENT_DATA.getAll();
        if (raw.size() != 0) {
            getBoostEvent(raw);
        }
    }
	
    public void sendPacket(Player player){
        Map<Integer, BoostEvents> boost = getCurrentBoost();
        for(BoostEvents be : boost.values()){
            long start = be.getStartDate().getMillis() / 1000;
            long end = be.getEndDate().getMillis() / 1000;
            PacketSendUtility.sendPacket(player , new SM_BOOST_EVENTS(be.getBuffId(), be.getBuffValue(), start, end));
        }
    }
	
    public Map<Integer, BoostEvents> getCurrentBoost() {
        Map<Integer, BoostEvents> boost = new HashMap<Integer, BoostEvents>();
        for(BoostEvents be : data.values()){
            if(be.getStartDate().isBeforeNow() && be.getEndDate().isAfterNow()) {
                boost.put(be.getId(), be);
            }
        }
        return boost;
    }
	
    public void getBoostEvent(int id, BoostEvents be) {
        if (data.containsValue(id)) {
            return;
		}
        data.put(id, be);
    }
	
    public void getBoostEvent(Map<Integer, BoostEvents> raw) {
        data.putAll(raw);
        for (BoostEvents be : data.values()) {
            getBoostEvent(be.getId(), be);
        }
        log.info("[BoostEventService] Loaded " + data.size() + " Event Boost");
    }
	
    public static final BoostEventService getInstance() {
        return SingletonHolder.instance;
    }
	
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final BoostEventService instance = new BoostEventService();
    }
}