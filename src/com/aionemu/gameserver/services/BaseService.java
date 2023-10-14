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
package com.aionemu.gameserver.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.BaseDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.services.base.Base;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler
 */

public class BaseService
{
	private static final Logger log = LoggerFactory.getLogger(BaseService.class);
	private final Map<Integer, Base<?>> active = new FastMap<Integer, Base<?>>().shared();
	private Map<Integer, BaseLocation> bases;

	public void initBaseLocations() {
		bases = DataManager.BASE_DATA.getBaseLocations();
		DAOManager.getDAO(BaseDAO.class).loadBaseLocations(bases);
		log.info("[BaseService] Loaded " + bases.size() + " bases locations.");
	}
	
	public void initBases() {
		log.info("[BaseService] is initialized...");
		for (BaseLocation base : getBaseLocations().values()) {
			start(base.getId());
		}
	}
	
	public void initBaseReset() {
		Race race = null;
		log.info("[BaseService] initializing <Base Reset>...");
		String weekly = "0 0 9 ? * WED *";
		CronService.getInstance().schedule(new Runnable() {
			public void run() {
				//Elten.
				capture(45, Race.NPC);
				capture(46, Race.NPC);
				//Heiron.
				capture(47, Race.NPC);
				capture(48, Race.NPC);
				//Morheim.
				capture(49, Race.NPC);
				capture(50, Race.NPC);
				//Beluslan.
				capture(51, Race.NPC);
				capture(52, Race.NPC);
				//Reshanta.
				capture(53, Race.NPC);
				capture(54, Race.NPC);
				capture(55, Race.NPC);
				capture(56, Race.NPC);
				capture(57, Race.NPC);
				capture(58, Race.NPC);
				capture(59, Race.NPC);
				capture(60, Race.NPC);
				capture(61, Race.NPC);
				capture(62, Race.NPC);
				capture(63, Race.NPC);
				capture(64, Race.NPC);
				//Katalam.
				capture(71, Race.NPC);
				capture(72, Race.NPC);
				capture(73, Race.NPC);
				capture(74, Race.NPC);
				capture(75, Race.NPC);
				capture(76, Race.NPC);
				capture(77, Race.NPC);
				capture(78, Race.NPC);
				capture(79, Race.NPC);
				//Levinshor.
				capture(90, Race.NPC);
				capture(91, Race.NPC);
				capture(92, Race.NPC);
				capture(93, Race.NPC);
				capture(94, Race.NPC);
				capture(95, Race.NPC);
				capture(96, Race.NPC);
				capture(97, Race.NPC);
				capture(98, Race.NPC);
				capture(99, Race.NPC);
				capture(100, Race.NPC);
				capture(101, Race.NPC);
				capture(102, Race.NPC);
				//Kaldor.
				capture(103, Race.NPC);
				capture(104, Race.NPC);
			}
		}, weekly);
	}
	
	public Map<Integer, BaseLocation> getBaseLocations() {
		return bases;
	}
	
	public BaseLocation getBaseLocation(int id) {
		return bases.get(id);
	}
	
	public void start(final int id) {
		final Base<?> base;
		synchronized (this) {
			if (active.containsKey(id)) {
				return;
			}
			base = new Base<BaseLocation>(getBaseLocation(id));
			active.put(id, base);
		}
		base.start();
	}
	
	public void stop(int id) {
		if (!isActive(id)) {
			log.info("Trying to stop not active base:" + id);
			return;
		}
		Base<?> base;
		synchronized (this) {
			base = active.remove(id);
		} if (base == null || base.isFinished()) {
			log.info("Trying to stop null or finished base:" + id);
			return;
		}
		base.stop();
		start(id);
	}
	
	public void capture(int id, Race race) {
		if (!isActive(id)) {
			log.info("Detecting not active base capture.");
			return;
		}
		getActiveBase(id).setRace(race);
		stop(id);
		broadcastUpdate(getBaseLocation(id));
		getDAO().updateLocation(getBaseLocation(getBaseLocation(id).getId()));
	}
	
	public boolean isActive(int id) {
		return active.containsKey(id);
	}
	
	public Base<?> getActiveBase(int id) {
		return active.get(id);
	}
	
	public void onEnterBaseWorld(Player player) {
		for (BaseLocation baseLocation : getBaseLocations().values()) {
			if (baseLocation.getWorldId() == player.getWorldId() && isActive(baseLocation.getId())) {
				Base<?> base = getActiveBase(baseLocation.getId());
				PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, base.getFlag()));
				player.getController().updateZone();
			    player.getController().updateNearbyQuests();
			}
		}
	}
	
	public void broadcastUpdate(final BaseLocation baseLocation) {
		World.getInstance().getWorldMap(baseLocation.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (isActive(baseLocation.getId())) {
					Base<?> base = getActiveBase(baseLocation.getId());
					PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, base.getFlag()));
					player.getController().updateZone();
			        player.getController().updateNearbyQuests();
				}
			}
		});
	}
	
	public static BaseService getInstance() {
		return BaseServiceHolder.INSTANCE;
	}
	
	private static class BaseServiceHolder {
		private static final BaseService INSTANCE = new BaseService();
	}
	
	private BaseDAO getDAO() {
        return DAOManager.getDAO(BaseDAO.class);
    }
}