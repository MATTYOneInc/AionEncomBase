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
package quest.daevanion;

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

public class _80295Durable_Daevanion_Weapon extends QuestHandler
{
	private final static int questId = 80295;
	
	public _80295Durable_Daevanion_Weapon() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(831387).addOnQuestStart(questId);
		qe.registerQuestNpc(831387).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 831387) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					int plate = player.getEquipment().itemSetPartsEquipped(302);
					int chain = player.getEquipment().itemSetPartsEquipped(303);
					int leather = player.getEquipment().itemSetPartsEquipped(301);
					int cloth = player.getEquipment().itemSetPartsEquipped(300);
					int gunslinger = player.getEquipment().itemSetPartsEquipped(372);
					if (plate != 5 &&
					    chain != 5 &&
						leather != 5 &&
						cloth != 5 &&
						gunslinger != 5) {
						return sendQuestDialog(env, 1003);
					} else {
						return sendQuestDialog(env, 4762);
					}
				} else {
					return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 831387) {
				switch (env.getDialog()) {
					case START_DIALOG:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
					case CHECK_COLLECTED_ITEMS:
					if (var == 0) {
						return checkQuestItems(env, 0, 1, true, 5, 0);
					}
					break;
					case SELECT_ACTION_1352:
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				}
			}
			return false;
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831387) {
				return sendQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
}