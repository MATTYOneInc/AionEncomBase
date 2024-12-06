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
package quest.steel_rake;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4205Smack_The_Shulack extends QuestHandler
{
	private final static int questId = 4205;
	private final static int[] Steel_Rake_Watcher = {218972, 218974, 218975, 218976, 218979};
	
	public _4205Smack_The_Shulack() {
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(205233).addOnTalkEvent(questId);
		qe.registerQuestNpc(204792).addOnTalkEvent(questId);
		qe.registerQuestNpc(805842).addOnTalkEvent(questId);
		for (int mob: Steel_Rake_Watcher) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205233) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 15) {
                            return sendQuestDialog(env, 1352);
                        }
					} case STEP_TO_2: {
                        changeQuestStep(env, 15, 16, false);
						return closeDialogWindow(env);
					}
                }
            } if (targetId == 204792) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 16) {
                            return sendQuestDialog(env, 1694);
                        }
					} case SET_REWARD: {
                        qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
                }
            }
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 805842) {
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
	int targetId = env.getTargetId();	
    return defaultOnKillEvent(env, Steel_Rake_Watcher, 0, 15);    
    }
}