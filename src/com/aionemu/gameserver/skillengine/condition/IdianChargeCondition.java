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
package com.aionemu.gameserver.skillengine.condition;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Skill;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Ranastic
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdianChargeCondition")
public class IdianChargeCondition extends ChargeCondition
{
    @Override
    public boolean validate(Skill env) {
        if (env.getEffector() instanceof Player) {
            Player effector = (Player) env.getEffector();
            for (Item item : effector.getEquipment().getEquippedItems()) {
                if (item.getItemTemplate().isWeapon() && item.getIdianStone() != null) {
                    item.getIdianStone().decreasePolishCharge(effector, 500);
				}
            }
        }
        return true;
    }
}