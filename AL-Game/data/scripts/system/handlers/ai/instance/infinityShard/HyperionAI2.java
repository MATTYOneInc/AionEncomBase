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
package ai.instance.infinityShard;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("hyperion")
public class HyperionAI2 extends AggressiveNpcAI2
{
	private int castc = 0;
	private Future<?> Cast;
	private boolean canThink = true;
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void spawnIdeResonator() {
		//Ide Resonators are charging the Hyperion.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDRuneWP_Charging, 0);
		//Phase 1 of the Ide energy charging complete.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDRuneWP_Charger1_Completed, 3000);
		//Phase 2 of the Ide energy charging complete.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDRuneWP_Charger2_Completed, 9000);
		//Phase 3 of the Ide energy charging complete.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDRuneWP_Charger3_Completed, 15000);
		//All phases of Ide energy charging complete. Hyperion ultimate attack imminent.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDRuneWP_Charger4_Completed, 21000);
		switch (Rnd.get(1, 4)) {
		    case 1:
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(276519, 108.55013f, 138.96940f, 132.60164f, (byte) 0);
					}
				}, 3000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231093, 126.54710f, 154.47961f, 131.47116f, (byte) 0);
					}
				}, 9000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231094, 146.72450f, 139.12267f, 132.68515f, (byte) 0);
					}
				}, 15000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231095, 129.41306f, 121.34766f, 131.47110f, (byte) 0);
					}
				}, 21000);
			break;
			case 2:
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(276519, 129.41306f, 121.34766f, 131.47110f, (byte) 0);
					}
				}, 3000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231093, 108.55013f, 138.96940f, 132.60164f, (byte) 0);
					}
				}, 9000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231094, 126.54710f, 154.47961f, 131.47116f, (byte) 0);
					}
				}, 15000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231095, 146.72450f, 139.12267f, 132.68515f, (byte) 0);
					}
				}, 21000);
			break;
			case 3:
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(276519, 146.72450f, 139.12267f, 132.68515f, (byte) 0);
					}
				}, 3000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231093, 129.41306f, 121.34766f, 131.47110f, (byte) 0);
					}
				}, 9000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231094, 108.55013f, 138.96940f, 132.60164f, (byte) 0);
					}
				}, 15000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231095, 126.54710f, 154.47961f, 131.47116f, (byte) 0);
					}
				}, 21000);
			break;
			case 4:
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(276519, 126.54710f, 154.47961f, 131.47116f, (byte) 0);
					}
				}, 3000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231093, 146.72450f, 139.12267f, 132.68515f, (byte) 0);
					}
				}, 9000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231094, 129.41306f, 121.34766f, 131.47110f, (byte) 0);
					}
				}, 15000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(231095, 108.55013f, 138.96940f, 132.60164f, (byte) 0);
					}
				}, 21000);
			break;
		}
	}
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{99, 85, 80, 70, 60, 55, 54, 50, 46, 45, 40, 35, 25, 23, 20, 18, 10, 8, 5, 3});
	}
	
	private void checkPercentage(int hpPercentage) {
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 99:
						startCast();
						spawnIdeResonator();
					break;
					case 85:
						castc = 1;
						startCast();
					break;
					case 80:
						AI2Actions.useSkill(this, 21253);
						firstWaveEvent();
					break;
					case 70:
						castc = 1;
						startCast2();
						firstWaveEvent();
					break;
					case 60:
						castc = 0;
						startCast3();
						secondWaveEvent();
					break;
					case 55:
						castc = 0;
						startCast4();
						secondWaveEvent();
					break;
					case 54:
						AI2Actions.useSkill(this, 21244);
					break;
					case 50:
						castc = 0;
						startCast4();
						secondWaveEvent();
					break;
					case 46:
						AI2Actions.useSkill(this, 21244);
					break;
					case 45:
						castc = 1;
						startCast5();
						secondWaveEvent();
					break;
					case 40:
						AI2Actions.useSkill(this, 21248);
					break;
					case 35:
						castc = 1;
						startCast2();
					break;
					case 25:
						castc = 1;
						startCast5();
						thirdWaveEvent();
					break;
					case 23:
						AI2Actions.useSkill(this, 21244);
					break;
					case 20:
						castc = 0;
						startCast4();
						thirdWaveEvent();
					break;
					case 18:
						AI2Actions.useSkill(this, 21244);
					break;
					case 10:
						AI2Actions.useSkill(this, 21246);
					break;
					case 8:
						AI2Actions.useSkill(this, 21249);
					break;
					case 5:
						castc = 0;
						startCast4();
						thirdWaveEvent();
					break;
					case 3:
						AI2Actions.useSkill(this, 21249);
					break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void startCast() {
		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (castc >= 3)
				Cast.cancel(false);
				Cast1();
				castc++;
			}
		}, 1 * 1000 * 1, 5 * 1000 * 1);
    }
	
	private void Cast1() {
		AI2Actions.useSkill(this, 21241);
	}
	
	private void startCast2() {
		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (castc >= 3)
				Cast.cancel(false);
				Cast2();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);
    }
	
	private void Cast2() {
		switch (castc) {
		    case 1:
		       AI2Actions.useSkill(this, 21250);
		    break;
		    case 2:
		       AI2Actions.useSkill(this, 21251);
		    break;
		}
	}
	
	private void startCast3() {
		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (castc >= 4)
				Cast.cancel(false);
				Cast3();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);
    }
	
	private void Cast3() {
		switch (castc) {
		    case 0:
		       AI2Actions.useSkill(this, 21250);
		    break;
		    case 1:
		       AI2Actions.useSkill(this, 21251);
		    break;
		    case 2:
		       AI2Actions.useSkill(this, 21245);
		    break;
		    case 3:
		       AI2Actions.useSkill(this, 21253);
		    break;
		}
	}
	
	private void startCast4() {
		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (castc >= 3)
				Cast.cancel(false);
				Cast4();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);
    }
	
	private void Cast4() {
		switch (castc) {
		    case 0:
		       AI2Actions.useSkill(this, 21250);
		    break;
		    case 1:
		       AI2Actions.useSkill(this, 21251);
		    break;
		    case 2:
		       AI2Actions.useSkill(this, 21253);
		    break;
		}
	}
	
	private void startCast5() {
		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (castc >= 3)
				Cast.cancel(false);
				Cast5();
				castc++;
			}
		}, 1 * 1000 * 1, 5 * 1000 * 1);
    }
	
	private void Cast5() {
		switch (castc) {
		    case 1:
		       AI2Actions.useSkill(this, 21241);
		    break;
		    case 2:
		       AI2Actions.useSkill(this, 21245);
		    break;
		}
	}
	
	private void rushInfinityShard(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void firstWaveEvent() {
		rushInfinityShard((Npc)spawn(231103, 123.10f, 145.36f, 112.12f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231103, 123.26f, 130.70f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231103, 135.74f, 129.67f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
	}
	
	private void secondWaveEvent() {
		rushInfinityShard((Npc)spawn(231096, 123.10f, 145.36f, 112.12f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231097, 123.26f, 130.70f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231098, 135.74f, 129.67f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(233297, 123.26f, 130.70f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(233298, 135.26f, 117.27f, 116.74f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
	}
	
	private void thirdWaveEvent() {
		rushInfinityShard((Npc)spawn(231103, 139.07f, 142.46f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231103, 135.26f, 117.27f, 116.74f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(231096, 123.10f, 145.36f, 112.12f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(233297, 139.07f, 142.46f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
		rushInfinityShard((Npc)spawn(233298, 135.74f, 129.67f, 112.17f, (byte) 0), 133.75711f, 137.96413f, 112.17429f, false);
	}
	
	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(231093)); //Ide Resonator.
			deleteNpcs(p.getWorldMapInstance().getNpcs(231094)); //Ide Resonator.
			deleteNpcs(p.getWorldMapInstance().getNpcs(231095)); //Ide Resonator.
			deleteNpcs(p.getWorldMapInstance().getNpcs(276519)); //Ide Resonator.
		}
		addPercent();
		super.handleBackHome();
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(231096)); //Hyperion Defense Combatant.
			deleteNpcs(p.getWorldMapInstance().getNpcs(231097)); //Hyperion Defense Scout.
			deleteNpcs(p.getWorldMapInstance().getNpcs(231098)); //Hyperion Defense Medic.
			deleteNpcs(p.getWorldMapInstance().getNpcs(233297)); //Hyperion Defense Assaulter.
			deleteNpcs(p.getWorldMapInstance().getNpcs(233298)); //Hyperion Defense Assassin.
			deleteNpcs(p.getWorldMapInstance().getNpcs(233299)); //Hyperion Defense Healer.
			deleteNpcs(p.getWorldMapInstance().getNpcs(231103)); //Summoned Ancien Tyrhund.
		}
		percents.clear();
		if (Cast != null) {
			Cast.cancel(false);
		}
		super.handleDied();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}