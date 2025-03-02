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
package quest.theobomos_test_chamber;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18992Another_Fragment_Another_Fight extends QuestHandler {

    private final static int questId = 18992;
    public _18992Another_Fragment_Another_Fight() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806075).addOnQuestStart(questId); //Weatha.
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); //Weatha.
        qe.registerQuestNpc(806215).addOnTalkEvent(questId); //Stochio.
		qe.registerQuestNpc(220424).addOnKillEvent(questId);
		qe.registerQuestNpc(220425).addOnKillEvent(questId);
		qe.registerQuestNpc(220426).addOnKillEvent(questId);
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
            if (targetId == 806215) { //Stochio.
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
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        if (qs.getStatus() != QuestStatus.START) {
            return false;
        } if (var == 1) {
			if (targetId == 220424) { //í”¼ì?˜ ê³„ì•½ì?„ ë§ºì?€ ì•„ë?¼í?¬ë„¤.
				qs.setQuestVarById(1, 1);
			} else if (targetId == 220425) { //í”¼ì?˜ ê³„ì•½ì?„ ë§ºì?€ ê°ˆë?¼í…Œì?´ì•„.
				qs.setQuestVarById(2, 1);
			}
			updateQuestStatus(env);
			if (qs.getQuestVarById(1) == 1 && qs.getQuestVarById(2) == 1) {
				changeQuestStep(env, 1, 2, false);
			}
		} else if (var == 2) {
            if (targetId == 220426) { //ì•„í‹°íŒ©íŠ¸ë¥¼ ì§€ë°°í•˜ëŠ” ì›?ì†Œ êµ°ì£¼.
                qs.setStatus(QuestStatus.REWARD);
				changeQuestStep(env, 2, 3, false);
				updateQuestStatus(env);
            }
        }
        return false;
    }
}