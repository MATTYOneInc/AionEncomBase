package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Balthazar
 */
public class _1648UndeadWarAlert extends QuestHandler {

	private final static int questId = 1648;
	public _1648UndeadWarAlert() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204545).addOnQuestStart(questId);
		qe.registerQuestNpc(204545).addOnTalkEvent(questId);
		qe.registerQuestNpc(204612).addOnTalkEvent(questId);
		qe.registerQuestNpc(204500).addOnTalkEvent(questId);
		qe.registerQuestNpc(204590).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204545) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204612: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 0) {
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_1: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
					}
				}
				case 204500: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 1) {
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_2: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204590) {
				if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}