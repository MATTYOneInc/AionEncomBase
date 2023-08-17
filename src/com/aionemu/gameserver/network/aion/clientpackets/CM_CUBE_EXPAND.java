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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic (Encom)
 */

public class CM_CUBE_EXPAND extends AionClientPacket
{
	int type;
	
	public CM_CUBE_EXPAND(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		type = readC();
	}
	
	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		if (type == 0) { //Kinah
			if (activePlayer.getNpcExpands() < 15) {
				if (activePlayer.getNpcExpands() == 0) {
					if (activePlayer.getInventory().tryDecreaseKinah(1000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 1) {
					if (activePlayer.getInventory().tryDecreaseKinah(10000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 2) {
					if (activePlayer.getInventory().tryDecreaseKinah(50000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 3) {
					if (activePlayer.getInventory().tryDecreaseKinah(150000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 4) {
					if (activePlayer.getInventory().tryDecreaseKinah(300000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 5) {
					if (activePlayer.getInventory().tryDecreaseKinah(3000000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 6) {
					if (activePlayer.getInventory().tryDecreaseKinah(6000000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 7) {
					if (activePlayer.getInventory().tryDecreaseKinah(12000000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 8) {
					if (activePlayer.getInventory().tryDecreaseKinah(24000000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				} else if (activePlayer.getNpcExpands() == 9) {
					if (activePlayer.getInventory().tryDecreaseKinah(48000000)) {
						CubeExpandService.expand(activePlayer, true);
					}
				}
			}
		}
		//Cube Expansion Coin.
		else if (type == 1) {
			if (activePlayer.getNpcExpands() < 10) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			} else if (activePlayer.getNpcExpands() < 11) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			} else if (activePlayer.getNpcExpands() < 12) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			} else if (activePlayer.getNpcExpands() < 13) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			} else if (activePlayer.getNpcExpands() < 14) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			} else if (activePlayer.getNpcExpands() < 15) {
				if (activePlayer.getInventory().decreaseByItemId(186000419, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000440, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000444, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				} else if (activePlayer.getInventory().decreaseByItemId(186000445, 5)) { //Cube Expansion Coin.
					CubeExpandService.expand(activePlayer, true);
				}
			}
		}
	}
}