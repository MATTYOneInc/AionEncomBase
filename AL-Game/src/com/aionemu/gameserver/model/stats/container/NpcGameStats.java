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
package com.aionemu.gameserver.model.stats.container;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.SummonedObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.templates.npc.NpcRating;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xavier
 */
public class NpcGameStats extends CreatureGameStats<Npc> {

	int currentRunSpeed = 0;
	private long lastAttackTime = 0;
	private long lastAttackedTime = 0;
	private long nextAttackTime = 0;
	private long lastSkillTime = 0;
	private long fightStartingTime = 0;
	private int cachedState;
	private Stat2 cachedSpeedStat;
	private long lastGeoZUpdate;
	private long lastChangeTarget = 0;
	private int pAccuracy = 0;
	private int mRes = 0;

	public NpcGameStats(Npc owner) {
		super(owner);
	}

	@Override
	protected void onStatsChange() {
		checkSpeedStats();
	}

	private void checkSpeedStats() {
		Stat2 oldSpeed = cachedSpeedStat;
		cachedSpeedStat = null;
		Stat2 newSpeed = getMovementSpeed();
		cachedSpeedStat = newSpeed;
		if (oldSpeed == null || oldSpeed.getCurrent() != newSpeed.getCurrent()) {
			owner.addPacketBroadcastMask(BroadcastMode.UPDATE_SPEED);
		}
	}

	@Override
	public Stat2 getMaxHp() {
		return getStat(StatEnum.MAXHP, owner.getObjectTemplate().getStatsTemplate().getMaxHp());
	}

	@Override
	public Stat2 getMaxMp() {
		return getStat(StatEnum.MAXMP, owner.getObjectTemplate().getStatsTemplate().getMaxMp());
	}

	@Override
	public Stat2 getAttackSpeed() {
		return getStat(StatEnum.ATTACK_SPEED, owner.getObjectTemplate().getAttackDelay());
	}

	public Stat2 getStrikeResist() {
		return getStat(StatEnum.PHYSICAL_CRITICAL_RESIST, 0);
	}

	public Stat2 getStrikeFort() {
		return getStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE, 0);
	}

	public Stat2 getSpellResist() {
		return getStat(StatEnum.MAGICAL_CRITICAL_RESIST, 0);
	}

	public Stat2 getSpellFort() {
		return getStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE, 0);
	}

	public Stat2 getBCastingTime() {
		return getStat(StatEnum.BOOST_CASTING_TIME, 1000);
	}

	public Stat2 getConcentration() {
		return getStat(StatEnum.CONCENTRATION, 0);
	}

	public Stat2 getRootResistance() {
		return getStat(StatEnum.ROOT_RESISTANCE, 0);
	}

	public Stat2 getSnareResistance() {
		return getStat(StatEnum.SNARE_RESISTANCE, 0);
	}

	public Stat2 getBindResistance() {
		return getStat(StatEnum.BIND_RESISTANCE, 0);
	}

	public Stat2 getFearResistance() {
		return getStat(StatEnum.FEAR_RESISTANCE, 0);
	}

	public Stat2 getSleepResistance() {
		return getStat(StatEnum.SLEEP_RESISTANCE, 0);
	}

	public Stat2 getAllSpeed() {
		return getStat(StatEnum.ALLSPEED, 7500);
	}

	@Override
	public Stat2 getMovementSpeed() {
		int currentState = owner.getState();
		Stat2 cachedSpeed = cachedSpeedStat;
		if (cachedSpeed != null && cachedState == currentState) {
			return cachedSpeed;
		}
		Stat2 newSpeedStat = null;
		if (owner.isFlying()) {
			newSpeedStat = getStat(StatEnum.FLY_SPEED,
					Math.round(owner.getObjectTemplate().getStatsTemplate().getRunSpeed() * 1.3f * 1000));
		} else if (owner.isInState(CreatureState.WEAPON_EQUIPPED)) {
			newSpeedStat = getStat(StatEnum.SPEED,
					Math.round(owner.getObjectTemplate().getStatsTemplate().getRunSpeedFight() * 1000));
		} else if (owner.isInState(CreatureState.WALKING)) {
			newSpeedStat = getStat(StatEnum.SPEED,
					Math.round(owner.getObjectTemplate().getStatsTemplate().getWalkSpeed() * 1000));
		} else {
			newSpeedStat = getStat(StatEnum.SPEED,
					Math.round(owner.getObjectTemplate().getStatsTemplate().getRunSpeed() * 1000));
		}
		cachedState = currentState;
		cachedSpeedStat = newSpeedStat;
		return newSpeedStat;
	}

	@Override
	public Stat2 getAttackRange() {
		return getStat(StatEnum.ATTACK_RANGE, owner.getObjectTemplate().getAttackRange() * 1000);
	}

	@Override
	public Stat2 getPDef() {
		return getStat(StatEnum.PHYSICAL_DEFENSE, owner.getObjectTemplate().getStatsTemplate().getPdef());
	}

	@Override
	public Stat2 getMDef() {
		return getStat(StatEnum.MAGICAL_DEFEND, owner.getObjectTemplate().getStatsTemplate().getMdef());
	}

	@Override
	public Stat2 getMResist() {
		return getStat(StatEnum.MAGICAL_RESIST, owner.getObjectTemplate().getStatsTemplate().getMresist());
	}

	@Override
	public Stat2 getMBResist() {
		return getStat(StatEnum.MAGIC_SKILL_BOOST_RESIST, owner.getObjectTemplate().getStatsTemplate().getMBResist());
	}

	@Override
	public Stat2 getPower() {
		return getStat(StatEnum.POWER, 100);
	}

	@Override
	public Stat2 getHealth() {
		return getStat(StatEnum.HEALTH, 100);
	}

	@Override
	public Stat2 getAccuracy() {
		return getStat(StatEnum.ACCURACY, 100);
	}

	@Override
	public Stat2 getAgility() {
		return getStat(StatEnum.AGILITY, 100);
	}

	@Override
	public Stat2 getKnowledge() {
		return getStat(StatEnum.KNOWLEDGE, 100);
	}

	@Override
	public Stat2 getWill() {
		return getStat(StatEnum.WILL, 100);
	}

	@Override
	public Stat2 getEvasion() {
		return getStat(StatEnum.EVASION, owner.getObjectTemplate().getStatsTemplate().getEvasion());
	}

	@Override
	public Stat2 getParry() {
		return getStat(StatEnum.PARRY, owner.getObjectTemplate().getStatsTemplate().getParry());
	}

	@Override
	public Stat2 getBlock() {
		return getStat(StatEnum.BLOCK, owner.getObjectTemplate().getStatsTemplate().getBlock());
	}

	@Override
	public Stat2 getMainHandPAttack() {
		return getStat(StatEnum.PHYSICAL_ATTACK, owner.getObjectTemplate().getStatsTemplate().getMainHandAttack());
	}

	@Override
	public Stat2 getMainHandPCritical() {
		return getStat(StatEnum.PHYSICAL_CRITICAL, owner.getObjectTemplate().getStatsTemplate().getMainHandCritRate());
	}

	@Override
	public Stat2 getMainHandPAccuracy() {
		return getStat(StatEnum.PHYSICAL_ACCURACY, owner.getObjectTemplate().getStatsTemplate().getMainHandAccuracy());
	}

	@Override
	public Stat2 getMAttack() {
		return getStat(StatEnum.MAGICAL_ATTACK, 100);
	}

	@Override
	public Stat2 getMainHandMAttack() {
		return getStat(StatEnum.MAGICAL_ATTACK, owner.getObjectTemplate().getStatsTemplate().getPower());
	}

	@Override
	public Stat2 getOffHandMAttack() {
		return getStat(StatEnum.MAGICAL_ATTACK, 0);
	}

	@Override
	public Stat2 getMBoost() {
		return getStat(StatEnum.BOOST_MAGICAL_SKILL, owner.getObjectTemplate().getStatsTemplate().getMBoost()); // dmg
																												// npc
																												// from
																												// spells
	}

	@Override
	public Stat2 getMAccuracy() {
		if (pAccuracy == 0)
			calcStats();
		// Trap's MAccuracy is being calculated into TrapGameStats and is related to
		// master's MAccuracy
		if (owner instanceof SummonedObject)
			return getStat(StatEnum.MAGICAL_ACCURACY, pAccuracy);
		return getMainHandPAccuracy();
	}

	@Override
	public Stat2 getMCritical() {
		return getStat(StatEnum.MAGICAL_CRITICAL, owner.getObjectTemplate().getStatsTemplate().getMCritical());
	}

	@Override
	public Stat2 getHpRegenRate() {
		// NpcStatsTemplate nst = owner.getObjectTemplate().getStatsTemplate();
		return getStat(StatEnum.REGEN_HP, owner.getObjectTemplate().getStatsTemplate().getHpRegenRate());
	}

	@Override
	public Stat2 getMpRegenRate() {
		throw new IllegalStateException("No mp regen for NPC");
	}

	public int getLastAttackTimeDelta() {
		return Math.round((System.currentTimeMillis() - lastAttackTime) / 1000f);
	}

	public int getLastAttackedTimeDelta() {
		return Math.round((System.currentTimeMillis() - lastAttackedTime) / 1000f);
	}

	public void renewLastAttackTime() {
		this.lastAttackTime = System.currentTimeMillis();
	}

	public void renewLastAttackedTime() {
		this.lastAttackedTime = System.currentTimeMillis();
	}

	public boolean isNextAttackScheduled() {
		return nextAttackTime - System.currentTimeMillis() > 50;
	}

	public void setFightStartingTime() {
		this.fightStartingTime = System.currentTimeMillis();
	}

	public long getFightStartingTime() {
		return this.fightStartingTime;
	}

	public void setNextAttackTime(long nextAttackTime) {
		this.nextAttackTime = nextAttackTime;
	}

	/**
	 * @return next possible attack time depending on stats
	 */
	public int getNextAttackInterval() {
		long attackDelay = System.currentTimeMillis() - lastAttackTime;
		int attackSpeed = getAttackSpeed().getCurrent();
		if (attackSpeed == 0) {
			attackSpeed = 2000;
		}
		if (owner.getAi2().isLogging()) {
			AI2Logger.info(owner.getAi2(), "adelay = " + attackDelay + " aspeed = " + attackSpeed);
		}
		int nextAttack = 0;
		if (attackDelay < attackSpeed) {
			nextAttack = (int) (attackSpeed - attackDelay);
		}
		return nextAttack;
	}

	/**
	 * @return next possible skill time depending on time
	 */

	public void renewLastSkillTime() {
		this.lastSkillTime = System.currentTimeMillis();
	}

	// not used at the moment
	/*
	 * public void renewLastSkilledTime() { this.lastSkilledTime =
	 * System.currentTimeMillis(); }
	 */

	public void renewLastChangeTargetTime() {
		this.lastChangeTarget = System.currentTimeMillis();
	}

	public int getLastSkillTimeDelta() {
		return Math.round((System.currentTimeMillis() - lastSkillTime) / 1000f);
	}

	// not used at the moment
	/*
	 * public int getLastSkilledTimeDelta() { return
	 * Math.round((System.currentTimeMillis() - lastSkilledTime) / 1000f); }
	 */

	public int getLastChangeTargetTimeDelta() {
		return Math.round((System.currentTimeMillis() - lastChangeTarget) / 1000f);
	}

	// only use skills after a minimum cooldown of 3 to 9 seconds
	// TODO: Check wether this is a suitable time or not
	public boolean canUseNextSkill() {
		if (getLastSkillTimeDelta() >= 6 + Rnd.get(-3, 3)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSpeedInfo() {
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
	}

	public final long getLastGeoZUpdate() {
		return lastGeoZUpdate;
	}

	/**
	 * @param lastGeoZUpdate the lastGeoZUpdate to set
	 */
	public void setLastGeoZUpdate(long lastGeoZUpdate) {
		this.lastGeoZUpdate = lastGeoZUpdate;
	}

	private void calcStats() {
		int lvl = owner.getLevel();
		double accuracy = lvl * (33.6f - (0.16 * lvl)) + 5;
		NpcRating npcRating = owner.getObjectTemplate().getRating();
		if (npcRating != null) {
			switch (npcRating) {
			case JUNK:
				accuracy *= 1.00f;
				break;
			case NORMAL:
				accuracy *= 1.05f;
				break;
			case ELITE:
				accuracy *= 1.15f;
				break;
			case HERO:
				accuracy *= 1.25f;
				break;
			case LEGENDARY:
				accuracy *= 1.35f;
				break;
			}
		}
		this.pAccuracy = Math.round(owner.getAi2().modifyMaccuracy((int) accuracy));
	}
}