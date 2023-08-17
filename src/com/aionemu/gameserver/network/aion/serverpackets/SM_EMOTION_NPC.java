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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_EMOTION_NPC extends AionServerPacket
{
    private Creature npc;
    private Player player;
    private int state = 0;
    private int emotionType = 0;
    private boolean isPlayer = false;
	
    public SM_EMOTION_NPC(Npc npc, int state, EmotionType et) {
        this.npc = npc;
        this.state = state;
        this.emotionType = et.getTypeId();
        this.isPlayer = false;
    }
	
    public SM_EMOTION_NPC(Player player, int state, EmotionType et) {
        this.player = player;
        this.state = state;
        this.emotionType = et.getTypeId();
        this.isPlayer = true;
    }
	
    @Override
    protected void writeImpl(AionConnection con) {
    	if (isPlayer) {
    		writeD(player.getObjectId());
		} else {
    		writeD(npc.getObjectId());
		}
        writeC(state);
        writeD(emotionType);
        writeD(0);
    }
}