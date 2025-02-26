/**
 *
 *  ADev Emulation 5.8 - Based On Encom Source.
 *  Reworked by MATTY
 *  Site <www.aionasteria.ru> - Forum <forum.aionasteria.ru>
 *
 */
 
package quest.baranath_dredgion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _3718Dredging_The_Dredgion extends QuestHandler {

	private final static int questId = 3718;
	public _3718Dredging_The_Dredgion() {
		super(questId);
	}

	public void register() {
		qe.registerOnDredgionReward(questId);
		qe.registerQuestNpc(279045).addOnQuestStart(questId);
		qe.registerQuestNpc(279045).addOnTalkEvent(questId);
		qe.registerQuestNpc(214814).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
        if (qs == null) {
            return false;
        }
		int var1 = qs.getQuestVarById(1);
		int var2 = qs.getQuestVarById(2);
		if (qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 279045) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 279045) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1011);
					} else if(var1 == 3 && var2 == 8) {
						return sendQuestDialog(env, 10002);
					}
				} else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				} else if (dialog == QuestDialog.SELECT_REWARD) {
					return defaultCloseDialog(env, 1, 1, true, true);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 279045) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 214814, 0, 8, 2);
	}
	
	@Override
	public boolean onDredgionRewardEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var1 = qs.getQuestVarById(1);
			if (var1 < 3) {
				changeQuestStep(env, var1, var1 + 1, false, 1); 
				return true;
			}
		}
		return false;
	}
}