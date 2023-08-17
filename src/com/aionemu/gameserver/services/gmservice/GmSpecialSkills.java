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
package com.aionemu.gameserver.services.gmservice;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public enum GmSpecialSkills {
	AccessLevel1(1, AdminConfig.ADMIN_TAG_1, "\uE042 Support\uE043", new int[]{240, 241, 282}),
    AccessLevel2(2, AdminConfig.ADMIN_TAG_1, "\uE042 Jr-GM\uE043", new int[]{240, 241, 282}),
    AccessLevel3(3, AdminConfig.ADMIN_TAG_1, "\uE042 GM\uE043", new int[]{240, 241, 282}),
    AccessLevel4(4, AdminConfig.ADMIN_TAG_1, "\uE042 Head-GM\uE043", new int[]{240, 241, 282}),
    AccessLevel5(5, AdminConfig.ADMIN_TAG_1, "\uE050 Admin\uE050", new int[]{240, 241, 282});

    private final int level;
    private final String nameLevel;
    private String status;
    private int[] skills;

    GmSpecialSkills(int id, String name, String status, int[] skills) {
        this.level = id;
        this.nameLevel = name;
        this.status = status;
        this.skills = skills;
    }

    public String getName() {
        return nameLevel;
    }

    public int getLevel() {
        return level;
    }

    public String getStatusName() {
        return status;
    }

    public int[] getSkills() {
        return skills;
    }

    public static GmSpecialSkills getAlType(int level) {
        for (GmSpecialSkills al : GmSpecialSkills.values()) {
            if (level == al.getLevel()) {
                return al;
            }
        }
        return null;
    }

    public static String getAlName(int level) {
        for (GmSpecialSkills al : GmSpecialSkills.values()) {
            if (level == al.getLevel()) {
                return al.getName();
            }
        }
        return "%s";
    }

    public static String getStatusName(Player player) {
        return player.getAccessLevel() > 0 ? GmSpecialSkills.getAlType(player.getAccessLevel()).getStatusName() : player.getLegion().getLegionName();
    }
}