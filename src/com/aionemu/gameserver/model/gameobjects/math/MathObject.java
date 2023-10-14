/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.model.gameobjects.math;

import com.aionemu.gameserver.controllers.MathController;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.CreatureAwareKnownList;
import com.aionemu.gameserver.world.knownlist.NpcKnownList;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

public class MathObject
extends VisibleObject {
    private double minRange;
    private double maxRange;
    private int skillId;
    private int npcId;
    private Npc master;
    private MathObjectType type;
    private MathObjectReaction reaction = MathObjectReaction.PC;
    private int duration;

    public MathObject(SpawnTemplate spawnTemplate, MathObjectType type, MathObjectReaction reaction, double minRange, double maxRange) {
        super(IDFactory.getInstance().nextId(), new MathController(), spawnTemplate, null, new WorldPosition(spawnTemplate.getWorldId()));
        this.type = type;
        this.reaction = reaction;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.getController().setOwner(this);
        switch (this.reaction) {
            case PC: {
                this.setKnownlist(new PlayerAwareKnownList(this));
                break;
            }
            case NPC: {
                this.setKnownlist(new NpcKnownList(this));
                break;
            }
            case ALL: {
                this.setKnownlist(new CreatureAwareKnownList(this));
            }
        }
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public MathController getController() {
        return (MathController)super.getController();
    }

    public double getMinRange() {
        return this.minRange;
    }

    public double getMaxRange() {
        return this.maxRange;
    }

    public int getSkillId() {
        return this.skillId;
    }

    public int getNpcId() {
        return this.npcId;
    }

    public Npc getMaster() {
        return this.master;
    }

    public void setMaster(Npc master) {
        this.master = master;
    }

    public MathObjectType getType() {
        return this.type;
    }

    public MathObjectReaction getReaction() {
        return this.reaction;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public float getVisibilityDistance() {
        return (float)(this.getMaxRange() + 5.0);
    }

    @Override
    public float getMaxZVisibleDistance() {
        return (float)(this.getMaxRange() + 5.0);
    }

    @Override
    public String getName() {
        return "Geometric Object";
    }
}

