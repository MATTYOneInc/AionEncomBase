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
package ai.instance.tiamatStronghold;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/
 
@AIName("kahrun")
public class KahrunAI2 extends NpcAI2
{
    @Override
    protected void handleDialogStart(Player player) {
        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
    }
	
	@Override
    	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
			startProtectorateEvent();
			AI2Actions.deleteOwner(this);
        }
        return true;
    }
	
	private void startProtectorateEvent() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Npc fileLadderCGF = getPosition().getWorldMapInstance().getNpc(730612);
				Npc aionFXPostGlow = getPosition().getWorldMapInstance().getNpc(730694);
				Npc kharunReianLeader = (Npc)spawn(800335, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 60);
			    kharunReianLeader.setTarget(aionFXPostGlow);
			    //Stand back. I will take care of this barrier.
				NpcShoutsService.getInstance().sendMsg(kharunReianLeader, 1500596, kharunReianLeader.getObjectId(), 0, 1000);
				SkillEngine.getInstance().getSkill(kharunReianLeader, 20943, 60, aionFXPostGlow).useNoAnimationSkill();
			    fileLadderCGF.getController().onDelete();
			    aionFXPostGlow.getController().onDelete();
			}
	    }, 3000);
    }
}