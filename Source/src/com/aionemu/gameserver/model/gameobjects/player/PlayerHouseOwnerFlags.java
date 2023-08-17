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
package com.aionemu.gameserver.model.gameobjects.player;

public enum PlayerHouseOwnerFlags
{
    IS_OWNER(1 << 0),
    HAS_OWNER(1 << 0),
    BUY_STUDIO_ALLOWED(1 << 1),
    SINGLE_HOUSE(1 << 1),
    BIDDING_ALLOWED(1 << 2),
    HOUSE_OWNER((IS_OWNER.getId() | BIDDING_ALLOWED.getId()) & ~BUY_STUDIO_ALLOWED.getId()),
    SELLING_HOUSE(IS_OWNER.getId() | BUY_STUDIO_ALLOWED.getId()),
    
	//Player Status
    SOLD_HOUSE(BIDDING_ALLOWED.getId() | BUY_STUDIO_ALLOWED.getId());
    private byte id;
	
    private PlayerHouseOwnerFlags(int id) {
        this.id = (byte) (id & 0xFF);
    }
	
    public byte getId() {
        return id;
    }
}