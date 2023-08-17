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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.*;

/****/
/** Author (Encom)
/** Source: https://aionpowerbook.com/powerbook/Mirash_Sanctuary
/** Video: https://www.youtube.com/watch?v=_5o4HbRkJFY
/****/
@InstanceID(301720000)
public class MirashSanctuaryInstance extends GeneralInstanceHandler
{
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		doors = instance.getDoors();
		switch (Rnd.get(1, 2)) {
			case 1:
				spawn(248533, 664.0945f, 623.5564f, 532.5159f, (byte) 2); //IDAbRe_Core_03_Key_Drakan_High_As_An.
			break;
			case 2:
				spawn(248533, 684.9298f, 630.2946f, 530.1250f, (byte) 74); //IDAbRe_Core_03_Key_Drakan_High_As_An.
			break;
		}
    }
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 835730: //IDAbRe_Core_03_TreasureBox01.
			case 835732: //IDAbRe_Core_03_TreasureBox03.
			case 835733: //IDAbRe_Core_03_TreasureBox04.
				switch (Rnd.get(1, 4)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058115, 1)); //미�?�쉬 성소 중급 유물 꾸러미.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058116, 1)); //미�?�쉬 성소 �?급 유물 꾸러미.
				    break;
					case 3:
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058117, 1)); //미�?�쉬 성소 스피넬 공훈 훈장 �?�?.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058118, 1)); //미�?�쉬 성소 스티그마 꾸러미.
					break;
				}
			break;
			case 835784: //IDAb1_Core_03_Stone_01.
			case 835785: //IDAb1_Core_03_Stone_02.
			case 835786: //IDAb1_Core_03_Stone_03.
			case 835787: //IDAb1_Core_03_Stone_04.
			case 835788: //IDAb1_Core_03_Stone_05.
			case 835789: //IDAb1_Core_03_Stone_06.
				switch (Rnd.get(1, 5)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000312, 1)); //item_core_03_stone_01.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000313, 1)); //item_core_03_stone_02.
				    break;
					case 3:
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000314, 1)); //item_core_03_stone_03.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000315, 1)); //item_core_03_stone_04.
					break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000316, 1)); //item_core_03_stone_05.
					break;
				}
			break;
			/*
			case 248382: //IDAbRe_Core_03_A1_Witch_An.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000531, 1)); //IDAbRe_Core_03_Move_Item_Skill.
			break;
			*/
			case 248533: //IDAbRe_Core_03_Key_Drakan_High_As_An.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000317, 1)); //item_core_03_key_01.
			break;
			case 248013: //노래하는 키르쉬카.
				switch (Rnd.get(1, 6)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058117, 1)); //미�?�쉬 성소 스피넬 공훈 훈장 �?�?.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058118, 1)); //미�?�쉬 성소 스티그마 꾸러미.
				    break;
					case 3:
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190200000, 50)); //미니움.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058130, 1)); //미�?�쉬 성소 하�?� 친위대 무기 �?�?.
					break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058131, 1)); //미�?�쉬 성소 하�?� 친위대 방어구 �?�?.
					break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058132, 1)); //미�?�쉬 성소 하�?� 친위대 장신구 �?�?.
					break;
				} switch (Rnd.get(1, 4)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080005, 5)); //Lesser Minion Contract.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080006, 5)); //Greater Minion Contract.
					break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080007, 5)); //Major Minion Contract.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080008, 5)); //Cute Minion Contract.
					break;
				}
			break;
		}
	}
	
	@Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 248382: //IDAbRe_Core_03_A1_Witch_An.
			    player.getSkillList().addSkill(player, 11333, 1);
			break;
			case 248013: //노래하는 키르쉬카.
			    SkillLearnService.removeSkill(player, 11333);
			    spawn(835733, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); //IDAbRe_Core_03_TreasureBox04.
			break;
			case 248389:
			    despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						mirashSanctuaryWave();
					}
				}, 5000);
			break;
			case 248444: //IDAbRe_Core_03_Resurrect_Drakan_Statue_01.
				spawn(248449, player.getX(), player.getY(), player.getZ(), (byte) 0);
			break;
			case 248445: //IDAbRe_Core_03_Resurrect_Drakan_Statue_02.
				spawn(248450, player.getX(), player.getY(), player.getZ(), (byte) 0);
			break;
			case 248446: //IDAbRe_Core_03_Resurrect_Drakan_Statue_03.
				spawn(248451, player.getX(), player.getY(), player.getZ(), (byte) 0);
			break;
			case 248447: //IDAbRe_Core_03_Resurrect_Drakan_Statue_04.
				spawn(248452, player.getX(), player.getY(), player.getZ(), (byte) 0);
			break;
		}
    }
	
	private void raidMirashSanctuary(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void mirashSanctuaryWave() {
		raidMirashSanctuary((Npc)spawn(248385, 283.95505f, 482.93155f, 548.0041f, (byte) 30), 284.07160f, 559.67145f, 547.99650f, false);
		raidMirashSanctuary((Npc)spawn(248383, 282.09033f, 485.19363f, 547.9946f, (byte) 30), 281.76416f, 559.63020f, 547.99585f, false);
		raidMirashSanctuary((Npc)spawn(248387, 279.90730f, 483.05050f, 548.0014f, (byte) 29), 279.76407f, 559.63390f, 547.99390f, false);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	@Override
    public void onInstanceDestroy() {
		isInstanceDestroyed = true;
        doors.clear();
    }
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000531, storage.getItemCountByItemId(164000531));
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