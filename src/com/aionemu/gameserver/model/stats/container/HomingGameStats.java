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

import com.aionemu.gameserver.model.gameobjects.Homing;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.stats.calc.Stat2;

public class HomingGameStats extends SummonedObjectGameStats
{
	public HomingGameStats(Npc owner) {
		super(owner);
	}
	
	@Override
	public Stat2 getStat(StatEnum statEnum, int base) {
		Stat2 stat = super.getStat(statEnum, base);
		if (owner.getMaster() == null) {
			return stat;
		}
		switch (statEnum) {
			case MAGICAL_ATTACK:
			stat.setBonusRate(0.2f);
			return owner.getMaster().getGameStats().getItemStatBoost(statEnum, stat);
		}
		return stat;
	}
	
	@Override
    public Stat2 getMAttack() {
        Homing homing = (Homing) owner;
        int power = homing.getObjectTemplate().getStatsTemplate().getPower();
        int level = homing.getObjectTemplate().getLevel();
		switch (level) {
            case 10:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 260;
				}
            break;
			case 15:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 360;
				}
            break;
			case 20:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 460;
				}
            break;
			case 25:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 560;
				}
            break;
			case 30:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 660;
				}
            break;
			case 35:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 760;
				}
            break;
			case 40:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 860;
				}
            break;
			case 45:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 960;
				}
            break;
			case 49:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 1060;
				}
            break;
			case 53:
			case 57:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 1160;
				}
            break;
			case 61:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 1260;
				}
            break;
			case 65:
                if (homing.getName().equals("Energy of Cyclone") && homing.getName().equals("Energy of Wind")) {
                    power = 1360;
				}
            break;
        }
        return getStat(StatEnum.MAGICAL_ATTACK, power);
    }
}