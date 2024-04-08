package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.audit.AuditLogger;

import java.util.HashMap;
import java.util.Map;

public class CmdCombineSkill extends AbstractGMHandler {

    private static final Map<String, Integer> craftId = new HashMap<>();

    static {
        craftId.put("gathering_b", 30002);
        craftId.put("aerial_gathering", 30003);
        craftId.put("cooking", 40001);
        craftId.put("weaponsmith", 40002);
        craftId.put("armorsmith", 40003);
        craftId.put("tailoring", 40004);
        craftId.put("alchemy", 40007);
        craftId.put("handiwork", 40008);
        craftId.put("menuisier", 40010);
    }

    public CmdCombineSkill(Player admin, String params) {
        super(admin, params);
        run();
    }
    private void run() {
        if (admin.getAccessLevel() < AdminConfig.GM_PANEL) {
            AuditLogger.info(admin, "Player trying send combine skill Command.");
            return;
        }

        String[] p = params.split(" ");
        String skillName = p[0];
        int skillLevel = Integer.parseInt(p[1]);

        if (skillName.equals("gathering_b") || skillName.equals("aerial_gathering")) {
            skillLevel = Math.min(skillLevel, 499);
        }

        admin.getSkillList().addSkill(admin, craftId.get(p[0]), skillLevel);
    }
}
