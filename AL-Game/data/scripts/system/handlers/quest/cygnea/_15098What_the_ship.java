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
package quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _15098What_the_ship extends QuestHandler
{
	public static final int questId = 15098;
	
	public _15098What_the_ship() {
		super(questId);
	}
	/*
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	*/
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(804964).addOnQuestStart(questId); //LF5_Ship_ZoneTeleport_L.
		qe.registerQuestNpc(804964).addOnTalkEvent(questId); //LF5_Ship_ZoneTeleport_L.
		qe.registerQuestNpc(804964).addOnTalkEvent(questId); //LF5_Ship_ZoneTeleport_L.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804964) { //LF5_Ship_ZoneTeleport_L.
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);	
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 804964: { //LF5_Ship_ZoneTeleport_L.
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 10002);
						} case SELECT_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 804964) { //LF5_Ship_ZoneTeleport_L.
			    return sendQuestEndDialog(env);
		    }
		}
		return false;
	}
}