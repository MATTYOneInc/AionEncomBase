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
package quest.morheim;

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

public class _2343Ancient_Ginseng extends QuestHandler {

	private final static int questId = 2343;
	public _2343Ancient_Ginseng() {
		super(questId);
	}
	
	public void register() {
		qe.registerQuestItem(182204134, questId);
		qe.registerQuestNpc(700243).addOnTalkEvent(questId);
		qe.registerCanAct(questId, 700243);
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
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		int targetId = 0;
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 0) { 
				if (env.getDialog() == QuestDialog.ACCEPT_QUEST) {
					return sendQuestStartDialog(env);
				}
				if (env.getDialog() == QuestDialog.REFUSE_QUEST) {
					return closeDialogWindow(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 700243) {
				Npc npc = player.getPosition().getWorldMapInstance().getNpc(212814);
				if (npc == null) {
				    QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 212814, 1008.36f, 481.88f, 509.3f, (byte) 60);
				    removeQuestItem(env, 182204134, 1);
				    qs.setStatus(QuestStatus.REWARD);
				    QuestService.finishQuest(env);
				    return true;
				}
			}
		}
		return false;
	}
}