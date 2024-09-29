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
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(300120000)
public class GraveOfSteelStoreroomInstance extends GeneralInstanceHandler
{
	private Future<?> graveOfSteelStoreroomTask;
	private boolean isStartTimer1 = false;
	private boolean isStartTimer2 = false;
	private boolean isStartTimer3 = false;
	private boolean isStartTimer4 = false;
	private boolean isStartTimer5 = false;
	private boolean isStartTimer6 = false;
	private boolean isStartTimer7 = false;
	private boolean isStartTimer8 = false;
	private boolean isStartTimer9 = false;
	private boolean isStartTimer10 = false;
	private boolean isStartTimer11 = false;
	private boolean isStartTimer12 = false;
	private Map<Integer, StaticDoor> doors;
	private List<Npc> graveOfSteelStoreroomChest = new ArrayList<Npc>();
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch(npc.getNpcId()) {
			case 215414: //Kysis Chamber Artifact.
				sendMsg("You win effect <Shield Of Compassion>");
				SkillEngine.getInstance().getSkill(npc, 276, 10, player).useNoAnimationSkill();
			break;
		}
	}
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 215173: //Treasurer Darmaraja.
			case 215174: //Treasurer Swaraja.
			case 215175: //Treasurer Chandra.
			case 215176: //Treasurer Dragagh.
				switch (Rnd.get(1, 3)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000061, 1)); //Kysis Armory Key.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000062, 1)); //Kysis Supply Base Key.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000063, 1)); //Kysis Operations Room Key.
					break;
				}
			break;
			case 215178: //Weakened Kysis Duke.
			case 215179: //Awakened Kysis Duke.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000065, 1)); //Kysis Gold Room Key.
			break;
        }
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        doors = instance.getDoors();
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(215173, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Darmaraja.
			break;
			case 2:
				spawn(215174, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Swaraja.
			break;
			case 3:
				spawn(215175, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Chandra.
			break;
			case 4:
				spawn(215176, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Dragagh.
			break;
		} switch (Rnd.get(1, 2)) {
			case 1:
				sendMsg("<Weakened Kysis Duke> appear!!!");
				spawn(215178, 526.6656f, 845.7792f, 199.44875f, (byte) 90); //Weakened Kysis Duke.
			break;
			case 2:
				sendMsg("<Awakened Kysis Duke> appear!!!");
				spawn(215179, 526.6656f, 845.7792f, 199.44875f, (byte) 90); //Awakened Kysis Duke.
			break;
		}
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 215147: //Ranx Deathbow.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(731580);
				    }
			    }, 5000);
			break;
			case 215159: //Ranx High Mage.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700545);
				    }
			    }, 5000);
			break;
			case 215172: //Ranx Medico.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700546);
				    }
			    }, 5000);
			break;
			case 215177: //Ebonlord Nukuam.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700547);
				    }
			    }, 5000);
			break;
			case 215178: //Weakened Kysis Duke.
			case 215179: //Awakened Kysis Duke.
				doors.get(11).setOpen(true);
				doors.get(15).setOpen(true);
				doors.get(17).setOpen(true);
				doors.get(18).setOpen(true);
				doors.get(19).setOpen(true);
				doors.get(20).setOpen(true);
				doors.get(28).setOpen(true);
				doors.get(74).setOpen(true);
				doors.get(76).setOpen(true);
				doors.get(79).setOpen(true);
				doors.get(80).setOpen(true);
				graveOfSteelStoreroomTask.cancel(true);
				//sendMsg("[SUCCES]: You have finished <Kysis Chamber>");
				instance.doOnAllPlayers(new Visitor<Player>() {
			        @Override
			        public void visit(Player player) {
				        if (player.isOnline()) {
						    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
					    }
				    }
			    });
			break;
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (!isStartTimer1) {
			isStartTimer1 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 478.56662f, 815.6565f, 199.76048f, (byte) 70));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 471.32745f, 834.5498f, 199.76048f, (byte) 63));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 470.52844f, 854.9471f, 199.76048f, (byte) 56));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 477.76843f, 873.94354f, 199.76036f, (byte) 50));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 490.90323f, 889.6053f, 199.76036f, (byte) 43));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 508.64328f, 899.91547f, 199.76036f, (byte) 36));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 528.42053f, 903.5909f, 199.76036f, (byte) 29));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 548.2363f, 900.31604f, 199.76036f, (byte) 23));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 565.53644f, 890.173f, 199.76036f, (byte) 16));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 578.9111f,  874.7958f, 199.76036f, (byte) 9));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 585.83545f, 855.7736f, 199.76036f, (byte) 3));
			graveOfSteelStoreroomChest.add((Npc) spawn(254574, 586.7527f, 835.4556f, 199.76036f, (byte) 116));
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer2();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(0).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer2() {
        if (!isStartTimer2) {
			isStartTimer2 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer3();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(1).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer3() {
	    if (!isStartTimer3) {
			isStartTimer3 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer4();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(2).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer4() {
	    if (!isStartTimer4) {
			isStartTimer4 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer5();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(3).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer5() {
	    if (!isStartTimer5) {
			isStartTimer5 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer6();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(4).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer6() {
	    if (!isStartTimer6) {
			isStartTimer6 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer7();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(5).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer7() {
	    if (!isStartTimer7) {
			isStartTimer7 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer8();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(6).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer8() {
	    if (!isStartTimer8) {
			isStartTimer8 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer9();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(7).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer9() {
	    if (!isStartTimer9) {
			isStartTimer9 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer10();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(8).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer10() {
	    if (!isStartTimer10) {
			isStartTimer10 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer11();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(9).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer11() {
	    if (!isStartTimer11) {
			isStartTimer11 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer12();
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(10).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer12() {
	    if (!isStartTimer12) {
			isStartTimer12 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>() {
			    @Override
			    public void visit(Player player) {
				    if (player.isOnline()) {
					    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			graveOfSteelStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					sendMsg(1400244);
					sendMsg(1400245);
					graveOfSteelStoreroomChest.get(11).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000061, storage.getItemCountByItemId(185000061));
		storage.decreaseByItemId(185000062, storage.getItemCountByItemId(185000062));
		storage.decreaseByItemId(185000063, storage.getItemCountByItemId(185000063));
		storage.decreaseByItemId(185000064, storage.getItemCountByItemId(185000064));
		storage.decreaseByItemId(185000065, storage.getItemCountByItemId(185000065));
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
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
        if (zone.getAreaTemplate().getZoneName() == ZoneName.get("KYSIS_ARTIFACT_CONTROL_ROOM_300120000")) {
            sendMsg("Use <Kysis Chamber Artifact> to receive a skill");
	    }
    }
}