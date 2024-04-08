package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

import java.util.Iterator;
import java.util.List;

public class CmdResetInven extends AbstractGMHandler {
    public CmdResetInven(Player admin, String params) {
        super(admin, params);
        run();
    }
    public void run(){

        if (admin.getAccessLevel() < AdminConfig.GM_PANEL) {
            AuditLogger.info(admin, "Player trying send reset inventory Command.");
            return;
        }

        List<Item> items = admin.getInventory().getItems();
        Iterator<Item> it = items.iterator();

        while (it.hasNext()) {
            Item act = it.next();
            if (act.getItemId() == 182400001) {
                admin.getInventory().tryDecreaseKinah(act.getItemCount());
            } else {
                admin.getInventory().decreaseByObjectId(act.getObjectId(), act.getItemCount());
            }
        }
        PacketSendUtility.sendMessage(admin, "Your inventory has been cleaned successfully.");
    }
}
