/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors.movement.motor;

import java.util.concurrent.ScheduledFuture;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.eleanor.processors.movement.MovementProcessor;

public class ReturnMotor extends AMovementMotor {
	private ScheduledFuture<?> _task;

	public ReturnMotor(Npc owner, Vector3f spot, MovementProcessor processor) {
		super(owner, processor);
		this._targetPosition = spot;
	}

	@Override
	public void start() {
		assert (this._task == null);
		this.recalculateMovementParams();
		float speed = this._owner.getGameStats().getMovementSpeedFloat();
		long movementTime = (long) (100.0f / speed * 1000.0f);
		PacketSendUtility.broadcastPacket(this._owner,
				new SM_MOVE(this._owner.getObjectId(), this._owner.getX(), this._owner.getY(), this._owner.getZ(),
						this._targetPosition.x, this._targetPosition.y, this._targetPosition.z, this._targetHeading,
						this._targetMask));
		this._task = this._processor.schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().updatePosition(ReturnMotor.this._owner, ReturnMotor.this._targetPosition.x,
						ReturnMotor.this._targetPosition.y, ReturnMotor.this._targetPosition.z,
						ReturnMotor.this._targetHeading, false);
				ReturnMotor.this._owner.getAi2().onGeneralEvent(AIEventType.MOVE_ARRIVED);
				ReturnMotor.this._owner.getAi2().onGeneralEvent(AIEventType.BACK_HOME);
			}
		}, movementTime);
	}

	@Override
	public void stop() {
	}
}
