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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class CraftConfig
{
    @Property(key = "gameserver.craft.skills.unrestricted.levelup.enable", defaultValue = "false")
    public static boolean UNABLE_CRAFT_SKILLS_UNRESTRICTED_LEVELUP;
    @Property(key = "gameserver.craft.max.expert.skills", defaultValue = "2")
    public static int MAX_EXPERT_CRAFTING_SKILLS;
    @Property(key = "gameserver.craft.max.master.skills", defaultValue = "1")
    public static int MAX_MASTER_CRAFTING_SKILLS;
    @Property(key = "gameserver.craft.critical.rate.regular", defaultValue = "15")
    public static int CRAFT_CRIT_RATE;
    @Property(key = "gameserver.craft.critical.rate.premium", defaultValue = "15")
    public static int PREMIUM_CRAFT_CRIT_RATE;
    @Property(key = "gameserver.craft.critical.rate.vip", defaultValue = "15")
    public static int VIP_CRAFT_CRIT_RATE;
    @Property(key = "gameserver.craft.combo.rate.regular", defaultValue = "25")
    public static int CRAFT_COMBO_RATE;
    @Property(key = "gameserver.craft.combo.rate.premium", defaultValue = "25")
    public static int PREMIUM_CRAFT_COMBO_RATE;
    @Property(key = "gameserver.craft.combo.rate.vip", defaultValue = "25")
    public static int VIP_CRAFT_COMBO_RATE;
	public static boolean CRAFT_CHECK_TASK = false;
    @Property(key = "gameserver.craft.chance.purple.crit", defaultValue = "1")
    public static int CRAFT_CHANCE_PURPLE_CRIT;
    @Property(key = "gameserver.craft.chance.blue.crit", defaultValue = "10")
    public static int CRAFT_CHANCE_BLUE_CRIT;
    @Property(key = "gameserver.craft.chance.instant", defaultValue = "100")
    public static int CRAFT_CHANCE_INSTANT;
	@Property(key = "gameserver.protection.gather.enable", defaultValue = "true")
	public static boolean PROTECTION_GATHER_ENABLE;
}