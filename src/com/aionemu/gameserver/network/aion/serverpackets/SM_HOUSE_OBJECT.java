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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.NpcObject;
import com.aionemu.gameserver.model.gameobjects.UseableItemObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_HOUSE_OBJECT extends AionServerPacket
{
	HouseObject<?> houseObject;
	
    public SM_HOUSE_OBJECT(HouseObject<?> owner) {
        this.houseObject = owner;
    }
	
    @Override
    protected void writeImpl(AionConnection con) {
    	Player player = con.getActivePlayer();
        if (player == null) {
            return;
        }
        House house = houseObject.getOwnerHouse();
        int templateId = houseObject.getObjectTemplate().getTemplateId();
        writeD(house.getAddress().getId());
        writeD(house.getOwnerId());
        writeD(houseObject.getObjectId());
        writeD(houseObject.getObjectId());
        writeD(templateId);
        writeF(houseObject.getX());
        writeF(houseObject.getY());
        writeF(houseObject.getZ());
        writeH(houseObject.getRotation());
        writeD(player.getHouseObjectCooldownList().getReuseDelay(houseObject.getObjectId()));
        if (houseObject.getUseSecondsLeft() > 0) {
            writeD(houseObject.getUseSecondsLeft());
        } else {
            writeD(0);
        }
        Integer color = null;
        if (houseObject != null) {
            color = houseObject.getColor();
        } if (color != null && color > 0) {
            writeC(1);
            writeC((color & 0xFF0000) >> 16);
            writeC((color & 0xFF00) >> 8);
            writeC((color & 0xFF));
        } else {
            writeC(0);
            writeC(0);
            writeC(0);
            writeC(0);
        }
        writeD(0);
        byte typeId = houseObject.getObjectTemplate().getTypeId();
        writeC(typeId);
        switch (typeId) {
            case 1:
                ((UseableItemObject) houseObject).writeUsageData(getBuf());
            break;
            case 7:
                NpcObject npcObj = (NpcObject) houseObject;
                writeD(npcObj.getNpcObjectId());
            default:
            break;
        }
    }
}