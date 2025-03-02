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
public class _25521Protect_Azurelight_Forest extends QuestHandler {

    private final static int questId = 25521;
    public _25521Protect_Azurelight_Forest() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(806108).addOnQuestStart(questId);
        qe.registerQuestNpc(806108).addOnTalkEvent(questId);
		qe.registerQuestNpc(241589).addOnKillEvent(questId);
		qe.registerQuestNpc(241590).addOnKillEvent(questId);
		qe.registerQuestNpc(241591).addOnKillEvent(questId);
		qe.registerQuestNpc(241592).addOnKillEvent(questId);
		qe.registerQuestNpc(241593).addOnKillEvent(questId);
		qe.registerQuestNpc(241594).addOnKillEvent(questId);
		qe.registerQuestNpc(241595).addOnKillEvent(questId);
		qe.registerQuestNpc(241596).addOnKillEvent(questId);
		qe.registerQuestNpc(241597).addOnKillEvent(questId);
		qe.registerQuestNpc(243264).addOnKillEvent(questId);
		qe.registerQuestNpc(243265).addOnKillEvent(questId);
		qe.registerQuestNpc(242147).addOnKillEvent(questId);
		qe.registerQuestNpc(242151).addOnKillEvent(questId);
		qe.registerQuestNpc(242155).addOnKillEvent(questId);
		qe.registerQuestNpc(242159).addOnKillEvent(questId);
		qe.registerQuestNpc(242163).addOnKillEvent(questId);
		qe.registerQuestNpc(242167).addOnKillEvent(questId);
		qe.registerQuestNpc(242171).addOnKillEvent(questId);
		qe.registerQuestNpc(242175).addOnKillEvent(questId);
		qe.registerQuestNpc(242179).addOnKillEvent(questId);
		qe.registerQuestNpc(242183).addOnKillEvent(questId);
		qe.registerQuestNpc(242187).addOnKillEvent(questId);
		qe.registerQuestNpc(242191).addOnKillEvent(questId);
		qe.registerQuestNpc(242195).addOnKillEvent(questId);
		qe.registerQuestNpc(242199).addOnKillEvent(questId);
		qe.registerQuestNpc(242203).addOnKillEvent(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 806108) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806108) {
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
				case 241589:
				case 241590:
				case 241591:
				case 241592:
				case 241593:
				case 241594:
				case 241595:
				case 241596:
				case 241597:
				case 243264:
				case 243265:
				case 242147:
				case 242151:
				case 242155:
				case 242159:
				case 242163:
				case 242167:
				case 242171:
				case 242175:
				case 242179:
				case 242183:
				case 242187:
				case 242191:
				case 242195:
				case 242199:
				case 242203:
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