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
package quest.mission;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique) [702041 Baranath Silencer]
/****/

public class _24054Crisis_In_Beluslan extends QuestHandler
{
    private final static int questId = 24054;
    private final static int[] npcs = {204701, 204702, 802053};
	
    public _24054Crisis_In_Beluslan() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
        qe.registerQuestNpc(702041).addOnKillEvent(questId); //Baranath Silencer
        qe.registerQuestNpc(233865).addOnKillEvent(questId); //Officer Bakuram.
    }
	
    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }
	
    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        int[] quests = {24050, 24051, 24052, 24053};
        return defaultOnLvlUpEvent(env, quests, false);
    }
	
    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int targetId = env.getTargetId();
            switch (targetId) {
                case 702041: { //Baranath Silencer
                    return defaultOnKillEvent(env, 702041, 2, 5);
                } case 233865: { //Officer Bakuram.
                    return defaultOnKillEvent(env, 233865, 5, 6);
                }
            }
        }
        return false;
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
        if (qs == null) {
            return false;
        }
        Npc target = (Npc) env.getVisibleObject();
        int targetId = target.getNpcId();
        int var = qs.getQuestVarById(0);
        QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204702) { //Nerita.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204702: { //Nerita.
                    if (dialog == QuestDialog.START_DIALOG && var == 0) {
                        return sendQuestDialog(env, 1011);
                    } if (dialog == QuestDialog.STEP_TO_1) {
                        return defaultCloseDialog(env, 0, 1);
                    }
                    break;
				} case 802053: { //Fafner.
                    if (dialog == QuestDialog.START_DIALOG && var == 1) {
                        return sendQuestDialog(env, 1352);
                    } if (dialog == QuestDialog.STEP_TO_2) {
                        return defaultCloseDialog(env, 1, 2);
                    }
                    break;
				} case 204701: { //Hod.
					if (dialog == QuestDialog.START_DIALOG && var == 6) {
                        return sendQuestDialog(env, 2375);
                    } else if (dialog == QuestDialog.SET_REWARD) {
                        return defaultCloseDialog(env, 6, 6, true, false);
                    }
                    break;
				}
            }
        }
        return false;
    }
}