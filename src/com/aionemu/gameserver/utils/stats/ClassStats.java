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
package com.aionemu.gameserver.utils.stats;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.utils.stats.enums.*;

public class ClassStats
{
	public static int getMaxHpFor(PlayerClass playerClass, int level) {
		return MAXHP.valueOf(playerClass.toString()).getMaxHpFor(level);
	}
	
	public static int getPowerFor(PlayerClass playerClass) {
		return POWER.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getHealthFor(PlayerClass playerClass) {
		return HEALTH.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getAgilityFor(PlayerClass playerClass) {
		return AGILITY.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getAccuracyFor(PlayerClass playerClass) {
		return ACCURACY.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getKnowledgeFor(PlayerClass playerClass) {
		return KNOWLEDGE.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getWillFor(PlayerClass playerClass) {
		return WILL.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getMainHandAttackFor(PlayerClass playerClass) {
		return MAIN_HAND_ATTACK.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getMainHandCritRateFor(PlayerClass playerClass) {
		return MAIN_HAND_CRITRATE.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getMainHandAccuracyFor(PlayerClass playerClass) {
		return MAIN_HAND_ACCURACY.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getWaterResistFor(PlayerClass playerClass) {
		return WATER_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getWindResistFor(PlayerClass playerClass) {
		return WIND_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getEarthResistFor(PlayerClass playerClass) {
		return EARTH_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getFireResistFor(PlayerClass playerClass) {
		return FIRE_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getMagicAccuracyFor(PlayerClass playerClass) {
		return MAGIC_ACCURACY.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getCritSpellFor(PlayerClass playerClass) {
		return CRIT_SPELL.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getSpellResistFor(PlayerClass playerClass) {
		return SPELL_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getStrikeResistFor(PlayerClass playerClass) {
		return STRIKE_RESIST.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getEvasionFor(PlayerClass playerClass) {
		return EVASION.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getBlockFor(PlayerClass playerClass) {
		return BLOCK.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getParryFor(PlayerClass playerClass) {
		return PARRY.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getAttackRangeFor(PlayerClass playerClass) {
		return ATTACK_RANGE.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getAttackSpeedFor(PlayerClass playerClass) {
		return ATTACK_SPEED.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getFlySpeedFor(PlayerClass playerClass) {
		return FLY_SPEED.valueOf(playerClass.toString()).getValue();
	}
	
	public static int getSpeedFor(PlayerClass playerClass) {
		return SPEED.valueOf(playerClass.toString()).getValue();
	}
}