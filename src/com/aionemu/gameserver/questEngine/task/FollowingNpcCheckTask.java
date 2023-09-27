/*

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
package com.aionemu.gameserver.questEngine.task;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author ATracer
 */
public class FollowingNpcCheckTask implements Runnable {

	private final QuestEnv env;
	private final DestinationChecker destinationChecker;

	/**
	 * @param player
	 * @param npc
	 * @param destinationChecker
	 */
	FollowingNpcCheckTask(QuestEnv env, DestinationChecker destinationChecker) {
		this.env = env;
		this.destinationChecker = destinationChecker;
	}

	@Override
	public void run() {
		final Player player = env.getPlayer();
		Npc npc = (Npc) destinationChecker.follower;
		if (player.getLifeStats().isAlreadyDead() || npc.getLifeStats().isAlreadyDead()) {
			onFail(env);
		}
		if (!MathUtil.isIn3dRange(player, npc, 50)) {
			onFail(env);
		}

		if (destinationChecker.check()) {
			onSuccess(env);
		}
	}

	/**
	 * Following task succeeded, proceed with quest
	 */
	private final void onSuccess(QuestEnv env) {
		stopFollowing(env);
		QuestEngine.getInstance().onNpcReachTarget(env);
	}

	/**
	 * Following task failed, abort further progress
	 */
	protected void onFail(QuestEnv env) {
		stopFollowing(env);
		QuestEngine.getInstance().onNpcLostTarget(env);
	}

	private final void stopFollowing(QuestEnv env) {
		Player player = env.getPlayer();
		Npc npc = (Npc) destinationChecker.follower;
		player.getController().cancelTask(TaskId.QUEST_FOLLOW);
		npc.getAi2().onCreatureEvent(AIEventType.STOP_FOLLOW_ME, player);
		if (!npc.getAi2().getName().equals("following")) {
			npc.getController().onDelete();
		}
	}
}