package quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author gigi
 */
public class _2654SpyTheLastPersuasion extends QuestHandler {

	private final static int questId = 2654;

	public _2654SpyTheLastPersuasion() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204775).addOnQuestStart(questId);
		qe.registerQuestNpc(204775).addOnTalkEvent(questId);
		qe.registerQuestNpc(204655).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2653);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204775) {
			if (qs == null) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 204655) {
			if (qs != null) {
				if (env.getDialog() == QuestDialog.START_DIALOG && qs.getStatus() == QuestStatus.START)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009 && qs.getStatus() != QuestStatus.COMPLETE
					&& qs.getStatus() != QuestStatus.NONE) {
					qs.setQuestVar(0);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
