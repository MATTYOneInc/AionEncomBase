/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.aionemu.gameserver.controllers.movement;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.handler.TargetEventHandler;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.dataholders.DataManager;
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
import com.aionemu.gameserver.world.geo.nav.NavService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eleanor.Global;
import com.eleanor.processors.movement.motor.FollowMotor;

public class NpcMoveController
        extends CreatureMoveController<Npc> {
    private static final Logger log = LoggerFactory.getLogger(NpcMoveController.class);
    public static final float MOVE_CHECK_OFFSET = 0.1f;
    private static final float MOVE_OFFSET = 0.05f;
    private int returnAttempts;
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
    private boolean cachedPathValid;
    private float[][] cachedPath;
    private FollowMotor _followMotor;

    public NpcMoveController(Npc owner) {
        super(owner);
    }
    private static enum Destination {
        TARGET_OBJECT,
        POINT,
        HOME,
    }
    private void applyFollow(VisibleObject target) {
        if ((this._followMotor != null && this._followMotor._target == target)) {
            return;
        }
        if (this._followMotor != null) {
            this._followMotor.stop();
        }
        this._followMotor = new FollowMotor(Global.MovementProcessor, (Npc)this.owner, target);
        this._followMotor.start();
    }
    private void cancelFollow() {
        if ((this._followMotor != null)) {
            this._followMotor.stop();
            this._followMotor = null;
        }
    }

    public void moveToTargetObject() {
        if (started.compareAndSet(false, true)) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "MC: moveToTarget started");
            }
            destination = Destination.TARGET_OBJECT;
            updateLastMove();
            MoveTaskManager.getInstance().addCreature(owner);
        }
    }

    public void moveToPoint(float x, float y, float z) {
        if (started.compareAndSet(false, true)) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "MC: moveToPoint started");
            }
            destination = Destination.POINT;
            pointX = x;
            pointY = y;
            pointZ = z;
            updateLastMove();
            MoveTaskManager.getInstance().addCreature(owner);
        }
    }


    public void moveToHome() {
        if (started.compareAndSet(false, true)) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "MC: moveToHome started");
            }
            cachedPathValid = false;
            float x = owner.getSpawn().getX(), y = owner.getSpawn().getY(), z = owner.getSpawn().getZ();
            destination = Destination.HOME;
            pointX = x;
            pointY = y;
            pointZ = z;
            updateLastMove();
            MoveTaskManager.getInstance().addCreature(owner);
        }
    }

    public void moveToNextPoint() {
        if (started.compareAndSet(false, true)) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "MC: moveToNextPoint started");
            }
            destination = Destination.POINT;
            updateLastMove();
            MoveTaskManager.getInstance().addCreature(owner);
        }
    }

    @Override
    public void moveToDestination() {
        if (owner.getAi2().isLogging()) {
            AI2Logger.moveinfo(owner, "moveToDestination destination: " + destination);
        }
        if (NpcActions.isAlreadyDead(owner)) {
            abortMove();
            return;
        }
        if (!owner.canPerformMove() || (owner.getAi2().getSubState() == AISubState.CAST)) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "moveToDestination can't perform move");
            }
            if (started.compareAndSet(true, false)) {
                cancelFollow();
                setAndSendStopMove(owner);
            }
            updateLastMove();
            return;
        } else if (started.compareAndSet(false, true)) {
            movementMask = MovementMask.NPC_STARTMOVE;
            PacketSendUtility.broadcastPacket(owner, new SM_MOVE(owner));
        }

        if (!started.get()) {
            if (owner.getAi2().isLogging()) {
                AI2Logger.moveinfo(owner, "moveToDestination not started");
            }
        }
        switch (destination) {
            case TARGET_OBJECT:
                if (GeoDataConfig.GEO_NAV_ENABLE) {
                    returnAttempts = 0;
                    VisibleObject target = owner.getTarget();// todo no target
                    if (target == null) { //This check is not needed, but I'll leave it for clarity.
                        return;
                    }
                    if (!(target instanceof Creature)) { //instanceof returns false if target is null.
                        return;
                    }
                    if ((MathUtil.getDistance(target.getX(), target.getY(), pointZ, pointX, pointY, pointZ) > MOVE_CHECK_OFFSET)) {
                        Creature creature = (Creature) target;
                        offset = owner.getController().getAttackDistanceToTarget();
                        pointX = target.getX();
                        pointY = target.getY();
                        pointZ = getTargetZ(owner, creature);
                        cachedPathValid = false;
                    }
                    if (!cachedPathValid || cachedPath == null) {
                        cachedPath = NavService.getInstance().navigateToTarget(owner, (Creature) target);
                        if (cachedPath != null) { //Add a bit of randomness to the last point to prevent entities from stacking directly ontop of eachother.
                            //TODO: Move to NavService and make sure this random point is on the navmesh!
                            if (cachedPath.length != 1) {
                                if (Rnd.nextBoolean()) {
                                    cachedPath[cachedPath.length - 1][0] += Rnd.nextDouble() * owner.getObjectTemplate().getBoundRadius().getSide();
                                } else {
                                    cachedPath[cachedPath.length - 1][0] -= Rnd.nextDouble() * owner.getObjectTemplate().getBoundRadius().getSide();
                                }
                                if (Rnd.nextBoolean()) {
                                    cachedPath[cachedPath.length - 1][1] += Rnd.nextDouble() * owner.getObjectTemplate().getBoundRadius().getSide();
                                } else {
                                    cachedPath[cachedPath.length - 1][1] -= Rnd.nextDouble() * owner.getObjectTemplate().getBoundRadius().getSide();
                                }
                            }
                        }
                        cachedPathValid = true;
                    }
                    if (cachedPath != null && cachedPath.length > 0) {
                        float[] p1 = cachedPath[0];
                        assert p1.length == 3;
                        moveToLocation(p1[0], p1[1], getTargetZ(owner, p1[0], p1[1], p1[2]), offset);
                    } else {
                        if (cachedPath != null) cachedPath = null;
                        moveToLocation(pointX, pointY, pointZ, offset);
                    }
                } else {
                    Npc npc = owner;
                    VisibleObject target = owner.getTarget();
                    if (target == null) {
                        cancelFollow();
                        return;
                    }
                    if (!(target instanceof Creature)) {
                        cancelFollow();
                        return;
                    }
                    if (owner.getAi2().getState() == AIState.FOLLOWING) {
                        cancelFollow();
                        offset = npc.getController().getAttackDistanceToTarget();
                        moveToLocation(target.getX(), target.getY(), target.getZ(), offset);
                        break;
                    }
                    applyFollow(target);
                }
                break;
            case POINT: {
                cancelFollow();
                moveToLocation(pointX, pointY, pointZ, offset);
                break;
            }
            case HOME: {
                if ((!cachedPathValid || cachedPath == null) && (returnAttempts<3)) {
                    cachedPath = NavService.getInstance().navigateToLocation(owner, pointX, pointY, pointZ);
                    returnAttempts++;
                    cachedPathValid = true;
                }
                if ((cachedPath != null) && (cachedPath.length > 0) && (returnAttempts<3)) {
                    float[] p1 = cachedPath[0];
                    moveToLocation(p1[0], p1[1], getTargetZ(owner, p1[0], p1[1], p1[2]), offset);
                } else{
                    moveToLocation(pointX, pointY, pointZ, offset);
                }
            }
        }
        this.updateLastMove();
    }

    private float getTargetZ(Npc npc, Creature creature) {
        float targetZ = creature.getZ();
        if (GeoDataConfig.GEO_NPC_MOVE && creature.isInFlyingState() && !npc.isInFlyingState()) {
//            if (npc.getGameStats().checkGeoNeedUpdate()) {
            this.cachedTargetZ = GeoService.getInstance().getZ(creature);
 //           }
            targetZ = this.cachedTargetZ;
        }
        return targetZ;
    }
    private float getTargetZ(Npc npc, float x, float y, float z) {
        float targetZ = z;
        if (GeoDataConfig.GEO_NPC_MOVE && !npc.isFlying()) {
//            if (npc.getGameStats().checkGeoNeedUpdate()) {
            cachedTargetZ = GeoService.getInstance().getZ(npc.getWorldId(), x, y, z, 1.1F, npc.getInstanceId());
            targetZ = cachedTargetZ;
//            }
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
        if (futureDistPassed == dist
                && (destination == Destination.TARGET_OBJECT || destination == Destination.HOME)) {
            if (cachedPath != null && cachedPath.length > 0) {
                float[][] tempCache = new float[cachedPath.length - 1][];
                if (tempCache.length > 0) {
                    System.arraycopy(cachedPath, 1, tempCache, 0, cachedPath.length - 1);
                    cachedPath = tempCache;
                } else {
                    cachedPath = null;
                    cachedPathValid = false;
                }
            }
        }
        float distFraction = futureDistPassed / dist;
        float newX = (this.targetDestX - ownerX) * distFraction + ownerX;
        float newY = (this.targetDestY - ownerY) * distFraction + ownerY;
        float newZ = (this.targetDestZ - ownerZ) * distFraction + ownerZ;
        if (ownerX == newX && ownerY == newY && ((Npc)this.owner).getSpawn().getRandomWalk() > 0) {
            return;
        }
        if (GeoDataConfig.GEO_NPC_MOVE && GeoDataConfig.GEO_ENABLE && owner.getAi2().getSubState() != AISubState.WALK_PATH && owner.getAi2().getState() != AIState.RETURNING
                && owner.getGameStats().checkGeoNeedUpdate()) {
            if (owner.getSpawn().getX() != targetDestX || owner.getSpawn().getY() != targetDestY || owner.getSpawn().getZ() != targetDestZ) {
                float geoZ = GeoService.getInstance().getZ(owner.getWorldId(), newX, newY, newZ, 0, owner.getInstanceId());
                if (Math.abs(newZ - geoZ) > 1) {
                    directionChanged = true;
                }
                newZ = geoZ ;
            }
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
            return MovementMask.NPC_STARTMOVE;
        }
        if (((Npc)this.owner).getAi2().getState() == AIState.RETURNING) {
            return MovementMask.NPC_RUN_FAST;
        }
        if (((Npc)this.owner).getAi2().getState() == AIState.FOLLOWING) {
            return MovementMask.NPC_WALK_SLOW;
        }
        byte mask = MovementMask.IMMEDIATE;
        Stat2 stat = ((Npc)this.owner).getGameStats().getMovementSpeed();
        if (((Npc)this.owner).isInState(CreatureState.WEAPON_EQUIPPED)) {
            mask = stat.getBonus() < 0 ? MovementMask.NPC_RUN_FAST : MovementMask.NPC_RUN_SLOW;
        } else if (((Npc)this.owner).isInState(CreatureState.WALKING) || ((Npc)this.owner).isInState(CreatureState.ACTIVE)) {
            byte by = mask = stat.getBonus() < 0 ? MovementMask.NPC_WALK_FAST : MovementMask.NPC_WALK_SLOW;
        }
        if (((Npc)this.owner).isFlying()) {
            mask |= MovementMask.GLIDE;
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
        if (owner.getAi2().isLogging()) {
            AI2Logger.moveinfo(owner, "MC perform stop");
        }
        cancelFollow();
        started.set(false);
        targetDestX = 0;
        targetDestY = 0;
        targetDestZ = 0;
        pointX = 0;
        pointY = 0;
        pointZ = 0;
    }

    public void setCurrentRoute(List<RouteStep> currentRoute) {
        if (currentRoute == null) {
            AI2Logger.info(owner.getAi2(), String.format("MC: setCurrentRoute is setting route to null (NPC id: {})!!!", owner.getNpcId()));
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

    @Override
    public void skillMovement() {
        // TODO Auto-generated method stub
    }
}