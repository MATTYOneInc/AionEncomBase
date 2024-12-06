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

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: http://gameguide.na.aiononline.com/aion/Abyssal+Splinter+Walkthrough
/****/

@InstanceID(300600000)
public class UnstableAbyssalSplinterInstance extends GeneralInstanceHandler
{
	private int hugeAetherFragment;
	private boolean isInstanceDestroyed;
	private int unstableLuminousWaterworm;
	private Map<Integer, StaticDoor> doors;
	private final FastList<Future<?>> abyssalSplinterTask = FastList.newInstance();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		abyssalBlessing();
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 219548: //Unstable Enos Watcher.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000104, 1)); //Abyssal Fragment.
			break;
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 219548: //Unstable Enos Watcher.
				doors.get(15).setOpen(true);
				doors.get(16).setOpen(true);
				doors.get(18).setOpen(true);
				doors.get(69).setOpen(true);
			break;
			case 219551: //Unstable Rukril.
			case 219552: //Unstable Ebonsoul.
			    Npc unstableRukril = instance.getNpc(219551); //Unstable Rukril.
			    Npc unstableEbonsoul = instance.getNpc(216949); //Unstable Ebonsoul.
			    if (isDead(unstableRukril) && isDead(unstableEbonsoul)) {
					//A treasure chest has appeared.
					sendMsgByRace(1400636, Race.PC_ALL, 3000);
					spawn(701574, 401.22794f, 655.66284f, 439.81107f, (byte) 7); //Abyssal Treasure Box.
					sp(700955, npc.getX(), npc.getY(), npc.getZ(), (byte) 0, 3000, 0, null); //Huge Aether Fragment.
				}
				despawnNpc(npc);
			break;
			case 219553: //Unstable Kaluva The Fourth.
			    despawnNpc(npc);
				//A treasure chest has appeared.
				sendMsgByRace(1400636, Race.PC_ALL, 3000);
				spawn(701575, 592.54000f, 585.94570f, 422.85745f, (byte) 106); //Abyssal Treasure Box.
				sp(700955, npc.getX(), npc.getY(), npc.getZ(), (byte) 0, 3000, 0, null); //Huge Aether Fragment.
			break;
			case 219554: //Unstable Pazuzu.
			    despawnNpc(npc);
				//A treasure chest has appeared.
				sendMsgByRace(1400636, Race.PC_ALL, 3000);
				spawn(701576, 649.09143f, 359.91174f, 466.13498f, (byte) 103); //Abyssal Treasure Box.
				spawn(700861, 661.16443f, 357.63345f, 465.99103f, (byte) 0, 67); //Unstable Pazuzu's Treasure Box.
				sp(700955, npc.getX(), npc.getY(), npc.getZ(), (byte) 0, 3000, 0, null); //Huge Aether Fragment.
			break;
			case 219555: //Durable Yamennes Blindsight.
			    despawnNpc(npc);
				//sendMsg("[SUCCES]: You have finished <Unstable Abyssal Splinter>");
				spawn(701577, 328.41187f, 759.68880f, 197.14168f, (byte) 93); //Abyssal Treasure Box.
				spawn(730317, 308.19241f, 756.48370f, 196.75534f, (byte) 0, 123); //Abyssal Splinter Exit.
			break;
			case 219563: //Unstable Yamennes Painflare.
			    despawnNpc(npc);
				//sendMsg("[SUCCES]: You have finished <Unstable Abyssal Splinter>");
				spawn(701578, 328.41187f, 759.68880f, 197.14168f, (byte) 93); //Abyssal Treasure Box.
				spawn(730317, 308.19241f, 756.48370f, 196.75534f, (byte) 0, 123); //Abyssal Splinter Exit.
			break;
			case 700955: //Huge Aether Fragment.
				hugeAetherFragment++;
				if (hugeAetherFragment == 1) {
					//The destruction of the Huge Aether Fragment has destabilized the artifact!
				    sendMsgByRace(1400689, Race.PC_ALL, 0);
				} else if (hugeAetherFragment == 2) {
					//The destruction of the Huge Aether Fragment has put the artifact protector on alert!
				    sendMsgByRace(1400690, Race.PC_ALL, 0);
				} else if (hugeAetherFragment == 3) {
					//The destruction of the Huge Aether Fragment has caused abnormality on the artifact. The artifact protector is furious!
				    sendMsgByRace(1400691, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
			break;
			case 219570: //Unstable Luminous Waterworm.
                Npc unstablePazuzu = instance.getNpc(219554); //Unstable Pazuzu.
				unstableLuminousWaterworm++;
				if (unstablePazuzu != null) {
					if (unstableLuminousWaterworm == 5) {
                        unstablePazuzu.getEffectController().removeEffect(19291); //Replenishment.
                    }
                }
				despawnNpc(npc);
            break;
		}
	}
	
	private void abyssalBlessing() {
		for (Player p: instance.getPlayersInside()) {
			SkillTemplate st =  DataManager.SKILL_DATA.getSkillTemplate(19283); //Abyssal Blessing.
			Effect e = new Effect(p, p, st, 1, st.getEffectsDuration(9));
			e.initialize();
			e.applyEffect();
		}
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000104, storage.getItemCountByItemId(185000104)); //Abyssal Fragment.
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(19283); //Abyssal Blessing.
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = abyssalSplinterTask.head(), end = abyssalSplinterTask.tail(); (n = n.getNext()) != end; ) {
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
        abyssalSplinterTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        abyssalSplinterTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
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
}