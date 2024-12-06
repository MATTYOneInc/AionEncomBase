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
package instance.steelRake;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
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
import javolution.util.FastMap;

import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(300460000)
public class SteelRakeCabineInstance extends GeneralInstanceHandler
{
	private boolean isInstanceDestroyed;
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		switch (Rnd.get(1, 2)) {
			case 1:
				spawn(219032, 462.71558f, 512.5599f, 952.5455f, (byte) 1); //Madame Bovariki.
			break;
			case 2:
				spawn(219032, 506.1134f, 545.7197f, 952.4226f, (byte) 74); //Madame Bovariki.
			break;
		}
	}
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
        switch (npcId) {
			case 219040: //Tamer Anikiki [Steel Rake Cabin]
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182209084, 1)); //Taming A Manduri.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182209099, 1)); //Taming A Manduri.
			break;
			case 219033: //Golden Eye Mantutu.
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
			case 215489: //Treasure Box.
			case 700553: //Treasure Box.
			case 700554: //Pirate Ship Treasure Box.
				switch (Rnd.get(1, 6)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050017, 2)); //Blue Idian: Physical Attack.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050018, 2)); //Blue Idian: Magical Attack.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050019, 2)); //Blue Idian: Physical Defense.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050020, 2)); //Blue Idian: Magical Defense.
					break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050021, 2)); //Blue Idian: Assistance.
					break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050022, 2)); //Blue Idian: Resistance.
					break;
				}
			break;
        }
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 700549: //Air Vent Cover.
			    despawnNpc(npc);
			break;
			case 219033: //Golden Eye Mantutu.
				////sendMsg("[SUCCES]: You have finished <Steel Rake Cabine>");
				spawn(700554, 736.64728f, 493.73834f, 941.4781f, (byte) 45); //Pirate Ship Treasure Box.
				spawn(700554, 720.41028f, 511.63718f, 939.7604f, (byte) 90); //Pirate Ship Treasure Box.
		        spawn(700554, 739.51251f, 506.14313f, 941.4781f, (byte) 77); //Pirate Ship Treasure Box.
				spawn(700554, 721.76172f, 491.83142f, 939.6068f, (byte) 32); //Pirate Ship Treasure Box.
				spawn(730766, 734.18994f, 484.61578f, 941.70868f, (byte) 0, 61); //Hidden Passage.
			break;
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
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
}