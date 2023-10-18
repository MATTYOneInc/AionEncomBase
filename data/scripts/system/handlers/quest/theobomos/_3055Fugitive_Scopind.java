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
package quest.theobomos;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.quest.Rewards;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _3055Fugitive_Scopind extends QuestHandler
{
	private final static int questId = 3055;
	
	public _3055Fugitive_Scopind() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(730146).addOnQuestStart(questId);
		qe.registerQuestNpc(730146).addOnTalkEvent(questId);
		qe.registerQuestNpc(798195).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		int targetId = 0;
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730146) {
				switch (env.getDialog()) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 4762);
					} case STEP_TO_1: {
						QuestService.startQuest(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					} default:
						return sendQuestStartDialog(env);
				}
			}
		} if (qs == null) {
			return false;
		} if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798195: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (player.getInventory().getItemCountByItemId(182208040) < 1) {
							return sendQuestDialog(env, 2375);
						}
					}
					removeQuestItem(env, 182208040, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				    }
				}
			}
			
		} 
		else if (qs.getStatus() == QuestStatus.REWARD && targetId == 798195) {
			return sendQuestEndDialog(env);
		}
		return false;
	}
}