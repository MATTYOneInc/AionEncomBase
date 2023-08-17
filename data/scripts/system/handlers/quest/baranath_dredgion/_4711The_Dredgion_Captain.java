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

public class _4711The_Dredgion_Captain extends QuestHandler
{
	private final static int questId = 4711;
	
	public _4711The_Dredgion_Captain() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(278001).addOnQuestStart(questId);
		qe.registerQuestNpc(278001).addOnTalkEvent(questId);
		qe.registerQuestNpc(279042).addOnTalkEvent(questId);
		qe.registerQuestNpc(730196).addOnTalkEvent(questId);
		qe.registerQuestNpc(214823).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 278001) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 279042) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1011);
					}
				} else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				}
			} else if (targetId == 730196) {
				if (dialog == QuestDialog.START_DIALOG) {
					if(qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1352);
					}
				} else if (dialog == QuestDialog.STEP_TO_2) {
					return defaultCloseDialog(env, 1, 2);
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 278001) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 1693);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 214823, 2, true);
	}
}