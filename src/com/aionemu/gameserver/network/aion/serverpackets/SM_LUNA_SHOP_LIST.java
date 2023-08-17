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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Made by Rinzler (Encom)
 */
public class SM_LUNA_SHOP_LIST extends AionServerPacket {

    private static final Logger log = LoggerFactory.getLogger(SM_LUNA_SHOP_LIST.class);
	private int actionId;
	private long points;
	private int keys;
	private int costId;
	private int entryCount;
	private int tableId;
	private List<Integer> idList;
	private List<Integer> randomDailyCraft;

	public SM_LUNA_SHOP_LIST(int actionId) {
		this.actionId = actionId;
	}

	public SM_LUNA_SHOP_LIST(int actionId, long points) {
		this.actionId = actionId;
		this.points = points;
	}

	public SM_LUNA_SHOP_LIST(int actionId, int keys) {
		this.actionId = actionId;
		this.keys = keys;
	}

	public SM_LUNA_SHOP_LIST(int actionId, int tableId, List<Integer> idList) {
		this.actionId = 2;
		this.tableId = 0;
		this.idList = idList;
	}
	
	public SM_LUNA_SHOP_LIST(List<Integer> randomDailyCraft) {
		this.actionId = 2;
		this.tableId = 1;
		this.randomDailyCraft = randomDailyCraft;
	}
	
	public SM_LUNA_SHOP_LIST(int actionId, int tableId, int costId) {
		this.actionId = actionId;
		this.tableId = tableId;
		this.costId = costId;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeC(actionId);//actionid
		switch (actionId) {
			case 0://luna point handler id
				writeQ(con.getAccount().getLuna());
			break;
			case 1://taki advanture update
				writeH(tableId);//size?
				writeD(costId);
				writeD(1);
			break;
			case 2:
				writeC(tableId);//tabId
				switch (tableId) {
					case 0:
						writeD(1474466400);//Start time
						writeD(0);
						writeD(1476280799);//End time
						writeD(0);
						writeH(idList.size());//size
						for (int i =0; i < idList.size(); i++) {
							writeD(idList.get(i));//luna recipe id
						}
					break;
					case 1 :
						writeD(1482393600);
						writeD(0); //test
						writeD(1482480000);
						writeD(0);
						writeH(randomDailyCraft.size());//size
						for (int i =0; i < randomDailyCraft.size(); i++) {
							writeD(randomDailyCraft.get(i));//luna recipe id
						}
					break;
				}
				break;
			case 4:// munirunerk's keys
				writeD(con.getActivePlayer().getMuniKeys());
				break;
			case 5:// luna consume point spent
				writeD(con.getActivePlayer().getLunaConsumePoint());
				break;
			case 6:// update taki's mission?
				break;
			case 7:
				writeC(0);
				writeH(100);
				break;
			case 8:// Updated for 5.8 on 16.05.2018
				writeH(tableId <= 5 ? tableId : (tableId - 1));
				writeD(10 + tableId);
				writeC((tableId + 10) == 16 ? 1 : 0);
				break;
			case 9:
				writeH(-1);
				writeC(0);
		}
	}
}