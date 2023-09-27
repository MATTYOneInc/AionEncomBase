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
package com.aionemu.gameserver.model.gameobjects.player;

/**
 * @author Ranastic
 */
public class PlayerUpgradeArcade
{
	private int frenzyPoints = 0;
	private int frenzyCount = 0;
	private int frenzyLevel = 1;
	private int failedLevel = 1;
	private boolean isFrenzy = false;
	private boolean reTry = false;
	private boolean failed = false;
	
	public int getFrenzyPoints() {
		return frenzyPoints;
	}
	
	public void setFrenzyPoints(int frenzyPoints) {
		this.frenzyPoints = frenzyPoints;
	}
	
	public int getFrenzyCount() {
		return frenzyCount;
	}
	
	public void setFrenzyCount(int frenzyCount) {
		this.frenzyCount = frenzyCount;
	}
	
	public int getFrenzyLevel() {
		return frenzyLevel;
	}
	
	public void setFrenzyLevel(int frenzyLevel) {
		this.frenzyLevel = frenzyLevel;
	}
	
	public int getFailedLevel() {
		return failedLevel;
	}
	
	public void setFailedLevel(int failedLevel) {
		this.failedLevel = failedLevel;
	}
	
	public boolean isFrenzy() {
		return isFrenzy;
	}
	
	public void setFrenzy(boolean isFrenzy) {
		this.isFrenzy = isFrenzy;
	}
	
	public boolean isReTry() {
		return reTry;
	}
	
	public void setReTry(boolean reTry) {
		this.reTry = reTry;
	}
	
	public boolean isFailed() {
		return failed;
	}
	
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	
	public void reset() {
		this.isFrenzy = false;
		this.failed = false;
		this.frenzyLevel = 1;
		this.failedLevel = 1;
		this.reTry = false;
	}
}