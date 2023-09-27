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

import com.aionemu.gameserver.model.Petition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PETITION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.PetitionService;

/**
 * @author zdead
 */
public class CM_PETITION extends AionClientPacket {

	private int action;
	private String title = "";
	private String text = "";
	private String additionalData = "";

	public CM_PETITION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readH();
		if (action == 2) {
			readD();
		}
		else {
			String data = readS();
			String[] dataArr = data.split("/", 3);
			title = dataArr[0];
			text = dataArr[1];
			additionalData = dataArr[2];
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		int playerObjId =  player.getObjectId();
		if (action == 2) {
			if (PetitionService.getInstance().hasRegisteredPetition(playerObjId)) {
				int petitionId = PetitionService.getInstance().getPetition(playerObjId).getPetitionId();
				PetitionService.getInstance().deletePetition(playerObjId);
				sendPacket(new SM_SYSTEM_MESSAGE(1300552, petitionId));
				sendPacket(new SM_SYSTEM_MESSAGE(1300553, 49));
				return;
			}
		}

		if (!PetitionService.getInstance().hasRegisteredPetition(playerObjId)) {
			Petition petition = PetitionService.getInstance().registerPetition(player, action, title, text, additionalData);
			sendPacket(new SM_PETITION(petition));
		}
	}
}