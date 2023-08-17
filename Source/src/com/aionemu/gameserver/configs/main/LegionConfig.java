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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LegionConfig
{
	protected static final Logger log = LoggerFactory.getLogger(LegionConfig.class);
	@Property(key = "gameserver.legion.pattern", defaultValue = "[a-zA-Z ]{2,32}")
	public static Pattern LEGION_NAME_PATTERN;
	@Property(key = "gameserver.legion.self.intro.pattern", defaultValue = ".{1,32}")
	public static Pattern SELF_INTRO_PATTERN;
	@Property(key = "gameserver.legion.nick.name.pattern", defaultValue = ".{1,10}")
	public static Pattern NICKNAME_PATTERN;
	@Property(key = "gameserver.legion.announcement.pattern", defaultValue = ".{1,256}")
	public static Pattern ANNOUNCEMENT_PATTERN;
	@Property(key = "gameserver.legion.disband.time", defaultValue = "86400")
	public static int LEGION_DISBAND_TIME;
	@Property(key = "gameserver.legion.disband.difference", defaultValue = "604800")
	public static int LEGION_DISBAND_DIFFERENCE;
	@Property(key = "gameserver.legion.creation.required.kinah", defaultValue = "10000")
	public static int LEGION_CREATE_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.emblem.required.kinah", defaultValue = "10000")
	public static int LEGION_EMBLEM_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.task.requirement.enable", defaultValue = "true")
	public static boolean ENABLE_GUILD_TASK_REQ;
	@Property(key = "gameserver.legion.level2.required.kinah", defaultValue = "100000")
	public static int LEGION_LEVEL2_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level3.required.kinah", defaultValue = "1000000")
	public static int LEGION_LEVEL3_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level4.required.kinah", defaultValue = "2000000")
	public static int LEGION_LEVEL4_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level5.required.kinah", defaultValue = "6000000")
	public static int LEGION_LEVEL5_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level6.required.kinah", defaultValue = "50000000")
    public static int LEGION_LEVEL6_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level7.required.kinah", defaultValue = "75000000")
    public static int LEGION_LEVEL7_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level8.required.kinah", defaultValue = "100000000")
    public static int LEGION_LEVEL8_REQUIRED_KINAH;
	@Property(key = "gameserver.legion.level2.required.members", defaultValue = "10")
	public static int LEGION_LEVEL2_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level3.required.members", defaultValue = "20")
	public static int LEGION_LEVEL3_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level4.required.members", defaultValue = "30")
	public static int LEGION_LEVEL4_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level5.required.members", defaultValue = "40")
	public static int LEGION_LEVEL5_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level6.required.members", defaultValue = "50")
    public static int LEGION_LEVEL6_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level7.required.members", defaultValue = "60")
    public static int LEGION_LEVEL7_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level8.required.members", defaultValue = "70")
    public static int LEGION_LEVEL8_REQUIRED_MEMBERS;
	@Property(key = "gameserver.legion.level2.required.contribution", defaultValue = "0")
	public static int LEGION_LEVEL2_REQUIRED_CONTRIBUTION;
	@Property(key = "gameserver.legion.level3.required.contribution", defaultValue = "20000")
	public static int LEGION_LEVEL3_REQUIRED_CONTRIBUTION;
	@Property(key = "gameserver.legion.level4.required.contribution", defaultValue = "100000")
	public static int LEGION_LEVEL4_REQUIRED_CONTRIBUTION;
	@Property(key = "gameserver.legion.level5.required.contribution", defaultValue = "500000")
	public static int LEGION_LEVEL5_REQUIRED_CONTRIBUTION;
	@Property(key = "gameserver.legion.level6.required.contribution", defaultValue = "25000000")
    public static int LEGION_LEVEL6_REQUIRED_CONTRIBUTION;
    @Property(key = "gameserver.legion.level7.required.contribution", defaultValue = "12500000")
    public static int LEGION_LEVEL7_REQUIRED_CONTRIBUTION;
    @Property(key = "gameserver.legion.level8.required.contribution", defaultValue = "62500000")
    public static int LEGION_LEVEL8_REQUIRED_CONTRIBUTION;
	@Property(key = "gameserver.legion.level1.max.members", defaultValue = "30")
	public static int LEGION_LEVEL1_MAX_MEMBERS;
	@Property(key = "gameserver.legion.level2.max.members", defaultValue = "60")
	public static int LEGION_LEVEL2_MAX_MEMBERS;
	@Property(key = "gameserver.legion.level3.max.members", defaultValue = "90")
	public static int LEGION_LEVEL3_MAX_MEMBERS;
	@Property(key = "gameserver.legion.level4.max.members", defaultValue = "120")
	public static int LEGION_LEVEL4_MAX_MEMBERS;
	@Property(key = "gameserver.legion.level5.max.members", defaultValue = "150")
	public static int LEGION_LEVEL5_MAX_MEMBERS;
	@Property(key = "gameserver.legion.level6.max.members", defaultValue = "180")
    public static int LEGION_LEVEL6_MAX_MEMBERS;
    @Property(key = "gameserver.legion.level7.max.members", defaultValue = "210")
    public static int LEGION_LEVEL7_MAX_MEMBERS;
    @Property(key = "gameserver.legion.level8.max.members", defaultValue = "240")
    public static int LEGION_LEVEL8_MAX_MEMBERS;
	@Property(key = "gameserver.legion.warehouse", defaultValue = "true")
	public static boolean LEGION_WAREHOUSE;
	@Property(key = "gameserver.legion.invite.other.faction", defaultValue = "false")
	public static boolean LEGION_INVITEOTHERFACTION;
	@Property(key = "gameserver.legion.warehouse.level1.slots", defaultValue = "24")
	public static int LWH_LEVEL1_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level2.slots", defaultValue = "32")
	public static int LWH_LEVEL2_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level3.slots", defaultValue = "40")
	public static int LWH_LEVEL3_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level4.slots", defaultValue = "48")
	public static int LWH_LEVEL4_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level5.slots", defaultValue = "56")
	public static int LWH_LEVEL5_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level6.slots", defaultValue = "64")
	public static int LWH_LEVEL6_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level7.slots", defaultValue = "72")
	public static int LWH_LEVEL7_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level8.slots", defaultValue = "80")
	public static int LWH_LEVEL8_SLOTS;
}