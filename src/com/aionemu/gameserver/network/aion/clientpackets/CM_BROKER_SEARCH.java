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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CM_BROKER_SEARCH extends AionClientPacket
{
	Logger log = LoggerFactory.getLogger(CM_BROKER_SEARCH.class);
	
    @SuppressWarnings("unused")
    private int brokerId;
    private int sortType;
    private int page;
    private int mask;
    private int itemCount;
    private List<Integer> itemList;
    
    private int unk1;
    private int unk2;
    private int unk3;
    private int minLvl;
    private int maxLvl;
    private int minUnk;
    private int maxUnk;
    private int unk4;
	
    public CM_BROKER_SEARCH(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
	
    @Override
    protected void readImpl() {
        this.brokerId = readD();
        this.sortType = readC();
        this.page = readH();
        unk1 = readC();
        unk2 = readH();
        this.mask = readH();
        unk3 = readD();
        minLvl = readH();
        maxLvl = readH();
        minUnk = readC();
        maxUnk = readC();
        unk4 = readH();
        
        this.itemCount = readH();
        this.itemList = new ArrayList<Integer>();
        for (int index = 0; index < this.itemCount; index++) {
            this.itemList.add(readD());
        }
    }
	
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        BrokerService.getInstance().showRequestedItems(player, mask, sortType, page, itemList);
        //log.info("CM_BROKER_SEARCH brokerId:"+brokerId+" sortType:"+sortType+" page:"+page+" unk1:"+unk1+" unk2:"+unk2+" mask:"+mask+" unk3:"+unk3+" minLvl:"+minLvl+" maxLvl:"+maxLvl+" minUnk:"+minUnk+" maxUnk:"+maxUnk+" unk4:"+unk4+" itemCount:"+itemCount+"");
    }
}