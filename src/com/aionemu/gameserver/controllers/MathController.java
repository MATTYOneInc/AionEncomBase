/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javolution.util.FastMap
 */
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.controllers.observer.MathObjectObserver;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectReaction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class MathController extends VisibleObjectController<MathObject> {
	FastMap<Creature, MathObjectObserver> observers = new FastMap().shared();

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		if (((MathObject) this.getOwner()).getReaction() == MathObjectReaction.PC ? !(object instanceof Player)
				: (((MathObject) this.getOwner()).getReaction() == MathObjectReaction.NPC ? !(object instanceof Npc)
						: ((MathObject) this.getOwner()).getReaction() == MathObjectReaction.ALL
								&& !(object instanceof Creature))) {
			return;
		}
		Creature creature = (Creature) object;
		MathObjectObserver observer = new MathObjectObserver((MathObject) this.getOwner(), creature,
				((MathObject) this.getOwner()).getType());
		creature.getObserveController().addObserver(observer);
		this.observers.put(creature, observer);
		observer.moved();
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (((MathObject) this.getOwner()).getReaction() == MathObjectReaction.PC ? !(object instanceof Player)
				: (((MathObject) this.getOwner()).getReaction() == MathObjectReaction.NPC ? !(object instanceof Npc)
						: ((MathObject) this.getOwner()).getReaction() == MathObjectReaction.ALL
								&& !(object instanceof Creature))) {
			return;
		}
		if (isOutOfRange && object instanceof Creature) {
			Creature creature = (Creature) object;
			MathObjectObserver observer = (MathObjectObserver) this.observers.remove((Object) creature);
			observer.clearShedules();
			creature.getObserveController().removeObserver(observer);
		}
	}

	/*
	 * public Npc spawn(int worldId, int npcId, float x, float y, float z, byte
	 * heading, int staticId, int instanceId, int randomWalk, String ai) {
	 * SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn((int)worldId,
	 * (int)npcId, (float)x, (float)y, (float)z, (byte)heading, (int)randomWalk);
	 * template.setStaticId(staticId); Npc master =
	 * (Npc)SpawnEngine.spawnObject(template, instanceId); if (randomWalk > 0) { if
	 * (master.getObjectTemplate().getStatsTemplate().getWalkSpeed() == 0.0f) { //
	 * empty if block } WalkManager.startWalking((NpcAI2)master.getAi2()); } if (ai
	 * != null) { AI2Engine.getInstance().setupAI(ai, (Creature)master);
	 * ((NpcAI2)master.getAi2()).setStateIfNot(AIState.IDLE); }
	 * ((MathObject)this.getOwner()).setMaster(master); return
	 * ((MathObject)this.getOwner()).getMaster(); }
	 */
	public void onDelete(int delay) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				MathController.this.delete();
			}
		}, delay);
	}

	@Override
	public void delete() {
		if (((MathObject) this.getOwner()).getMaster() != null) {
			((MathObject) this.getOwner()).getMaster().getController().delete();
		}
		((MathObject) this.getOwner()).getKnownList().doOnAllObjects(new Visitor<VisibleObject>() {

			@Override
			public void visit(VisibleObject object) {
				if (!(object instanceof Creature)) {
					return;
				}
				Creature creature = (Creature) object;
				MathObjectObserver observer = (MathObjectObserver) MathController.this.observers
						.remove((Object) creature);
				if (observer == null) {
					return;
				}
				observer.clearShedules();
				creature.getObserveController().removeObserver(observer);
			}
		});
		super.delete();
	}
}
