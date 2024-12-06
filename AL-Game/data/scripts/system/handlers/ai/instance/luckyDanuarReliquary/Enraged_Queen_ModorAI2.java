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
package ai.instance.luckyDanuarReliquary;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("enraged_queen_modor")
public class Enraged_Queen_ModorAI2 extends AggressiveNpcAI2
{
	private Future<?> skillTask;
	private boolean canThink = true;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		if (isHome.compareAndSet(true, false)) {
			sendMsg(1500747);
			startSkillTask();
		}
	}
	
    private void addPercent() {
        percents.clear();
        Collections.addAll(percents, new Integer[]{90, 85, 75, 70, 65, 60, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5});
    }
	
    private void checkPercentage(int hpPercentage) {
        for (Integer percent : percents) {
            if (hpPercentage <= percent) {
                switch (percent) {
					case 90:
                        Teleport();
                    break;
					case 85:
                        VengefullOrbEvent();
                    break;
                    case 75:
                        Teleport2();
                    break;
					case 70:
                        VengefullOrbEvent();
                    break;
                    case 65:
                        Teleport3();
                    break;
					case 60:
                        VengefullOrbEvent();
                    break;
                    case 50:
                        skillfear();
                    break;
					case 45:
                        VengefullOrbEvent();
                    break;
                    case 40:
                        Teleport4();
                    break;
					case 35:
                        VengefullOrbEvent();
                    break;
					case 30:
                        Teleport5();
                    break;
					case 25:
                        VengefullOrbEvent();
                    break;
                    case 20:
                        Teleport6();
                    break;
					case 15:
                        VengefullOrbEvent();
                    break;
                    case 10:
                        Teleport7();
                    break;
                    case 5:
                        Teleport8();
                    break;
                }
                percents.remove(percent);
                break;
            }
        }
    }
	
	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
				    cancelTask1();
				} else {
					chooseRandomEvent();
				}
			}
		}, 4000, 30000);
	}
	
	private void cancelTask1() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
	}
	
    private void chooseRandomEvent() {
        switch (Rnd.get(1, 2)) {
            case 1:
                AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
                SkillEngine.getInstance().getSkill(getOwner(), 21171, 60, getOwner()).useNoAnimationSkill();
            break;
            case 2:
                sendMsg(1500745);
                AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
                SkillEngine.getInstance().getSkill(getOwner(), 21176, 60, getOwner()).useNoAnimationSkill();
            break;
        }
    }
	
	private void VengefullOrbEvent() {
		AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21177, 1, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				spawnSorcererQueenModor();
			}
		}, 11000);
	}
	
	private void spawnSorcererQueenModor() {
		if (!isAlreadyDead()) {
			spawn(284443, 256.45197f, 257.91986f, 241.78688f, (byte) 90);
		}
	}
	
	private void Teleport() {
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1500750);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
                modorNpc();
				World.getInstance().updatePosition(getOwner(), 284, 262, 249, (byte) 63);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
		    }
		}, 2000);
	}
	
    private void Teleport2() {
        AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
        SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
        sendMsg(1500741);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run(){
                float pos1[][] = {
                    {
                        232.426f, 263.818f, 248.6419f, 115
                    }, {
                        271.426f, 230.243f, 250.9022f, 38
                    }, {
                        240.130f, 235.219f, 251.1553f, 17
                    }
                };
                float pos[] = pos1[Rnd.get(0, 2)];
                World.getInstance().updatePosition(getOwner(), pos[0], pos[1], pos[2], (byte) pos[3]);
                PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
            }
        }, 2000);
    }
	
	private void Teleport3() {
		AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				modorNpc();
				World.getInstance().updatePosition(getOwner(), 256, 258, 242, (byte) 10);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
			}
		}, 2000);
	}
	
	private void skillfear() {
		AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
		SkillEngine.getInstance().getSkill(getOwner(), 21268, 60, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
				SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					public void run() {
				        World.getInstance().updatePosition(getOwner(), 232, 263, 249, (byte) 115);
				        PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
					}
				}, 2000);
			}
		}, 3000);
	}
	
	private void Teleport4() {
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				modorNpc();
				World.getInstance().updatePosition(getOwner(), 256, 258, 242, (byte) 10);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
			}
		}, 2000);
	}
	
    private void Teleport5() {
        AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
        SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
        sendMsg(1500750);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                float pos1[][] = {
                    {
                        232.426f, 263.818f, 248.6419f, 115
                    }, {
                        271.426f, 230.243f, 250.9022f, 38
                    }, {
                        240.130f, 235.219f, 251.1553f, 17
                    }
                };
                float pos[] = pos1[Rnd.get(0, 2)];
                World.getInstance().updatePosition(getOwner(), pos[0], pos[1], pos[2], (byte) pos[3]);
                PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
            }
        }, 2000);
    }
	
	private void Teleport6() {
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				modorNpc2();
				World.getInstance().updatePosition(getOwner(), 256, 258, 242, (byte) 10);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
			}
		}, 2000);
	}
	
    private void Teleport7() {
        AI2Actions.targetSelf(Enraged_Queen_ModorAI2.this);
        SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
        sendMsg(1500750);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                float pos1[][] = {
                    {
                        232.426f, 263.818f, 248.6419f, 115
                    }, {
                        271.426f, 230.243f, 250.9022f, 38
                    }, {
                        240.130f, 235.219f, 251.1553f, 17
                    }
                };
                float pos[] = pos1[Rnd.get(0, 2)];
                World.getInstance().updatePosition(getOwner(), pos[0], pos[1], pos[2], (byte) pos[3]);
                PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
            }
        }, 2000);
    }
	
	private void Teleport8() {
		SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
		sendMsg(1500741);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				modorNpc2();
				World.getInstance().updatePosition(getOwner(), 256, 258, 242, (byte) 10);
				PacketSendUtility.broadcastPacketAndReceive(getOwner(), new SM_FORCED_MOVE(getOwner(), getOwner()));
			}
		}, 2000);
	}
	
	private void modorNpc() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
                spawn(284659, 271.12497f, 247.17401f, 242.625f, (byte) 90);
                spawn(284660, 244.12497f, 245.17401f, 242.625f, (byte) 90);
                spawn(284664, 243.12497f, 270.17401f, 242.625f, (byte) 90);
                spawn(284660, 268.12497f, 271.17401f, 242.625f, (byte) 90);
			}
		}, 4000);
	}
	
	private void modorNpc2() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
                spawn(284663, 271.12497f, 247.17401f, 242.625f, (byte) 90);
                spawn(284662, 244.12497f, 245.17401f, 242.625f, (byte) 90);
                spawn(284664, 243.12497f, 270.17401f, 242.625f, (byte) 90);
                spawn(284663, 268.12497f, 271.17401f, 242.625f, (byte) 90);
			}
		}, 4000);
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(284659));
			deleteNpcs(p.getWorldMapInstance().getNpcs(284660));
			deleteNpcs(p.getWorldMapInstance().getNpcs(284662));
			deleteNpcs(p.getWorldMapInstance().getNpcs(284663));
			deleteNpcs(p.getWorldMapInstance().getNpcs(284664));
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
	}
	
    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        addPercent();
		buff();
    }
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
		cancelTask1();
	}
	
	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
		isHome.set(true);
		cancelTask1();
	}
	
	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}
	
	private void buff() {
		SkillEngine.getInstance().getSkill(getOwner(), 21169, 1, getOwner()).useNoAnimationSkill();
	}
	
	private void despawnNpcs(int npcId) {
		List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}