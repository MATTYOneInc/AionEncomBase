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
package instance.danuarReliquary;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(301360000)
public class Infernal_DanuarReliquaryInstance extends GeneralInstanceHandler
{
	private int ideanKilled;
	private int cloneModorKilled;
	private Future<?> infernalReliquaryTask;
	protected boolean isInstanceDestroyed = false;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
            case 701795: //[Infernal] Danuar Reliquary Box.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052388, 1)); //Modor's Equipment Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053099, 1)); //Pure Modor's Equipment Crux Box.
                    }
                }
            break;
        }
    }
	
   /**
	* Modor activated the Danuar Bomb of grudge.
	*/
	private void startInfernalReliquaryTimer() {
		//Modor activated the Danuar Bomb of grudge. You have 15 minutes to defeat her.
		sendMsgByRace(1401676, Race.PC_ALL, 5000);
		this.sendMessage(1401677, 10 * 60 * 1000); //10 minutes elapsed.
		this.sendMessage(1401678, 15 * 60 * 1000); //The bomb has detonated.
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
				    infernalReliquaryTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									onExitInstance(player);
								}
							});
							onInstanceDestroy();
						}
					}, 900000); //15 Minutes.
				}
			}
		});
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 284380:
			case 284381:
			case 284382:
			case 284659:
			case 284660:
			case 284662:
			case 284663:
			case 284664:
			    despawnNpc(npc);
			break;
			case 284377: //Danuar Reliquary Novun.
			case 284378: //Idean Lapilima.
			case 284379: //Idean Obscura.
				ideanKilled ++;
				if (ideanKilled == 1) {
				} else if (ideanKilled == 2) {
				} else if (ideanKilled == 3) {
				    spawn(234690, 256.45197f, 257.91986f, 241.78688f, (byte) 90); //Vengeful Modor.
					instance.doOnAllPlayers(new Visitor<Player>() {
					    @Override
					    public void visit(Player player) {
						    if (player.isOnline()) {
							    startInfernalReliquaryTimer();
							    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900)); //15 Minutes.
						    }
					    }
				    });
				}
				despawnNpc(npc);
			break;
			case 855244: //Clone's Modor.
				cloneModorKilled ++;
				if (cloneModorKilled == 1) {
				} else if (cloneModorKilled == 2) {
				} else if (cloneModorKilled == 3) {
				} else if (cloneModorKilled == 4) {
				} else if (cloneModorKilled == 5) {
				    spawn(234691, 256.45197f, 257.91986f, 241.78688f, (byte) 90); //Crazed Modor.
				}
				despawnNpc(npc);
			break;
			case 234691: //Crazed Modor.
				infernalReliquaryTask.cancel(true);
				//sendMsg("[SUCCES]: You have finished <[Infernal] Danuar Reliquary>");
				spawn(730843, 256.45197f, 257.91986f, 241.78688f, (byte) 90); //[Infernal] Danuar Reliquary Exit.
				spawn(701795, 256.39725f, 255.52034f, 241.78006f, (byte) 90); //[Infernal] Danuar Reliquary Box.
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
	
	private void sendMessage(final int msgId, long delay) {
        if (delay == 0) {
            this.sendMsg(msgId);
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                public void run() {
                    sendMsg(msgId);
                }
            }, delay);
        }
    }
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}