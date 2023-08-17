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
package quest.brusthonin;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemService;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _4074Gain_Or_Lose extends QuestHandler
{
	private final static int questId = 4074;
	private int reward = -1;
	
	public _4074Gain_Or_Lose() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(205181).addOnQuestStart(questId);
		qe.registerQuestNpc(205181).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 205181) { //Bonarunerk.
				if (dialog == QuestDialog.EXCHANGE_COIN) {
					if (QuestService.startQuest(env)) {
						return sendQuestDialog(env, 1011);
					} else {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205181) { //Bonarunerk.
				long kinahAmount = player.getInventory().getKinah();
				long demonsEye = player.getInventory().getItemCountByItemId(186000038);
				switch (dialog) {
					case EXCHANGE_COIN: {
						return sendQuestDialog(env, 1011);
					} case SELECT_ACTION_1011: {
						if (kinahAmount >= 1000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 0;
							return sendQuestDialog(env, 5);
						} else {
							return sendQuestDialog(env, 1009);
						}
					} case SELECT_ACTION_1352: {
						if (kinahAmount >= 5000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 1;
							return sendQuestDialog(env, 6);
						} else {
							return sendQuestDialog(env, 1009);
						}
					} case SELECT_ACTION_1693: {
						if (kinahAmount >= 25000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 2;
							return sendQuestDialog(env, 7);
						} else {
							return sendQuestDialog(env, 1009);
						}
					} case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205181) { //Bonarunerk.
				if (dialog == QuestDialog.SELECT_NO_REWARD) {
					switch (reward) {
						case 0: {
							if (QuestService.finishQuest(env, 0)) {
								player.getInventory().decreaseKinah(1000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, 1);
								reward = -1;
								break;
							}
						} case 1: {
							if (QuestService.finishQuest(env, 1)) {
								player.getInventory().decreaseKinah(5000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, Rnd.get(1, 3));
								reward = -1;
								break;
							}
						} case 2: {
							if (QuestService.finishQuest(env, 2)) {
								player.getInventory().decreaseKinah(25000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, Rnd.get(1, 6));
								reward = -1;
								break;
							}
						}
					}
					return closeDialogWindow(env);
				} else {
					QuestService.abandonQuest(player, questId);
				}
			}
		}
		return false;
	}
}