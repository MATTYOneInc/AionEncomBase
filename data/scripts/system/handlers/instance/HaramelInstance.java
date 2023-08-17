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
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(300200000)
public class HaramelInstance extends GeneralInstanceHandler
{
	private List<Integer> movies = new ArrayList<Integer>();
    
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("instances/haramel.xhtml"));
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 217025: //Keymaster MuMu Dang.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000103, 1)); //Rusty Key.
			break;
			case 217108: //MuMu Mechanic.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000107, 1)); //Lubricating Oil.
			break;
			case 700829: //Ancient Treasure Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188052574, 1)); //Hamerun's Special Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053787, 1)); //Stigma Support Bundle.
				switch (Rnd.get(1, 5)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080005, 2)); //Lesser Minion Contract.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080006, 2)); //Greater Minion Contract.
					break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080007, 2)); //Major Minion Contract.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080008, 2)); //Cute Minion Contract.
					break;
					case 5:
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190200000, 50)); //Minium.
					break;
				}
			break;
		}
	}
	
	@Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 216915: //Boss Nukiti.
                sendMsg("<Hamerun The Bleeder> appear");
				spawn(216922, 224.39445f, 262.08163f, 144.89798f, (byte) 30); //Hamerun The Bleeder.
            break;
			case 700855: //Prison Doors.
			    despawnNpc(npc);
			break;
			case 216922: //Hamerun The Bleeder.
                despawnNpc(npc);
			    sendMovie(player, 457);
				//Hamerun has dropped a treasure chest.
				sendMsgByRace(1400713, Race.PC_ALL, 0);
				sendMsg("[SUCCES]: You have finished <Haramel>");
			    spawn(700829, 224.137f, 268.608f, 144.898f, (byte) 90); //Ancient Treasure Box.
				spawn(700852, 223.93062f, 337.5487f, 142.43079f, (byte) 90); //Opened Dimensional Gate.
			    ItemService.addItem(player, 188900008, 1); //Secret Remedy Of Growth II.
			break;
        }
    }
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	@Override
    public void onInstanceDestroy() {
		movies.clear();
    }
}