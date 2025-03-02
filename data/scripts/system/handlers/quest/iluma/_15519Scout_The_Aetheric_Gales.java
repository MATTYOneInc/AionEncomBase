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
package quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _15519Scout_The_Aetheric_Gales extends QuestHandler {

    private final static int questId = 15519;
    public _15519Scout_The_Aetheric_Gales() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806095).addOnQuestStart(questId);
        qe.registerQuestNpc(806095).addOnTalkEvent(questId);
		qe.registerQuestNpc(241580).addOnKillEvent(questId);
		qe.registerQuestNpc(241581).addOnKillEvent(questId);
		qe.registerQuestNpc(241582).addOnKillEvent(questId);
		qe.registerQuestNpc(241583).addOnKillEvent(questId);
		qe.registerQuestNpc(241584).addOnKillEvent(questId);
		qe.registerQuestNpc(241585).addOnKillEvent(questId);
		qe.registerQuestNpc(241212).addOnKillEvent(questId);
		qe.registerQuestNpc(243380).addOnKillEvent(questId);
		qe.registerQuestNpc(243381).addOnKillEvent(questId);
		qe.registerQuestNpc(243382).addOnKillEvent(questId);
		qe.registerQuestNpc(243383).addOnKillEvent(questId);
		qe.registerQuestNpc(243384).addOnKillEvent(questId);
		qe.registerQuestNpc(243385).addOnKillEvent(questId);
		qe.registerQuestNpc(243386).addOnKillEvent(questId);
		qe.registerQuestNpc(243387).addOnKillEvent(questId);
		qe.registerQuestNpc(243388).addOnKillEvent(questId);
		qe.registerQuestNpc(242087).addOnKillEvent(questId);
		qe.registerQuestNpc(242091).addOnKillEvent(questId);
		qe.registerQuestNpc(242095).addOnKillEvent(questId);
		qe.registerQuestNpc(242099).addOnKillEvent(questId);
		qe.registerQuestNpc(242103).addOnKillEvent(questId);
		qe.registerQuestNpc(242107).addOnKillEvent(questId);
		qe.registerQuestNpc(242111).addOnKillEvent(questId);
		qe.registerQuestNpc(242115).addOnKillEvent(questId);
		qe.registerQuestNpc(242119).addOnKillEvent(questId);
		qe.registerQuestNpc(242123).addOnKillEvent(questId);
		qe.registerQuestNpc(242127).addOnKillEvent(questId);
		qe.registerQuestNpc(242131).addOnKillEvent(questId);
		qe.registerQuestNpc(242135).addOnKillEvent(questId);
		qe.registerQuestNpc(242139).addOnKillEvent(questId);
		qe.registerQuestNpc(242143).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806095) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806095) {
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
				case 241580:
				case 241581:
				case 241582:
				case 241583:
				case 241584:
				case 241585:
				case 241212:
				case 243380:
				case 243381:
				case 243382:
				case 243383:
				case 243384:
				case 243385:
				case 243386:
				case 243387:
				case 243388:
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