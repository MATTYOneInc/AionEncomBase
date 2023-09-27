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
package com.aionemu.gameserver.skillengine.model;

public class ChainSkill {

	private String category;
	private int chainCount = 0;
	private long useTime;

	public ChainSkill(String category, int chainCount, long useTime) {
		this.category = category;
		this.chainCount = chainCount;
		this.useTime = useTime;
	}

	public void updateChainSkill(String category) {
		this.category = category;
		chainCount = 0;
		useTime = System.currentTimeMillis();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String name) {
		category = name;
	}

	public int getChainCount() {
		return chainCount;
	}

	public void setChainCount(int chainCount) {
		this.chainCount = chainCount;
	}

	public void increaseChainCount() {
		chainCount ++;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}
}