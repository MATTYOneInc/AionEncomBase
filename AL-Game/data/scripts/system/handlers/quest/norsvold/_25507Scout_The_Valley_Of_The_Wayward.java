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
public class _25507Scout_The_Valley_Of_The_Wayward extends QuestHandler {

    private final static int questId = 25507;
    public _25507Scout_The_Valley_Of_The_Wayward() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806103).addOnQuestStart(questId);
        qe.registerQuestNpc(806103).addOnTalkEvent(questId);
		qe.registerQuestNpc(241683).addOnKillEvent(questId);
		qe.registerQuestNpc(241684).addOnKillEvent(questId);
		qe.registerQuestNpc(241685).addOnKillEvent(questId);
		qe.registerQuestNpc(241686).addOnKillEvent(questId);
		qe.registerQuestNpc(241687).addOnKillEvent(questId);
		qe.registerQuestNpc(241688).addOnKillEvent(questId);
		qe.registerQuestNpc(241689).addOnKillEvent(questId);
		qe.registerQuestNpc(241690).addOnKillEvent(questId);
		qe.registerQuestNpc(241691).addOnKillEvent(questId);
		qe.registerQuestNpc(242547).addOnKillEvent(questId);
		qe.registerQuestNpc(242551).addOnKillEvent(questId);
		qe.registerQuestNpc(242555).addOnKillEvent(questId);
		qe.registerQuestNpc(242559).addOnKillEvent(questId);
		qe.registerQuestNpc(242563).addOnKillEvent(questId);
		qe.registerQuestNpc(242564).addOnKillEvent(questId);
		qe.registerQuestNpc(242571).addOnKillEvent(questId);
		qe.registerQuestNpc(242575).addOnKillEvent(questId);
		qe.registerQuestNpc(242579).addOnKillEvent(questId);
		qe.registerQuestNpc(242583).addOnKillEvent(questId);
		qe.registerQuestNpc(242587).addOnKillEvent(questId);
		qe.registerQuestNpc(242591).addOnKillEvent(questId);
		qe.registerQuestNpc(242595).addOnKillEvent(questId);
		qe.registerQuestNpc(242599).addOnKillEvent(questId);
		qe.registerQuestNpc(242603).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 806103) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806103) {
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
				case 241683:
				case 241684:
				case 241685:
				case 241686:
				case 241687:
				case 241688:
				case 241689:
				case 241690:
				case 241691:
				case 242547:
				case 242551:
				case 242555:
				case 242559:
				case 242563:
				case 242564:
				case 242571:
				case 242575:
				case 242579:
				case 242583:
				case 242587:
				case 242591:
				case 242595:
				case 242599:
				case 242603:
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