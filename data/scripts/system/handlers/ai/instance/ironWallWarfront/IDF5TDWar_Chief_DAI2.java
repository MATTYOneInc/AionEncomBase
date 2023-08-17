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
package ai.instance.ironWallWarfront;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("IDF5TDWar_chief_d")
public class IDF5TDWar_Chief_DAI2 extends AggressiveNpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 233498: //Asmodians Supply Base Beta Officer.
				    announceIDF5TDWarDV01();
				break;
				case 233499: //Asmodians Military Supply Base Officer.
				    announceIDF5TDWarDV02();
				break;
				case 233500: //Asmodians Supply Base Alpha Officer.
					announceIDF5TDWarDV03();
				break;
				case 233501: //Asmodians Artillery Base Officer.
					announceIDF5TDWarDV04();
				break;
				case 233502: //Asmodians Sentry Post Alpha Officer.
					announceIDF5TDWarDV05();
				break;
				case 233503: //Asmodians Sentry Post Beta Officer.
					announceIDF5TDWarDV06();
				break;
				case 233504: //Asmodians Holy Grounds Officer.
					announceIDF5TDWarDV07();
				break;
				case 233505: //Asmodians Command Center Officer.
					announceIDF5TDWarDV08();
				break;
				case 233506: //Asmodians Headquarters Alpha Officer.
					announceIDF5TDWarDV09();
				break;
				case 233507: //Asmodians Headquarters Beta Officer.
					announceIDF5TDWarDV10();
				break;
			}
		}
	}
	
	private void announceIDF5TDWarDV01() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_01);
				}
			}
		});
	}
	private void announceIDF5TDWarDV02() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_02);
				}
			}
		});
	}
	private void announceIDF5TDWarDV03() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_03);
				}
			}
		});
	}
	private void announceIDF5TDWarDV04() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_04);
				}
			}
		});
	}
	private void announceIDF5TDWarDV05() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_05);
				}
			}
		});
	}
	private void announceIDF5TDWarDV06() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_06);
				}
			}
		});
	}
	private void announceIDF5TDWarDV07() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_07);
				}
			}
		});
	}
	private void announceIDF5TDWarDV08() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_08);
				}
			}
		});
	}
	private void announceIDF5TDWarDV09() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_09);
				}
			}
		});
	}
	private void announceIDF5TDWarDV10() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_TD_War_Officer_Da_10);
				}
			}
		});
	}
}