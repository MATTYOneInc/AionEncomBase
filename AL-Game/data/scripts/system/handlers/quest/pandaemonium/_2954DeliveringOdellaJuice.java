package quest.pandaemonium;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Altaress
 * @reworked vlog
 */
public class _2954DeliveringOdellaJuice extends QuestHandler {

	private final static int questId = 2954;

	public _2954DeliveringOdellaJuice() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204191).addOnQuestStart(questId);
		qe.registerQuestNpc(204191).addOnTalkEvent(questId);
		qe.registerQuestNpc(204221).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 204191) { // Doman
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 182207040, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204221) { // Haven
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				}
				else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1); // 1
				}
			}
			else if (targetId == 204191) { // Doman
				if (dialog == QuestDialog.START_DIALOG) {
					if (var == 1) {
						return sendQuestDialog(env, 2375);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD) {
					changeQuestStep(env, 1, 1, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204191) { // Doman
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
