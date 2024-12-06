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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceBuff;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.robot.RobotInfo;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Ranastic (Encom)
 */

public class SM_PLAYER_SPAWN extends AionServerPacket {
	private final Player player;
	private static InstanceBuff instanceBuff;

	public SM_PLAYER_SPAWN(Player player) {
		super();
		this.player = player;
		if (player.isUseRobot() || player.getRobotId() != 0) {
			PacketSendUtility.broadcastPacketAndReceive(player,
					new SM_USE_ROBOT(player, getRobotInfo(player).getRobotId()));
		}
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getWorldId());
		writeD(player.getWorldId());
		writeD(0x00);
		writeC(WorldMapType.getWorld(player.getWorldId()).isPersonal() ? 1 : 0);
		writeF(player.getX());
		writeF(player.getY());
		writeF(player.getZ());
		writeC(player.getHeading());
		writeD(0);
		writeD(0);

		/*
		 * //[PK MOD AREA] if ((player.getWorldId() == 210020000 || player.getWorldId()
		 * == 210040000 || player.getWorldId() == 210050000 || player.getWorldId() ==
		 * 210060000 || player.getWorldId() == 210070000 || player.getWorldId() ==
		 * 210100000 || player.getWorldId() == 220020000 || player.getWorldId() ==
		 * 220040000 || player.getWorldId() == 220050000 || player.getWorldId() ==
		 * 220070000 || player.getWorldId() == 220080000 || player.getWorldId() ==
		 * 220110000 || player.getWorldId() == 400010000 || player.getWorldId() ==
		 * 400020000 || player.getWorldId() == 400040000 || player.getWorldId() ==
		 * 400050000 || player.getWorldId() == 400060000 || player.getWorldId() ==
		 * 600010000 || player.getWorldId() == 600090000 || player.getWorldId() ==
		 * 600100000)) { //This game area is a free PK area. Abide by the rules and you
		 * will have a great gaming experience. PacketSendUtility.sendPacket(player, new
		 * SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSGBOX_AKS_ENTER_PK_SERVER, 0, 0));
		 * }
		 */

		// [LIVE_Party_Match_Boost 20+]
		if ((player.getWorldId() == 300200000 || player.getWorldId() == 320100000 || player.getWorldId() == 320050000
				|| player.getWorldId() == 300030000)) {
			writeD(2);
			instanceBuff = new InstanceBuff(2);
			instanceBuff.applyPledge(player, 2);
		}
		// [LIVE_Party_Match_Boost 30+]
		else if ((player.getWorldId() == 300080000 || player.getWorldId() == 300090000
				|| player.getWorldId() == 300060000 || player.getWorldId() == 301160000
				|| player.getWorldId() == 310050000 || player.getWorldId() == 320110000
				|| player.getWorldId() == 310100000 || player.getWorldId() == 300230000
				|| player.getWorldId() == 320080000)) {
			writeD(3);
			instanceBuff = new InstanceBuff(3);
			instanceBuff.applyPledge(player, 3);
		}
		// [LIVE_Party_Match_Boost 40+]
		else if ((player.getWorldId() == 300050000 || player.getWorldId() == 300070000
				|| player.getWorldId() == 300110000 || player.getWorldId() == 300140000
				|| player.getWorldId() == 300120000 || player.getWorldId() == 300130000
				|| player.getWorldId() == 300100000 || player.getWorldId() == 300460000
				|| player.getWorldId() == 310090000 || player.getWorldId() == 320130000
				|| player.getWorldId() == 310110000 || player.getWorldId() == 300040000)) {
			writeD(4);
			instanceBuff = new InstanceBuff(4);
			instanceBuff.applyPledge(player, 4);
		}
		// [LIVE_Party_Match_Boost 50+]
		else if ((player.getWorldId() == 300210000 || player.getWorldId() == 300300000
				|| player.getWorldId() == 300320000 || player.getWorldId() == 300250000
				|| player.getWorldId() == 300160000 || player.getWorldId() == 300560000
				|| player.getWorldId() == 300190000 || player.getWorldId() == 300150000
				|| player.getWorldId() == 300170000 || player.getWorldId() == 300240000
				|| player.getWorldId() == 320150000 || player.getWorldId() == 300700000
				|| player.getWorldId() == 300280000 || player.getWorldId() == 300440000)) {
			writeD(5);
			instanceBuff = new InstanceBuff(5);
			instanceBuff.applyPledge(player, 5);
		}
		// [LIVE_Party_Match_Boost 60+]
		else if ((player.getWorldId() == 300220000 || player.getWorldId() == 300510000
				|| player.getWorldId() == 300520000 || player.getWorldId() == 300600000
				|| player.getWorldId() == 300480000 || player.getWorldId() == 301110000
				|| player.getWorldId() == 301140000 || player.getWorldId() == 300800000
				|| player.getWorldId() == 300590000 || player.getWorldId() == 301130000
				|| player.getWorldId() == 300540000 ||
				// Illuminary Obelisk & Linkgate Foundry.
				player.getWorldId() == 301230000 || player.getWorldId() == 301270000 ||
				// [Infernal] Illuminary Obelisk. & [Infernal] Danuar Reliquary.
				player.getWorldId() == 301360000 || player.getWorldId() == 301370000 ||
				// [Occupied] Rentus Base. & [Anguished] Dragon Lord's Refuge. & Raksang Ruins.
				// & Danuar Sanctuary.
				player.getWorldId() == 300610000 || player.getWorldId() == 300620000 || player.getWorldId() == 300630000
				|| player.getWorldId() == 301380000 ||
				// Drakenspire Depths. & Sealed Argent Manor.
				player.getWorldId() == 301390000 || player.getWorldId() == 301510000 ||
				// Archives/Cradle/Trials Of Eternity.
				player.getWorldId() == 301540000 || player.getWorldId() == 301550000 || player.getWorldId() == 301560000
				||
				// Adma's Fall. & Theobomos Test Chamber.
				player.getWorldId() == 301600000 || player.getWorldId() == 301610000 ||
				// Drakenseer's Lair. & Ashunatal Dredgion.
				player.getWorldId() == 301620000 || player.getWorldId() == 301650000 ||
				// Fallen Poeta & Fissure Of Oblivion.
				player.getWorldId() == 301660000 || player.getWorldId() == 302100000 ||
				// Mirash Sanctuary & Divine Tower L & Divine Tower D.
				player.getWorldId() == 301720000 || player.getWorldId() == 310160000 || player.getWorldId() == 320160000
				||
				// Dredgion Defense: Sanctum/Pandaemonium.
				player.getWorldId() == 302200000 || player.getWorldId() == 302300000 ||
				// Bastion Of Souls. & Crucible Spire.
				player.getWorldId() == 302340000 || player.getWorldId() == 302400000)) {
			writeD(6);
			instanceBuff = new InstanceBuff(6);
			instanceBuff.applyPledge(player, 6);
		}
		// [Arena Pvp]
		else if ((player.getWorldId() == 210120000 || player.getWorldId() == 220130000
				|| player.getWorldId() == 300350000 || player.getWorldId() == 300360000
				|| player.getWorldId() == 300420000 || player.getWorldId() == 300430000
				|| player.getWorldId() == 300450000 || player.getWorldId() == 300550000
				|| player.getWorldId() == 300570000 || player.getWorldId() == 301100000
				|| player.getWorldId() == 302310000 || player.getWorldId() == 302360000
				|| player.getWorldId() == 302380000 || player.getWorldId() == 302410000)) {
			writeD(9);
			instanceBuff = new InstanceBuff(9);
			instanceBuff.applyPledge(player, 9);
		}
		// [Lucky] Ophidan Bridge & [Lucky] Danuar Reliquary.
		else if ((player.getWorldId() == 301320000 || player.getWorldId() == 301330000 ||
		// Shugo Emperor's Vault. & Emperor Trillirunerk's Safe.
				player.getWorldId() == 301400000 || player.getWorldId() == 301590000 ||
				// Contaminated Underpath & IDEvent_Def & Secret Munitions Factory.
				player.getWorldId() == 301630000 || player.getWorldId() == 301631000 || player.getWorldId() == 301640000
				||
				// Smoldering Fire Temple. & [Opportunity] Fissure Of Oblivion.
				player.getWorldId() == 302000000 || player.getWorldId() == 302110000 ||
				// Kumuki Cave. & IDStation_Event.
				player.getWorldId() == 302330000 || player.getWorldId() == 300241000)) {
			writeD(14);
			instanceBuff = new InstanceBuff(14);
			instanceBuff.applyPledge(player, 14);
		}
		// Evergale Canyon.
		else if ((player.getWorldId() == 302350000)) {
			writeD(17);
			instanceBuff = new InstanceBuff(17);
			instanceBuff.applyPledge(player, 17);
		} else {
			writeD(0);
			if (player.getBonusId() > 0) {
				instanceBuff.endPledge(player);
			}
		}
		if (World.getInstance().getWorldMap(player.getWorldId()).getTemplate().getBeginnerTwinCount() > 0) {
			writeC(1);
		} else {
			writeC(0);
		}
		writeC(0);
		writeD(0);
		writeC(0);
	}

	public static RobotInfo getRobotInfo(Player player) {
		ItemTemplate template = player.getEquipment().getMainHandWeapon().getItemSkinTemplate();
		return DataManager.ROBOT_DATA.getRobotInfo(template.getRobotId());
	}
}
