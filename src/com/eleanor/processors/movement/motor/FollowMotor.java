/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors.movement.motor;

import java.util.concurrent.ScheduledFuture;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.eleanor.processors.movement.MovementProcessor;
import com.eleanor.processors.movement.PathfindHelper;
import com.eleanor.utils.GeomUtil;

public class FollowMotor extends AMovementMotor {
	private static final int TARGET_REVALIDATE_TIME = 300;
	private static long pathfindRevalidationTime;
	public VisibleObject _target;
	private ScheduledFuture<?> _task;
	private long _lastMoveMs;
	private Vector3f _lastMovePoint;
	private byte new_targetHeading;

	public FollowMotor(MovementProcessor parentProcessor, Npc owner, VisibleObject target) {
		super(owner, parentProcessor);
		this._target = target;
	}

	@Override
	public void start() {
		assert (this._task == null);
		this.update();
	}

	@Override
	public void stop() {
		if (this._task != null) {
			this._task.cancel(true);
		}
		pathfindRevalidationTime = 0L;
		this._lastMoveMs = 0L;
		this._lastMovePoint = null;
		this._target = null;
	}

	public boolean update() {
		VisibleObject target = this._target;
		if (target == null || this._task != null && this._task.isCancelled()
				|| this._owner.getLifeStats().isAlreadyDead() || this._owner.getAi2().getState() == AIState.DIED) {
			return false;
		}
		boolean directionChanged = false;
		this._lastMovePoint = new Vector3f(this._owner.getX(), this._owner.getY(), this._owner.getZ());
		boolean canPass = GeoService.getInstance().canPass(this._owner, target);
		if (this.canMove() && !canPass && pathfindRevalidationTime < System.currentTimeMillis()) {
			this._targetPosition = PathfindHelper.selectFollowStep(this._owner, target);
		} else if (this.canMove() && canPass) {
			float newZ = GeoService.getInstance().getZ(this._owner.getWorldId(), target.getX(), target.getY(),
					target.getZ(), 100.0f, this._owner.getInstanceId());
			Vector3f getTargetPos = new Vector3f(target.getX(), target.getY(), newZ);
			float range = (float) this._owner.getGameStats().getAttackRange().getCurrent() / 1000.0f;

			if (this._lastMovePoint == null) {
				this._lastMovePoint = new Vector3f(this._owner.getX(), this._owner.getY(), this._owner.getZ());
			}
			double distance = GeomUtil.getDistance3D(this._lastMovePoint, getTargetPos.x, getTargetPos.y,
					getTargetPos.z) - Math.max(range, this._owner.getCollision());
			Vector3f dir = GeomUtil.getDirection3D(this._lastMovePoint, getTargetPos);
			this._targetPosition = GeomUtil.getNextPoint3D(this._lastMovePoint, dir, (float) distance);
		} else if (pathfindRevalidationTime < System.currentTimeMillis()) {
			this._targetPosition = null;
		}
		if (this._targetPosition != null) {
			directionChanged = this._targetPosition.x != this.targetDestX || this._targetPosition.y != this.targetDestY
					|| this._targetPosition.z != this.targetDestZ;
			this.targetDestX = this._targetPosition.x;
			this.targetDestY = this._targetPosition.y;
			this.targetDestZ = this._targetPosition.z;
			double distance = GeomUtil.getDistance3D(this._owner.getX(), this._owner.getY(), this._owner.getZ(),
					this._targetPosition.x, this._targetPosition.y, this._targetPosition.z);
			float speed = this._owner.getGameStats().getMovementSpeedFloat();
			long movementTime = (long) (distance / (double) speed * 1000.0);
			pathfindRevalidationTime = System.currentTimeMillis() + movementTime;
			this.recalculateMovementParams();
			this.new_targetHeading = (byte) (Math.toDegrees(Math.atan2(this._targetPosition.y - this._owner.getY(),
					this._targetPosition.x - this._owner.getX())) / 3.0);
			if (directionChanged) {
				PacketSendUtility.broadcastPacket(this._owner,
						new SM_MOVE(this._owner.getObjectId(), this._owner.getX(), this._owner.getY(),
								this._owner.getZ(), this._targetPosition.x, this._targetPosition.y,
								this._targetPosition.z, this.new_targetHeading, this._targetMask));
			}
			this._lastMoveMs = System.currentTimeMillis();
		}
		this._task = this._processor.schedule(new Runnable() {

			@Override
			public void run() {
				if (FollowMotor.this._targetPosition != null) {
					Vector3f lastMove = FollowMotor.this._lastMovePoint;
					Vector3f targetMove = new Vector3f(FollowMotor.this._targetPosition.x,
							FollowMotor.this._targetPosition.y, FollowMotor.this._targetPosition.z);
					float speed = FollowMotor.this._owner.getGameStats().getMovementSpeedFloat();
					long time = System.currentTimeMillis() - FollowMotor.this._lastMoveMs;
					float distPassed = speed * ((float) time / 1000.0f);
					if (lastMove == null) {
						lastMove = new Vector3f(FollowMotor.this._owner.getX(), FollowMotor.this._owner.getY(),
								FollowMotor.this._owner.getZ());
					}
					float maxDist = lastMove.distance(targetMove);
					if (distPassed <= 0.0f) {
						return;
					}
					if (distPassed > maxDist) {
						distPassed = maxDist;
					}
					Vector3f dir = GeomUtil.getDirection3D(lastMove, targetMove);
					Vector3f position = GeomUtil.getNextPoint3D(lastMove, dir, distPassed);
					if (FollowMotor.this._owner.getWorldId() != 300230000) {
						float newZ = GeoService.getInstance().getZ(FollowMotor.this._owner.getWorldId(), position.x,
								position.y, position.z, 100.0f, FollowMotor.this._owner.getInstanceId());
						position.z = lastMove.getZ() < newZ & Math.abs(lastMove.getZ() - newZ) > 1.0f
								? newZ + FollowMotor.this._owner.getObjectTemplate().getBoundRadius().getUpper()
										- FollowMotor.this._owner.getObjectTemplate().getHeight()
								: newZ;
					}
					World.getInstance().updatePosition(FollowMotor.this._owner, position.x, position.y, position.z,
							FollowMotor.this.new_targetHeading, false);
				} else {
					PacketSendUtility.broadcastPacket(FollowMotor.this._owner,
							new SM_MOVE(FollowMotor.this._owner.getObjectId(), FollowMotor.this._owner.getX(),
									FollowMotor.this._owner.getY(), FollowMotor.this._owner.getZ(),
									FollowMotor.this._owner.getX(), FollowMotor.this._owner.getY(),
									FollowMotor.this._owner.getZ(), FollowMotor.this._owner.getHeading(), (byte) 0));
					pathfindRevalidationTime = 0L;
				}
				FollowMotor.this._processor.schedule(new Runnable() {

					@Override
					public void run() {
						FollowMotor.this.update();
					}
				}, 0L);
			}
		}, TARGET_REVALIDATE_TIME);
		return true;
	}

	private boolean canMove() {
		return !this._owner.getEffectController().isUnderFear() && this._owner.canPerformMove()
				&& this._owner.getAi2().getSubState() != AISubState.CAST;
	}
}
