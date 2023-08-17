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
package ai.instance.empyreanCrucible;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("priest_asmodians_preceptor")
public class PriestAsmodiansPreceptorAI2 extends AggressiveNpcAI2
{
	private AtomicBoolean is75EventStarted = new AtomicBoolean(false);
	private AtomicBoolean is25EventStarted = new AtomicBoolean(false);
	
	@Override
	public void handleSpawned() {
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 19612, 46, getOwner()).useNoAnimationSkill();
			}
		}, 1000);
	}
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int percentage) {
		if (percentage <= 75) {
			if (is75EventStarted.compareAndSet(false, true)) {
				SkillEngine.getInstance().getSkill(getOwner(), 19611, 46, getTargetPlayer()).useNoAnimationSkill();
			}
		} if (percentage <= 25) {
			if (is25EventStarted.compareAndSet(false, true)) {
				startEvent();
			}
		}
	}
	
	private void startEvent() {
		SkillEngine.getInstance().getSkill(getOwner(), 19610, 46, getOwner()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(getOwner(), 19614, 46, getOwner()).useNoAnimationSkill();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						WorldPosition p = getPosition();
						switch (Rnd.get(1, 3)) {
							case 1:
								applySoulSickness((Npc) spawn(282369, p.getX(), p.getY(), p.getZ(), p.getHeading())); //Traufnir.
							break;
							case 2:
								applySoulSickness((Npc) spawn(282370, p.getX(), p.getY(), p.getZ(), p.getHeading())); //Sigyn.
							break;
							case 3:
								applySoulSickness((Npc) spawn(282371, p.getX(), p.getY(), p.getZ(), p.getHeading())); //Sif.
							break;
						}
					}
				}, 5000);
			}
		}, 2000);
	}
	
	private Player getTargetPlayer() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 25)) {
				players.add(player);
			}
		}
		return players.get(Rnd.get(players.size()));
	}
	
	private void applySoulSickness(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				npc.getLifeStats().setCurrentHpPercent(50);
				SkillEngine.getInstance().getSkill(npc, 19594, 4, npc).useNoAnimationSkill();
			}
		}, 1000);
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282369)); //Traufnir.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282370)); //Sigyn.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282371)); //Sif.
		}
		super.handleDied();
	}
	
	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282369)); //Traufnir.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282370)); //Sigyn.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282371)); //Sif.
		}
		is75EventStarted.set(false);
		is25EventStarted.set(false);
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