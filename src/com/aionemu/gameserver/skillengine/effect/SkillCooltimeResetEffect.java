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
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;

/**
 * @author Dr.Nism
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillCooltimeResetEffect")
public class SkillCooltimeResetEffect extends EffectTemplate {

    @XmlAttribute(name = "first_cd", required = true)
    protected int firstCd;
    @XmlAttribute(name = "second_cd", required = true)
    protected int secondCd;
	
    @Override
    public void applyEffect(Effect effect) {
        Creature effected = effect.getEffected();
        HashMap<Integer, Long> resetSkillCoolDowns = new HashMap<>();
        for (int i = firstCd; i <= secondCd; i++) {
            long delay = effected.getSkillCoolDown(i) - System.currentTimeMillis();
            if (delay <= 0) {
                continue;
            } if (delta > 0) {
                delay -= delay * (delta / 100);
            } else {
                delay -= value;
            }
            effected.setSkillCoolDown(i, delay + System.currentTimeMillis());
            resetSkillCoolDowns.put(i, delay + System.currentTimeMillis());
        } if (effected instanceof Player) {
            if (resetSkillCoolDowns.size() > 0) {
                PacketSendUtility.sendPacket((Player) effected, new SM_SKILL_COOLDOWN(resetSkillCoolDowns));
            }
        }
    }
}