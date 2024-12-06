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

@InstanceID(300130000)
public class TwilightBattlefieldStoreroomInstance extends GeneralInstanceHandler
{
	private Future<?> twilightBattlefieldTask;
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
	private List<Npc> twilightBattlefieldChest = new ArrayList<Npc>();
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch(npc.getNpcId()) {
			case 215415: //Miren Chamber Artifact.
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
			case 215216: //Treasurer Karpasha.
			case 215217: //Treasurer Pasikar.
			case 215218: //Treasurer Subatita.
			case 215219: //Treasurer Mahirwa.
				switch (Rnd.get(1, 3)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000066, 1)); //Miren Armory Key.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000067, 1)); //Miren Supply Base Key.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000068, 1)); //Miren Operations Room Key.
					break;
				}
			break;
			case 215221: //Weakened Miren Prince.
			case 215222: //Awakened Miren Prince.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000070, 1)); //Miren Gold Room Key.
			break;
        }
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        doors = instance.getDoors();
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(215216, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Karpasha.
			break;
			case 2:
				spawn(215217, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Pasikar.
			break;
			case 3:
				spawn(215218, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Subatita.
			break;
			case 4:
				spawn(215219, 527.769f, 212.12146f, 178.46744f, (byte) 90); //Treasurer Mahirwa.
			break;
		} switch (Rnd.get(1, 2)) {
			case 1:
				sendMsg("<Weakened Miren Prince> appear!!!");
				spawn(215221, 526.6656f, 845.7792f, 199.44875f, (byte) 90); //Weakened Miren Prince.
			break;
			case 2:
				sendMsg("<Awakened Miren Prince> appear!!!");
				spawn(215222, 526.6656f, 845.7792f, 199.44875f, (byte) 90); //Awakened Miren Prince.
			break;
		}
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 215189: //Ranx Scout Captain.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(731580);
				    }
			    }, 5000);
			break;
			case 215200: //Ranx Deathbringer.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700545);
				    }
			    }, 5000);
			break;
			case 215214: //Ranx Quaestor.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700546);
				    }
			    }, 5000);
			break;
			case 215220: //Ebonlord Vershinak.
			    //A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(700547);
				    }
			    }, 5000);
			break;
			case 215221: //Weakened Miren Prince.
			case 215222: //Awakened Miren Prince.
				doors.get(2).setOpen(true);
				doors.get(3).setOpen(true);
				doors.get(5).setOpen(true);
				doors.get(6).setOpen(true);
				doors.get(17).setOpen(true);
				doors.get(18).setOpen(true);
				doors.get(28).setOpen(true);
				doors.get(74).setOpen(true);
				doors.get(76).setOpen(true);
				doors.get(79).setOpen(true);
				doors.get(80).setOpen(true);
				twilightBattlefieldTask.cancel(true);
				//sendMsg("[SUCCES]: You have finished <Miren Chamber>");
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
			twilightBattlefieldChest.add((Npc) spawn(254574, 478.56662f, 815.6565f, 199.76048f, (byte) 70));
			twilightBattlefieldChest.add((Npc) spawn(254574, 471.32745f, 834.5498f, 199.76048f, (byte) 63));
			twilightBattlefieldChest.add((Npc) spawn(254574, 470.52844f, 854.9471f, 199.76048f, (byte) 56));
			twilightBattlefieldChest.add((Npc) spawn(254574, 477.76843f, 873.94354f, 199.76036f, (byte) 50));
			twilightBattlefieldChest.add((Npc) spawn(254574, 490.90323f, 889.6053f, 199.76036f, (byte) 43));
			twilightBattlefieldChest.add((Npc) spawn(254574, 508.64328f, 899.91547f, 199.76036f, (byte) 36));
			twilightBattlefieldChest.add((Npc) spawn(254574, 528.42053f, 903.5909f, 199.76036f, (byte) 29));
			twilightBattlefieldChest.add((Npc) spawn(254574, 548.2363f, 900.31604f, 199.76036f, (byte) 23));
			twilightBattlefieldChest.add((Npc) spawn(254574, 565.53644f, 890.173f, 199.76036f, (byte) 16));
			twilightBattlefieldChest.add((Npc) spawn(254574, 578.9111f,  874.7958f, 199.76036f, (byte) 9));
			twilightBattlefieldChest.add((Npc) spawn(254574, 585.83545f, 855.7736f, 199.76036f, (byte) 3));
			twilightBattlefieldChest.add((Npc) spawn(254574, 586.7527f, 835.4556f, 199.76036f, (byte) 116));
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer2();
					sendMsg(1400245);
					twilightBattlefieldChest.get(0).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer3();
					sendMsg(1400245);
					twilightBattlefieldChest.get(1).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer4();
					sendMsg(1400245);
					twilightBattlefieldChest.get(2).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer5();
					sendMsg(1400245);
					twilightBattlefieldChest.get(3).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer6();
					sendMsg(1400245);
					twilightBattlefieldChest.get(4).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer7();
					sendMsg(1400245);
					twilightBattlefieldChest.get(5).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer8();
					sendMsg(1400245);
					twilightBattlefieldChest.get(6).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer9();
					sendMsg(1400245);
					twilightBattlefieldChest.get(7).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer10();
					sendMsg(1400245);
					twilightBattlefieldChest.get(8).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer11();
					sendMsg(1400245);
					twilightBattlefieldChest.get(9).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					StartTimer12();
					sendMsg(1400245);
					twilightBattlefieldChest.get(10).getController().onDelete();
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
			twilightBattlefieldTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					sendMsg(1400244);
					sendMsg(1400245);
					twilightBattlefieldChest.get(11).getController().onDelete();
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
		storage.decreaseByItemId(185000066, storage.getItemCountByItemId(185000066));
		storage.decreaseByItemId(185000067, storage.getItemCountByItemId(185000067));
		storage.decreaseByItemId(185000068, storage.getItemCountByItemId(185000068));
		storage.decreaseByItemId(185000069, storage.getItemCountByItemId(185000069));
		storage.decreaseByItemId(185000070, storage.getItemCountByItemId(185000070));
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
        if (zone.getAreaTemplate().getZoneName() == ZoneName.get("MIREN_ARTIFACT_CONTROL_ROOM_300130000")) {
            sendMsg("Use <Miren Chamber Artifact> to receive a skill");
	    }
    }
}