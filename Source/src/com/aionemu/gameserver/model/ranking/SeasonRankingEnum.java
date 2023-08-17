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
package com.aionemu.gameserver.model.ranking;

/**
 * Created by Wnkrz on 24/07/2017.
 */

public enum  SeasonRankingEnum {
    HALL_OF_TENACITY(1),
    ARENA_OF_TENACITY(541),
    TOWER_OF_CHALLENGE(2),
    ARENA_6V6(3);

    private int tableId;

    private SeasonRankingEnum(int tableId){
        this.tableId = tableId;
    }

    public int getId() {
        return tableId;
    }
}