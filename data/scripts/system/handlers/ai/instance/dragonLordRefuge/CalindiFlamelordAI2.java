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
package ai.instance.dragonLordRefuge;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("calindi")
public class CalindiFlamelordAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private Future<?> trapTask;
    private boolean isFinalBuff;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	
    @Override
    protected void handleAttack(Creature creature) {
	    super.handleAttack(creature);
	    if (isHome.compareAndSet(true, false))
		   startSkillTask();
	    if (!isFinalBuff) {
		    blazeEngraving();
		    if (getOwner().getLifeStats().getHpPercentage() <= 12) {
			   isFinalBuff = true;
			   cancelTask();
			   AI2Actions.useSkill(this, 20915);
		    }
	    }
    }
	
    private void startSkillTask() {
	    trapTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
			    if (isAlreadyDead())
			       cancelTask();
			    else {
			       startHallucinatoryVictoryEvent();
			    }
		    }
	    }, 5000, 80000);
    }
	
    private void cancelTask() {
	    if (trapTask != null && !trapTask.isCancelled()) {
		    trapTask.cancel(true);
	    }
    }
	
    private void startHallucinatoryVictoryEvent() {
	    if (getPosition().getWorldMapInstance().getNpc(730695) == null) {
		    AI2Actions.useSkill(this, 20911);
		    SkillEngine.getInstance().applyEffectDirectly(20590, getOwner(), getOwner(), 0);
		    SkillEngine.getInstance().applyEffectDirectly(20591, getOwner(), getOwner(), 0);
		    spawn(730695, 482.21f, 458.06f, 427.42f, (byte) 98);
		    spawn(730695, 482.21f, 571.16f, 427.42f, (byte) 22);
		    rndSpawn(283132, 5);
	    }
    }
	
    private void blazeEngraving() {
	    if (Rnd.get(0, 100) < 2 && getPosition().getWorldMapInstance().getNpc(283130) == null) {
		    NpcShoutsService.getInstance().sendMsg(getOwner(), 1500718, getObjectId(), 0, 0);
			SkillEngine.getInstance().getSkill(getOwner(), 20913, 60, getOwner().getTarget()).useNoAnimationSkill();
		    Player target = getRandomTarget();
		    if (target == null)
		    return;
		    spawn(283130, target.getX(), target.getY(), target.getZ(), (byte) 0);
	    }
    }
	
    private void rndSpawn(int npcId, int count) {
	    for (int i = 0; i < count; i++) {
		    SpawnTemplate template = rndSpawnInRange(npcId);
		    SpawnEngine.spawnObject(template, getPosition().getInstanceId());
	    }
    }
	
    private SpawnTemplate rndSpawnInRange(int npcId) {
	    float direction = Rnd.get(0, 199) / 100f;
	    int range = Rnd.get(5, 20);
	    float x1 = (float) (Math.cos(Math.PI * direction) * range);
	    float y1 = (float) (Math.sin(Math.PI * direction) * range);
	    return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), getPosition().getHeading());
    }
	
    private Player getRandomTarget() {
	    List<Player> players = new ArrayList<Player>();
	    for (Player player : getKnownList().getKnownPlayers().values()) {
		   if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 50)) {
			   players.add(player);
		   }
	    }
	    if (players.isEmpty())
		   return null;
	    return players.get(Rnd.get(players.size()));
    }
	
	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(283130));
			deleteNpcs(instance.getNpcs(283132));
			deleteNpcs(instance.getNpcs(730695));
		}
	}
	
    @Override
    protected void handleDied() {
		cancelTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(283130));
			deleteNpcs(p.getWorldMapInstance().getNpcs(283132));
			deleteNpcs(p.getWorldMapInstance().getNpcs(730695));
		}
		sendMsg(1500728);
		super.handleDied();
    }
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
    @Override
    protected void handleDespawned() {
	    super.handleDespawned();
	    cancelTask();
    }
	
    @Override
    protected void handleBackHome() {
	    super.handleBackHome();
	    cancelTask();
		deleteHelpers();
	    isFinalBuff = false;
	    isHome.set(true);
		canThink = true;
    }
	
	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}
}