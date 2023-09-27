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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.PvPModConfig;
import com.aionemu.gameserver.model.autogroup.EntryRequestType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.services.instance.AsyunatarService;
import com.aionemu.gameserver.services.instance.DredgionService2;
import com.aionemu.gameserver.services.instance.EngulfedOphidanBridgeService;
import com.aionemu.gameserver.services.instance.HallOfTenacityService;
import com.aionemu.gameserver.services.instance.GrandArenaTrainingCampService;
import com.aionemu.gameserver.services.instance.IDRunService;
import com.aionemu.gameserver.services.instance.IdgelDomeLandmarkService;
import com.aionemu.gameserver.services.instance.IdgelDomeService;
import com.aionemu.gameserver.services.instance.IronWallWarfrontService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.SuspiciousOphidanBridgeService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_AUTO_GROUP extends AionClientPacket
{
	private Logger log = LoggerFactory.getLogger(CM_AUTO_GROUP.class);
	private byte instanceMaskId;
	private byte windowId;
	private byte entryRequestId;
	
	public CM_AUTO_GROUP(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		instanceMaskId = (byte) readD();
		windowId = (byte) readC();
		entryRequestId = (byte) readC();
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (!AutoGroupConfig.AUTO_GROUP_ENABLED) {
			PacketSendUtility.sendMessage(player, "Auto Group is disabled");
			return;
		} switch (windowId) {
			case 100:
				EntryRequestType ert = EntryRequestType.getTypeById(entryRequestId);
				if (ert == null) {
					return;
				}
				AutoGroupService.getInstance().startLooking(player, instanceMaskId, ert);
			break;
			case 101:
				AutoGroupService.getInstance().unregisterLooking(player, instanceMaskId);
			break;
			case 102:
				AutoGroupService.getInstance().pressEnter(player, instanceMaskId);
			break;
			case 103:
				AutoGroupService.getInstance().cancelEnter(player, instanceMaskId);
			break;
			case 104:
                DredgionService2.getInstance().showWindow(player, instanceMaskId);
                KamarBattlefieldService.getInstance().showWindow(player, instanceMaskId);
			    EngulfedOphidanBridgeService.getInstance().showWindow(player, instanceMaskId);
				IronWallWarfrontService.getInstance().showWindow(player, instanceMaskId);
				IdgelDomeService.getInstance().showWindow(player, instanceMaskId);
				//Ver. 5.1
				AsyunatarService.getInstance().showWindow(player, instanceMaskId);
				IdgelDomeLandmarkService.getInstance().showWindow(player, instanceMaskId);
				SuspiciousOphidanBridgeService.getInstance().showWindow(player, instanceMaskId);
				//Ver. 5.3
				HallOfTenacityService.getInstance().showWindow(player, instanceMaskId);
				//Ver. 5.6
				GrandArenaTrainingCampService.getInstance().showWindow(player, instanceMaskId);
				//Ver. 5.8
				IDRunService.getInstance().showWindow(player, instanceMaskId);
			break;
			case 105:
			break;
		} if (PvPModConfig.BG_ENABLED) {
			LadderService.getInstance().handleWindow(player, windowId, entryRequestId);
		}
	}
}