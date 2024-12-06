package ai.instance.nochsanaTrainingCamp;

import ai.GeneralNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

@AIName("nochsanadoor")
public class NochsanaDoorAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		getOwner().getController().onDelete();
	}
}
