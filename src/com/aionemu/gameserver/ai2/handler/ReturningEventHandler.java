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
package com.aionemu.gameserver.ai2.handler;

import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

/**
 * @author ATracer
 * @modified Yon (Aion Reconstruction Project) -- added handling to {@link #onNotAtHome(NpcAI2)} for when the entity cannot move;
 * removed deprecated method calls
 */
public class ReturningEventHandler {

	/**
	 * @param npcAI
	 */
	public static void onNotAtHome(NpcAI2 npcAI) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onNotAtHome");
		}
		if (npcAI.setStateIfNot(AIState.RETURNING)) {
			if (npcAI.isLogging()) {
				AI2Logger.info(npcAI, "returning and restoring");
			}
			EmoteManager.emoteStartReturning(npcAI.getOwner());
		}
		if (npcAI.isInState(AIState.RETURNING)) {
			Npc npc = (Npc) npcAI.getOwner();
			if (npc.hasWalkRoutes()) {
				WalkManager.startWalking(npcAI);
			}else if (npcAI.isMoveSupported() && npc.getDistanceToSpawnLocation() < 100) { //Arbitrary distance
//					Point3D prevStep = npc.getMoveController().recallPreviousStep();
//					npcAI.getOwner().getMoveController().moveToPoint(prevStep.getX(), prevStep.getY(), prevStep.getZ());
				npc.getMoveController().abortMove();
				npc.getMoveController().moveToHome();
			} else {
				if (npc.isDeleteDelayed()) {
					onBackHome(npcAI);
				} else {
					//FIXME: Maybe there's better handling here?
					/*
					 * The idea is the entity cannot move, but has been moved from its spawn...
					 * so instead of moving it back to spawn (not possible), it should just
					 * despawn and then respawn back at the original spawn point.
					 *
					 * Or, if the entity can move, but is too far away from spawn to worry about
					 * moving back directly (which can happen since mob leashes have been removed).
					 */
					SpawnTemplate spawn = npc.getSpawn();
					int instanceId = npc.getInstanceId();
					npc.getController().onDelete();
					SpawnEngine.spawnObject(spawn, instanceId);
				}
			}
		}
	}

	/**
	 * @param npcAI
	 */
	public static void onBackHome(NpcAI2 npcAI) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onBackHome");
		}
//		npcAI.getOwner().getMoveController().clearBackSteps();
		if (npcAI.setStateIfNot(AIState.IDLE)) {
			EmoteManager.emoteStartIdling(npcAI.getOwner());
			ThinkEventHandler.thinkIdle(npcAI);
		}
		Npc npc = (Npc) npcAI.getOwner();
		npc.getController().onReturnHome();
	}
}