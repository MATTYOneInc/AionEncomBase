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
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25671Destroying_The_Light_Armored_Guardian_Transporter extends QuestHandler {

    private final static int questId = 25671;
	private final static int[] Reinhard = {806116};
	private final static int[] DF6RaidSum = {246463, 246464, 246465, 246466};
	private final static int[] DF6RaidGuard = {246682, 246683, 246684, 246685, 246686, 246687};
    public _25671Destroying_The_Light_Armored_Guardian_Transporter() {
        super(questId);
    }
	
    public void register() {
        for (int npc: Reinhard) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: DF6RaidSum) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: DF6RaidGuard) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerQuestNpc(806116).addOnAtDistanceEvent(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806116) {
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
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		int[] DF6RaidSum = {246463, 246464, 246465, 246466};
		int[] DF6RaidGuard = {246682, 246683, 246684, 246685, 246686, 246687};
		if (qs != null && qs.getStatus() == QuestStatus.START) {
        int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
            if (var == 0) {
				if (var1 + var2 < 6) {
					if (targetId == 246682 || targetId == 246683 || targetId == 246684 ||
						targetId == 246685 || targetId == 246686 || targetId == 246687) {
						if (var2 < 6) {
							return defaultOnKillEvent(env, DF6RaidGuard, var2, var2 + 1, 2);
						}
					} else {
						if (var1 < 1) {
							return defaultOnKillEvent(env, DF6RaidSum, var1, var1 + 1, 1);
						}
					}
				} else {
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
        return false;
    }
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
			return true;
		}
		return false;
	}
}