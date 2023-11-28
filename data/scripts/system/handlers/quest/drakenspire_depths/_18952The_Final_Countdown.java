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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18952The_Final_Countdown extends QuestHandler {
	
    private final static int questId = 18952;
	private final static int[] boss = {236225, 236226, 236229, 236232, 236238, 236244, 236245, 236246};
	
    public _18952The_Final_Countdown() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(209678).addOnQuestStart(questId);
        qe.registerQuestNpc(209678).addOnTalkEvent(questId);
		qe.registerQuestNpc(804711).addOnTalkEvent(questId);
		for (int mob_id: boss) {
            qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
        }
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 209678) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804711) {
                if (dialog == QuestDialog.START_DIALOG) {
                    if (qs.getQuestVarById(0) == 5) {
                        return sendQuestDialog(env, 2375);
                    }
                } if (dialog == QuestDialog.SELECT_REWARD) {
                    changeQuestStep(env, 5, 6, true);
                    return sendQuestEndDialog(env);
                }
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804711) {
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
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		int var2 = qs.getQuestVarById(2);
		int var3 = qs.getQuestVarById(3);
		int var4 = qs.getQuestVarById(4);
        int targetId = 0;
        Npc npc = null;
        if (env.getVisibleObject() instanceof Npc) {
            npc = (Npc) env.getVisibleObject();
            targetId = npc.getNpcId();
        } switch (targetId) {
            case 236225:
			case 236226:
                qs.setQuestVarById(1, var1 + 1);
                updateQuestStatus(env);
                return true;
            case 236229:
			case 236232:
                qs.setQuestVarById(2, var2 + 1);
                updateQuestStatus(env);
                return true;
            case 236238:
                qs.setQuestVarById(3, var3 + 1);
                updateQuestStatus(env);
                return true;
            case 236244:
			case 236245:
			case 236246:
			    qs.setQuestVarById(4, var4 + 1);
				qs.setQuestVarById(0, var + 1);
				qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return true;
        }
        return false;
    }
}