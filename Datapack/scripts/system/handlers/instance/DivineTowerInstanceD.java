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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
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
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;
import java.util.*;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: https://aionpowerbook.com/powerbook/Divine_Tower
/** Video: https://www.youtube.com/watch?v=aSpIBA0TWHg
/****/
@InstanceID(320160000)
public class DivineTowerInstanceD extends GeneralInstanceHandler
{
	private int IDAb1Heroes1STWaveDoor;
	private int IDAb1Heroes2NDWaveDoor;
	private int IDAb1Heroes3RDWaveDoor;
	private int IDAb1Heroes4THWaveDoor;
	private boolean isInstanceDestroyed;
	private final FastList<Future<?>> divineTowerTask = FastList.newInstance();
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
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
			case 248025: //IDAb1_Heroes_Boss_73_Ah.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058413, 1)); //�?�계 암룡�?� 무기 �?�?.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 152012758, 3)); //헤카�?��?�트.
					} switch (Rnd.get(1, 2)) {
						case 1:
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 168300002, 1)); //Conditioning: Level 1.
						break;
						case 2:
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 168300005, 1)); //Conditioning: Level 2.
						break;
					} switch (Rnd.get(1, 4)) {
						case 1:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058135, 1)); //신성�?� 탑 특급 정예 친위병�?� 무기 �?�?.
						break;
						case 2:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058136, 1)); //신성�?� 탑 특급 정예 친위병�?� 방어구 �?�?.
						break;
						case 3:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058137, 1)); //신성�?� 탑 특급 친위병�?� 무기 �?�?.
						break;
						case 4:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058138, 1)); //신성�?� 탑 특급 친위병�?� 방어구 �?�?.
						break;
					} switch (Rnd.get(1, 2)) {
						case 1:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058133, 1)); //신성�?� 탑 장비 진화 재료 꾸러미.
						break;
						case 2:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058134, 1)); //신성�?� 탑 스피넬주화 꾸러미.
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
			case 248024: //IDAb1_Heroes_Drakan_High_Wi_73_Ae.
				//Asault Pod 1
				sp(248440, 274.65070f, 219.50723f, 381.5356f, (byte) 57, 0, 1000, 0, null);
				sp(248015, 272.74588f, 219.99610f, 381.2752f, (byte) 55, 0, 1500, 0, null);
				sp(248017, 273.50082f, 222.32999f, 381.4562f, (byte) 55, 0, 2000, 0, null);
				sp(248018, 272.33270f, 217.57129f, 381.2576f, (byte) 55, 0, 2500, 0, null);
			break;
			case 248440: //IDAb1_Heroes_1st_Wave_Door.
			    IDAb1Heroes1STWaveDoor++;
				if (IDAb1Heroes1STWaveDoor == 1) {
					deleteNpc(248404); //IDAb1_Heroes_Raid_Wall_1
				    deleteNpc(248437); //IDAb1_Heroes_Witch_73_An.
					sp(248437, 291.55310f, 267.65878f, 388.91710f, (byte) 94, 0, 0, 0, null);
					//Asault Pod 1
					sp(248441, 288.32297f, 237.95130f, 384.26694f, (byte) 75, 0, 2000, 0, null);
					sp(248016, 286.88046f, 236.62110f, 383.94525f, (byte) 75, 0, 2500, 0, null);
					sp(248019, 285.53640f, 238.70550f, 384.02600f, (byte) 75, 0, 3000, 0, null);
					sp(248018, 288.37463f, 235.15918f, 383.98465f, (byte) 73, 0, 3500, 0, null);
					//Asault Pod 2
					sp(248441, 293.10750f, 253.48555f, 385.60123f, (byte) 85, 0, 4000, 0, null);
					sp(248015, 292.37190f, 251.41414f, 385.37726f, (byte) 82, 0, 4500, 0, null);
					sp(248019, 289.98602f, 252.98904f, 385.61615f, (byte) 85, 0, 5000, 0, null);
					sp(248017, 294.88315f, 251.00540f, 385.32916f, (byte) 85, 0, 5500, 0, null);
				}
			break;
			case 248441: //IDAb1_Heroes_2nd_Wave_Door.
			    IDAb1Heroes2NDWaveDoor++;
				if (IDAb1Heroes2NDWaveDoor == 2) {
					deleteNpc(248405); //IDAb1_Heroes_Raid_Wall_2
				    deleteNpc(248437); //IDAb1_Heroes_Witch_73_An.
					sp(248437, 249.54076f, 293.02783f, 397.47150f, (byte) 1, 0, 0, 0, null);
					//Asault Pod 1
					sp(248442, 282.97067f, 279.81448f, 391.71695f, (byte) 99, 0, 2000, 0, null);
					sp(248016, 283.84750f, 277.8729f, 391.231900f, (byte) 98, 0, 2500, 0, null);
					sp(248019, 281.27963f, 277.30408f, 391.49887f, (byte) 101, 0, 3000, 0, null);
					sp(248017, 286.04254f, 279.46674f, 391.39523f, (byte) 101, 0, 3500, 0, null);
					//Asault Pod 2
					sp(248442, 270.33910f, 294.79022f, 394.65875f, (byte) 109, 0, 4000, 0, null);
					sp(248018, 272.03342f, 293.49160f, 394.26907f, (byte) 108, 0, 4500, 0, null);
					sp(248015, 269.88412f, 291.74857f, 394.31802f, (byte) 112, 0, 5000, 0, null);
					sp(248016, 273.07538f, 295.98523f, 394.51290f, (byte) 110, 0, 5500, 0, null);
					//Asault Pod 3
					sp(248442, 259.76960f, 293.15176f, 396.11285f, (byte) 118, 0, 6000, 0, null);
					sp(248019, 261.67660f, 292.81170f, 395.80280f, (byte) 118, 0, 6500, 0, null);
					sp(248018, 260.57330f, 290.20682f, 395.81760f, (byte) 118, 0, 7000, 0, null);
					sp(248017, 261.79352f, 295.17120f, 395.97670f, (byte) 118, 0, 7500, 0, null);
				}
			break;
			case 248442: //IDAb1_Heroes_3rd_Wave_Door.
			    IDAb1Heroes3RDWaveDoor++;
				if (IDAb1Heroes3RDWaveDoor == 3) {
					deleteNpc(248406); //IDAb1_Heroes_Raid_Wall_3
				    deleteNpc(248437); //IDAb1_Heroes_Witch_73_An.
					sp(248437, 218.84665f, 253.76576f, 402.57077f, (byte) 29, 0, 0, 0, null);
					//Pod 1
					sp(248443, 229.72566f, 287.20285f, 399.83800f, (byte) 10, 0, 2000, 0, null);
					sp(248015, 231.62456f, 288.11545f, 399.73123f, (byte) 10, 0, 2500, 0, null);
					sp(248017, 230.01387f, 290.13498f, 399.96634f, (byte) 10, 0, 3000, 0, null);
					sp(248018, 232.32887f, 285.37875f, 399.51970f, (byte) 10, 0, 3500, 0, null);
					//Pod 2
					sp(248443, 228.43282f, 271.93466f, 400.20178f, (byte) 17, 0, 4000, 0, null);
					sp(248019, 229.90218f, 273.55557f, 400.07750f, (byte) 16, 0, 4500, 0, null);
					sp(248017, 227.64496f, 274.86100f, 400.09784f, (byte) 17, 0, 5000, 0, null);
					sp(248016, 231.28198f, 272.02988f, 400.11700f, (byte) 17, 0, 5500, 0, null);
					//Pod 3
					sp(248443, 217.99918f, 273.38297f, 400.94200f, (byte) 20, 0, 6000, 0, null);
					sp(248018, 219.07759f, 275.07516f, 400.82830f, (byte) 19, 0, 6500, 0, null);
					sp(248015, 217.01062f, 276.02527f, 401.05838f, (byte) 19, 0, 7000, 0, null);
					sp(248019, 221.01494f, 273.08100f, 400.63760f, (byte) 19, 0, 7500, 0, null);
					//Pod 4
					sp(248443, 219.45404f, 264.54462f, 401.15222f, (byte) 27, 0, 8000, 0, null);
					sp(248015, 219.84862f, 266.51490f, 401.01150f, (byte) 26, 0, 8500, 0, null);
					sp(248016, 222.52934f, 265.02078f, 400.86620f, (byte) 26, 0, 9000, 0, null);
					sp(248017, 216.92758f, 265.95483f, 401.34375f, (byte) 31, 0, 9500, 0, null);
				}
			break;
			case 248443: //IDAb1_Heroes_4th_Wave_Door.
			    IDAb1Heroes4THWaveDoor++;
				if (IDAb1Heroes4THWaveDoor == 4) {
					deleteNpc(248407); //IDAb1_Heroes_Raid_Wall_4
					deleteNpc(248437); //IDAb1_Heroes_Witch_73_An.
				}
			break;
			case 248025: //IDAb1_Heroes_Boss_73_Ah.
				sendMsg("[SUCCES]: You have finished <Divine Tower>");
				sp(806732, 239.40633f, 249.12549f, 404.25793f, (byte) 9, 0, 0, 0, null); //Tamke.
			break;
		}
    }
	
	@Override
    public void onInstanceDestroy() {
		isInstanceDestroyed = true;
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
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = divineTowerTask.head(), end = divineTowerTask.tail(); (n = n.getNext()) != end;) {
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
        divineTowerTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        divineTowerTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
}