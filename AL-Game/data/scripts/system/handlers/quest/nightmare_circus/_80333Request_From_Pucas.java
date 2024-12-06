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
package quest.nightmare_circus;

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

public class _80333Request_From_Pucas extends QuestHandler
{
    private final static int questId = 80333;
	
    public _80333Request_From_Pucas() {
        super(questId);
    }
	
	@Override
	public void register() {
		//Otherworldly Pucas.
		qe.registerQuestNpc(831541).addOnQuestStart(questId);
		qe.registerQuestNpc(831542).addOnQuestStart(questId);
		qe.registerQuestNpc(831543).addOnQuestStart(questId);
		qe.registerQuestNpc(831544).addOnQuestStart(questId);
		qe.registerQuestNpc(831545).addOnQuestStart(questId);
		qe.registerQuestNpc(831546).addOnQuestStart(questId);
		qe.registerQuestNpc(831547).addOnQuestStart(questId);
		qe.registerQuestNpc(831548).addOnQuestStart(questId);
		qe.registerQuestNpc(831541).addOnTalkEvent(questId); 
		qe.registerQuestNpc(831542).addOnTalkEvent(questId);
		qe.registerQuestNpc(831543).addOnTalkEvent(questId);
		qe.registerQuestNpc(831544).addOnTalkEvent(questId);
		qe.registerQuestNpc(831545).addOnTalkEvent(questId);
		qe.registerQuestNpc(831546).addOnTalkEvent(questId);
		qe.registerQuestNpc(831547).addOnTalkEvent(questId);
		qe.registerQuestNpc(831548).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			//Otherworldly Pucas.
			if (targetId == 831541 || targetId == 831542 ||
				targetId == 831543 || targetId == 831544 ||
				targetId == 831545 || targetId == 831546 ||
				targetId == 831547 || targetId == 831548) {
				switch (dialog) {
					case START_DIALOG:
						return sendQuestDialog(env, 1011);
					case ASK_ACCEPTION:
					    return sendQuestDialog(env, 4);
					case ACCEPT_QUEST:
					    QuestService.startQuest(env);
					    return sendQuestDialog(env, 1003);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			//Otherworldly Pucas.
			switch (targetId) {
				case 831541:
				case 831542:
				case 831543:
				case 831544:
				case 831545:
				case 831546:
				case 831547:
				case 831548: {
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						} case SELECT_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831541 || targetId == 831542 ||
				targetId == 831543 || targetId == 831544 ||
				targetId == 831545 || targetId == 831546 ||
				targetId == 831547 || targetId == 831548) {
				if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}