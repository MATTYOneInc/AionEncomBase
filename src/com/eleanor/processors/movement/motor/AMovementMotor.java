/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors.movement.motor;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.eleanor.processors.movement.MovementProcessor;

public abstract class AMovementMotor {
	final Npc _owner;
	final MovementProcessor _processor;
	Vector3f _targetPosition;
	protected float targetDestX;
	protected float targetDestY;
	protected float targetDestZ;
	byte _targetMask;
	byte _targetHeading;

	AMovementMotor(Npc owner, MovementProcessor processor) {
		this._owner = owner;
		this._processor = processor;
	}

	public abstract void start();

	public abstract void stop();

	public Vector3f getCurrentTarget() {
		return this._targetPosition;
	}

	public byte getMovementMask() {
		return this._targetMask;
	}

	void recalculateMovementParams() {
		byte oldHeading = this._owner.getHeading();
		byte _targetHeading = (byte) (Math.toDegrees(Math.atan2(this._targetPosition.getY() - this._owner.getY(),
				this._targetPosition.getX() - this._owner.getX())) / 3.0);
		this._targetMask = 0;
		if (oldHeading != _targetHeading) {
			this._targetMask = (byte) (this._targetMask | 0xFFFFFFE0);
		}
		Stat2 stat = this._owner.getGameStats().getMovementSpeed();
		if (this._owner.isInState(CreatureState.WEAPON_EQUIPPED)) {
			this._targetMask = (byte) (this._targetMask | (stat.getBonus() < 0 ? -30 : -28));
		} else if (this._owner.isInState(CreatureState.WALKING) || this._owner.isInState(CreatureState.ACTIVE)) {
			this._targetMask = (byte) (this._targetMask | (stat.getBonus() < 0 ? -24 : -22));
		}
		if (this._owner.isFlying()) {
			this._targetMask = (byte) (this._targetMask | 4);
		}
		if (this._owner.getAi2().getState() == AIState.RETURNING) {
			this._targetMask = (byte) (this._targetMask | 0xFFFFFFE2);
		}
	}
}
