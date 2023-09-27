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
package com.aionemu.gameserver.model.skill;

class NpcSkillParameterEntry extends NpcSkillEntry {

	public NpcSkillParameterEntry(int skillId, int skillLevel) {
		super(skillId, skillLevel);
	}

	@Override
	public boolean isReady(int hpPercentage, long fightingTimeInMSec) {
		return true;
	}

	@Override
	public boolean chanceReady() {
		return true;
	}

	@Override
	public boolean hpReady(int hpPercentage) {
		return true;
	}

	@Override
	public boolean timeReady(long fightingTimeInMSec) {
		return true;
	}

	@Override
	public boolean hasCooldown() {
		return false;
	}

	@Override
	public boolean UseInSpawned() {
		return true;
	}
}