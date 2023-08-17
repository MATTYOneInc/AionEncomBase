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
import java.util.Collections;
import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("mage_preceptor")
public class MagePreceptorAI2 extends AggressiveNpcAI2
{
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void startEvent(int percent) {
		if (percent == 50 || percent == 25) {
			SkillEngine.getInstance().getSkill(getOwner(), 19606, 46, getTarget()).useNoAnimationSkill();
		} switch (percent) {
			case 75:
				SkillEngine.getInstance().getSkill(getOwner(), 19605, 46, getTargetPlayer()).useNoAnimationSkill();
			break;
			case 50:
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (!isAlreadyDead()) {
							SkillEngine.getInstance().getSkill(getOwner(), 19609, 46, getOwner()).useNoAnimationSkill();	
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									WorldPosition p = getPosition();
									switch (Rnd.get(1, 2)) {
										case 1:
										    spawn(282363, p.getX(), p.getY(), p.getZ(), p.getHeading()); //Summoned Tran Of Fire.
										break;
										case 2:
										    spawn(282364, p.getX(), p.getY(), p.getZ(), p.getHeading()); //Summoned Tran Of Wind.
										break;
									}
									scheduleSkill(2000);
								}
							}, 4500);
						}
					}
				}, 3000);
			break;
			case 25:
				scheduleSkill(3000);
				scheduleSkill(9000);
				scheduleSkill(15000);
			break;
		}
	}
	
	private void scheduleSkill(int delay) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isAlreadyDead()) {
					SkillEngine.getInstance().getSkill(getOwner(), 19605, 46, getTargetPlayer()).useNoAnimationSkill();
				}
			}
		}, delay);
	}
	
	private Player getTargetPlayer() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 37)) {
				players.add(player);
			}
		}
		return players.get(Rnd.get(players.size()));
	}
	
	private void checkPercentage(int percentage) {
		for (Integer percent : percents) {
			if (percentage <= percent) {
				percents.remove(percent);
				startEvent(percent);
				break;
			}
		}
	}
	private void addPercents() {
		percents.clear();
		Collections.addAll(percents, new Integer[] {75, 50, 25});
	}
	
	@Override
	public void handleSpawned() {
		super.handleSpawned();
		addPercents();
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282363)); //Summoned Tran Of Fire.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282364)); //Summoned Tran Of Wind.
		}
		super.handleDied();
	}
	
	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282363)); //Summoned Tran Of Fire.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282364)); //Summoned Tran Of Wind.
		}
		addPercents();
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