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
public class _15515Scouting_A_War_Zone extends QuestHandler {

    private final static int questId = 15515;
    public _15515Scouting_A_War_Zone() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806094).addOnQuestStart(questId);
        qe.registerQuestNpc(806094).addOnTalkEvent(questId);
		qe.registerQuestNpc(241725).addOnKillEvent(questId);
		qe.registerQuestNpc(241726).addOnKillEvent(questId);
		qe.registerQuestNpc(241727).addOnKillEvent(questId);
		qe.registerQuestNpc(241728).addOnKillEvent(questId);
		qe.registerQuestNpc(241729).addOnKillEvent(questId);
		qe.registerQuestNpc(241730).addOnKillEvent(questId);
		qe.registerQuestNpc(241731).addOnKillEvent(questId);
		qe.registerQuestNpc(241732).addOnKillEvent(questId);
		qe.registerQuestNpc(241733).addOnKillEvent(questId);
		qe.registerQuestNpc(241734).addOnKillEvent(questId);
		qe.registerQuestNpc(241735).addOnKillEvent(questId);
		qe.registerQuestNpc(241736).addOnKillEvent(questId);
		qe.registerQuestNpc(242743).addOnKillEvent(questId);
		qe.registerQuestNpc(241744).addOnKillEvent(questId);
		qe.registerQuestNpc(241745).addOnKillEvent(questId);
		qe.registerQuestNpc(241746).addOnKillEvent(questId);
		qe.registerQuestNpc(242747).addOnKillEvent(questId);
		qe.registerQuestNpc(242751).addOnKillEvent(questId);
		qe.registerQuestNpc(242755).addOnKillEvent(questId);
		qe.registerQuestNpc(242759).addOnKillEvent(questId);
		qe.registerQuestNpc(242763).addOnKillEvent(questId);
		qe.registerQuestNpc(242767).addOnKillEvent(questId);
		qe.registerQuestNpc(242771).addOnKillEvent(questId);
		qe.registerQuestNpc(242775).addOnKillEvent(questId);
		qe.registerQuestNpc(242779).addOnKillEvent(questId);
		qe.registerQuestNpc(242783).addOnKillEvent(questId);
		qe.registerQuestNpc(242787).addOnKillEvent(questId);
		qe.registerQuestNpc(242791).addOnKillEvent(questId);
		qe.registerQuestNpc(242795).addOnKillEvent(questId);
		qe.registerQuestNpc(242799).addOnKillEvent(questId);
		qe.registerQuestNpc(242803).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806094) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }  
        else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806094) {
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
				case 241725:
				case 241726:
				case 241727:
				case 241728:
				case 241729:
				case 241730:
				case 241731:
				case 241732:
				case 241733:
				case 241734:
				case 241735:
				case 241736:
				case 242743:
				case 241744:
				case 241745:
				case 242746:
				case 242747:
				case 242751:
				case 242755:
				case 242759:
				case 242763:
				case 242767:
				case 242771:
				case 242775:
				case 242779:
				case 242783:
				case 242787:
				case 242791:
				case 242795:
				case 242799:
				case 242803:
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