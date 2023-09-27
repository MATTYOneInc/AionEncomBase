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

import java.sql.Timestamp;
import java.util.concurrent.Future;

import com.aionemu.gameserver.services.events.thievesguildservice.ThievesStatusList;
import com.aionemu.gameserver.services.events.thievesguildservice.ThievesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.dao.PlayerThievesListDAO;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CAPTCHA;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.captcha.CAPTCHAUtil;
import com.aionemu.commons.database.dao.DAOManager;

/**
 * Thieves Guild Service 5.0.6
 */
public class ThievesGuildService {
	
	private static final Logger log = LoggerFactory.getLogger(ThievesGuildService.class);

	public void onEnterWorld(Player player) {
		if (!CustomConfig.THIEVES_ENABLE) {
			return;
		}
		try {
			ThievesStatusList thieves = DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(player.getObjectId());
			if(thieves == null) {
				player.setThieves(new ThievesStatusList(player.getObjectId(), 0, 0, 0l, 0, "Нет", 0, new Timestamp(System.currentTimeMillis())));
				DAOManager.getDAO(PlayerThievesListDAO.class).saveNewThieves(player.getThieves());
			}
			log.info("ThievesGuildService loadThievesStatus try [Player = " + player.getThieves().getPlayerId() + "]");
		}
		catch (Exception ex) {
			log.error("Error in ThievesGuildService.onEnterWorld [Player = " + player.getName() + "]", ex);
		}
	}

	public void thieves(Player player) {
		if (!CustomConfig.THIEVES_ENABLE)
			return;
		
		for (Player target : player.getKnownList().getKnownPlayers().values()) {
			if (!PlayerActions.isAlreadyDead(target) && MathUtil.isIn3dRange(target, player, 2)) {
				if (!player.isThieves()) {
					player.setIsThieves(true);
				}
				player.setCaptchaWord(CAPTCHAUtil.getRandomWord());
				player.setCaptchaImage(CAPTCHAUtil.createCAPTCHA(player.getCaptchaWord()).array());
				captchaCheck(player, target, 0, true, SecurityConfig.CAPTCHA_EXTRACTION_BAN_TIME * 1000L);
			}
		}
	}
	
	public void createRevenge(Player player, Player target) {
		if (!CustomConfig.THIEVES_ENABLE) {
			return;
		}
		if (player.isThievesDuel()) {
			return;
		}
		target.setThieves(DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(target.getObjectId()));
		ThievesStatusList thieves = target.getThieves();
		if (thieves.getRevengeName().equals(player.getName()) && !PlayerActions.isAlreadyDead(target) && MathUtil.isIn3dRange(target, player, 2)) {
			player.setThievesDuel(true);
			thievesMessage(player, "Thief " + target.getName() + " in the reach zone. The duel begins.", 0);
			thievesMessage(target, "Sacrifice " + player.getName() + " in the zone of revenge. The duel begins.", 0);
			//DuelService.getInstance().startDuel(player, target);
		}
		log.info("Aion-Unique Console: ThievesGuildService createRevenge [Player = " + player.getName() + "]");
	}
	
	public void revenge(Player player, Player target) {
		if (!CustomConfig.THIEVES_ENABLE) {
			return;
		}
		player.setThieves(DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(player.getObjectId()));
		target.setThieves(DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(target.getObjectId()));
		ThievesStatusList thievesPlayer = player.getThieves();
		ThievesStatusList thievesTarget = target.getThieves();
		if (thievesPlayer == null || thievesTarget == null) {
			return;
		}
		Timestamp nextTime = thievesTarget.getRevengeDate();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		int revengeCount = thievesPlayer.getRevengeCount();
		long kinahResult = (target.getInventory().getKinah() / thievesPlayer.getRankId()) + thievesTarget.getLastThievesKinah();
		if (!player.getName().equals(thievesTarget.getRevengeName()) && currentTime.after(nextTime) && !currentTime.equals(nextTime)) {
			return;
		}
		else {
			player.getInventory().increaseKinah(kinahResult);
			target.getInventory().decreaseKinah(kinahResult);
			thievesPlayer.setRevengeCount(revengeCount + 1);
			thievesMessage(player, "Вы наказали вора " + target.getName() + " и вернули " + kinahResult + " кинар.", 0);
		}
		thievesTarget.setLastThievesKinah(0l);
		thievesTarget.setRevengeName("Нет");
		thievesTarget.setRevengeDate(new Timestamp(System.currentTimeMillis()));
		DAOManager.getDAO(PlayerThievesListDAO.class).storeThieves(thievesPlayer);
		DAOManager.getDAO(PlayerThievesListDAO.class).storeThieves(thievesTarget);
		log.info("Aion-Unique Console: ThievesGuildService revenge [Player = " + player.getName() + "]");
	}   
	/* TODO
	private void thievesIn(Player player) {
		player.setThieves(DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(player.getObjectId()));
		ThievesStatusList thieves = player.getThieves();
		if (thieves.getRankId() >= 3) {

			for (Legion legion : LegionService.getInstance().getCachedLegions()) {
				if (legion.getLegionName() == "ThievesGuild" && !player.getLegion().getLegionName().equals(legion.getLegionName())) {
					LegionService.getInstance().directAddPlayer(legion, player);
					log.info("Aion-Unique Console: ThievesGuildService thievesIn [Player = " + player.getName() + "]");
				}
			}
		}
	}
	*/

	/* TODO
	private void thievesLegionCreate(Player player) {
		for (Legion legion : LegionService.getInstance().getCachedLegions()) {
			if (!legion.getLegionName().contains("ThievesGuild")) {
				LegionService.getInstance().createLegion(player, "ThievesGuild");
				log.info("Aion-Unique Console: ThievesGuildService thievesLegionCreate done");
			}
		}
	}
	*/

	public void captchaCheck(Player player, int captchaCount, boolean state, long delay) {
		captchaCheck(player, null, captchaCount, state, delay);
	}
	
	public void captchaCheck(Player player, Player target, int captchaCount, boolean state, long delay) {
		stopThievesTask(player, false);
		
		if (state) {
			if (captchaCount < 3) {
				PacketSendUtility.sendPacket(player, new SM_CAPTCHA(captchaCount + 1, player.getCaptchaImage()));
			}
			else {
				player.setCaptchaWord(null);
				player.setCaptchaImage(null);
			}
			player.setThievesTimer((int) delay);
			player.setStopThieves(System.currentTimeMillis());
			scheduleThievesTask(player, delay);
			log.info("Aion-Unique Console: ThievesGuildService captchaCheck state [Player = " + player.getName() + "]");
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400269));
			player.setCaptchaWord(null);
			player.setCaptchaImage(null);
			player.setThievesTimer(0);
			player.setStopThieves(0);
			player.setIsThieves(false);
			// Thieves success
			player.setThieves(DAOManager.getDAO(PlayerThievesListDAO.class).loadThieves(player.getObjectId()));
			ThievesStatusList thieves = player.getThieves();
			Timestamp nextTime = thieves.getRevengeDate();
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			int thievesCount = thieves.getThievesCount();
			if (target != null && target.getName().equals(thieves.getRevengeName()) && currentTime.after(nextTime) && !currentTime.equals(nextTime)) {
				long kinah = 0;
				switch (ThievesType.getThievesType(thieves.getRankId())) {
					case SILVER:
						kinah = 1000 * thieves.getRankId();
						break;
					case GOLD:
						kinah = 2000 * thieves.getRankId();
						break;
					case PLATINUM:
						kinah = 3000 * thieves.getRankId();
						break;
					case MITHRIL:
						kinah = 4000 * thieves.getRankId();
						break;
					case SERAMIUM:
						kinah = 5000 * thieves.getRankId();
						break;
					default:
						kinah = 600;
						break;
				}
				int rank = 0;
				switch (thieves.getThievesCount()) {
					case 10:
						rank = 1;
						thievesMessage(player, "Thief", 1);
						break;
					case 50:
						rank = 2;
						thievesMessage(player, "Pickpocket", 1);
						break;
					case 100:
						rank = 3;
						thievesMessage(player, "Voryaga", 1);
						//thievesLegionCreate(player); TODO
						//thievesIn(player); TODO
						break;
					case 150:
						rank = 4;
						thievesMessage(player, "Sleek Hands", 1);
						break;
					case 200:
						rank = 5;
						thievesMessage(player, "Fast hands", 1);
						break;
					case 300:
						rank = 6;
						thievesMessage(player, "Elusive", 1);
						break;
				}
				thieves.setRankId(rank);
				thieves.setThievesCount(thievesCount + 1);
				thieves.setRevengeName(target.getName());
				//thieves.setRevengeDate(DateTimeService.getInstance().countNextRepeatTimeDay(1)); //TODO
				thieves.setLastThievesKinah(kinah);
				player.getInventory().increaseKinah(kinah);
				target.getInventory().decreaseKinah(kinah);
				DAOManager.getDAO(PlayerThievesListDAO.class).storeThieves(thieves);
				thievesMessage(player, "You are robbed " + target.getName(), 0);
				thievesMessage(player, "Be careful! Revenge can be swift from " + target.getName(), 0);
				thievesMessage(player,  target.getName() + " Has the ability to attack you at any time ", 0);
				thievesMessage(player,  "if " + target.getName() + " you will be killed. He will get stolen from% and you will lose this %", 0);
				log.info("Aion-Unique Console: ThievesGuildService captchaCheck [Player = " + player.getName() + "]");
			}
		}
	}

	private void thievesMessage(Player player, String msg, int type) {
		String typeMsg = "";
		switch (type) {
			case 1:
				typeMsg = "Received a new rank of theft: ";
				break;
			default:
				break;
		}
		PacketSendUtility.sendMessage(player, "[color:Guild;0 255 0][color:in;0 255 0][color:moat;0 255 0]: " + typeMsg + msg + ".");
	}
	
	private void stopThievesTask(Player player, boolean state) {
		Future<?> thievesTask = player.getController().getTask(TaskId.THIEVES);
		if (thievesTask != null) {
			if (state) {
				long delay = player.getThievesTimer();
				if (delay < 0)
					delay = 0;
				player.setThievesTimer((int) delay);
			}
			player.getController().cancelTask(TaskId.THIEVES);
		}
	}
	
	private void scheduleThievesTask(final Player player, long thievesTimer) {
		player.setThievesTimer((int) thievesTimer);
		
		player.getController().addTask(TaskId.THIEVES, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				captchaCheck(player, 0, false, 0);
			}
		}, thievesTimer));
	}
	
	public static ThievesGuildService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final ThievesGuildService instance = new ThievesGuildService();
	}
}