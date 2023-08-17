/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services.player;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.*;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.*;
import com.aionemu.gameserver.services.drop.DropService;
import com.aionemu.gameserver.services.events.EventWindowService;
import com.aionemu.gameserver.services.events.ShugoSweepService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.summons.SummonsService;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.services.toypet.PetService;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.GMService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class PlayerLeaveWorldService
{
	private static final Logger log = LoggerFactory.getLogger(PlayerLeaveWorldService.class);

	public static final void startLeaveWorldDelay(final Player player, int delay) {
		player.getController().stopMoving();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				startLeaveWorld(player);
			}
		}, delay);
	}

	public static final void startLeaveWorld(Player player) {
		log.info("Player Logged Out: " + player.getName() + " Account: " + (player.getClientConnection() != null ? player.getClientConnection().getAccount().getName() : "Disconnected"));
		FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
		FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
		player.onLoggedOut();
		PetService.getInstance().onPlayerLogout(player);
		BrokerService.getInstance().removePlayerCache(player);
		ExchangeService.getInstance().cancelExchange(player);
		RepurchaseService.getInstance().removeRepurchaseItems(player);
		if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
			AutoGroupService.getInstance().onPlayerLogOut(player);
		}
		ProtectorConquerorService.getInstance().onLogout(player);
		InstanceService.onLogOut(player);
		GMService.getInstance().onPlayerLogedOut(player);
		KiskService.getInstance().onLogout(player);
		player.getMoveController().abortMove();
		if (player.isLooting()) {
			DropService.getInstance().closeDropList(player, player.getLootingNpcOid());
		}
		if (player.isInPrison()) {
			long prisonTimer = System.currentTimeMillis() - player.getStartPrison();
			prisonTimer = player.getPrisonTimer() - prisonTimer;
			player.setPrisonTimer(prisonTimer);
			log.debug("Update prison timer to " + prisonTimer / 1000 + " seconds !");
		}
		DAOManager.getDAO(PlayerEffectsDAO.class).storePlayerEffects(player);
		DAOManager.getDAO(PlayerCooldownsDAO.class).storePlayerCooldowns(player);
		DAOManager.getDAO(ItemCooldownsDAO.class).storeItemCooldowns(player);
		DAOManager.getDAO(HouseObjectCooldownsDAO.class).storeHouseObjectCooldowns(player);
		DAOManager.getDAO(PlayerLifeStatsDAO.class).updatePlayerLifeStat(player);
		DAOManager.getDAO(EventItemsDAO.class).storeItems(player);
		//SHUGO SWEEP
		ShugoSweepService.getInstance().onLogout(player);
		PlayerGroupService.onPlayerLogout(player);
		PlayerAllianceService.onPlayerLogout(player);
		LegionService.getInstance().LegionWhUpdate(player);
		player.getEffectController().removeAllEffects(true);
		player.getLifeStats().cancelAllTasks();
		if (player.getLifeStats().isAlreadyDead()) {
			if (player.isInInstance()) {
				PlayerReviveService.instanceRevive(player);
			} else {
				PlayerReviveService.bindRevive(player);
			}
		} else if (DuelService.getInstance().isDueling(player.getObjectId())) {
			DuelService.getInstance().loseDuel(player);
		}

		Summon summon = player.getSummon();
		if (summon != null) {
			SummonsService.doMode(SummonMode.RELEASE, summon, UnsummonType.LOGOUT);
		}

		PetSpawnService.dismissPet(player, true);

		if (player.getMinion() != null) {
			MinionService.getInstance().despawnMinion(player, player.getMinion().getObjectId());	
		}

		if (player.getPostman() != null) {
			player.getPostman().getController().onDelete();
		}
		player.setPostman(null);
		PunishmentService.stopPrisonTask(player, true);
		PunishmentService.stopGatherableTask(player, true);
		if (player.isLegionMember()) {
			LegionService.getInstance().onLogout(player);
		}
		QuestEngine.getInstance().onLogOut(new QuestEnv(null, player, 0, 0));
		player.getController().delete();
		//Reset Floor "Crucible Spire 5.6"
		player.getCommonData().setFloor(0);
		player.getCommonData().setOnline(false);
		player.getCommonData().setLastOnline(new Timestamp(System.currentTimeMillis()));
		player.setClientConnection(null);
		DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, false);
		if (GSConfig.ENABLE_CHAT_SERVER) {
			ChatService.onPlayerLogout(player);
		}
		PlayerService.storePlayer(player);
		ExpireTimerTask.getInstance().removePlayer(player);
		if (player.getCraftingTask() != null) {
			player.getCraftingTask().stop(true);
		}
		player.getEquipment().setOwner(null);
		player.getInventory().setOwner(null);
		player.getWarehouse().setOwner(null);
		player.getStorage(StorageType.ACCOUNT_WAREHOUSE.getId()).setOwner(null);
		//****//
		PacketSendUtility.broadcastPacket(player, new SM_DELETE(player, 2), 50);
		PlayerAccountData pad = player.getPlayerAccount().getPlayerAccountData(player.getObjectId());
		pad.setEquipment(player.getEquipment().getEquippedItems());
		StigmaLinkedService.onLogOut(player);
		EventWindowService.getInstance().onLogout(player);
	}

	public static void tryLeaveWorld(Player player) {
		player.getMoveController().abortMove();
		if (player.getController().isInShutdownProgress()) {
			PlayerLeaveWorldService.startLeaveWorld(player);
		} else {
			int delay = 15;
			PlayerLeaveWorldService.startLeaveWorldDelay(player, (delay * 1000));
		}
	}
}