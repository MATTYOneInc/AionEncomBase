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
package com.aionemu.gameserver.model.templates.stats;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.utils.stats.ClassStats;

public class CalculatedPlayerStatsTemplate extends PlayerStatsTemplate {
	private PlayerClass playerClass;

	public CalculatedPlayerStatsTemplate(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}

	@Override
	public int getAccuracy() {
		return ClassStats.getAccuracyFor(playerClass);
	}

	@Override
	public int getAgility() {
		return ClassStats.getAgilityFor(playerClass);
	}

	public float getAttackRange() {
		return ClassStats.getAttackRangeFor(playerClass) / 1500f;
	}

	@Override
	public float getAttackSpeed() {
		return ClassStats.getAttackSpeedFor(playerClass) / 1000f;
	}

	@Override
	public int getBlock() {
		return ClassStats.getBlockFor(playerClass);
	}

	public int getCritSpell() {
		return ClassStats.getCritSpellFor(playerClass);
	}

	@Override
	public int getEvasion() {
		return ClassStats.getEvasionFor(playerClass);
	}

	@Override
	public float getFlySpeed() {
		return ClassStats.getFlySpeedFor(playerClass);
	}

	@Override
	public int getHealth() {
		return ClassStats.getHealthFor(playerClass);
	}

	@Override
	public int getKnowledge() {
		return ClassStats.getKnowledgeFor(playerClass);
	}

	@Override
	public int getMagicAccuracy() {
		return ClassStats.getMagicAccuracyFor(playerClass);
	}

	@Override
	public int getMainHandAccuracy() {
		return ClassStats.getMainHandAccuracyFor(playerClass);
	}

	@Override
	public int getMainHandAttack() {
		return ClassStats.getMainHandAttackFor(playerClass);
	}

	@Override
	public int getMainHandCritRate() {
		return ClassStats.getMainHandCritRateFor(playerClass);
	}

	@Override
	public int getMaxHp() {
		return ClassStats.getMaxHpFor(playerClass, 17); // 10
	}

	@Override
	public int getMaxMp() {
		return 1000;
	}

	@Override
	public int getParry() {
		return ClassStats.getParryFor(playerClass);
	}

	@Override
	public int getPower() {
		return ClassStats.getPowerFor(playerClass);
	}

	public float getSpeed() {
		return ClassStats.getSpeedFor(playerClass);
	}

	@Override
	public int getSpellResist() {
		return ClassStats.getSpellResistFor(playerClass);
	}

	@Override
	public int getStrikeResist() {
		return ClassStats.getStrikeResistFor(playerClass);
	}

	@Override
	public int getWill() {
		return ClassStats.getWillFor(playerClass);
	}

	@Override
	public float getWalkSpeed() {
		return 1.5f;
	}
}