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
package quest.harbinger_landing;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25407Thwart_The_Beritra_Legion extends QuestHandler
{
    private final static int questId = 25407;
	
    public _25407Thwart_The_Beritra_Legion() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(805401).addOnQuestStart(questId);
        qe.registerQuestNpc(805401).addOnTalkEvent(questId);
		qe.registerQuestNpc(883019).addOnKillEvent(questId);
		qe.registerQuestNpc(883025).addOnKillEvent(questId);
		qe.registerQuestNpc(883031).addOnKillEvent(questId);
		qe.registerQuestNpc(883037).addOnKillEvent(questId);
		qe.registerQuestNpc(883043).addOnKillEvent(questId);
		qe.registerQuestNpc(883049).addOnKillEvent(questId);
		qe.registerQuestNpc(883021).addOnKillEvent(questId);
		qe.registerQuestNpc(883027).addOnKillEvent(questId);
		qe.registerQuestNpc(883033).addOnKillEvent(questId);
		qe.registerQuestNpc(883039).addOnKillEvent(questId);
		qe.registerQuestNpc(883045).addOnKillEvent(questId);
		qe.registerQuestNpc(883051).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 805401) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 805401) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 4) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 4, 5, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805401) {
				if (env.getDialogId() == 1352) {
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
                case 883019:
				case 883025:
				case 883031:
				case 883037:
				case 883043:
				case 883049:
				case 883021:
				case 883027:
				case 883033:
				case 883039:
				case 883045:
				case 883051:
                if (qs.getQuestVarById(1) < 4) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 4) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}