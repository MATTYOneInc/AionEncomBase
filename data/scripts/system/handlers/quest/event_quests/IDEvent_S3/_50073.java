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
package quest.event_quests.IDEvent_S3;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

import java.util.*;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _50073 extends QuestHandler
{
    private final static int questId = 50073;
	private static final Set<Integer> IdEventS3Seller;
	
    public _50073() {
        super(questId);
    }
	
	static {
		IdEventS3Seller = new HashSet<Integer>();
		IdEventS3Seller.add(835570);
		IdEventS3Seller.add(835571);
	}
	
    @Override
	public void register() {
		Iterator<Integer> iter = IdEventS3Seller.iterator();
		while (iter.hasNext()) {
			int s3 = iter.next();
			qe.registerQuestNpc(s3).addOnQuestStart(questId);
			qe.registerQuestNpc(s3).addOnTalkEvent(questId);
			qe.registerQuestNpc(246293).addOnKillEvent(questId);
		}
	}
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        int targetId = env.getTargetId();
		final Player player = env.getPlayer();
		if (!IdEventS3Seller.contains(targetId)) {
			return false;
		}
        QuestDialog dialog = env.getDialog();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            switch (dialog) {
				case START_DIALOG: {
					return sendQuestDialog(env, 4762);
				} case ACCEPT_QUEST:
				case ACCEPT_QUEST_SIMPLE: {
					return sendQuestStartDialog(env);
				} case REFUSE_QUEST_SIMPLE: {
				    return closeDialogWindow(env);
				}
			}
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (dialog) {
				case START_DIALOG: {
					return sendQuestDialog(env, 2375);
				} case SELECT_REWARD: {
					changeQuestStep(env, 1, 2, true);
					return sendQuestEndDialog(env);
				}
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getDialog() == QuestDialog.START_DIALOG) {
                return sendQuestDialog(env, 10002);
			} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
				return sendQuestDialog(env, 5);
			} else {
				return sendQuestEndDialog(env);
			}
		}
        return false;
    }
	
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 246293:
                if (qs.getQuestVarById(1) < 2) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 2) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}