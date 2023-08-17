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
package com.aionemu.gameserver.model.house;

import java.sql.Timestamp;

public class PlayerHouseBid implements Comparable<PlayerHouseBid>
{
	private int playerId;
    private int houseId;
    private long offer;
    private Timestamp time;
	
    public PlayerHouseBid(int playerId, int houseId, long offer, Timestamp time) {
        this.playerId = playerId;
        this.houseId = houseId;
        this.offer = offer;
        this.time = time;
    }
	
    public int getPlayerId() {
        return playerId;
    }
	
    public int getHouseId() {
        return houseId;
    }
	
    public long getBidOffer() {
        return offer;
    }
	
    public Timestamp getTime() {
        return time;
    }
	
    @Override
    public int compareTo(PlayerHouseBid o) {
        return (int) (time.getTime() - o.getTime().getTime());
    }
}