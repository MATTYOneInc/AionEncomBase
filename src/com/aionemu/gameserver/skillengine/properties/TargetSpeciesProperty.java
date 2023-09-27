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
package com.aionemu.gameserver.skillengine.properties;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Skill;

import java.util.Iterator;
import java.util.List;

public class TargetSpeciesProperty
{
	public static boolean set(final Skill skill, Properties properties) {
        TargetSpeciesAttribute value = properties.getTargetSpecies();
        final List<Creature> effectedList = skill.getEffectedList();
        switch (value) {
            case NPC:
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();
                    if (nextEffected instanceof Npc) {
                        continue;
                    }
                    iter.remove();
                }
            break;
            case PC:
                for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();) {
                    Creature nextEffected = iter.next();
                    if (nextEffected instanceof Player) {
                        continue;
                    }
                    iter.remove();
                }
                break;
				default:
			break;
        }
        return true;
    }
}