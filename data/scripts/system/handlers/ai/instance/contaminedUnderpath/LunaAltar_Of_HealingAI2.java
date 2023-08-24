package ai.instance.contaminedUnderpath;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.CreatureEventHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

/****/
/** Author: Rinzler (Encom)
/** Rework: MATTY (ADev Team)
/****/

@AIName("LunaAltar_Of_Healing")
public class LunaAltar_Of_HealingAI2 extends AggressiveNpcAI2
{
	@Override
    protected void handleCreatureMoved(Creature creature) {
        CreatureEventHandler.onCreatureSee(this, creature);
    	if (creature instanceof Player) {
			final Player player = (Player) creature;
    		if (!creature.getEffectController().hasAbnormalEffect(17560)) { // Использование скилла Bless of Guardian Spring
    		    SkillEngine.getInstance().getSkill(getOwner(), 17560, 1, (Player) creature).useNoAnimationSkill(); // Использование скилла Bless of Guardian Spring
			}
    	}
    }
}