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
package quest.esoterrace;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _17541The_Drana_Farm extends QuestHandler {

    private final static int questId = 17541;
    public _17541The_Drana_Farm() {
        super(questId);
    }
	
    public void register() {
        qe.registerQuestNpc(799553).addOnQuestStart(questId); //Daidra.
        qe.registerQuestNpc(799553).addOnTalkEvent(questId); //Daidra.
        qe.registerQuestNpc(217185).addOnKillEvent(questId); //Dalia Charlands.
		qe.registerQuestNpc(217195).addOnKillEvent(questId); //Captain Murugan.
		qe.registerQuestNpc(217204).addOnKillEvent(questId); //Kexkra.
		qe.registerQuestNpc(217206).addOnKillEvent(questId); //Warden Surama.
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799553) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
                }
			}
		}
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 799553) { //Daidra.
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
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
            int var2 = qs.getQuestVarById(2);
			int var3 = qs.getQuestVarById(3);
			int targetId = env.getTargetId();
            if (var == 0) {
                if (targetId == 217185) { //Dalia Charlands.
                    if (var1 < 4) {
                        return defaultOnKillEvent(env, 217185, 0, 4, 1);
                    } else if (var1 == 4) {
                        if (var2 == 3 && var3 == 2) {
                            return true;
                        } else {
                            return defaultOnKillEvent(env, 217185, 4, 1, 1);
                        }
                    }
                } if (targetId == 217195) { //Captain Murugan.
                    if (var2 < 2) {
                        return defaultOnKillEvent(env, 217195, 0, 2, 2);
                    } else if (var2 == 2) {
                        if (var1 == 5 && var3 == 2) {
                            return true;
                        } else {
                            return defaultOnKillEvent(env, 217195, 2, 1, 2);
                        }
                    }
                } if (targetId == 217204 || //Kexkra.
					targetId == 217206) { //Warden Surama.
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}
        }
        return false;
    }
}