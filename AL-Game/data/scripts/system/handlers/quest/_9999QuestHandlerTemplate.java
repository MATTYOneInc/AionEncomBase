/**
 *
 *  ADev Emulation 5.8 - Based On Encom Source.
 *  Reworked by MATTY
 *  Site <www.aionasteria.ru> - Forum <forum.aionasteria.ru>
 *
 */
 
package quest;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

public class _9999QuestHandlerTemplate extends QuestHandler {

	private static final int questId = 9999;

	public _9999QuestHandlerTemplate() {
		super(questId);
	}

	@Override
	public void register() {
		// register needed events here
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		// If this is a mission, the qs should be != null and you will not need this
		if (qs == null || qs.canRepeat()) {
			if (targetId == 000000) { // Viktor Logwin
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011); // can be different
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 111111: { // Oliver
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 4) {
								return sendQuestDialog(env, 1693);
							}
							else if (var == 5) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SELECT_ACTION_1353: {
							playQuestMovie(env, 0);
							return sendQuestDialog(env, 1353);
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1); // 1
						}
						case STEP_TO_2: {
							return defaultCloseDialog(env, 4, 5); // 5
						}
						case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 5, 6, true, 2375, 10001);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 000000) { // Viktor Logwin
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 20001);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 010101, 1, 3); // 1 - 3
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.isInsideZone(ZoneName.get("DF1A_ITEMUSEAREA_Q2016"))) { // example zone
				return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
			}
		}
		return HandlerResult.FAILED;
	}
}