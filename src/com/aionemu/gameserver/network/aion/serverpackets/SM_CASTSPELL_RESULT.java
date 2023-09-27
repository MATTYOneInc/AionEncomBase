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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

public class SM_CASTSPELL_RESULT extends AionServerPacket
{
	private Creature effector;
	private Creature target;
	private Skill skill;
	private int cooldown;
	private int hitTime;
	private List<Effect> effects;
	private int spellStatus;
	private int dashStatus;
	private int targetType;
	private boolean chainSuccess;
	private int skinId;
	
	public SM_CASTSPELL_RESULT(Skill skill, List<Effect> effects, int hitTime, boolean chainSuccess, int spellStatus, int dashStatus , int skinId ) {
		this.skill = skill;
		this.effector = skill.getEffector();
		this.target = skill.getFirstTarget();
		this.effects = effects;
		this.cooldown = effector.getSkillCooldown(skill.getSkillTemplate());
		this.spellStatus = spellStatus;
		this.chainSuccess = chainSuccess;
		this.targetType = 0;
		this.hitTime = hitTime;
		this.dashStatus = dashStatus;
		this.skinId = skinId;
	}
	
	public SM_CASTSPELL_RESULT(Skill skill, List<Effect> effects, int hitTime, boolean chainSuccess, int spellStatus, int dashStatus, int targetType, int skinId) {
		this(skill, effects, hitTime, chainSuccess, spellStatus, dashStatus, skinId);
		this.targetType = targetType;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(effector.getObjectId());
		writeC(targetType);
		switch (targetType) {
		    case 0:
		    case 3:
		    case 4:
			    writeD(target.getObjectId());
		    break;
		    case 1:
			    writeF(skill.getX());
			    writeF(skill.getY());
			    writeF(skill.getZ());
		    break;
		    case 2:
			    writeF(skill.getX());
			    writeF(skill.getY());
			    writeF(skill.getZ());
			    writeF(0);
			    writeF(0);
			    writeF(0);
			    writeF(0);
			    writeF(0);
			    writeF(0);
			    writeF(0);
			    writeF(0);
		    break;
		}
		writeH(skill.getSkillTemplate().getSkillId());
		writeC(skill.getSkillTemplate().getLvl());
		cooldown = skill.StigmaEnchantCoolDown(skill, cooldown);
		writeD(cooldown);
		writeH(hitTime);
		writeC(0);
		
		if (effects.isEmpty()) {
			writeH(16);
		} else if (chainSuccess && skill.getChainCategory() != null) {
			writeH(32);
        } else if (skill.getChainCategory() == null) {
        	writeH(160);
		} else {
			writeH(0);
		}
		writeC(this.dashStatus);
		switch (this.dashStatus) {
		    case 1:
            case 2:
            case 3:
            case 4:
            case 6:
			    writeC(skill.getH());
			    writeF(skill.getX());
			    writeF(skill.getY());
			    writeF(skill.getZ());
			break;
		}
		writeH(skinId);
		writeH(effects.size());
		for (Effect effect : effects) {
			Creature effected = effect.getEffected();

			if (effected != null) {
				writeD(effected.getObjectId());
				writeC(effect.getEffectResult().getId());// 0 - NORMAL, 1 - ABSORBED, 2 - CONFLICT
				writeC((int) (100f * effected.getLifeStats().getCurrentHp() / target.getLifeStats().getMaxHp()));
			} else {
				writeD(0);
				writeC(0);
				writeC(0);
            }
			writeC((int) (100f * effector.getLifeStats().getCurrentHp() / effector.getLifeStats().getMaxHp()));

			/**
			 * Spell Status 1 : stumble 2 : knockback 4 : open aerial 8 : close aerial 16 : spin 32 : block 64 : parry 128 :dodge 256 : resist
			 */
			writeC(this.spellStatus);
			writeC(effect.getSkillMoveType().getId());
			writeH(0);
			writeC(effect.getCarvedSignet()); // current carve signet count

			switch (this.spellStatus) {
			    case 1:
			    case 2:
			    case 4:
			    case 8:
				    writeF(effect.getTargetX());
				    writeF(effect.getTargetY());
				    writeF(effect.getTargetZ());
				break;
			    case 16:
                case 3:
				    writeC(effect.getEffector().getHeading());
			    break;
			    default:
				switch (effect.getSkillMoveType()) {
				    case PULL:
				    case KNOCKBACK:
					    writeF(effect.getTargetX());
					    writeF(effect.getTargetY());
					    writeF(effect.getTargetZ());
				    default:
					break;
				}
				break;
			}
			writeC(1); {
				writeC(effect.isMphealInstant() ? 1 : 0);
                if (effect.isDelayedDamage()) {
                    writeD(0);
                } else {
                    writeD(effect.getReserved1());
                }
                writeC(effect.getAttackStatus().getId());
				if (effect.getEffected() instanceof Player) {
					if (effect.getAttackStatus().isCounterSkill()) {
						((Player) effect.getEffected()).setLastCounterSkill(effect.getAttackStatus());
					}
				}
				writeC(effect.getShieldDefense());
				
				switch (effect.getShieldDefense()) {
				    case 0:
				    case 2:
					break;
				    case 8:
				    case 10:
					    writeD(effect.getMpShield());
					    writeD(effect.getProtectorId());
					    writeD(effect.getProtectedDamage());
					    writeD(effect.getProtectedSkillId());
				    break;
				    default:
					    writeD(effect.getProtectorId());
					    writeD(effect.getProtectedDamage());
					    writeD(effect.getProtectedSkillId());
					    writeD(effect.getReflectedDamage());
					    writeD(effect.getReflectedSkillId());
				    break;
				}
			}
		}
	}
}