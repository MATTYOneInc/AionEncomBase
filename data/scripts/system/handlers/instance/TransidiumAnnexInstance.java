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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(400030000)
public class TransidiumAnnexInstance extends GeneralInstanceHandler
{
    private long startTime;
	private Race spawnRace;
	private int hangarBarricade;
	private Future<?> instanceTimer;
	private int transidiumAnnexBase;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 277224: //Ahserion.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053117, 1)); //Ahserion's Glory Reward Box.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188056852, 1)); //Ahserion's Equipment Box.
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
		Npc npc = instance.getNpc(277224); //Ahserion.
		if (npc != null) {
			SkillEngine.getInstance().getSkill(npc, 21571, 60, npc).useNoAnimationSkill(); //Ereshkigal's Reign.
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			//Loading the Advance Corridor Shield... Please wait.
			sendMsgByRace(1402252, Race.PC_ALL, 10000);
			//The Advance Corridor Shield has been activated.
			//If the protection device is destroyed, the corridor will disappear and you will return to the fortress.
			sendMsgByRace(1402637, Race.PC_ALL, 20000);
			//The member recruitment window has passed. You cannot recruit any more members.
			sendMsgByRace(1401181, Race.PC_ALL, 50000);
			//The effect of the Transidium Annex has weakened the Hangar Barricade.
			sendMsgByRace(1402638, Race.PC_ALL, 1200000);
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					openFirstDoors();
					sendMsg(1401838);
					sendQuestionWindow();
				}
			}, 60000);
		}
	}
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CHARIOT_HANGAR_1_400030000")) {
			transidiumAnnexBase = 1;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CHARIOT_HANGAR_2_400030000")) {
			transidiumAnnexBase = 2;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("IGNUS_ENGINE_HANGAR_1_400030000")) {
            transidiumAnnexBase = 3;
	    } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("IGNUS_ENGINE_HANGAR_2_400030000")) {
			transidiumAnnexBase = 4;
		}
    }
	
	@Override
    public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        }
		Race race = mostPlayerDamage.getRace();
		switch (npc.getObjectTemplate().getTemplateId()) {
			//Belus Advance Corridor Shield.
			case 297306:
				//The Belus Advance Corridor Shield has been destroyed.
				//The Daevas from the Belus camp have returned to the Arcadian Fortress.
				sendMsgByRace(1402270, Race.PC_ALL, 2000);
				//The Advance Corridor Shield will disappear soon.
				sendMsgByRace(1402641, Race.PC_ALL, 7000);
				//You will return to the fortress soon.
				sendMsgByRace(1402642, Race.PC_ALL, 12000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								//[Arcadian Fortress]
								TeleportService2.teleportTo(player, 400020000, 1023.73315f, 1023.5483f, 1530.4855f, (byte) 27);
							}
						});
						onInstanceDestroy();
					}
				}, 15000);
			break;
			//Aspida Advance Corridor Shield.
			case 297307:
				//The Aspida Advance Corridor Shield is under attack.
				//The Daevas from the Aspida camp have returned to the Umbral Fortress.
				sendMsgByRace(1402271, Race.PC_ALL, 2000);
				//The Advance Corridor Shield will disappear soon.
				sendMsgByRace(1402641, Race.PC_ALL, 7000);
				//You will return to the fortress soon.
				sendMsgByRace(1402642, Race.PC_ALL, 12000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								//[Umbral Fortress]
								TeleportService2.teleportTo(player, 400040000, 1023.73315f, 1023.5483f, 1530.4855f, (byte) 27);
							}
						});
						onInstanceDestroy();
					}
				}, 15000);
			break;
			//Atanatos Advance Corridor Shield.
			case 297308:
				//The Atanatos Advance Corridor Shield is under attack.
				//The Daevas from the Atanatos camp have returned to the Eternum Fortress.
				sendMsgByRace(1402272, Race.PC_ALL, 2000);
				//The Advance Corridor Shield will disappear soon.
				sendMsgByRace(1402641, Race.PC_ALL, 7000);
				//You will return to the fortress soon.
				sendMsgByRace(1402642, Race.PC_ALL, 12000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								//[Eternum Fortress]
								TeleportService2.teleportTo(player, 400050000, 1023.73315f, 1023.5483f, 1530.4855f, (byte) 27);
							}
						});
						onInstanceDestroy();
					}
				}, 15000);
			break;
			//Disillon Advance Corridor Shield.
			case 297309:
				//The Disillon Advance Corridor Shield has been destroyed.
				//The Daevas from the Disillon camp have returned to the Skyclash Fortress.
				sendMsgByRace(1402273, Race.PC_ALL, 2000);
				//The Advance Corridor Shield will disappear soon.
				sendMsgByRace(1402641, Race.PC_ALL, 7000);
				//You will return to the fortress soon.
				sendMsgByRace(1402642, Race.PC_ALL, 12000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								//[Skyclash Fortress]
								TeleportService2.teleportTo(player, 400060000, 1023.73315f, 1023.5483f, 1530.4855f, (byte) 27);
							}
						});
						onInstanceDestroy();
					}
				}, 15000);
			break;
			case 297310: //Chariot Hangar I Controller.
		        despawnNpc(npc);
				if (transidiumAnnexBase == 1) {
				    if (race.equals(Race.ELYOS)) {
					    deleteNpc(804118);
						//Chariot Hangar I Controller has been destroyed.
						sendMsgByRace(1402262, Race.PC_ALL, 0);
					    spawn(804116, 335.55713f, 512.7856f, 683.0075f, (byte) 61); //Elyos Chariot Hangar I Flag.
				    } else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(804118);
						//Chariot Hangar I Controller has been destroyed.
						sendMsgByRace(1402262, Race.PC_ALL, 0);
					    spawn(804114, 335.55713f, 512.7856f, 683.0075f, (byte) 61); //Elyos Chariot Hangar I Flag.
				    }
				}
			break;
			case 297311: //Chariot Hangar II Controller.
			    despawnNpc(npc);
				if (transidiumAnnexBase == 2) {
				    if (race.equals(Race.ELYOS)) {
					    deleteNpc(804123);
						//Chariot Hangar II Controller has been destroyed.
						sendMsgByRace(1402263, Race.PC_ALL, 0);
					    spawn(804121, 681.18427f, 513.76154f, 683.0339f, (byte) 0); //Elyos Chariot Hangar II Flag.
				    } else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(804123);
						//Chariot Hangar II Controller has been destroyed.
						sendMsgByRace(1402263, Race.PC_ALL, 0);
					    spawn(804119, 681.18427f, 513.76154f, 683.0339f, (byte) 0); //Asmodians Chariot Hangar II Flag.
				    }
				}
			break;
			case 297312: //Ignus Engine Hangar I Controller.
			    despawnNpc(npc);
				if (transidiumAnnexBase == 3) {
				    if (race.equals(Race.ELYOS)) {
					    deleteNpc(804128);
						//Ignus Engine Hangar I Controller has been destroyed.
						sendMsgByRace(1402264, Race.PC_ALL, 0);
					    spawn(804126, 508.25092f, 339.45773f, 683.0075f, (byte) 91); //Elyos Ignus Engine Hangar I Flag.
				    } else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(804128);
						//Ignus Engine Hangar I Controller has been destroyed.
						sendMsgByRace(1402264, Race.PC_ALL, 0);
					    spawn(804124, 508.25092f, 339.45773f, 683.0075f, (byte) 91); //Asmodians Ignus Engine Hangar I Flag.
				    }
				}
			break;
			case 297313: //Ignus Engine Hangar II Controller.
				despawnNpc(npc);
				if (transidiumAnnexBase == 4) {
				    if (race.equals(Race.ELYOS)) {
					    deleteNpc(804133);
						//Ignus Engine Hangar II Controller has been destroyed.
						sendMsgByRace(1402265, Race.PC_ALL, 0);
					    spawn(804131, 508.54236f, 686.10504f, 683.0075f, (byte) 30); //Elyos Ignus Engine Hangar II Flag.
				    } else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(804133);
						//Ignus Engine Hangar II Controller has been destroyed.
						sendMsgByRace(1402265, Race.PC_ALL, 0);
					    spawn(804129, 508.54236f, 686.10504f, 683.0075f, (byte) 30); //Asmodians Ignus Engine Hangar II Flag.
				    }
				}
			break;
			case 277229: //Hangar Barricade.
				Npc ahserion = instance.getNpc(277224); //Ereshkigal's Reign.
				hangarBarricade++;
				if (ahserion != null) {
				    if (hangarBarricade == 1) {
				    } else if (hangarBarricade == 2) {
				    } else if (hangarBarricade == 3) {
				    } else if (hangarBarricade == 4) {
					    ahserion.getEffectController().removeEffect(21571); //Ereshkigal's Reign.
				    }
				}
				despawnNpc(npc);
			break;
			case 277224: //Ahserion.
				//sendMsg("[SUCCES]: You have finished <Transidium Annex>");
				final int Pasha = spawnRace == Race.ASMODIANS ? 804750 : 804749;
				spawn(Pasha, 499.92294f, 512.67365f, 675.0881f, (byte) 0);
            break;
		}
	}
	
	private void sendQuestionWindow() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSG_SVS_DIRECT_PORTAL_OPEN_NOTICE, 0, 0));
			}
		});
	}
	
	protected void openDoor(int doorId) {
        StaticDoor door = doors.get(doorId);
        if (door != null) {
            door.setOpen(true);
        }
    }
	
	protected void openFirstDoors() {
	    openDoor(176);
		openDoor(177);
		openDoor(178);
		openDoor(179);
    }
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 277225: //Belus Camp Defense Cannon.
			case 277226: //Aspida Camp Defense Cannon.
			case 277227: //Atanatos Camp Defense Cannon.
			case 277228: //Disilon Camp Defense Cannon.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21652, 60, player).useNoAnimationSkill(); //Armaments Thief.
			break;
			//**/////////**//
			//**/////////**//
			case 297331: //Belus Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21582, 60, player).useNoAnimationSkill(); //Board The Chariot.
			break;
			case 297332: //Aspida Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21589, 60, player).useNoAnimationSkill(); //Board The Chariot.
			break;
			case 297333: //Atanatos Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21590, 60, player).useNoAnimationSkill(); //Board The Chariot.
			break;
			case 297334: //Disilon Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21591, 60, player).useNoAnimationSkill(); //Board The Chariot.
			break;
			//**/////////**//
			//**/////////**//
			case 297472: //Belus Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21579, 60, player).useNoAnimationSkill(); //Board The Ignus Engine.
			break;
			case 297473: //Aspida Chariot.
                despawnNpc(npc);			
				SkillEngine.getInstance().getSkill(npc, 21586, 60, player).useNoAnimationSkill(); //Board The Ignus Engine.
			break;
			case 297474: //Atanatos Chariot.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21587, 60, player).useNoAnimationSkill(); //Board The Ignus Engine.
			break;
			case 297475: //Disilon Chariot.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21588, 60, player).useNoAnimationSkill(); //Board The Ignus Engine.
			break;
		}
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21728);
		effectController.removeEffect(21729);
		effectController.removeEffect(21730);
		effectController.removeEffect(21731);
		effectController.removeEffect(21579);
		effectController.removeEffect(21582);
		effectController.removeEffect(21586);
		effectController.removeEffect(21587);
		effectController.removeEffect(21588);
		effectController.removeEffect(21589);
		effectController.removeEffect(21590);
		effectController.removeEffect(21591);
	}
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
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