package ai.instance.contaminedUnderpath;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author MATTY (ADev Team)
 */
 
@AIName("RecoveringEnergy")
public class RecoveringEnergyAI2 extends NpcAI2 {

    @Override
    protected void handleCreatureSee(Creature creature) {
        checkDistance(this, creature);
    }

    @Override
    protected void handleCreatureMoved(Creature creature) {
        checkDistance(this, creature);
    }

    protected void checkDistance(NpcAI2 ai, Creature creature) {
        Npc owner = ai.getOwner();
        if (creature == null || creature.getLifeStats() == null) {
            return;
        }
        if (creature.getLifeStats().isAlreadyDead()) {
            return;
        }
        if (!owner.canSee(creature)) {
            return;
        }
        if (!owner.getActiveRegion().isMapRegionActive()) {
            return;
        }
        if (!(creature instanceof Player)) {
            return;
        }
        if (MathUtil.isIn3dRange(owner, creature, 1.0f)) {
            if (GeoService.getInstance().canSee(owner, creature)) {
                AI2Actions.targetCreature(this, creature);
                AI2Actions.useSkill(this, 11171);
                AI2Actions.targetCreature(this, null);
            }
        }
    }

    @Override
    protected AIAnswer pollInstance(AIQuestion question) {
        switch (question) {
            case SHOULD_DECAY:
                return AIAnswers.NEGATIVE;
            case SHOULD_RESPAWN:
                return AIAnswers.NEGATIVE;
            case SHOULD_REWARD:
                return AIAnswers.NEGATIVE;
            default:
                return null;
        }
    }

}