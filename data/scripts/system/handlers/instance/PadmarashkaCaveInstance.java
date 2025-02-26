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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(320150000)
public class PadmarashkaCaveInstance extends GeneralInstanceHandler
{
	private int dramataEgg55;
	private int dramataFi55Ae;
	private Future<?> dramataTask;
	private List<Integer> movies = new ArrayList<Integer>();
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		//You must defeat the protector within the time limit to wake Padmarashka from the Protective Slumber.
		sendMsgByRace(1400711, Race.PC_ALL, 10000);
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					startPadmarashkaTimer();
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 7200)); //2Hrs.
				}
			}
		});
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		Npc npc = instance.getNpc(218756); //Padmarashka.
		if (npc != null) {
			npc.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
			SkillEngine.getInstance().getSkill(npc, 19186, 60, npc).useNoAnimationSkill(); //Protective Slumber.
		}
	}
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 218756: //Padmarashka.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057935, 1)); //Padmarashka's Raging Weapon Box.
					    break;
						case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057935, 1)); //Padmarashka's Weapon Chest.
					    break;
					} switch (Rnd.get(1, 14)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100001640, 1)); //Padmarashka's Raging Sword Skin.
					    break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100101258, 1)); //Padmarashka's Raging Warhammer Skin.
					    break;
						case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100201433, 1)); //Padmarashka's Raging Dagger Skin.
					    break;
						case 4:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100501248, 1)); //Padmarashka's Raging Jewel Skin.
					    break;
						case 5:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100601352, 1)); //Padmarashka's Raging Spellbook Skin.
					    break;
						case 6:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 100901276, 1)); //Padmarashka's Raging Greatsword Skin.
					    break;
						case 7:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 101301191, 1)); //Padmarashka's Raging Polearm Skin.
					    break;
						case 8:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 101501280, 1)); //Padmarashka's Raging Staff Skin.
					    break;
						case 9:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 101701299, 1)); //Padmarashka's Raging Longbow Skin.
					    break;
						case 10:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 101801148, 1)); //Padmarashka's Raging Pistol Skin.
					    break;
						case 11:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 101901059, 1)); //Padmarashka's Raging Aethercannon Skin.
					    break;
						case 12:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 102001175, 1)); //Padmarashka's Raging Harp Skin.
					    break;
						case 13:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001680, 1)); //Padmarashka's Raging Shield Skin.
					    break;
						case 14:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001794, 1)); //Padmarashka's Raging Shield Skin.
					    break;
					}
				}
			break;
        }
    }
	
    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 218756: //Padmarashka.
			    dramataTask.cancel(true);
				//Padmarashka has died. You will be removed from Padmarashka's Cave in 30 minutes.
				sendMsgByRace(1400675, Race.PC_ALL, 10000);
				//sendMsg("[SUCCES]: You have finished <Padmarashka Cave>");
				instance.doOnAllPlayers(new Visitor<Player>() {
			        @Override
			        public void visit(Player player) {
				        if (player.isOnline()) {
						    PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
					    }
				    }
			    });
			break;
			case 282613: //Padmarashka's Eggs.
			case 282614: //Huge Padmarashka's Eggs.
			    dramataEgg55++;
				if (dramataEgg55 == 2) {
					//Padmarashka is about to lay eggs.
					sendMsgByRace(1400526, Race.PC_ALL, 0);
				} else if (dramataEgg55 == 5) {
					//Padmarashka is furious after seeing so many of her eggs destroyed.
					sendMsgByRace(1401213, Race.PC_ALL, 0);
				}
			break;
			case 218670: //Padmarashka's Elite Commander.
			case 218671: //Padmarashka Sartip.
			case 218673: //Padmarashka's Elite Captain.
			case 218674: //Padmarashka's Chief Medic.
				Npc dramata55Al = instance.getNpc(218756); //Padmarashka.
				dramataFi55Ae++;
				if (dramata55Al != null) {
					if (dramataFi55Ae == 1) {
					} else if (dramataFi55Ae == 2) {
					} else if (dramataFi55Ae == 3) {
					} else if (dramataFi55Ae == 4) {
						deleteNpc(282123); //Dramata Shield.
						//Padmarashka has awoken from the Protective Slumber.
						sendMsgByRace(1400728, Race.PC_ALL, 10000);
						dramata55Al.getEffectController().removeEffect(19186); //Protective Slumber.
					}
				}
			break;
		}
    }
	
	private void startPadmarashkaTimer() {
        //Padmarashka has cast defensive magic. You will be removed from Padmarashka's Cave in 2 hours.
		sendMsg(1400506);
		//You will be removed from Padmarashka's Cave in 1 hour and 30 minutes.
        this.sendMessage(1400507, 30 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 1 hour.
		this.sendMessage(1400508, 60 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 30 minutes.
		this.sendMessage(1400509, 90 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 15 minutes.
		this.sendMessage(1400510, 105 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 10 minutes.
		this.sendMessage(1400511, 110 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 5 minutes.
		this.sendMessage(1400512, 115 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 3 minutes.
		this.sendMessage(1400513, 117 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 2 minutes.
		this.sendMessage(1400514, 118 * 60 * 1000);
		//You will be removed from Padmarashka's Cave in 1 minute.
		this.sendMessage(1400515, 119 * 60 * 1000);
        dramataTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//You have been forcibly removed from Padmarashka's Cave by Padmarashka's defensive magic.
				sendMsgByRace(1400524, Race.PC_ALL, 0);
				deleteNpc(218756); //Padmarashka.
            }
        }, 7200000);
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
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
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
            movies.add(movie);
            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
        if (zone.getAreaTemplate().getZoneName() == ZoneName.get("PADMARASHKAS_NEST_320150000")) {
			sendMovie(player, 488);
	    }
    }
	
	@Override
    public void onInstanceDestroy() {
		movies.clear();
    }
}