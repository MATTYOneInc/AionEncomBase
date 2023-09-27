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
package com.aionemu.gameserver.model.instance.playerreward;

/****/
/** Author Rinzler (Encom)
/****/

public class IDEventDefPlayerReward extends InstancePlayerReward
{
	private int scoreAP;
	private boolean isRewarded = false;
	private int wrapCashIDEventDefLiveSRank;
	private int wrapCashIDEventDefLiveARank;
	private int wrapCashIDEventDefLiveBRank;
	
	public IDEventDefPlayerReward(Integer object) {
		super(object);
	}
	
	public boolean isRewarded() {
		return isRewarded;
	}
	public void setRewarded() {
		isRewarded = true;
	}
	
	public int getScoreAP() {
		return scoreAP;
	}
	public void setScoreAP(int ap) {
		this.scoreAP = ap;
	}
	
	public int getWrapCashIDEventDefLiveSRank() {
		return wrapCashIDEventDefLiveSRank;
	}
	public int getWrapCashIDEventDefLiveARank() {
		return wrapCashIDEventDefLiveARank;
	}
	public int getWrapCashIDEventDefLiveBRank() {
		return wrapCashIDEventDefLiveBRank;
	}
	
	public void setWrapCashIDEventDefLiveSRank(int wrapCashIDEventDefLiveSRank) {
		this.wrapCashIDEventDefLiveSRank = wrapCashIDEventDefLiveSRank;
	}
	public void setWrapCashIDEventDefLiveARank(int wrapCashIDEventDefLiveARank) {
		this.wrapCashIDEventDefLiveARank = wrapCashIDEventDefLiveARank;
	}
	public void setWrapCashIDEventDefLiveBRank(int wrapCashIDEventDefLiveBRank) {
		this.wrapCashIDEventDefLiveBRank = wrapCashIDEventDefLiveBRank;
	}
}