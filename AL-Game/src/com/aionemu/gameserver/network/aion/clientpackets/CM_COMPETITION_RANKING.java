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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Ranastic
 */
public class CM_COMPETITION_RANKING extends AionClientPacket {
	private static Logger log = LoggerFactory.getLogger(CM_COMPETITION_RANKING.class);
	private int unk1;
	private int unk2;

	public CM_COMPETITION_RANKING(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		/*
		 * Hall Of Tenacity: 01 00 00 00 00 Arena Of Discipline: 1D 02 00 00 00 3rd
		 * board: 02 00 00 00 00 4th board: 03 00 00 00 00 My history: 03 00 00 00
		 */
		unk1 = readD();
		unk2 = readC();
	}

	@Override
	protected void runImpl() {
		final Player player = this.getConnection().getActivePlayer();
		log.info("unk1:" + unk1 + " unk2:" + unk2);
	}
}