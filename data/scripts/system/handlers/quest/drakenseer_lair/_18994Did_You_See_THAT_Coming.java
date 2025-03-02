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
package quest.drakenseer_lair;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18994Did_You_See_THAT_Coming extends QuestHandler {

    private final static int questId = 18994;
	private final static int[] IDF6DragonArtifactBoost = {857974,857975, 857976}; //ìš©ì¡± ì°¨ì›?ë¬¸ ì¦?í?­ ì¥ì¹˜.
    public _18994Did_You_See_THAT_Coming() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806075).addOnQuestStart(questId); //Weatha.
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); //Weatha.
        qe.registerQuestNpc(806241).addOnTalkEvent(questId); //Riseon.
		for (int mob: IDF6DragonArtifactBoost) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerQuestNpc(220450).addOnKillEvent(questId); //Drakenseer Akhal.
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806075) { //Weatha.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 806241) { //Riseon.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
                } if (env.getDialog() == QuestDialog.STEP_TO_1) {
					qs.setQuestVarById(0, 1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
                }
            }
        }
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806075) { //Weatha.
				if (env.getDialogId() == 1352) {
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
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 1) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 2) {
                    return defaultOnKillEvent(env, IDF6DragonArtifactBoost, var1, var1 + 1, 1);
                } else if (var1 == 2) {
					qs.setQuestVar(2);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 2) {
				switch (targetId) {
                    case 220450: { //Drakenseer Akhal.
						changeQuestStep(env, 2, 3, false);
						qs.setStatus(QuestStatus.REWARD);
					    updateQuestStatus(env);
						return true;
					}
                }
			}
        }
        return false;
    }
}