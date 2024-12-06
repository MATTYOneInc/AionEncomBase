/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.norsvold;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25640Mysterious_Organisms_In_Norsvold extends QuestHandler
{
    private final static int questId = 25640;
	
	private final static int[] Q25640 = {
		241947, 241951, 241955, 241959, 241963, 241967, 241971, 241975, 241979, 241983,
		241987, 241991, 241995, 241999, 242003, 242087, 242091, 242095, 242099, 242103,
		242107, 242111, 242115, 242119, 242123, 242127, 242131, 242135, 242139, 242143,
		242287, 242291, 242295, 242299, 242303, 242307, 242311, 242315, 242319, 242323,
		242327, 242331, 242335, 242339, 242343, 242487, 242491, 242495, 242499, 242503,
		242507, 242511, 242515, 242519, 242523, 242527, 242531, 242535, 242539, 242543};
	
    public _25640Mysterious_Organisms_In_Norsvold() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806101).addOnQuestStart(questId); //Vadorei.
        qe.registerQuestNpc(806101).addOnTalkEvent(questId); //Vadorei.
		for (int mob: Q25640) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806101) { //Vadorei.
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806101) { //Vadorei.
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 30) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 30, 31, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806101) { //Vadorei.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
				case 241947:
				case 241951:
				case 241955:
				case 241959:
				case 241963:
				case 241967:
				case 241971:
				case 241975:
				case 241979:
				case 241983:
				case 241987:
				case 241991:
				case 241995:
				case 241999:
				case 242003:
				case 242087:
				case 242091:
				case 242095:
				case 242099:
				case 242103:
				case 242107:
				case 242111:
				case 242115:
				case 242119:
				case 242123:
				case 242127:
				case 242131:
				case 242135:
				case 242139:
				case 242143:
				case 242287:
				case 242291:
				case 242295:
				case 242299:
				case 242303:
				case 242307:
				case 242311:
				case 242315:
				case 242319:
				case 242323:
				case 242327:
				case 242331:
				case 242335:
				case 242339:
				case 242343:
				case 242487:
				case 242491:
				case 242495:
				case 242499:
				case 242503:
				case 242507:
				case 242511:
				case 242515:
				case 242519:
				case 242523:
				case 242527:
				case 242531:
				case 242535:
				case 242539:
				case 242543:
                if (qs.getQuestVarById(1) < 30) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 30) {
					qs.setQuestVarById(0, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}