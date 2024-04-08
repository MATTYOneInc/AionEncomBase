package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

import java.util.HashMap;
import java.util.Map;

public class CmdDeleteItems extends AbstractGMHandler{
    private static final Map<Integer, String> itemQualityName = new HashMap<>();

    static {
        itemQualityName.put(0, "Junk");
        itemQualityName.put(1, "Common");
        itemQualityName.put(2, "Superior");
        itemQualityName.put(3, "Heroic");
        itemQualityName.put(4, "Fabled");
        itemQualityName.put(5, "Eternal");
        itemQualityName.put(6, "Mythic");
    }

    public CmdDeleteItems(Player admin, String params) {
        super(admin, params);
        run(params);
    }

    private void run(String grade) {
        if (admin.getAccessLevel() < AdminConfig.GM_PANEL) {
            AuditLogger.info(admin, "Player trying send delete items Command.");
            return;
        }

        Storage inv = admin.getInventory();
        for (Item item : inv.getItems()) {
            if (item.getItemTemplate().getItemQuality().getQualityId() <= Integer.parseInt(grade)){
                inv.decreaseByItemId(item.getItemId(), item.getItemCount());
            }
        }
        PacketSendUtility.sendMessage(admin, "All items with grade " + itemQualityName.get(Integer.parseInt(grade)) + " and bellow were deleted.");
    }
}
