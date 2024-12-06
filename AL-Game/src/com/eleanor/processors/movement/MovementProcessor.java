/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors.movement;

import java.util.concurrent.ConcurrentHashMap;

import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.eleanor.processors.AGameProcessor;
import com.eleanor.processors.movement.motor.AMovementMotor;
import com.eleanor.processors.movement.motor.ReturnMotor;

public class MovementProcessor extends AGameProcessor {
	private final ConcurrentHashMap<Creature, AMovementMotor> _registeredCreatures = new ConcurrentHashMap<Creature, AMovementMotor>();

	public MovementProcessor() {
		super(12);
	}

	private boolean applyMotor(Creature creature, AMovementMotor newMotor) {
		AMovementMotor oldMotor = this._registeredCreatures.put(creature, newMotor);
		if (oldMotor == newMotor) {
			throw new Error("Attempt to replace same movement motors");
		}
		if (oldMotor != null) {
			oldMotor.stop();
			newMotor.start();
		}
		return true;
	}

	public AMovementMotor applyReturn(Npc creature, Vector3f spot) {
		ReturnMotor returnMotor = new ReturnMotor(creature, spot, this);
		if (this.applyMotor(creature, returnMotor)) {
			return returnMotor;
		}
		return null;
	}
}
