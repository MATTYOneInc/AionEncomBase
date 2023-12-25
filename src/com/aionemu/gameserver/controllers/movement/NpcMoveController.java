/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.aionemu.gameserver.controllers.movement;

import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.handler.TargetEventHandler;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.controllers.movement.CreatureMoveController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.templates.walker.RouteStep;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;
import com.aionemu.gameserver.model.templates.zone.Point2D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.spawnengine.WalkerFormator;
import com.aionemu.gameserver.spawnengine.WalkerGroup;
import com.aionemu.gameserver.taskmanager.tasks.MoveTaskManager;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.collections.LastUsedCache;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.eleanor.Global;
import com.eleanor.processors.movement.motor.FollowMotor;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NpcMoveController
extends CreatureMoveController<Npc> {
    private static final Logger log = LoggerFactory.getLogger(NpcMoveController.class);
    public static final float MOVE_CHECK_OFFSET = 0.1f;
    private static final float MOVE_OFFSET = 0.05f;
    private Destination destination = Destination.TARGET_OBJECT;
    private float pointX;
    private float pointY;
    private float pointZ;
    private LastUsedCache<Byte, Point3D> lastSteps = null;
    private byte stepSequenceNr = 0;
    private float offset = 0.1f;
    List<RouteStep> currentRoute;
    int currentPoint;
    int walkPause;
    private float cachedTargetZ;
    private FollowMotor _followMotor;

    private void applyFollow(VisibleObject target) {
        if (this._followMotor != null && this._followMotor._target == target) {
            return;
        }
        if (this._followMotor != null) {
            this._followMotor.stop();
        }
        this._followMotor = new FollowMotor(Global.MovementProcessor, (Npc)this.owner, target);
        this._followMotor.start();
    }

    private void cancelFollow() {
        if (this._followMotor != null) {
            this._followMotor.stop();
            this._followMotor = null;
        }
    }

    public NpcMoveController(Npc owner) {
        super(owner);
    }

    public void moveToTargetObject() {
        if (this.started.compareAndSet(false, true)) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "MC: \u5f00\u59cb\u79fb\u52a8\u5230\u76ee\u6807");
            }
            this.destination = Destination.TARGET_OBJECT;
            this.updateLastMove();
            MoveTaskManager.getInstance().addCreature((Creature)this.owner);
        }
    }

    public void moveToPoint(float x, float y, float z) {
        if (this.started.compareAndSet(false, true)) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "MC: \u5f00\u59cb\u79fb\u52a8\u5230\u70b9");
            }
            this.destination = Destination.POINT;
            this.pointX = x;
            this.pointY = y;
            this.pointZ = z;
            this.updateLastMove();
            MoveTaskManager.getInstance().addCreature((Creature)this.owner);
        }
    }

    public void moveToNextPoint() {
        if (this.started.compareAndSet(false, true)) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "MC: \u79fb\u52a8\u5230\u4e0b\u4e00\u4e2a\u70b9\u5f00\u59cb");
            }
            this.destination = Destination.POINT;
            this.updateLastMove();
            MoveTaskManager.getInstance().addCreature((Creature)this.owner);
        }
    }

    @Override
    public void moveToDestination() {
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "\u79fb\u81f3\u76ee\u7684\u5730: " + (Object)((Object)this.destination));
        }
        if (NpcActions.isAlreadyDead((Creature)this.owner)) {
            this.abortMove();
            return;
        }
        if (!((Npc)this.owner).canPerformMove() || ((Npc)this.owner).getAi2().getSubState() == AISubState.CAST) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "\u79fb\u52a8\u5230\u76ee\u7684\u5730\u65e0\u6cd5\u6267\u884c\u79fb\u52a8");
            }
            if (this.started.compareAndSet(true, false)) {
                this.cancelFollow();
                this.setAndSendStopMove((Creature)this.owner);
            }
            this.updateLastMove();
            return;
        }
        if (this.started.compareAndSet(false, true)) {
            this.movementMask = (byte)-32;
            PacketSendUtility.broadcastPacket(this.owner, new SM_MOVE((Creature)this.owner));
        }
        if (!this.started.get() && ((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "\u79fb\u81f3\u76ee\u7684\u5730\u5c1a\u672a\u5f00\u59cb");
        }
        switch (this.destination) {
            case TARGET_OBJECT: {
                Npc npc = (Npc)this.owner;
                VisibleObject target = ((Npc)this.owner).getTarget();
                if (target == null) {
                    this.cancelFollow();
                    return;
                }
                if (!(target instanceof Creature)) {
                    this.cancelFollow();
                    return;
                }
                if (((Npc)this.owner).getAi2().getState() == AIState.FOLLOWING) {
                    this.cancelFollow();
                    this.offset = npc.getController().getAttackDistanceToTarget();
                    this.moveToLocation(target.getX(), target.getY(), target.getZ(), this.offset);
                    break;
                }
                this.applyFollow(target);
                break;
            }
            case POINT: {
                this.cancelFollow();
                this.offset = 0.1f;
                this.moveToLocation(this.pointX, this.pointY, this.pointZ, this.offset);
            }
        }
        this.updateLastMove();
    }

    private float getTargetZ(Npc npc, Creature creature) {
        float targetZ = creature.getZ();
        if (GeoDataConfig.GEO_NPC_MOVE && creature.isInFlyingState() && !npc.isInFlyingState()) {
            if (npc.getGameStats().checkGeoNeedUpdate()) {
                this.cachedTargetZ = GeoService.getInstance().getZ(creature);
            }
            targetZ = this.cachedTargetZ;
        }
        return targetZ;
    }

    protected void moveToLocation(float targetX, float targetY, float targetZ, float offset) {
        boolean directionChanged = false;
        float ownerX = ((Npc)this.owner).getX();
        float ownerY = ((Npc)this.owner).getY();
        float ownerZ = ((Npc)this.owner).getZ();
        boolean bl = directionChanged = targetX != this.targetDestX || targetY != this.targetDestY || targetZ != this.targetDestZ;
        if (directionChanged) {
            this.heading = (byte)(Math.toDegrees(Math.atan2(targetY - ownerY, targetX - ownerX)) / 3.0);
        }
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "OLD targetDestX: " + this.targetDestX + " targetDestY: " + this.targetDestY + " targetDestZ " + this.targetDestZ);
        }
        if (targetX == 0.0f && targetY == 0.0f) {
            targetX = ((Npc)this.owner).getSpawn().getX();
            targetY = ((Npc)this.owner).getSpawn().getY();
            targetZ = ((Npc)this.owner).getSpawn().getZ();
        }
        this.targetDestX = targetX;
        this.targetDestY = targetY;
        this.targetDestZ = targetZ;
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "ownerX=" + ownerX + " ownerY=" + ownerY + " ownerZ=" + ownerZ);
            AI2Logger.moveinfo((Creature)this.owner, "targetDestX: " + this.targetDestX + " targetDestY: " + this.targetDestY + " targetDestZ " + this.targetDestZ);
        }
        float currentSpeed = ((Npc)this.owner).getGameStats().getMovementSpeedFloat();
        float futureDistPassed = currentSpeed * (float)(System.currentTimeMillis() - this.lastMoveUpdate) / 1000.0f;
        float dist = (float)MathUtil.getDistance(ownerX, ownerY, ownerZ, targetX, targetY, targetZ);
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "futureDist: " + futureDistPassed + " dist: " + dist);
        }
        if (dist == 0.0f) {
            if (((Npc)this.owner).getAi2().getState() == AIState.RETURNING) {
                if (((Npc)this.owner).getAi2().isLogging()) {
                    AI2Logger.moveinfo((Creature)this.owner, "\u72b6\u6001\u8fd4\u56de\uff1a\u4e2d\u6b62\u79fb\u52a8");
                }
                TargetEventHandler.onTargetReached((NpcAI2)((Npc)this.owner).getAi2());
            }
            return;
        }
        if (futureDistPassed > dist) {
            futureDistPassed = dist;
        }
        float distFraction = futureDistPassed / dist;
        float newX = (this.targetDestX - ownerX) * distFraction + ownerX;
        float newY = (this.targetDestY - ownerY) * distFraction + ownerY;
        float newZ = (this.targetDestZ - ownerZ) * distFraction + ownerZ;
        if (ownerX == newX && ownerY == newY && ((Npc)this.owner).getSpawn().getRandomWalk() > 0) {
            return;
        }
        if (GeoDataConfig.GEO_NPC_MOVE && GeoDataConfig.GEO_ENABLE && ((Npc)this.owner).getAi2().getSubState() != AISubState.WALK_PATH && ((Npc)this.owner).getAi2().getState() != AIState.RETURNING && ((Npc)this.owner).getGameStats().getLastGeoZUpdate() < System.currentTimeMillis()) {
            if (((Npc)this.owner).getSpawn().getX() != this.targetDestX || ((Npc)this.owner).getSpawn().getY() != this.targetDestY || ((Npc)this.owner).getSpawn().getZ() != this.targetDestZ) {
                float geoZ = GeoService.getInstance().getZ(((Npc)this.owner).getWorldId(), newX, newY, newZ, 0.0f, ((Npc)this.owner).getInstanceId());
                if (Math.abs(newZ - geoZ) > 1.0f) {
                    directionChanged = true;
                }
                newZ = geoZ + ((Npc)this.owner).getObjectTemplate().getBoundRadius().getUpper() - ((Npc)this.owner).getObjectTemplate().getHeight();
            }
            ((Npc)this.owner).getGameStats().setLastGeoZUpdate(System.currentTimeMillis() + 1000L);
        }
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "newX=" + newX + " newY=" + newY + " newZ=" + newZ + " mask=" + this.movementMask);
        }
        World.getInstance().updatePosition(this.owner, newX, newY, newZ, this.heading, false);
        byte newMask = this.getMoveMask(directionChanged);
        if (this.movementMask != newMask) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "oldMask=" + this.movementMask + " newMask=" + newMask);
            }
            this.movementMask = newMask;
            PacketSendUtility.broadcastPacket(this.owner, new SM_MOVE((Creature)this.owner));
        }
    }

    private byte getMoveMask(boolean directionChanged) {
        if (directionChanged) {
            return -32;
        }
        if (((Npc)this.owner).getAi2().getState() == AIState.RETURNING) {
            return -30;
        }
        if (((Npc)this.owner).getAi2().getState() == AIState.FOLLOWING) {
            return -22;
        }
        byte mask = 0;
        Stat2 stat = ((Npc)this.owner).getGameStats().getMovementSpeed();
        if (((Npc)this.owner).isInState(CreatureState.WEAPON_EQUIPPED)) {
            mask = stat.getBonus() < 0 ? (byte)-30 : -28;
        } else if (((Npc)this.owner).isInState(CreatureState.WALKING) || ((Npc)this.owner).isInState(CreatureState.ACTIVE)) {
            byte by = mask = stat.getBonus() < 0 ? (byte)-24 : -22;
        }
        if (((Npc)this.owner).isFlying()) {
            mask = (byte)(mask | 4);
        }
        return mask;
    }

    @Override
    public void abortMove() {
        if (!this.started.get()) {
            return;
        }
        this.resetMove();
        this.setAndSendStopMove((Creature)this.owner);
    }

    public void resetMove() {
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "MC \u505c\u6b62");
        }
        this.cancelFollow();
        this.started.set(false);
        this.targetDestX = 0.0f;
        this.targetDestY = 0.0f;
        this.targetDestZ = 0.0f;
        this.pointX = 0.0f;
        this.pointY = 0.0f;
        this.pointZ = 0.0f;
    }

    public void setCurrentRoute(List<RouteStep> currentRoute) {
        if (currentRoute == null) {
            AI2Logger.info(((Npc)this.owner).getAi2(), String.format("MC: \u7f6e\u5f53\u524d\u8def\u7ebf\u5c06\u8def\u7ebf\u8bbe\u7f6e\u4e3a\u7a7a (NPC id: {})!!!", ((Npc)this.owner).getNpcId()));
        } else {
            this.currentRoute = currentRoute;
        }
        this.currentPoint = 0;
    }

    public void setRouteStep(RouteStep paramRouteStep1, RouteStep paramRouteStep2) {
        Point2D localPoint2D = null;
        if (((Npc)this.owner).getWalkerGroup() != null) {
            if (((Npc)this.owner).getWalkerGroupShift() == null) {
                log.warn("Missing WalkerGroupShift for: " + ((Npc)this.owner).getNpcId());
                return;
            }
            localPoint2D = WalkerGroup.getLinePoint(new Point2D(paramRouteStep2.getX(), paramRouteStep2.getY()), new Point2D(paramRouteStep1.getX(), paramRouteStep1.getY()), ((Npc)this.owner).getWalkerGroupShift());
            this.pointZ = paramRouteStep2.getZ();
            if (GeoDataConfig.GEO_ENABLE && GeoDataConfig.GEO_NPC_MOVE && !(this.owner.isInFlyingState())) {
				this.pointZ = GeoService.getInstance().getZ(((Creature)this.owner).getWorldId(), paramRouteStep2.getX(), paramRouteStep2.getY(), paramRouteStep2.getZ()-1, 100f, 1);
            }
            ((Npc)this.owner).getWalkerGroup().setStep((Npc)this.owner, paramRouteStep1.getRouteStep());
        } else {
            this.pointZ = paramRouteStep1.getZ();
            if (GeoDataConfig.GEO_ENABLE && GeoDataConfig.GEO_NPC_MOVE && !(this.owner.isInFlyingState())) {
				this.pointZ = GeoService.getInstance().getZ(((Creature)this.owner).getWorldId(), paramRouteStep1.getX(), paramRouteStep1.getY(), paramRouteStep1.getZ()-1, 100f, 1);
            }
        }
        this.currentPoint = paramRouteStep1.getRouteStep() - 1;
        this.pointX = localPoint2D == null ? paramRouteStep1.getX() : localPoint2D.getX();
        this.pointY = localPoint2D == null ? paramRouteStep1.getY() : localPoint2D.getY();
        this.destination = Destination.POINT;
        this.walkPause = paramRouteStep1.getRestTime();
    }

    public int getCurrentPoint() {
        return this.currentPoint;
    }

    public boolean isReachedPoint() {
        return MathUtil.getDistance(((Npc)this.owner).getX(), ((Npc)this.owner).getY(), ((Npc)this.owner).getZ(), this.pointX, this.pointY, this.pointZ) < (double)0.05f;
    }

    public void chooseNextStep() {
        int oldPoint = this.currentPoint;
        if (this.currentRoute == null) {
            WalkerTemplate template;
            WalkManager.stopWalking((NpcAI2)((Npc)this.owner).getAi2());
            if (!WalkerFormator.processClusteredNpc((Npc)this.owner, ((Npc)this.owner).getWorldId(), ((Npc)this.owner).getInstanceId()) && (template = DataManager.WALKER_DATA.getWalkerTemplate(((Npc)this.owner).getSpawn().getWalkerId())) != null) {
                this.currentRoute = template.getRouteSteps();
            }
            if (this.currentRoute == null) {
                log.warn("Bad Walker Id: " + ((Npc)this.owner).getNpcId() + " - point: " + oldPoint);
                return;
            }
        }
        this.currentPoint = this.currentPoint < this.currentRoute.size() - 1 ? ++this.currentPoint : 0;
        this.setRouteStep(this.currentRoute.get(this.currentPoint), this.currentRoute.get(oldPoint));
    }

    public int getWalkPause() {
        return this.walkPause;
    }

    public boolean isChangingDirection() {
        return this.currentPoint == 0;
    }

    @Override
    public final float getTargetX2() {
        return this.started.get() ? this.targetDestX : ((Npc)this.owner).getX();
    }

    @Override
    public final float getTargetY2() {
        return this.started.get() ? this.targetDestY : ((Npc)this.owner).getY();
    }

    @Override
    public final float getTargetZ2() {
        return this.started.get() ? this.targetDestZ : ((Npc)this.owner).getZ();
    }

    public boolean isFollowingTarget() {
        return this.destination == Destination.TARGET_OBJECT;
    }

    public void storeStep() {
        if (((Npc)this.owner).getAi2().getState() == AIState.RETURNING) {
            return;
        }
        if (this.lastSteps == null) {
            this.lastSteps = new LastUsedCache(10);
        }
        Point3D currentStep = new Point3D(((Npc)this.owner).getX(), ((Npc)this.owner).getY(), ((Npc)this.owner).getZ());
        if (((Npc)this.owner).getAi2().isLogging()) {
            AI2Logger.moveinfo((Creature)this.owner, "store back step: X=" + ((Npc)this.owner).getX() + " Y=" + ((Npc)this.owner).getY() + " Z=" + ((Npc)this.owner).getZ());
        }
        if (this.stepSequenceNr == 0 || MathUtil.getDistance(this.lastSteps.get(this.stepSequenceNr), currentStep) >= 5.0) {
            this.stepSequenceNr = (byte)(this.stepSequenceNr + 1);
            this.lastSteps.put(this.stepSequenceNr, currentStep);
        }
    }

    public Point3D recallPreviousStep() {
        Point3D result =  stepSequenceNr == 0 ? null : lastSteps.get(stepSequenceNr--);
        Point3D point3D;
        if (this.lastSteps == null) {
            this.lastSteps = new LastUsedCache(10);
        }
        if (this.stepSequenceNr == 0) {
            point3D = null;
        } else {
            byte by = this.stepSequenceNr;
            this.stepSequenceNr = (byte)(by - 1);
            point3D = result = this.lastSteps.get(by);
        }
        if (result == null) {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "recall back step: spawn point");
            }
            this.targetDestX = ((Npc)this.owner).getSpawn().getX();
            this.targetDestY = ((Npc)this.owner).getSpawn().getY();
            this.targetDestZ = ((Npc)this.owner).getSpawn().getZ();
            result = new Point3D(this.targetDestX, this.targetDestY, this.targetDestZ);
        } else {
            if (((Npc)this.owner).getAi2().isLogging()) {
                AI2Logger.moveinfo((Creature)this.owner, "recall back step: X=" + result.getX() + " Y=" + result.getY() + " Z=" + result.getZ());
            }
            this.targetDestX = result.getX();
            this.targetDestY = result.getY();
            this.targetDestZ = result.getZ();
        }
        return result;
    }

    public void clearBackSteps() {
        this.stepSequenceNr = 0;
        this.lastSteps = null;
        this.movementMask = 0;
    }

    private static enum Destination {
        TARGET_OBJECT,
        POINT;

    }
	@Override
	public void skillMovement() {
		// TODO Auto-generated method stub
		
	}
}

