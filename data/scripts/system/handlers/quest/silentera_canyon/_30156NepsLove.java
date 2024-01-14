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
public class _30156NepsLove extends QuestHandler {

	private final static int questId = 30156;
	public _30156NepsLove() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799234).addOnQuestStart(questId); //Nep
		qe.registerQuestNpc(799234).addOnTalkEvent(questId); //Nep
		qe.registerQuestNpc(204304).addOnTalkEvent(questId); //Vili
		qe.registerQuestNpc(700570).addOnTalkEvent(questId); //Statue Sinigalla
		qe.registerQuestNpc(799339).addOnTalkEvent(questId); //Sinigalla
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if(qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799234) {
				if(dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (dialog == QuestDialog.ACCEPT_QUEST)
				{
					if (!giveQuestItem(env, 182209253, 1))
						return true;
					return sendQuestStartDialog(env);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204304) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 2375);
					case SELECT_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 700570) {
				switch (dialog) {
					case USE_OBJECT:
						if (var == 0) {
							QuestService.addNewSpawn(600110000, 1, 799339, (float) 545.308, (float) 1232.3855, (float) 304.35193, (byte) 73);
							return useQuestObject(env, 0, 0, false, 0, 0, 0, 182209223, 1);
						}
				}
			}
			if (targetId == 799339) {
				switch (dialog) {
					case START_DIALOG:
						if (var == 0)
							return sendQuestDialog(env, 1352);
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