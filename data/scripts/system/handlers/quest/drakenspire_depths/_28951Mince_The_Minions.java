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
package quest.drakenspire_depths;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _28951Mince_The_Minions extends QuestHandler {

    private final static int questId = 28951;
	private final static int[] minion = {236106, 236109, 236113, 236116, 236119, 236120, 236124, 236126, 236128, 236129, 236131, 236132, 236137, 236141, 236142, 236154, 236155, 236156, 236157, 236159, 236162, 236163, 236165, 236166, 236167, 236168, 236170, 236172, 236174, 236175, 236177, 236186, 236185, 236187, 236192, 236194, 236199, 236201, 236204, 236205, 236206, 236216, 236217, 236218, 236219, 236220};
    public _28951Mince_The_Minions() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(209743).addOnQuestStart(questId);
        qe.registerQuestNpc(209743).addOnTalkEvent(questId);
		qe.registerQuestNpc(804738).addOnTalkEvent(questId);
		for (int mob: minion) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 209743) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804738) {
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
                case 236106:
				case 236109:
				case 236113:
				case 236116:
				case 236119:
				case 236120:
				case 236124:
				case 236126:
				case 236128:
				case 236129:
				case 236131:
				case 236132:
				case 236137:
				case 236141:
				case 236142:
				case 236154:
				case 236155:
				case 236156:
				case 236157:
				case 236159:
				case 236162:
				case 236163:
				case 236165:
				case 236166:
		        case 236167:
				case 236168:
				case 236170:
				case 236172:
				case 236174:
				case 236175:
		        case 236177:
				case 236186:
				case 236185:
				case 236187:
				case 236192:
				case 236194:
		        case 236199:
				case 236201:
				case 236204:
				case 236205:
				case 236206:
				case 236216:
		        case 236217:
				case 236218:
				case 236219:
				case 236220:
                if (qs.getQuestVarById(1) < 25) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 25) {
					qs.setQuestVarById(0, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}