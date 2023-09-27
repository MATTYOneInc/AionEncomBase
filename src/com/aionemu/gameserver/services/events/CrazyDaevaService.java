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

import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.services.PvpService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rinzler (Encom)
 */
public class CrazyDaevaService {

	private static final Logger log = LoggerFactory.getLogger(CrazyDaevaService.class);
	int crazyCount = 0;

	//calculate time
	public void startTimer() {
		String[] times = EventsConfig.CRAZY_TIMES.split("\\|");
		for (String cron : times) {
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					checkStart();
				}
			}, cron);
			log.info("Scheduled Crazy Daeva: based on cron expression: " + cron + " Duration: " + EventsConfig.CRAZY_ENDTIME + " in minutes");
		}
	}

	//check time start
	public void checkStart() {
		startChoose();
		clearCrazy();
		log.info("Crazy Daeva start choose random player and calculate end time.");
	}

	//start choose rnd
	public void startChoose() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(final Player player) {
				int rnd = 0;
				rnd = Rnd.get(1, 100);
				player.setRndCrazy(rnd);
				if (player.getRndCrazy() >= EventsConfig.CRAZY_LOWEST_RND && player.getLevel() >= 55) {
					crazyCount++;
					if (crazyCount == 1) {
						TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
						PacketSendUtility.sendYellowMessageOnCenter(player, "CRAZY DAEVA "+ player.getName() +"");
						log.info("System choose "+ player.getName() +".");
						player.setInCrazy(true);
						PvpService.getInstance().doReward(player);
					}
				}
				log.info("Player "+ player.getName() +" got random "+ rnd +"");
			}
		});
	}

	//increase kill count
	public void increaseRawKillCount(Player winner) {
		int currentCrazyKillCount = winner.getCrazyKillCount();
		winner.setCrazyKillCount(currentCrazyKillCount + 1);
		int newCrazyKillCount = currentCrazyKillCount + 1;

		if (newCrazyKillCount >= 0 && newCrazyKillCount <= 10) {
			updateCrazyLevel(winner, 1);
		}
		if (newCrazyKillCount >= 10 && newCrazyKillCount <= 20) {
			updateCrazyLevel(winner, 2);
		}
		if (newCrazyKillCount >= 20 && newCrazyKillCount <= 30) {
			updateCrazyLevel(winner, 3);
		}
		log.info("Killed "+ newCrazyKillCount +" player.");
	}

	//update kill level
	private void updateCrazyLevel(Player winner, int level) {
		winner.setCrazyLevel(level);
	}

	//when die
	public void crazyOnDie(Player victim, Creature killer, boolean isPvPDeath) {
		if (victim.isInCrazy()) {
			victim.setCrazyLevel(0);
			sendEndSpreeMessage(victim, killer, isPvPDeath);
		}
	}

	//killer reward + announce
	private void sendEndSpreeMessage(final Player victim, Creature killer, boolean isPvPDeath) {
		if (killer instanceof Player) {
			if (killer.getRace().getRaceId() != victim.getRace().getRaceId()) {
				final String spreeEnder = isPvPDeath ? ((Player)killer).getName() : "Killer";
				AbyssPointsService.addAp((Player)killer, 5000);
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(final Player player) {
						PacketSendUtility.sendYellowMessageOnCenter(player, "Crazier "+ victim.getName() +" has slain by "+ spreeEnder +"!");
					}
				});
				log.info("Crazier " + victim.getName() + " was killed by " + spreeEnder + "");
			}
		}
	}

	//end event clear all and reward
	public void clearCrazy() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(final Player player) {
						if (player.isInCrazy()) {
							TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
							if (player.getCrazyLevel() == 1) {
								AbyssPointsService.addAp(player, 5000);
								log.info("Crazy Daeva "+ player.getName() +" killed "+ player.getCrazyKillCount() +" and get reward 5000 AP.");
							}
							if (player.getCrazyLevel() == 2) {
								AbyssPointsService.addAp(player, 10000);
								log.info("Crazy Daeva "+ player.getName() +" killed "+ player.getCrazyKillCount() +" and get reward 10000 AP.");
							}
							if (player.getCrazyLevel() == 3) {
								AbyssPointsService.addAp(player, 15000);
								log.info("Crazy Daeva "+ player.getName() +" killed "+ player.getCrazyKillCount() +" and get reward 15000 AP.");
							}
							player.setCrazyKillCount(0);
							player.setCrazyLevel(0);
							player.setInCrazy(false);
							player.setRndCrazy(0);
						}
						player.setInCrazy(false);
						player.setRndCrazy(0);
						player.getLifeStats().increaseHp(TYPE.HP, player.getLifeStats().getMaxHp() + 5000);
						
						PacketSendUtility.sendYellowMessageOnCenter(player, "Crazy Daeva event has stopped!");
					}
				});	
				log.info("Crazy Daeva cleared.");
			}
		}, EventsConfig.CRAZY_ENDTIME * 60 * 1000); //time stop
	}

	public static final CrazyDaevaService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final CrazyDaevaService instance = new CrazyDaevaService();
	}
}