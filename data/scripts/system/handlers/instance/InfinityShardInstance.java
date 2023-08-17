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
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(300800000)
public class InfinityShardInstance extends GeneralInstanceHandler
{
	private int ideForcefieldGenerator;
	private boolean isInstanceDestroyed = false;
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
            case 231073: //Hyperion.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
                    } switch (Rnd.get(1, 4)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052387, 1)); //Hyperion's Equipment Box.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052718, 1)); //Hyperion's Weapons Chest.
				        break;
						case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053005, 1)); //Hyperion's Wing Chest.
				        break;
						case 4:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053154, 1)); //Hyperion's Accessory Box.
				        break;
					}
                }
            break;
			case 802184: //Infinity Shard Opportunity Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000051, 30)); //Major Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000052, 30)); //Greater Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000236, 50)); //Blood Mark.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000237, 50)); //Ancient Coin.
			break;
        }
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		SpawnTemplate protectiveShield = SpawnEngine.addNewSingleTimeSpawn(300800000, 284437, 129.26147f, 137.86557f, 110.50481f, (byte) 0);
		protectiveShield.setEntityId(27);
		objects.put(284437, SpawnEngine.spawnObject(protectiveShield, instanceId)); //Protective Shield.
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 231096: //Hyperion Defense Combatant.
			case 231097: //Hyperion Defense Scout.
			case 231098: //Hyperion Defense Medic.
			case 231103: //Summoned Ancien Tyrhund.
			case 233297: //Hyperion Defense Assaulter.
			case 233298: //Hyperion Defense Assassin.
			    despawnNpc(npc);
			break;
			case 231074: //Ide Forcefield Generator I.
			case 231078: //Ide Forcefield Generator II.
			case 231082: //Ide Forcefield Generator III.
			case 231086: //Ide Forcefield Generator IV.
				ideForcefieldGenerator++;
				if (ideForcefieldGenerator == 1) {
				} else if (ideForcefieldGenerator == 2) {
				} else if (ideForcefieldGenerator == 3) {
				} else if (ideForcefieldGenerator == 4) {
				    //The Hyperion's shields are down.
					sendMsgByRace(1401796, Race.PC_ALL, 10000);
					deleteNpc(284437); //Protective Shield.
				}
				despawnNpc(npc);
				//The Hyperion's shields are faltering.
				sendMsgByRace(1401795, Race.PC_ALL, 0);
            break;
			case 231073: //Hyperion.
			    sendMsg("[SUCCES]: You have finished <Infinity Shard>");
				spawn(730842, 124.669853f, 137.840668f, 113.942917f, (byte) 0); //Infinity Shard Exit.
				spawn(802184, 127.32316f, 131.72421f, 112.17429f, (byte) 25); //Infinity Shard Opportunity Bundle.
			break;
		}
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	@Override
    public void onInstanceDestroy() {
        isInstanceDestroyed = true;
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
}