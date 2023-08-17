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
package quest.inggison;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _11216How_Many_Draks_Does_It_Take_To_Map extends QuestHandler {

	private final static int questId = 11216;
	
	public _11216How_Many_Draks_Does_It_Take_To_Map() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestItem(182206825, questId); //Balaur's Map.
		qe.registerQuestNpc(799017).addOnTalkEvent(questId); //Sulinia.
		qe.registerQuestNpc(700624).addOnTalkEvent(questId); //Eastern Star.
		qe.registerQuestNpc(700625).addOnTalkEvent(questId); //Western Star.
		qe.registerQuestNpc(700626).addOnTalkEvent(questId); //Southern Star.
		qe.registerQuestNpc(700627).addOnTalkEvent(questId); //Northern Star.
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0)  { 
				if (env.getDialog() == QuestDialog.ACCEPT_QUEST) {
					removeQuestItem(env, 182206825, 1); //Balaur's Map.
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		} if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799017) { //Sulinia.
				switch (env.getDialog()) {
				    case USE_OBJECT: {
                        return sendQuestDialog(env, 10002);
					} case SELECT_REWARD: {
                        return sendQuestDialog(env, 5);
					} default:
                        return sendQuestEndDialog(env);	
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 700624: { //Eastern Star.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182206827) == 0) {
                        		giveQuestItem(env, 182206827,1); //Eastern Symbol.
                    		}
                        }
                    }
                    break;
                } case 700625: { //Western Star.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
						    if (player.getInventory().getItemCountByItemId(182206828) == 0) {
                        		giveQuestItem(env, 182206828,1); //Western Symbol.
                    		}
                        }
						break;
                    }
                } case 700626: { //Southern Star.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182206829) == 0) {
                        		giveQuestItem(env, 182206829,1); //Southern Symbol.
                    		}
                        }
						break;
                    }
                } case 700627: { //Northern Star.
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
						    if (player.getInventory().getItemCountByItemId(182206830) == 0) {
                        		giveQuestItem(env, 182206830,1); //Northern Symbol.
                    		}
                        }
						break;
                    }
                } case 799017: { //Sulinia.
                    switch (env.getDialog()) {
						case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						} else if (var == 1) {
							return sendQuestDialog(env, 1352);
						} case SELECT_ACTION_1012: {
							return defaultCloseDialog(env, 0, 1);
						} case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 2, true, 5, 10001);
						}
                    }
                    break;
                }
            }
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}