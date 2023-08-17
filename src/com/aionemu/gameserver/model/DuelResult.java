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
package com.aionemu.gameserver.model;

public enum DuelResult
{
	DUEL_YOU_WIN(1300098, (byte) 2),
	DUEL_YOU_LOSE(1300099, (byte) 0),
	DUEL_TIMEOUT(1300100, (byte) 1);
	
	private int msgId;
	private byte resultId;
	
	private DuelResult(int msgId, byte resultId) {
		this.msgId = msgId;
		this.resultId = resultId;
	}
	
	public int getMsgId() {
		return msgId;
	}
	
	public byte getResultId() {
		return resultId;
	}
}