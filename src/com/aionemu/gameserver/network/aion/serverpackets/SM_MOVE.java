/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.controllers.movement.MoveController;
import com.aionemu.gameserver.controllers.movement.PlayableMoveController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_MOVE
extends AionServerPacket {
    private final Creature creature;
    private int _objectId;
    private float _sX;
    private float _sY;
    private float _sZ;
    private float _tX;
    private float _tY;
    private float _tZ;
    private byte _heading;
    private byte _moveTypeFlag;

    public SM_MOVE(Creature creature) {
        this.creature = creature;
    }

    public SM_MOVE(int objectId, float sX, float sY, float sZ, float tX, float tY, float tZ, byte heading, byte flag) {
        this.creature = null;
        this._objectId = objectId;
        this._sX = sX;
        this._sY = sY;
        this._sZ = sZ;
        this._tX = tX;
        this._tY = tY;
        this._tZ = tZ;
        this._heading = heading;
        this._moveTypeFlag = flag;
    }

    @Override
    protected void writeImpl(AionConnection client) {
        if (this.creature == null) {
            this.writeD(this._objectId);
            this.writeF(this._sX);
            this.writeF(this._sY);
            this.writeF(this._sZ);
            this.writeC(this._heading);
            this.writeC(this._moveTypeFlag);
            if ((this._moveTypeFlag & 0xFFFFFFC0) == -64 || (this._moveTypeFlag & 0xFFFFFFE0) == -32) {
                this.writeF(this._tX);
                this.writeF(this._tY);
                this.writeF(this._tZ);
            }
        } else {
            MoveController moveData = this.creature.getMoveController();
            this.writeD(this.creature.getObjectId());
            this.writeF(this.creature.getX());
            this.writeF(this.creature.getY());
            this.writeF(this.creature.getZ());
            this.writeC(this.creature.getHeading());
            this.writeC(moveData.getMovementMask());
            if (moveData instanceof PlayableMoveController) {
                PlayableMoveController playermoveData = (PlayableMoveController)moveData;
                if ((moveData.getMovementMask() & 0xFFFFFFC0) == -64) {
                    if ((moveData.getMovementMask() & 0x20) == 0) {
                        this.writeF(playermoveData.vectorX);
                        this.writeF(playermoveData.vectorY);
                        this.writeF(playermoveData.vectorZ);
                    } else {
                        this.writeF(moveData.getTargetX2());
                        this.writeF(moveData.getTargetY2());
                        this.writeF(moveData.getTargetZ2());
                    }
                }
                if ((moveData.getMovementMask() & 4) == 4) {
                    this.writeC(playermoveData.glideFlag);
                }
                if ((moveData.getMovementMask() & 0x10) == 16) {
                    this.writeD(playermoveData.unk1);
                    this.writeD(playermoveData.unk2);
                    this.writeF(playermoveData.vectorX);
                    this.writeF(playermoveData.vectorY);
                    this.writeF(playermoveData.vectorZ);
                }
            } else if ((moveData.getMovementMask() & 0xFFFFFFC0) == -64) {
                this.writeF(moveData.getTargetX2());
                this.writeF(moveData.getTargetY2());
                this.writeF(moveData.getTargetZ2());
            }
        }
    }
}

