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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Map;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(310050000)
public class AetherogeneticsLabInstance extends GeneralInstanceHandler
{
    private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 212341: //The Keykeeper.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000001, 1)); //Lepharist Research Center Key 1.
		    break;
			case 212175: //Expert Lab Scholar.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000002, 1)); //Lepharist Research Center Key 2.
		    break;
			case 212193: //Pretor Key Keeper.
				switch (Rnd.get(1, 2)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000003, 1)); //Lepharist Research Center Key 3.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000004, 1)); //Lepharist Research Center Key 4.
				    break;
				}
		    break;
			case 212202: //Gatekeeper.
			case 212342: //Key Eater.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000005, 1)); //Lepharist Research Center Key 5.
		    break;
			case 212211: //RM-78C.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
                    }
                }
            break;
        }
    }
	
    @Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		switch (Rnd.get(1, 2)) {
			case 1:
				spawn(212193, 206.10997f, 245.62978f, 132.8339f, (byte) 0); //Pretor Key Keeper.
			break;
			case 2:
				spawn(212193, 205.3984f, 215.02821f, 132.83458f, (byte) 0); //Pretor Key Keeper.
			break;
		}
	}
	
    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 212211: //RM-78C.
			    sendMsg("Congratulation]: you finish <Aetherogenetics Lab>");
			break;
		}
    }
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(185000001, storage.getItemCountByItemId(185000001)); //Lepharist Research Center Key 1.
		storage.decreaseByItemId(185000002, storage.getItemCountByItemId(185000002)); //Lepharist Research Center Key 2.
		storage.decreaseByItemId(185000003, storage.getItemCountByItemId(185000003)); //Lepharist Research Center Key 3.
		storage.decreaseByItemId(185000004, storage.getItemCountByItemId(185000004)); //Lepharist Research Center Key 4.
		storage.decreaseByItemId(185000005, storage.getItemCountByItemId(185000005)); //Lepharist Research Center Key 5.
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
}