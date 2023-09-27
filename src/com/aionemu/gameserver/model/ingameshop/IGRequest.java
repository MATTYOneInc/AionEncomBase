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
package com.aionemu.gameserver.model.ingameshop;

/**
 * @author KID
 */
public class IGRequest {

	public boolean gift = false, sync = false;
	public int playerId;
	public int cost;
	public int requestId, itemObjId;
	public String receiver, message;
	public int accountId;

	public IGRequest(int requestId, int playerId, int itemObjId) {
		this.requestId = requestId;
		this.playerId = playerId;
		this.itemObjId = itemObjId;
	}

	public IGRequest(int requestId, int playerId, String receiver, String message, int itemObjId) {
		this.requestId = requestId;
		this.playerId = playerId;
		this.receiver = receiver;
		this.message = message;
		this.itemObjId = itemObjId;
		gift = true;
	}

	public IGRequest(int requestId, int playerId, int cost, boolean a) {
		this.requestId = requestId;
		this.playerId = playerId;
		this.cost = cost;
		sync = a;
	}
}