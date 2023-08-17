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
package quest.harbinger_landing;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25480Clearing_The_Way extends QuestHandler
{
    private final static int questId = 25480;
	private static final Set<Integer> ab1BLv8D;
	
    public _25480Clearing_The_Way() {
        super(questId);
    }
	
	static {
		ab1BLv8D = new HashSet<Integer>();
		ab1BLv8D.add(805822);
		ab1BLv8D.add(805823);
		ab1BLv8D.add(805824);
		ab1BLv8D.add(805825);
	}
	
	@Override
	public void register() {
		Iterator<Integer> iter = ab1BLv8D.iterator();
		while (iter.hasNext()) {
			int ab1Id = iter.next();
			qe.registerQuestNpc(ab1Id).addOnQuestStart(questId);
			qe.registerQuestNpc(ab1Id).addOnTalkEvent(questId);
			qe.registerQuestNpc(884009).addOnKillEvent(questId);
			qe.registerQuestNpc(884010).addOnKillEvent(questId);
			qe.registerQuestNpc(884011).addOnKillEvent(questId);
			qe.registerQuestNpc(884012).addOnKillEvent(questId);
			qe.registerQuestNpc(884013).addOnKillEvent(questId);
			qe.registerQuestNpc(884014).addOnKillEvent(questId);
			qe.registerQuestNpc(884015).addOnKillEvent(questId);
			qe.registerQuestNpc(884016).addOnKillEvent(questId);
			qe.registerQuestNpc(884017).addOnKillEvent(questId);
			qe.registerQuestNpc(884018).addOnKillEvent(questId);
			qe.registerQuestNpc(884019).addOnKillEvent(questId);
			qe.registerQuestNpc(884020).addOnKillEvent(questId);
			qe.registerQuestNpc(884021).addOnKillEvent(questId);
			qe.registerQuestNpc(884022).addOnKillEvent(questId);
			qe.registerQuestNpc(884023).addOnKillEvent(questId);
			qe.registerQuestNpc(884024).addOnKillEvent(questId);
			qe.registerQuestNpc(884025).addOnKillEvent(questId);
			qe.registerQuestNpc(884026).addOnKillEvent(questId);
		}
	}
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        int targetId = env.getTargetId();
		final Player player = env.getPlayer();
		if (!ab1BLv8D.contains(targetId)) {
			return false;
		}
        QuestDialog dialog = env.getDialog();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
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
		    } else {
				return sendQuestEndDialog(env);
			}
		}
        return false;
    }
	
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
				case 884009:
				case 884010:
				case 884011:
				case 884012:
				case 884013:
				case 884014:
				case 884015:
				case 884016:
				case 884017:
				case 884018:
				case 884019:
				case 884020:
				case 884021:
				case 884022:
				case 884023:
				case 884024:
				case 884025:
				case 884026:
                if (qs.getQuestVarById(1) < 1) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 1) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
            }
        }
        return false;
    }
}