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
package quest.quest_specialize;

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
public class _14112Pollution_Resolution extends QuestHandler {

	private final static int questId = 14112;
	public _14112Pollution_Resolution() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203149).addOnQuestStart(questId);
        qe.registerQuestNpc(203149).addOnTalkEvent(questId);
		qe.registerQuestNpc(203148).addOnTalkEvent(questId);
		qe.registerQuestNpc(203195).addOnTalkEvent(questId);
		qe.registerQuestNpc(210318).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203149) {
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 1011);
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		}
        if (qs == null) {
		    return false;
		} 
        else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203148: {
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						} case STEP_TO_1: {
							return sendQuestEndDialog(env);
						} default:
							return sendQuestEndDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203195: {
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							return sendQuestEndDialog(env);
						} default:
							return sendQuestEndDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 203195) {
			    switch (dialog) {
					case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
						return sendQuestEndDialog(env);
				}
		    }
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		} switch (targetId) {
			case 210318:
				if (qs.getQuestVarById(1) < 1) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				} if (qs.getQuestVarById(1) >= 1) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			break;
		}
		return false;
	}
}