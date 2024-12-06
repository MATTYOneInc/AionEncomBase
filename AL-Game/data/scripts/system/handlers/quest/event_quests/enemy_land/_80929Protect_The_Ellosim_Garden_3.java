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
package quest.event_quests.enemy_land;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _80929Protect_The_Ellosim_Garden_3 extends QuestHandler
{
    private final static int questId = 80929;
	
    public _80929Protect_The_Ellosim_Garden_3() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(835068).addOnQuestStart(questId);
        qe.registerQuestNpc(835068).addOnTalkEvent(questId);
		qe.registerQuestNpc(241668).addOnKillEvent(questId);
		qe.registerQuestNpc(241669).addOnKillEvent(questId);
		qe.registerQuestNpc(241670).addOnKillEvent(questId);
		qe.registerQuestNpc(241671).addOnKillEvent(questId);
		qe.registerQuestNpc(241672).addOnKillEvent(questId);
		qe.registerQuestNpc(241673).addOnKillEvent(questId);
		qe.registerQuestNpc(241674).addOnKillEvent(questId);
		qe.registerQuestNpc(241675).addOnKillEvent(questId);
		qe.registerQuestNpc(241676).addOnKillEvent(questId);
		qe.registerQuestNpc(241677).addOnKillEvent(questId);
		qe.registerQuestNpc(241678).addOnKillEvent(questId);
		qe.registerQuestNpc(241679).addOnKillEvent(questId);
		qe.registerQuestNpc(243285).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 835068) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 835068) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 27) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 27, 28, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 835068) {
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
                case 241668:
				case 241669:
				case 241670:
				case 241671:
				case 241672:
				case 241673:
				case 241674:
				case 241675:
				case 241676:
				case 241677:
				case 241678:
				case 241679:
				case 243285:
                if (qs.getQuestVarById(1) < 27) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 27) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}