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

import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import java.util.List;

public class SM_ATTACK extends AionServerPacket
{
	private int attackno;
	private int time;
	private int type;
	private int SimpleAttackType;
	private List<AttackResult> attackList;
	private Creature attacker;
	private Creature target;
	
	public SM_ATTACK(Creature attacker, Creature target, int attackno, int time, int type, List<AttackResult> attackList) {
		this.attacker = attacker;
		this.target = target;
		this.attackno = attackno;
		this.time = time;
		this.type = type;
		this.attackList = attackList;
		this.SimpleAttackType = attacker.getController().getSimpleAttackType();
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(attacker.getObjectId());
		writeC(attackno); // Attack Number e.g. 1, 2, 3, 5, ..., Max_Integer_Value
		writeH(time); // unknown
		writeC((byte) SimpleAttackType);// 0=Ground Attacks | 1=Air Attacks (v4.7.5.17)
		writeC(type); // 0, 1, 2
		writeD(target.getObjectId());
		int attackerMaxHp = attacker.getLifeStats().getMaxHp();
		int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
		int targetMaxHp = target.getLifeStats().getMaxHp();
		int targetCurrHp = target.getLifeStats().getCurrentHp();

		writeC((int) (100f * targetCurrHp / targetMaxHp)); // target %hp
		writeC((int) (100f * attackerCurrHp / attackerMaxHp)); // attacker %hp

		switch (attackList.get(0).getAttackStatus().getId()) { // Counter skills
			case 196: // case CRITICAL_BLOCK 4.5
			case 4: // case BLOCK
            case 5:
            case 213:
				writeH(32);
			break;
			case 194: // case CRITICAL_PARRY 4.5
			case 2: // case PARRY
            case 3:
            case 211:
				writeH(64);
			break;
			case 192: // case CRITICAL_DODGE 4.5
			case 0: // case DODGE
            case 1:
            case 209:
				writeH(128);
			break;
			case 198: // case CRITICAL_RESIST 4.5
			case 6: // case RESIST
            case 7:
            case 215:
				writeH(256); // need more info becuz sometimes 0
			break;
			default:
				writeH(0);
				break;
		}
		// setting counter skill from packet to have the best synchronization of time with client
		if (target instanceof Player) {
            if (attackList.get(0).getAttackStatus().isCounterSkill()) {
                ((Player) target).setLastCounterSkill(attackList.get(0).getAttackStatus());
            }
        }
		writeH(0);
		writeC(attackList.size());
		for (AttackResult attack : attackList) {
			writeD(attack.getDamage());
			writeC(attack.getAttackStatus().getId());
			byte shieldType = (byte) attack.getShieldType();
			writeC(shieldType);

			/**
			 * shield Type: 1: reflector 2: normal shield 8: protect effect (ex. skillId: 417 Bodyguard) TODO find out 4
			 */
			switch (shieldType) {
				case 0:
				case 2:
				break;
				case 8:
				case 10:
				    writeD(attack.getShieldMp());
					writeD(attack.getProtectorId());
					writeD(attack.getProtectedDamage());
					writeD(attack.getProtectedSkillId());
				break;
				default:
					writeD(attack.getProtectorId());
					writeD(attack.getProtectedDamage());
					writeD(attack.getProtectedSkillId());
					writeD(attack.getReflectedDamage()); 
					writeD(attack.getReflectedSkillId());
					writeD(0);
					writeD(0);
				break;
			}
		}
		writeC(0);
	}
}