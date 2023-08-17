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
package com.aionemu.gameserver.services.abyss;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_EDIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

public class AbyssPointsService
{
	@GlobalCallback(AddAPGlobalCallback.class)
	public static void addAp(Player player, VisibleObject obj, int value) {
		addAp(player, value);
	}
	
	@GlobalCallback(AddGPGlobalCallback.class)
	public static void addGp(Player player, VisibleObject obj, int value) {
		addGp(player, value);
	}
	
	public static void addAp(Player player, int value) {
		if (player == null) {
			return;
		} if (value > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_COMBAT_MY_ABYSS_POINT_GAIN(value));
		} else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300965, value *-1));
		}
		setAp(player, value);
		if (player.isLegionMember() && value > 0) {
			player.getLegion().addContributionPoints(value);
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_EDIT(0x03, player.getLegion()));
		}
	}
	
	public static void addGp(Player player, int value) {
		if (player == null) {
			return;
		} if (value > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GLORY_POINT_GAIN(value));
		} else {
		   PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402219, value *-1));
		}
		setGp(player, value);
	}
	
	public static void addAGp(Player player, int ap, int gp) {
		if (player == null) {
			return;
		} if (ap > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_COMBAT_MY_ABYSS_POINT_GAIN(ap));
		} else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300965, ap *-1));
		}
		setAp(player, ap);
		if (player.isLegionMember() && ap > 0) {
			player.getLegion().addContributionPoints(ap);
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_EDIT(0x03, player.getLegion()));
		} if (gp > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GLORY_POINT_GAIN(gp));
		} else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402219, gp *-1));
		}
		setGp(player, gp);
	}
	
	public static void setAp(Player player, int value) {
		if (player == null) {
			return;
		}
		AbyssRank rank = player.getAbyssRank();
		AbyssRankEnum oldAbyssRank = rank.getRank();
		rank.addAp(value, player);
		AbyssRankEnum newAbyssRank = rank.getRank();
		checkRankChanged(player, oldAbyssRank, newAbyssRank);
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
	}
	
	public static void setGp(Player player, int value) {
		if (player == null) {
			return;
		}
		AbyssRank rank = player.getAbyssRank();
		rank.addGp(value);
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
	}
	
	public static void checkRankChanged(Player player, AbyssRankEnum oldAbyssRank, AbyssRankEnum newAbyssRank) {
		if (oldAbyssRank == newAbyssRank) {
			return;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
		player.getEquipment().checkRankLimitItems();
	}
	
	public static void checkRankGpChanged(Player player, AbyssRankEnum oldGloryRank, AbyssRankEnum newGloryRank) {
		if (oldGloryRank == newGloryRank) {
			return;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
		player.getEquipment().checkRankLimitItems();
		AbyssSkillService.updateSkills(player);
	}
	
	public static void AbyssRankCheck (Player player) {
		if (player == null) {
			return;
		} if (player.getAbyssRank().getGp() < AbyssRankEnum.STAR1_OFFICER.getGpRequired()) {
			if (player.getAbyssRank().getAp() < 1200) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE9_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 1200 && player.getAbyssRank().getAp() < 4220) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE8_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 4220 && player.getAbyssRank().getAp() < 10990) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE7_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 10990 && player.getAbyssRank().getAp() < 23500) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE6_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 23500 && player.getAbyssRank().getAp() < 42780) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE5_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 42780 && player.getAbyssRank().getAp() < 69700) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE4_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 69700 && player.getAbyssRank().getAp() < 105600) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE3_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 105600 && player.getAbyssRank().getAp() < 150800) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE2_SOLDIER);
			} else if (player.getAbyssRank().getAp() >= 150800) {
				player.getAbyssRank().setRank(AbyssRankEnum.GRADE1_SOLDIER);
			}
			PacketSendUtility.broadcastPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK_UPDATE(0, player));
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank()));
		}
	}
	
   /**
	* <Abyss Point>
	*/
	@SuppressWarnings("rawtypes")
	public abstract static class AddAPGlobalCallback implements Callback {
		@Override
		public CallbackResult beforeCall(Object obj, Object[] args) {
			return CallbackResult.newContinue();
		}
		@Override
		public CallbackResult afterCall(Object obj, Object[] args, Object methodResult) {
			Player player = (Player) args[0];
			VisibleObject creature = (VisibleObject) args[1];
			int abyssPoints = (Integer) args[2];
			if ((creature instanceof Player)) {
				onAbyssPointsAdded(player, abyssPoints);
			} else if (((creature instanceof SiegeNpc)) && (!((SiegeNpc) creature).getSpawn().isPeace())) {
				onAbyssPointsAdded(player, abyssPoints);
			}
			return CallbackResult.newContinue();
		}
		@Override
		public Class<? extends Callback> getBaseClass() {
			return AddAPGlobalCallback.class;
		}
		public abstract void onAbyssPointsAdded(Player player, int abyssPoints);
	}
	
   /**
	* <Glory Point>
	*/
	@SuppressWarnings("rawtypes")
	public abstract static class AddGPGlobalCallback implements Callback {
		@Override
		public CallbackResult beforeCall(Object obj, Object[] args) {
			return CallbackResult.newContinue();
		}
		@Override
		public CallbackResult afterCall(Object obj, Object[] args, Object methodResult) {
			Player player = (Player) args[0];
			VisibleObject creature = (VisibleObject) args[1];
			int gloryPoints = (Integer) args[2];
			if ((creature instanceof Player)) {
				onGloryPointsAdded(player, gloryPoints);
			} else if (((creature instanceof SiegeNpc)) && (!((SiegeNpc) creature).getSpawn().isPeace())) {
				onGloryPointsAdded(player, gloryPoints);
			}
			return CallbackResult.newContinue();
		}
		@Override
		public Class<? extends Callback> getBaseClass() {
			return AddGPGlobalCallback.class;
		}
		public abstract void onGloryPointsAdded(Player player, int gloryPoints);
	}
}