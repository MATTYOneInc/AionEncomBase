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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4217The_Imprisoned_Executor extends QuestHandler {

	private final static int questId = 4217;
	public _4217The_Imprisoned_Executor() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestNpc(798336).addOnQuestStart(questId);
		qe.registerQuestNpc(798336).addOnTalkEvent(questId);
		qe.registerQuestNpc(204773).addOnTalkEvent(questId);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } 
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798336) {
                switch (env.getDialog()) {
				case START_DIALOG: {
					return sendQuestDialog(env, 4762);
				}  case ASK_ACCEPTION: {
					return sendQuestDialog(env, 4);
				}  case ACCEPT_QUEST: {
					return sendQuestStartDialog(env);
				}  case REFUSE_QUEST: {
					return sendQuestDialog(env, 1004);
				}
			}
			return false;
		  }
       }
       if (qs == null || qs.getStatus() == QuestStatus.START) {
			if (targetId == 798336) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case CHECK_COLLECTED_ITEMS: {
						if (QuestService.collectItemCheck(env, true)) {
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						   }
                       }
                       case SET_REWARD: {
						qs.setQuestVar(1);
                        qs.setStatus(QuestStatus.REWARD);  
						updateQuestStatus(env);
                        return closeDialogWindow(env);
					}
				}
			}
		} 
        else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204773) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}