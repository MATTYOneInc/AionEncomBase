package quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Ritsu
 * 
 */
public class _1345BearerOfBadNews extends QuestHandler {

	private final static int questId = 1345;

	public _1345BearerOfBadNews() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204006).addOnQuestStart(questId); //Demokritos
		qe.registerQuestNpc(204006).addOnTalkEvent(questId);
		qe.registerQuestNpc(203765).addOnTalkEvent(questId); //Kreon
		qe.registerQuestItem(182201320, questId); //Tarnished Ring
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START){
			if (player.isInsideZone(ZoneName.get("LC1_ITEMUSEAREA_Q1345"))) 
				return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, true, 0, 0, 0));
		}
		return HandlerResult.SUCCESS; // ??
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		QuestDialog dialog = env.getDialog();
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE){
			if (targetId == 204006){//Demokritos
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (dialog == QuestDialog.ACCEPT_QUEST) {
					if (player.getInventory().getItemCountByItemId(186000015) == 1)
					giveQuestItem(env, 182201320, 1);
					return sendQuestStartDialog(env);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START){
			int var = qs.getQuestVarById(0);
			if (targetId == 203765){//Kreon
				switch (dialog){
					case START_DIALOG:
						if (var == 0)
							removeQuestItem(env, 186000015, 1);
							return sendQuestDialog(env, 1352);
					case STEP_TO_1:
						if (var == 0)
							return defaultCloseDialog(env, 0, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD){
			if (targetId == 204006)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}
