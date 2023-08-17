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

import java.util.*;
import javolution.util.*;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@InstanceID(300170000)
public class BeshmundirTempleInstance extends GeneralInstanceHandler
{
	private int macunbelloSoul;
	private int warriorMonument;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> beshmundirTask = FastList.newInstance();
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 216161: //Vehala The Cursed.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 122001162, 1)); //Vehalla's Ring.
		    break;
			case 216163: //The Plaguebearer.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 120001039, 1)); //Plaguebearer's Earrings.
		    break;
			case 216168: //Flarestorm.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051387, 1)); //Flarestorm's Fabled Headgear Chest.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123001032, 1)); //Flarestorm's Leather Belt.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123001033, 1)); //Flarestorm's Sash.
				        break;
					} switch (Rnd.get(1, 4)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100001081, 1)); //Flarestorm's Sword.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100200954, 1)); //Flarestorm's Dagger.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101800869, 1)); //Flarestorm's Pistol.
				        break;
						case 4:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 102100758, 1)); //Flarestorm's Cipher-Blade.
				        break;
					}
				}
			break;
			case 216248: //Taros Lifebane.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000092, 1)); //Temple Of Eternity Key.
		    break;
			case 216170: //Gatekeeper Darfall.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000093, 1)); //Meditation Chamber Key.
		    break;
			case 216171: //Gatekeeper Kutarrun.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000094, 1)); //Contemplation Chamber Key.
		    break;
			case 216172: //Gatekeeper Samarrn.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000095, 1)); //Supplication Chamber Key.
		    break;
			case 216173: //Gatekeeper Rhapsharr.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000096, 1)); //Petition Chamber Key.
		    break;
			case 216238: //Captain Lakhara.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 122001163, 1)); //Lakhara's Ring.
					}
				}
			break;
			case 216239: //Ahbana The Wicked.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051390, 1)); //Ahbana's Eternal Shoes Chest.
					}
				}
			break;
			case 216241: //The Plaguebearer.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 120001040, 1)); //Manadar's Earrings.
		    break;
			case 216245: //Macunbello.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051391, 1)); //Macunbello's Eternal Gloves Chest.
					}
				}
			break;
			case 216246: //The Great Virhana.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					} switch (Rnd.get(1, 4)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100001006, 1)); //Virhana's Sword.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100200900, 1)); //Virhana's Dagger.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101800868, 1)); //Virhana's Pistol.
				        break;
						case 4:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 102100757, 1)); //Virhana's Cipher-Blade.
				        break;
					}
				}
			break;
			case 216250: //Dorakiki The Bold.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051392, 1)); //Dorakiki The Bold's Eternal Shoulder Armor Chest.
					}
				}
			break;
			case 216263: //Isbariya The Resolute.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051388, 1)); //Isbariya's Fabled Jacket Chest.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051393, 1)); //Isbariya's Eternal Pants Chest.
				        break;
					}
				}
			break;
			case 216264: //Stormwing.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); //Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000099, 1)); //Vorpal Essence.
					} switch (Rnd.get(1, 3)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051389, 1)); //Stormwing's Fabled Weapon Chest.
				        break;
						case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051394, 1)); //Stormwing's Eternal Jacket Chest.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051395, 1)); //Stormwing's Eternal Weapon Chest.
				        break;
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 170030043, 1)); //[Souvenir] Stormwing Wall-Mount Trophy.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 170490002, 1)); //[Souvenir] Stormwing Statue.
				        break;
					} switch (Rnd.get(1, 3)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000079, 1)); //Golden Stormwing Egg.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020133, 1)); //[Event] Stormwing Egg.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020138, 1)); //Stormwing Egg.
				        break;
					} switch (Rnd.get(1, 4)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100013, 1)); //[Souvenir] Stormwing's Scroll Piece.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100034, 1)); //[Souvenir] Stormwing's Head.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100035, 1)); //[Souvenir] Stormwing's Skeleton.
				        break;
						case 4:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100036, 1)); //[Souvenir] Stormwing's Wing.
				        break;
					}
				}
			break;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
        doors = instance.getDoors();
		Npc npc = instance.getNpc(216245); //Macunbello.
		if (npc != null) {
			npc.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
			SkillEngine.getInstance().getSkill(npc, 19046, 60, npc).useNoAnimationSkill(); //Soul Starved I.
		}
    }
	
	@Override
    public void handleUseItemFinish(Player player, Npc npc) {
        switch (npc.getNpcId()) {
			case 730290: //Entrance Of Blue Flame Incinerator.
				if (player.getInventory().decreaseByItemId(185000091, 1)) { //Incinerator Key.
					despawnNpc(npc);
			    } else {
					//Key required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403686));
				}
            break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000091, storage.getItemCountByItemId(185000091)); //Incinerator Key.
		storage.decreaseByItemId(185000092, storage.getItemCountByItemId(185000092)); //Temple Of Eternity Key.
		storage.decreaseByItemId(185000093, storage.getItemCountByItemId(185000093)); //Meditation Chamber Key.
		storage.decreaseByItemId(185000094, storage.getItemCountByItemId(185000094)); //Contemplation Chamber Key.
		storage.decreaseByItemId(185000095, storage.getItemCountByItemId(185000095)); //Supplication Chamber Key.
		storage.decreaseByItemId(185000096, storage.getItemCountByItemId(185000096)); //Petition Chamber Key.
	}
	
	@Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		   /**
			* Path To Watcher's Nexus.
			*/
			case 216238: //Captain Lakhara.
			    doors.get(470).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
            break;
			case 216739: //Warrior Monument.
                warriorMonument++;
				if (warriorMonument == 15) {
                	//Ahbana the Wicked has appeared in the Watcher's Nexus.
					sendMsgByRace(1400470, Race.PC_ALL, 5000);
					sp(216239, 1356.9945f, 149.51117f, 246.27036f, (byte) 29, 5000, 0, null); //Ahbana The Wicked.
                }
				despawnNpc(npc);
				//The Warrior Monument has been destroyed. Ahbana the Wicked is on alert.
				sendMsgByRace(1400465, Race.PC_ALL, 0);
            break;
			case 216239: //Ahbana The Wicked.
			    doors.get(471).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
            break;
			
		   /**
			* Path To Macunbello's Refuge.
			*/
			case 216583: //Brutal Soulwatcher (1st Island)
				sp(799518, 933.982971f, 444.269104f, 222.00f, (byte) 21, 3000, 0, null); //Plegeton Boatman II.
			break;
			case 216584: //Brutal Soulwatcher (2nd Island)
				sp(799519, 788.744690f, 442.353271f, 222.00f, (byte) 0, 3000, 0, null); //Plegeton Boatman III.
			break;
			case 216585: //Brutal Soulwatcher (3th Island)
				sp(799520, 818.578740f, 277.745270f, 220.19f, (byte) 53, 3000, 0, null); //Plegeton Boatman IV.
			break;
			case 216206: //Elyos Spiritblade.
			case 216207: //Elyos Spiritmage.
			case 216208: //Elyos Spiritbow.
			case 216209: //Elyos Spiritsalve.
			case 216210: //Asmodian Soulsword.
			case 216211: //Asmodian Soulspell.
			case 216212: //Asmodian Soulranger.
			case 216213: //Asmodian Soulmedic.
			    Npc macunbello = instance.getNpc(216245); //Macunbello.
			    macunbelloSoul++;
				if (macunbello != null) {
				    if (macunbelloSoul == 7) {
					    //Macunbello's power is weakening.
					    sendMsgByRace(1400466, Race.PC_ALL, 2000);
						macunbello.getEffectController().removeEffect(19046); //Soul Starved I.
						SkillEngine.getInstance().applyEffectDirectly(19047, macunbello, macunbello, 0); //Soul Starved II.
				    } else if (macunbelloSoul == 14) {
					    //Macunbello's power has weakened.
					    sendMsgByRace(1400467, Race.PC_ALL, 2000);
						macunbello.getEffectController().removeEffect(19047); //Soul Starved II.
						SkillEngine.getInstance().applyEffectDirectly(19048, macunbello, macunbello, 0); //Soul Starved III.
				    } else if (macunbelloSoul == 21) {
					    //Macunbello has been crippled.
					    sendMsgByRace(1400468, Race.PC_ALL, 2000);
						macunbello.getEffectController().removeEffect(19048); //Soul Starved III.
				    }
				}
			break;
			case 216586: //Temadaro.
			    sendMovie(player, 445);
				doors.get(467).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
			    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
			break;
			
		   /**
			* Path To Garden Of The Entombed.
			*/
			case 216246: //The Great Virhana.
				doors.get(473).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
			break;
			case 216250: //Dorakiki The Bold.
				deleteNpc(281647); //Fixit.
				deleteNpc(281648); //Sorcerer Haskin.
				deleteNpc(281649); //Chopper.
			break;
			
		   /**
			* Path To The Prison Of Ice.
			*/
			case 216263: //Isbariya The Resolute.
				sendMovie(player, 439);
				//The Seal Protector has fallen. The Rift Orb shines while the seal weakens.
				sendMsgByRace(1400480, Race.PC_ALL, 0);
				deleteNpc(281645); //Sacrificial Soul.
				sp(216264, 556.59375f, 1367.2274f, 224.79459f, (byte) 75, 3000, 0, null); //Stormwing.
				sp(730275, 1611.1663f, 1604.7267f, 311.04984f, (byte) 0, 426, 3000, 0, null); //Rift Orb.
			break;
			case 216264: //Stormwing.
				deleteNpc(281794);
				deleteNpc(281795);
				deleteNpc(281796);
				deleteNpc(281797);
				deleteNpc(281798);
				sp(730287, 565.25275f, 1376.6252f, 224.79459f, (byte) 74, 3000, 0, null); //Rift Orb.
			break;
		}
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = beshmundirTask.head(), end = beshmundirTask.tail(); (n = n.getNext()) != end;) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        beshmundirTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        beshmundirTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
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
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
    @Override
    public void onInstanceDestroy() {
        isInstanceDestroyed = true;
		movies.clear();
		doors.clear();
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
}