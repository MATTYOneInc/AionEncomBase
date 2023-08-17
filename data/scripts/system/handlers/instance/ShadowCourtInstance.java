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
package instance;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(320120000)
public class ShadowCourtInstance extends GeneralInstanceHandler
{
    private List<Integer> movies = new ArrayList<Integer>();
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		if (movies.contains(423)) {
            return;
        }
		sendMovie(player, 423);
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 214347: //Unfest Guard Captain.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000014, 1)); //Arena Basement Level 3 Key 1.
		    break;
			case 214349: //Dysceptic Karnif.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000011, 1)); //Arena Basement Level 2 Key 1.
		    break;
			case 214351: //Dysceptic Taiga.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000012, 1)); //Arena Basement Level 2 Key 2.
		    break;
			case 214353: //Bejeweled Mosbear.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000013, 1)); //Arena Basement Level 2 Key 3.
		    break;
			case 214357: //Cleric Wraith.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000009, 1)); //Arena Basement Level 1 Key 2.
		    break;
			case 214360: //Ranger Spirit.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000010, 1)); //Arena Basement Level 1 Key 3.
		    break;
			case 214531: //Prison Guard.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000008, 1)); //Arena Basement Level 1 Key 1.
		    break;
		}
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	@Override
    public void onInstanceDestroy() {
        movies.clear();
	}
}