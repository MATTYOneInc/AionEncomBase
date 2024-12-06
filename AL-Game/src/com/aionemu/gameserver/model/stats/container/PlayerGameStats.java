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
package com.aionemu.gameserver.model.stats.container;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.stats.calc.AdditionStat;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.model.templates.ride.RideInfo;
import com.aionemu.gameserver.model.templates.stats.PlayerStatsTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xavier
 */
public class PlayerGameStats extends CreatureGameStats<Player> {

	private int cachedSpeed;
	private int cachedAttackSpeed;

	/**
	 * @param owner
	 */
	public PlayerGameStats(Player owner) {
		super(owner);
	}

	@Override
	protected void onStatsChange() {
		super.onStatsChange();
		updateStatsAndSpeedVisually();
	}

	public void updateStatsAndSpeedVisually() {
		updateStatsVisually();
		checkSpeedStats();
	}

	public void updateStatsVisually() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_STATS);
	}

	private void checkSpeedStats() {
		int current = getMovementSpeed().getCurrent();
		int currentAttackSpeed = getAttackSpeed().getCurrent();
		if (current != cachedSpeed || currentAttackSpeed != cachedAttackSpeed) {
			owner.addPacketBroadcastMask(BroadcastMode.UPDATE_SPEED);
		}
		cachedSpeed = current;
		cachedAttackSpeed = currentAttackSpeed;
	}

	@Override
	public Stat2 getMaxHp() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		Stat2 stat = getStat(StatEnum.MAXHP, pst.getMaxHp());
		int HVIT = ((Player) owner).getGameStats().getStat(StatEnum.HVIT, 0).getCurrent();
		int MaxHpCalculation = Math.round(19118 * HVIT / (825.0F + HVIT));
		stat.addToBonus(MaxHpCalculation);
		return stat;
	}

	@Override
	public Stat2 getMaxMp() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		Stat2 stat = getStat(StatEnum.MAXMP, pst.getMaxMp());
		int HWIL = ((Player) owner).getGameStats().getStat(StatEnum.HWIL, 0).getCurrent();
		int MaxMpCalculation = Math.round(20540 * HWIL / (825.0F + HWIL));
		stat.addToBonus(MaxMpCalculation);
		return stat;
	}

	public Stat2 getStrikeResist() {
		Stat2 stat = getStat(StatEnum.PHYSICAL_CRITICAL_RESIST, 0);
		int HDEX = ((Player) owner).getGameStats().getStat(StatEnum.HDEX, 0).getCurrent();
		int Pcrculation = Math.round(1144 * HDEX / (187.0F + HDEX));
		stat.addToBonus(Pcrculation);
		return stat;		
	}

	public Stat2 getStrikeFort() {
		return getStat(StatEnum.PHYSICAL_CRITICAL_DAMAGE_REDUCE, 0);
	}

	public Stat2 getSpellResist() {
		int base = 0;
		int Pclass = owner.getPlayerClass().getClassId();
		if (Pclass == 7 || Pclass == 8 || Pclass == 10) {
			base = 50;
		}		
		Stat2 stat = getStat(StatEnum.MAGICAL_CRITICAL_RESIST, base);
		int HWIL = ((Player) owner).getGameStats().getStat(StatEnum.HWIL, 0).getCurrent();
		int MCrCalculation = Math.round(1236 * HWIL / (376.0F + HWIL));
		stat.addToBonus(MCrCalculation);
		return stat;
	}

	public Stat2 getSpellFort() {
		return getStat(StatEnum.MAGICAL_CRITICAL_DAMAGE_REDUCE, 0);
	}

	public Stat2 getMaxDp() {
		return getStat(StatEnum.MAXDP, 4000);
	}

	public Stat2 getFlyTime() {
		return getStat(StatEnum.FLY_TIME, CustomConfig.BASE_FLYTIME);
	}

	public Stat2 getAllSpeed() {
		return getStat(StatEnum.ALLSPEED, 7500);
	}

	@Override
	public Stat2 getAttackSpeed() {
		int base = 1500;
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getAttackSpeed();
			Item offWeapon = owner.getEquipment().getOffHandWeapon();
			if (offWeapon != null) {
				base += offWeapon.getItemTemplate().getWeaponStats().getAttackSpeed() / 4;
			}
		}
		Stat2 aSpeed = getStat(StatEnum.ATTACK_SPEED, base);
		return aSpeed;
	}

	@Override
	public Stat2 getBCastingTime() {
		int base = 0;
		int casterClass = owner.getPlayerClass().getClassId();
		if (casterClass == 7 || // Sorcerer.
				casterClass == 8 || // Spirit-Master.
				casterClass == 16) { // Songweaver.
			base = 800;
		}
		return getStat(StatEnum.BOOST_CASTING_TIME, base);
	}

	@Override
	public Stat2 getConcentration() {
		int base = 0;
		int sorcerer1 = owner.getPlayerClass().getClassId();
		int spiritMaster1 = owner.getPlayerClass().getClassId();
		int HDEX = ((Player) owner).getGameStats().getStat(StatEnum.HDEX, 0).getCurrent();
		int ConcentrationCalculation = Math.round(471 * HDEX / (825.0F + HDEX));		
		if (sorcerer1 == 7) {
			base = 25;
		} else if (spiritMaster1 == 8 && owner.getLevel() >= 56) {
			base = 100;
		}
		Stat2 stat = getStat(StatEnum.CONCENTRATION, base);
		stat.addToBonus(ConcentrationCalculation);				
		return stat;
	}

	@Override
	public Stat2 getRootResistance() {
		int base = 0;
		int aethertech2 = owner.getPlayerClass().getClassId();
		if (aethertech2 == 13) {
			base = 200;
		}
		return getStat(StatEnum.ROOT_RESISTANCE, base);
	}

	@Override
	public Stat2 getSnareResistance() {
		int base = 0;
		int aethertech3 = owner.getPlayerClass().getClassId();
		if (aethertech3 == 13) {
			base = 200;
		}
		return getStat(StatEnum.SNARE_RESISTANCE, base);
	}

	@Override
	public Stat2 getBindResistance() {
		int base = 0;
		int aethertech4 = owner.getPlayerClass().getClassId();
		if (aethertech4 == 13) {
			base = 200;
		}
		return getStat(StatEnum.BIND_RESISTANCE, base);
	}

	@Override
	public Stat2 getFearResistance() {
		int base = 0;
		int aethertech5 = owner.getPlayerClass().getClassId();
		if (aethertech5 == 13) {
			base = -200;
		}
		return getStat(StatEnum.FEAR_RESISTANCE, base);
	}

	@Override
	public Stat2 getSleepResistance() {
		int base = 0;
		int aethertech6 = owner.getPlayerClass().getClassId();
		if (aethertech6 == 13) {
			base = -200;
		}
		return getStat(StatEnum.SLEEP_RESISTANCE, base);
	}

	@Override
	public Stat2 getPDef() {
		int base = 0;
		Stat2 stats = getStat(StatEnum.PHYSICAL_DEFENSE, base);
		int HSTR = ((Player) owner).getGameStats().getStat(StatEnum.HSTR, 0).getCurrent();
		int phyDefCalculation = Math.round(3440 * HSTR / (135.0F + HSTR));		
		int gunslinger = owner.getPlayerClass().getClassId();
		int aethertech = owner.getPlayerClass().getClassId();
		if (gunslinger == 14) {
			base = 100;
		} else if (aethertech == 13) {
			base = 350;
		}
		stats.addToBonus(phyDefCalculation);
		return stats;
	}

	@Override
	public Stat2 getMResist() {
		int base = 0;
		int assassin = owner.getPlayerClass().getClassId();
		if (assassin == 4 && owner.getLevel() >= 37) {
			base = 30;
		}
		Stat2 stat = getStat(StatEnum.MAGICAL_RESIST, base);
		int HWIL = ((Player) owner).getGameStats().getStat(StatEnum.HWIL, 0).getCurrent();
		int MResistCalculation = Math.round(2844 * HWIL / (825.0F + HWIL));
		stat.addToBonus(MResistCalculation);  
		return stat;
	}

	@Override
	public Stat2 getMBResist() {
		int base = 0;
		int cleric = owner.getPlayerClass().getClassId();
		int sorcerer2 = owner.getPlayerClass().getClassId();
		int spiritMaster2 = owner.getPlayerClass().getClassId();
		if (cleric == 10 && owner.getLevel() >= 60) {
			base = 140;
		}
		if (sorcerer2 == 7 && owner.getLevel() >= 60) {
			base = 180;
		}
		if (spiritMaster2 == 8 && owner.getLevel() >= 60) {
			base = 180;
		}
		
		Stat2 stat = getStat(StatEnum.MAGIC_SKILL_BOOST_RESIST, base);
		int HKNO = ((Player) owner).getGameStats().getStat(StatEnum.HKNO, 0).getCurrent();
		int MBResistCalculation = Math.round(1392 * HKNO / (129.0F + HKNO));
		stat.addToBonus(MBResistCalculation);
		return stat;
	}

	@Override
	public Stat2 getMovementSpeed() {
		Stat2 movementSpeed;
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		if (owner.isInPlayerMode(PlayerMode.RIDE)) {
			RideInfo ride = owner.ride;
			int runSpeed = (int) pst.getRunSpeed() * 1000;
			if (owner.isInState(CreatureState.FLYING)) {
				movementSpeed = new AdditionStat(StatEnum.FLY_SPEED, runSpeed, owner);
				movementSpeed.addToBonus((int) (ride.getFlySpeed() * 1000) - runSpeed);
			} else {
				float speed = owner.isInSprintMode() ? ride.getSprintSpeed() : ride.getMoveSpeed();
				movementSpeed = new AdditionStat(StatEnum.SPEED, runSpeed, owner);
				movementSpeed.addToBonus((int) (speed * 1000) - runSpeed);
			}
		} else if (owner.isInFlyingState()) {
			movementSpeed = getStat(StatEnum.FLY_SPEED, Math.round(pst.getFlySpeed() * 1000));
		} else if (owner.isInState(CreatureState.FLIGHT_TELEPORT) && !owner.isInState(CreatureState.RESTING)) {
			movementSpeed = getStat(StatEnum.SPEED, 12000);
		} else if (owner.isInState(CreatureState.WALKING)) {
			movementSpeed = getStat(StatEnum.SPEED, Math.round(pst.getWalkSpeed() * 1000));
		} else if (getAllSpeed().getBonus() != 0) {
			movementSpeed = getStat(StatEnum.SPEED, getAllSpeed().getCurrent());
		} else {
			movementSpeed = getStat(StatEnum.SPEED, Math.round(pst.getRunSpeed() * 1000));
		}
		return movementSpeed;
	}

	@Override
	public Stat2 getAttackRange() {
		int base = 1500;
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		Item offHandWeapon = equipment.getOffHandWeapon();
		if (mainHandWeapon != null) {
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getAttackRange();
			if (!mainHandWeapon.getItemTemplate().isTwoHandWeapon() && mainHandWeapon != null && offHandWeapon != null
					&& offHandWeapon.getItemTemplate().getArmorType() != ArmorType.SHIELD) {
				if (mainHandWeapon.getItemTemplate().getWeaponStats().getAttackRange() != offHandWeapon
						.getItemTemplate().getWeaponStats().getAttackRange()) {
					if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.SWORD_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.SWORD_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.DAGGER_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.SWORD_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H) {
						base = 1500;
					} else if (mainHandWeapon.getItemTemplate().getWeaponType() == WeaponType.SWORD_1H
							&& offHandWeapon.getItemTemplate().getWeaponType() == WeaponType.MACE_1H) {
						base = 1500;
					} else {
						if (mainHandWeapon != null && offHandWeapon != null
								&& offHandWeapon.getItemTemplate().getArmorType() != ArmorType.SHIELD) {
							base = mainHandWeapon.getItemTemplate().getWeaponStats().getAttackRange();
							log.info("[Error] PlayerGameStats] mainHandWeapon ["
									+ mainHandWeapon.getItemTemplate().getItemType() + "] offHandWeapon ["
									+ offHandWeapon.getItemTemplate().getItemType() + "]");
						}
					}
				}
			}
		}
		return getStat(StatEnum.ATTACK_RANGE, base);
	}

	@Override
	public Stat2 getMDef() {
		return getStat(StatEnum.MAGICAL_DEFEND, 0);
	}

	@Override
	public Stat2 getPower() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.POWER, pst.getPower());
	}

	@Override
	public Stat2 getHealth() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.HEALTH, pst.getHealth());
	}

	@Override
	public Stat2 getAccuracy() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.ACCURACY, pst.getAccuracy());
	}

	@Override
	public Stat2 getAgility() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.AGILITY, pst.getAgility());
	}

	@Override
	public Stat2 getKnowledge() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.KNOWLEDGE, pst.getKnowledge());
	}

	@Override
	public Stat2 getWill() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		return getStat(StatEnum.WILL, pst.getWill());
	}

	@Override
	public Stat2 getEvasion() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		Stat2 stat = getStat(StatEnum.EVASION, pst.getEvasion());
		int HDEX = ((Player) owner).getGameStats().getStat(StatEnum.HDEX, 0).getCurrent();
		int EvasionCalculation = Math.round(3140 * HDEX / (800.0F + HDEX));
		stat.addToBonus(EvasionCalculation);
		return stat;
	}

	@Override
	public Stat2 getParry() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		int base = pst.getParry();
		Item mainHandWeapon = owner.getEquipment().getMainHandWeapon();
		if (mainHandWeapon != null) {
			base += mainHandWeapon.getItemTemplate().getWeaponStats().getParry();
		}
		Stat2 stat = getStat(StatEnum.PARRY, base);
		int HDEX = ((Player) owner).getGameStats().getStat(StatEnum.HDEX, 0).getCurrent();
		int ParryCalculation = Math.round(3112 * HDEX / (550.0F + HDEX));
		stat.addToBonus(ParryCalculation);
		return stat;
	}

	@Override
	public Stat2 getBlock() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		Stat2 stat = getStat(StatEnum.BLOCK, pst.getBlock());
		int HVIT = ((Player) owner).getGameStats().getStat(StatEnum.HVIT, 0).getCurrent();
		int BlockCalculation = Math.round(4740 * HVIT / (825.0F + HVIT));
		stat.addToBonus(BlockCalculation);
		return stat;
	}

	@Override
	public Stat2 getMainHandPAttack() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		int base = pst.getMainHandAttack();
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			if (mainHandWeapon.getItemTemplate().getAttackType().isMagical()) {
				return new AdditionStat(StatEnum.MAIN_HAND_POWER, 0, owner);
			}
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getMeanDamage();
		}
		Stat2 stat = getStat(StatEnum.PHYSICAL_ATTACK, base);
		int HSTR = ((Player) owner).getGameStats().getStat(StatEnum.HSTR, 0).getCurrent();
		int PhyAtkCalculation = Math.round(1256 * HSTR / (825.0F + HSTR));
		stat.addToBonus(PhyAtkCalculation);		
		return getStat(StatEnum.MAIN_HAND_POWER, stat);
	}

	public Stat2 getOffHandPAttack() {
		Equipment equipment = owner.getEquipment();
		Item offHandWeapon = equipment.getOffHandWeapon();
		if (offHandWeapon != null && offHandWeapon.getItemTemplate().isWeapon()) {
			int base = offHandWeapon.getItemTemplate().getWeaponStats().getMeanDamage();
			base *= 0.98;
			Stat2 stat = getStat(StatEnum.PHYSICAL_ATTACK, base);
			int HSTR = ((Player) owner).getGameStats().getStat(StatEnum.HSTR, 0).getCurrent();
			int PhyAtkCalculation = Math.round(1256 * HSTR / (825.0F + HSTR));
			stat.addToBonus(PhyAtkCalculation);			
			return getStat(StatEnum.OFF_HAND_POWER, stat);
		}
		return new AdditionStat(StatEnum.OFF_HAND_POWER, 0, owner);
	}

	@Override
	public Stat2 getMainHandPCritical() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), (owner.getLevel()));
		int base = pst.getMainHandCritRate();
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getPhysicalCritical();

			// TODO dead code!!!
		} else if (mainHandWeapon != null && mainHandWeapon.hasFusionedItem()) {
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getPhysicalCritical()
					+ mainHandWeapon.getFusionedItemTemplate().getWeaponStats().getPhysicalCritical();
		}
		Stat2 stat = getStat(StatEnum.PHYSICAL_CRITICAL, base);
		int HAGI = ((Player) owner).getGameStats().getStat(StatEnum.HAGI, 0).getCurrent();
		int PhyCriticalCalculation = Math.round(3160 * HAGI / (825.0F + HAGI));
		stat.addToBonus(PhyCriticalCalculation);  
		return stat;
	}

	public Stat2 getOffHandPCritical() {
		Equipment equipment = owner.getEquipment();
		Item offHandWeapon = equipment.getOffHandWeapon();
		if (offHandWeapon != null && offHandWeapon.getItemTemplate().isWeapon()) {
			int base = offHandWeapon.getItemTemplate().getWeaponStats().getPhysicalCritical();
			Stat2 stat = getStat(StatEnum.PHYSICAL_CRITICAL, base);
			int HAGI = ((Player) owner).getGameStats().getStat(StatEnum.HAGI, 0).getCurrent();
			int PhyCriticalCalculation = Math.round(3160 * HAGI / (825.0F + HAGI));
			stat.addToBonus(PhyCriticalCalculation);
			return stat;
		}
		return new AdditionStat(StatEnum.OFF_HAND_CRITICAL, 0, owner);
	}

	@Override
	public Stat2 getMainHandPAccuracy() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		int base = pst.getMainHandAccuracy();
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			base += mainHandWeapon.getItemTemplate().getWeaponStats().getPhysicalAccuracy();
		}
		Stat2 stat = getStat(StatEnum.PHYSICAL_ACCURACY, base);
		int HAGI = ((Player) owner).getGameStats().getStat(StatEnum.HAGI, 0).getCurrent();
		int PhyAccuracyCalculation = Math.round(4020 * HAGI / (510.0F + HAGI));
		stat.addToBonus(PhyAccuracyCalculation);
		return stat;
	}

	public Stat2 getOffHandPAccuracy() {
		Equipment equipment = owner.getEquipment();
		Item offHandWeapon = equipment.getOffHandWeapon();
		if (offHandWeapon != null && offHandWeapon.getItemTemplate().isWeapon()) {
			PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(),
					owner.getLevel());
			int base = pst.getMainHandAccuracy();
			base += offHandWeapon.getItemTemplate().getWeaponStats().getPhysicalAccuracy();
			
			Stat2 stat = getStat(StatEnum.PHYSICAL_ACCURACY, base);
			int HAGI = ((Player) owner).getGameStats().getStat(StatEnum.HAGI, 0).getCurrent();
			int PhyAccuracyCalculation = Math.round(4020 * HAGI / (510.0F + HAGI));
			stat.addToBonus(PhyAccuracyCalculation);
			return stat;
		}
		return new AdditionStat(StatEnum.OFF_HAND_ACCURACY, 0, owner);
	}

	@Override
	public Stat2 getMAttack() {
		int base;
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			if (!mainHandWeapon.getItemTemplate().getAttackType().isMagical()) {
				return new AdditionStat(StatEnum.MAGICAL_ATTACK, 0, owner);
			}
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getMeanDamage();
		} else {
			base = Rnd.get(16, 20);
		}
		return getStat(StatEnum.MAGICAL_ATTACK, base);
	}

	@Override
	public Stat2 getMainHandMAttack() {
		int base = 0;
		Equipment equipment = owner.getEquipment();
		Item mainHandWeapon = equipment.getMainHandWeapon();
		if (mainHandWeapon != null) {
			if (!mainHandWeapon.getItemTemplate().getAttackType().isMagical()) {
				return new AdditionStat(StatEnum.MAIN_HAND_MAGICAL_POWER, 0, owner);
			}
			base = mainHandWeapon.getItemTemplate().getWeaponStats().getMeanDamage();
		}
		Stat2 stat = getStat(StatEnum.MAGICAL_ATTACK, base);
		return getStat(StatEnum.MAIN_HAND_MAGICAL_POWER, stat);
	}

	@Override
	public Stat2 getOffHandMAttack() {
		int base = 0;
		Equipment equipment = owner.getEquipment();
		Item offHandWeapon = equipment.getOffHandWeapon();
		if (offHandWeapon != null && offHandWeapon.getItemTemplate().isWeapon()) {
			base = offHandWeapon.getItemTemplate().getWeaponStats().getMeanDamage();
			base *= 0.82;
			Stat2 stat = getStat(StatEnum.MAGICAL_ATTACK, base);
			return getStat(StatEnum.OFF_HAND_MAGICAL_POWER, stat);
		}
		return new AdditionStat(StatEnum.OFF_HAND_MAGICAL_POWER, 0, owner);
	}

	@Override
	public Stat2 getMBoost() {
		int base = 0;
		Item mainHandWeapon = owner.getEquipment().getMainHandWeapon();
		if (mainHandWeapon != null) {
			base += mainHandWeapon.getItemTemplate().getWeaponStats().getBoostMagicalSkill();
		}
		
		Stat2 stat = getStat(StatEnum.BOOST_MAGICAL_SKILL, base);
		int HKNO = ((Player) owner).getGameStats().getStat(StatEnum.HKNO, 0).getCurrent();
		int MBoostCalculation = Math.round(5056 * HKNO / (825.0F + HKNO));
		stat.addToBonus(MBoostCalculation);
		return stat;
	}

	@Override
	public Stat2 getMAccuracy() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		int base = pst.getMagicAccuracy();
		Item mainHandWeapon = owner.getEquipment().getMainHandWeapon();
		if (mainHandWeapon != null) {
			base += mainHandWeapon.getItemTemplate().getWeaponStats().getMagicalAccuracy();
		}
		Stat2 stat = getStat(StatEnum.MAGICAL_ACCURACY, base);
		int HAGI = ((Player) owner).getGameStats().getStat(StatEnum.HAGI, 0).getCurrent();
		int MAccuracyCalculation = Math.round(2286 * HAGI / (376.0F + HAGI));
		stat.addToBonus(MAccuracyCalculation);
		return stat;
	}

	@Override
	public Stat2 getMCritical() {
		PlayerStatsTemplate pst = DataManager.PLAYER_STATS_DATA.getTemplate(owner.getPlayerClass(), owner.getLevel());
		int base = pst.getMCritical();
		
		Stat2 stat = getStat(StatEnum.MAGICAL_CRITICAL, base);
		int HKNO = ((Player) owner).getGameStats().getStat(StatEnum.HKNO, 0).getCurrent();
		int MCriticalCalculation = Math.round(1884 * HKNO / (825.0F + HKNO));
		stat.addToBonus(MCriticalCalculation);
		return stat;
	}

	@Override
	public Stat2 getHpRegenRate() {
		int base = owner.getLevel() + 3;
		if (owner.isInState(CreatureState.RESTING)) {
			base *= 8;
		}
		base *= getHealth().getCurrent() / 100f;
		Stat2 stat = getStat(StatEnum.REGEN_HP, base);
		int HVIT = ((Player) owner).getGameStats().getStat(StatEnum.HVIT, 0).getCurrent();
		int RegenHpCalculation = Math.round(316 * HVIT / (825.0F + HVIT));
		stat.addToBonus(RegenHpCalculation);
		return stat;
	}

	@Override
	public Stat2 getMpRegenRate() {
		int base = owner.getLevel() + 8;
		if (owner.isInState(CreatureState.RESTING)) {
			base *= 8;
		}
		base *= getWill().getCurrent() / 100f;
		Stat2 stat = getStat(StatEnum.REGEN_MP, base);
		int HWIL = ((Player) owner).getGameStats().getStat(StatEnum.HWIL, 0).getCurrent();
		int RegenMpCalculation = Math.round(158 * HWIL / (825.0F + HWIL));
		stat.addToBonus(RegenMpCalculation);
		return stat;
	}

	@Override
	public void updateStatInfo() {
		PacketSendUtility.sendPacket(owner, new SM_STATS_INFO(owner));
	}

	@Override
	public void updateSpeedInfo() {
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0), true);
	}
}