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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_STATUPDATE_EXP extends AionServerPacket
{
	private long currentExp;
	private long recoverableExp;
	private long maxExp;
	private long curBoostExp = 0;
	private long maxBoostExp = 0;
	private long goldenStar;
	private long auraGrowth;
	
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp, long curBoostExp, long maxBoostExp) {
		this.currentExp = currentExp;
		this.recoverableExp = recoverableExp;
		this.maxExp = maxExp;
		this.curBoostExp = curBoostExp;
		this.maxBoostExp = maxBoostExp;
	}
	
	public SM_STATUPDATE_EXP(long currentExp, long recoverableExp, long maxExp, long curBoostExp, long maxBoostExp, long goldenStar, long auraGrowth) {
		this.currentExp = currentExp;
		this.recoverableExp = recoverableExp;
		this.maxExp = maxExp;
		this.curBoostExp = curBoostExp;
		this.maxBoostExp = maxBoostExp;
		this.goldenStar = goldenStar;
		this.auraGrowth = auraGrowth;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeQ(currentExp);
		writeQ(recoverableExp);
		writeQ(maxExp);
		writeQ(curBoostExp);
		writeQ(maxBoostExp);
		writeQ(goldenStar);
		writeQ(auraGrowth);
	}
}