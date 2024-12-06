/*
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
package ai.instance.esoterrace;

import com.aionemu.commons.utils.Rnd;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.ai2.manager.*;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.actions.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.*;
import com.aionemu.gameserver.utils.*;

/****/
/** Author (Encom)
/****/

@AIName("Reian_Refugee")
public class Reian_RefugeeAI2 extends NpcAI2
{
    private int size;
	
    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        spawnReianRefugee();
    }
	
    private void spawnReianRefugee() {
        size++;
        int refugee = 0;
        switch (Rnd.get(1, 2)) {
            case 1:
                refugee = 799626;
			break;
            case 2:
                refugee = 799627;
			break;
        }
		int msg = 0;
        switch (Rnd.get(1, 2)) {
            case 1:
                msg = 340937;
            break;
            case 2:
                msg = 340955;
            break;
        }
        Npc npc = (Npc) spawn(refugee, 391.13388f, 542.73413f, 319.51218f, (byte) 79);
        npc.getSpawn().setWalkerId("Reian_Refugee_1");
        WalkManager.startWalking((NpcAI2) npc.getAi2());
        npc.setState(1);
        PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
		if (msg != 0) {
            NpcShoutsService.getInstance().sendMsg(npc, msg, npc.getObjectId(), 0, 10000);
        }
    }
	
    @Override
    protected void handleCreatureMoved(Creature creature) {
        if (creature instanceof Npc) {
            Npc npc = (Npc) creature;
            int refugee = npc.getNpcId();
            if (refugee == 799626 || refugee == 799627) {
                int point = npc.getMoveController().getCurrentPoint();
                if (point == 3 && size < 2) {
                    spawnReianRefugee();
                } else if (point == 0) {
                    npc.getMoveController().abortMove();
                    npc.getSpawn().setWalkerId(null);
                    WalkManager.stopWalking((NpcAI2) npc.getAi2());
                    NpcActions.delete(npc);
                    size--;
                }
            }
        }
        super.handleCreatureMoved(creature);
    }
}