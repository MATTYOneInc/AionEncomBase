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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.ArmsfusionService;

/**
 * @author zdead modified by Wakizashi
 */
public class CM_FUSION_WEAPONS extends AionClientPacket {

	public CM_FUSION_WEAPONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	private int firstItemId;
	private int secondItemId;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		readD();
		firstItemId = readD();
		secondItemId = readD();
		/*
		 * Temporary: fusion price fixed to 50000 kinah TODO: find price value
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		ArmsfusionService.fusionWeapons(getConnection().getActivePlayer(), firstItemId, secondItemId);
	}
}