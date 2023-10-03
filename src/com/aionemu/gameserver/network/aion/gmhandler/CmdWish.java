/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Kill3r
 */
public final class CmdWish extends AbstractGMHandler {

    public CmdWish(Player admin, String params) {
        super(admin, params);
        run();
    }

    public void run() {
        String[] p = params.split(" ");
		if (p.length != 2){

            String npcName = params;
			TIntObjectHashMap<NpcTemplate> npcTemp = DataManager.NPC_DATA.getNpcData();
			
            float x = admin.getX();
            float y = admin.getY();
            float z = admin.getZ();
            byte heading = admin.getHeading();
            int worldId = admin.getWorldId();

            for (NpcTemplate nTemp : npcTemp.valueCollection()){
                if (nTemp.getNamedesc() != null && nTemp.getNamedesc().equalsIgnoreCase(npcName)){
                    SpawnTemplate spawn = SpawnEngine.addNewSpawn(worldId, nTemp.getTemplateId(), x, y, z, heading, 0);
                    VisibleObject visibleObject = SpawnEngine.spawnObject(spawn, admin.getInstanceId());
                    PacketSendUtility.sendMessage(admin, " spawned (ID:"+nTemp.getTemplateId()+ ")");
                }
            }
			return;
        }
            //WORKING PERFECTLY
			TIntObjectHashMap<ItemTemplate> itemTemp = DataManager.ITEM_DATA.getItemData();
            String[] itemN = params.split(" ");

            String itemName = itemN[0];
            Integer itemcount = Integer.parseInt(itemN[1]);
            if (itemcount == 0){
                itemcount = 1;
            }
			
			for (ItemTemplate it : itemTemp.valueCollection()){
				if (it.getNamedesc() != null && it.getNamedesc().equalsIgnoreCase(itemName)){
					ItemService.addItem(admin, it.getTemplateId(), itemcount);
				}
            }
    }
}