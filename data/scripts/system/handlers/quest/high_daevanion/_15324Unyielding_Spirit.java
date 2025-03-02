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
package quest.high_daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15324Unyielding_Spirit extends QuestHandler {

	private static final int questId = 15324;
	private final static int[] Cygnea = {235939, 235940, 235941, 235942, 235943, 235944, 235945, 235946, 235947};
	private final static int[] Levinshor = {233929, 233930, 233931, 233932, 233933, 233934, 233935, 233936};
	private final static int[] Kaldor = {234277, 234278, 234279, 234280, 234524, 234526, 234527, 234528, 234531, 234532};
    public _15324Unyielding_Spirit() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(805331).addOnQuestStart(questId); //Machina.
        qe.registerQuestNpc(805331).addOnTalkEvent(questId); //Machina.
		for (int mob: Cygnea) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: Levinshor) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: Kaldor) {
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
            if (targetId == 805331) { //Machina.
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805331) { //Machina.
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
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
        if (var == 0 && var1 >= 0 && var1 < 19) {
			return defaultOnKillEvent(env, Cygnea, var1, var1 + 1, 1);
		} else if (var == 0 && var1 == 19) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 0, 1, false);
			updateQuestStatus(env);
			return true;
		} if (var == 1 && var1 >= 0 && var1 < 19) {
			return defaultOnKillEvent(env, Levinshor, var1, var1 + 1, 1);
		} else if (var == 1 && var1 == 19) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		} if (var == 2 && var1 >= 0 && var1 < 19) {
			return defaultOnKillEvent(env, Kaldor, var1, var1 + 1, 1);
		} else if (var == 2 && var1 == 19) {
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(3);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}