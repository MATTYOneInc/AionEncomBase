package quest.silentera_canyon;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Ritsu
 *
 */
public class _30056DirvisiasSorrow extends QuestHandler  {

	private final static int questId = 30056;
	public _30056DirvisiasSorrow() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798929).addOnQuestStart(questId); //Gellius
		qe.registerQuestNpc(798929).addOnTalkEvent(questId); //Gellius
		qe.registerQuestNpc(203901).addOnTalkEvent(questId); //Telemachus
		qe.registerQuestNpc(700569).addOnTalkEvent(questId); //Statue Dirvisia
		qe.registerQuestNpc(799034).addOnTalkEvent(questId); //Dirvisia
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if(qs == null || qs.getStatus() == QuestStatus.NONE) {
			if(targetId == 798929) {
				if(dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (dialog == QuestDialog.ACCEPT_QUEST) {
					if (!giveQuestItem(env, 182209223, 1))
						return true;
					return sendQuestStartDialog(env);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203901) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 2375);
					case SELECT_REWARD:
						removeQuestItem(env, 182209224, 1);
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 700569) {
				switch (dialog) {
					case USE_OBJECT:
						if (var == 0) {
							QuestService.addNewSpawn(600110000, 1, 799034, 555.8842f, 307.8092f, 310.24997f, (byte) 0);
							return useQuestObject(env, 0, 0, false, 0, 0, 0, 182209223, 1);
						}
				}
			}
			if (targetId == 799034) {
				switch (dialog) {
					case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					case STEP_TO_1:
						if (var == 0) {
							defaultCloseDialog(env, 0, 0, true, false);
							final Npc npc = (Npc)env.getVisibleObject();
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									npc.getController().onDelete();	
								}
							}, 400);	
							return true;
						}
				}
			}
		}
		return false;
	}
}