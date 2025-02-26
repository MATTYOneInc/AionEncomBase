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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15671Destroying_The_Light_Armored_Archon_Carrier extends QuestHandler {

    private final static int questId = 15671;
	private final static int[] Ilisia = {806114};
	private final static int[] F6RaidSum = {246481, 246482, 246483, 246484};
	private final static int[] F6RaidGuard = {246688, 246689, 246690, 246691, 246692, 246693};
	
    public _15671Destroying_The_Light_Armored_Archon_Carrier() {
        super(questId);
    }
	
    public void register() {
        for (int npc: Ilisia) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } for (int mob: F6RaidSum) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		} for (int mob: F6RaidGuard) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerQuestNpc(806114).addOnAtDistanceEvent(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806114) {
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
        int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
		int[] F6RaidSum = {246481, 246482, 246483, 246484};
		int[] F6RaidGuard = {246688, 246689, 246690, 246691, 246692, 246693};
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (var == 0) {
				if (var1 + var2 < 6) {
					if (targetId == 246688 || targetId == 246689 || targetId == 246690 ||
						targetId == 246691 || targetId == 246692 || targetId == 246693) {
						if (var2 < 6) {
							return defaultOnKillEvent(env, F6RaidGuard, var2, var2 + 1, 2);
						}
					} else {
						if (var1 < 1) {
							return defaultOnKillEvent(env, F6RaidSum, var1, var1 + 1, 1);
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
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
			return true;
		}
		return false;
	}
}