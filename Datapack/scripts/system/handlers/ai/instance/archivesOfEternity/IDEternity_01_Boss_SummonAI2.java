/*
 * This file is part of Encom.
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
package ai.instance.archivesOfEternity;

import ai.ActionItemNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("IDEternity_01_Boss_Summon")
public class IDEternity_01_Boss_SummonAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 857869: //IDEternity_01_Boss_Summon_01.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon1();
					    spawn(857453, 457.21417f, 668.04736f, 468.97745f, (byte) 14); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon1();
					    spawn(857457, 457.21417f, 668.04736f, 468.97745f, (byte) 14); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857870: //IDEternity_01_Boss_Summon_02.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon2();
					    spawn(857453, 457.56555f, 675.5612f, 468.97745f, (byte) 107); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon2();
					    spawn(857457, 457.56555f, 675.5612f, 468.97745f, (byte) 107); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857871: //IDEternity_01_Boss_Summon_03.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon3();
					    spawn(857453, 464.77176f, 675.26624f, 468.97745f, (byte) 77); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon3();
					    spawn(857457, 464.77176f, 675.26624f, 468.97745f, (byte) 77); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
            break;
			case 857872: //IDEternity_01_Boss_Summon_04.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon4();
						spawn(857453, 464.6033f, 667.9444f, 468.97745f, (byte) 48); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon4();
					    spawn(857457, 464.6033f, 667.9444f, 468.97745f, (byte) 48); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857885: //IDEternity_01_Boss_Summon_05.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon1();
						spawn(857453, 548.9518f, 508.43805f, 468.97675f, (byte) 16); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon1();
						spawn(857457, 548.9518f, 508.43805f, 468.97675f, (byte) 16); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857886: //IDEternity_01_Boss_Summon_06.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon2();
						spawn(857453, 549.34735f, 516.0454f, 468.97675f, (byte) 107); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon2();
					    spawn(857457, 549.34735f, 516.0454f, 468.97675f, (byte) 107); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857887: //IDEternity_01_Boss_Summon_07.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon3();
					    spawn(857453, 556.68805f, 515.914f, 468.97675f, (byte) 73); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon3();
					    spawn(857457, 556.68805f, 515.914f, 468.97675f, (byte) 73); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857888: //IDEternity_01_Boss_Summon_08.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon4();
					    spawn(857453, 556.3089f, 508.03238f, 468.97675f, (byte) 45); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon4();
					    spawn(857457, 556.3089f, 508.03238f, 468.97675f, (byte) 45); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857889: //IDEternity_01_Boss_Summon_09.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon1();
					    spawn(857453, 457.088f, 348.71902f, 468.9799f, (byte) 15); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon1();
					    spawn(857457, 457.088f, 348.71902f, 468.9799f, (byte) 15); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857890: //IDEternity_01_Boss_Summon_10.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon2();
					    spawn(857453, 457.00275f, 356.12473f, 468.9799f, (byte) 108); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon2();
					    spawn(857457, 457.00275f, 356.12473f, 468.9799f, (byte) 108); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857891: //IDEternity_01_Boss_Summon_11.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon3();
					    spawn(857453, 465.28088f, 355.1954f, 468.9799f, (byte) 74); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon3();
					    spawn(857457, 465.28088f, 355.1954f, 468.9799f, (byte) 74); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857892: //IDEternity_01_Boss_Summon_12.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon4();
						spawn(857453, 464.42667f, 348.39755f, 468.9799f, (byte) 46); //IDEternity_01_Leibo_Fire_Summon_69_Ae.
					break;
					case 2:
					    announceSummon4();
					    spawn(857457, 464.42667f, 348.39755f, 468.9799f, (byte) 46); //IDEternity_01_Leibo_Wind_Summon_69_Ae.
					break;
				}
			break;
			case 857893: //IDEternity_01_Boss_Summon_13.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon1();
						spawn(857461, 253.1741f, 509.72418f, 468.8499f, (byte) 14); //IDEternity_01_Leibo_Fire_Zone_Summon_66_Ae.
					break;
					case 2:
					    announceSummon1();
					    spawn(857463, 253.1741f, 509.72418f, 468.8499f, (byte) 14); //IDEternity_01_Leibo_Wind_Zone_Summon_66_Ae.
					break;
				}
			break;
			case 857894: //IDEternity_01_Boss_Summon_14.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon2();
					    spawn(857461, 254.42969f, 516.1427f, 468.85364f, (byte) 106); //IDEternity_01_Leibo_Fire_Zone_Summon_66_Ae.
					break;
					case 2:
					    announceSummon2();
					    spawn(857463, 254.42969f, 516.1427f, 468.85364f, (byte) 106); //IDEternity_01_Leibo_Wind_Zone_Summon_66_Ae.
					break;
				}
			break;
			case 857895: //IDEternity_01_Boss_Summon_15.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon3();
						spawn(857461, 259.76352f, 515.1004f, 468.85147f, (byte) 75); //IDEternity_01_Leibo_Fire_Zone_Summon_66_Ae.
					break;
					case 2:
					    announceSummon3();
						spawn(857463, 259.76352f, 515.1004f, 468.85147f, (byte) 75); //IDEternity_01_Leibo_Wind_Zone_Summon_66_Ae.
					break;
				}
			break;
			case 857896: //IDEternity_01_Boss_Summon_16.
				switch (Rnd.get(1, 2)) {
					case 1:
					    announceSummon4();
						spawn(857461, 259.4065f, 509.59738f, 468.85f, (byte) 45); //IDEternity_01_Leibo_Fire_Zone_Summon_66_Ae.
					break;
					case 2:
					    announceSummon4();
					    spawn(857463, 259.4065f, 509.59738f, 468.85f, (byte) 45); //IDEternity_01_Leibo_Wind_Zone_Summon_66_Ae.
					break;
				}
			break;
		}
		AI2Actions.deleteOwner(this);
		AI2Actions.scheduleRespawn(this);
	}
	
	private void announceSummon1() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The First Column of Dominion  has been activated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_01_On, 0);
				    //The First Column of Dominion has been deactivated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_01_Off, 2000);
				}
			}
		});
	}
	private void announceSummon2() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Second Column of Dominion  has been activated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_02_On, 0);
				    //The Second Column of Dominion has been deactivated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_02_Off, 2000);
				}
			}
		});
	}
	private void announceSummon3() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Third Column of Dominion  has been activated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_03_On, 0);
				    //The Third Column of Dominion has been deactivated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_03_Off, 2000);
				}
			}
		});
	}
	private void announceSummon4() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Fourth Column of Dominion  has been activated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_04_On, 0);
				    //The Fourth Column of Dominion has been deactivated.
				    PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_01_Summon_Ctrl_04_Off, 2000);
				}
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}