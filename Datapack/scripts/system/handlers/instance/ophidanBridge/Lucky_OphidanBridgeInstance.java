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
package instance.ophidanBridge;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
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

import java.util.Map;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(301320000)
public class Lucky_OphidanBridgeInstance extends GeneralInstanceHandler
{
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(235780, 751.4241f, 527.29016f, 576.37476f, (byte) 33); //Fugitive Mazikin.
			break;
			case 2:
				spawn(235781, 751.4241f, 527.29016f, 576.37476f, (byte) 33); //Runaway Hirakiki.
			break;
			case 3:
				spawn(235782, 751.4241f, 527.29016f, 576.37476f, (byte) 33); //Escapee Asachin.
			break;
		} switch (Rnd.get(1, 4)) {
			case 1:
				spawn(235768, 318.23724f, 488.92276f, 607.64343f, (byte) 1); //Spirited Velkur.
			break;
			case 2:
				spawn(235769, 318.23724f, 488.92276f, 607.64343f, (byte) 1); //Velkur Aethercaster.
			break;
			case 3:
				spawn(235770, 318.23724f, 488.92276f, 607.64343f, (byte) 1); //Velkur Aetherpriest.
			break;
			case 4:
				spawn(235771, 318.23724f, 488.92276f, 607.64343f, (byte) 1); //Velkur Aetherknife.
			break;
		} switch (Rnd.get(1, 3)) {
			case 1:
				spawn(235721, 673.0f, 472.0f, 599.3125f, (byte) 0); //Post Defense Drakenclaw.
			break;
			case 2:
				spawn(235726, 673.0f, 472.0f, 599.3125f, (byte) 0); //Defense Spelltongue.
			break;
			case 3:
				spawn(235727, 673.0f, 472.0f, 599.3125f, (byte) 0); //Defense Swiftrunner.
			break;
		} switch (Rnd.get(1, 3)) {
			case 1:
				spawn(235728, 531.0988f, 437.3993f, 620.25f, (byte) 109); //North Defense Drakenclaw.
			break;
			case 2:
				spawn(235730, 531.0988f, 437.3993f, 620.25f, (byte) 109); //North Defense Ironscale.
			break;
			case 3:
				spawn(235731, 531.0988f, 437.3993f, 620.25f, (byte) 109); //North Defense Hidestitcher.
			break;
		} switch (Rnd.get(1, 5)) {
			case 1:
				spawn(235735, 608.1635f, 558.9905f, 590.57214f, (byte) 110); //South Defense Drakenclaw.
			break;
			case 2:
				spawn(235736, 608.1635f, 558.9905f, 590.57214f, (byte) 110); //South Defense Bard.
			break;
			case 3:
				spawn(235737, 608.1635f, 558.9905f, 590.57214f, (byte) 110); //South Defense Ironscale.
			break;
			case 4:
				spawn(235738, 608.1635f, 558.9905f, 590.57214f, (byte) 110); //South Defense Hidestitcher.
			break;
			case 5:
				spawn(235740, 608.1635f, 558.9905f, 590.57214f, (byte) 110); //South Defense Spelltongue.
			break;
		} switch (Rnd.get(1, 6)) {
			case 1:
				spawn(235742, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Drakenclaw.
			break;
			case 2:
				spawn(235743, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Bard.
			break;
			case 3:
				spawn(235745, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Hidestitcher.
			break;
			case 4:
				spawn(235746, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Gunner.
			break;
			case 5:
				spawn(235747, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Spelltongue.
			break;
			case 6:
				spawn(235748, 480.99368f, 524.84326f, 597.43713f, (byte) 10); //Post Defense Swiftrunner.
			break;
		} switch (Rnd.get(1, 4)) {
			case 1:
				spawn(235772, 672.9581f, 468.63168f, 599.4349f, (byte) 1); //Hakara.
			break;
			case 2:
				spawn(235773, 672.9581f, 468.63168f, 599.4349f, (byte) 1); //Zubala.
			break;
			case 3:
				spawn(235774, 672.9581f, 468.63168f, 599.4349f, (byte) 1); //Visha.
			break;
			case 4:
				spawn(235775, 672.9581f, 468.63168f, 599.4349f, (byte) 1); //Bahapa.
			break;
		} switch (Rnd.get(1, 4)) {
			case 1:
				spawn(235776, 552.2419f, 512.9514f, 610.10693f, (byte) 26); //Hakara.
			break;
			case 2:
				spawn(235777, 552.2419f, 512.9514f, 610.10693f, (byte) 26); //Zubala.
			break;
			case 3:
				spawn(235778, 552.2419f, 512.9514f, 610.10693f, (byte) 26); //Visha.
			break;
			case 4:
				spawn(235779, 552.2419f, 512.9514f, 610.10693f, (byte) 26); //Bahapa.
			break;
		}
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 235759: //Fugitive Mazikin Leader.
			case 235763: //Runaway Hirakiki Leader.
			case 235767: //Escapee Asachin Leader.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    if (player.getCommonData().getRace() == Race.ELYOS) {
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 182215759, 1)); //The Piece Carried By The Fugitive.
						} else if (player.getCommonData().getRace() == Race.ASMODIANS) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 182215760, 1)); //The Piece Carried By The Fugitive.
						} switch (Rnd.get(1, 3)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053708, 1)); //Stolen Shelter Consumables Bundle.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053709, 1)); //Stolen Shelter Ancient Coin Bundle.
				            break;
					        case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053710, 1)); //Captured Shelter Relics Bundle.
				            break;
						}
					}
				}
			break;
			case 235768: //Spirited Velkur.
			case 235769: //Velkur Aethercaster.
			case 235770: //Velkur Aetherpriest.
			case 235771: //Velkur Aetherknife.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052612, 1)); //Vera's Treasure Crate.
					}
				}
			break;
			case 702658: //Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); //[Event] Abbey Bundle.
		    break;
			case 702659: //Noble Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); //[Event] Noble Abbey Bundle.
		    break;
			case 802180: //Ophidan Bridge Opportunity Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000051, 30)); //Major Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000052, 30)); //Greater Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000236, 50)); //Blood Mark.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000237, 50)); //Ancient Coin.
			break;
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 235768: //Spirited Velkur.
			case 235769: //Velkur Aethercaster.
			case 235770: //Velkur Aetherpriest.
			case 235771: //Velkur Aetherknife.
				sendMsg("[SUCCES]: You have finished <[Lucky] Ophidan Bridge>");
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 349.57327f, 495.25214f, 606.76013f, (byte) 91); //Abbey Box.
					break;
					case 2:
					    spawn(702659, 349.57327f, 495.25214f, 606.76013f, (byte) 91); //Noble Abbey Box.
					break;
				}
				spawn(730868, 350.18478f, 490.73065f, 606.34015f, (byte) 1); //Ophidan Bridge Exit.
				spawn(802180, 350.39514f, 486.26636f, 606.75397f, (byte) 32); //Ophidan Bridge Opportunity Bundle.
            break;
			case 235786: //Steel Wall.
				despawnNpc(npc);
			break;
		}
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
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
}