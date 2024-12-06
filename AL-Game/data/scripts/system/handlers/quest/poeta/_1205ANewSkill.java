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
package quest.poeta;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Npc;
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

public class _1205ANewSkill extends QuestHandler
{
	private final static int questId = 1205;
	
	public _1205ANewSkill() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203087).addOnTalkEvent(questId); //Warrior.
		qe.registerQuestNpc(203088).addOnTalkEvent(questId); //Scout.
		qe.registerQuestNpc(203089).addOnTalkEvent(questId); //Mage.
		qe.registerQuestNpc(203090).addOnTalkEvent(questId); //Priest.
		qe.registerQuestNpc(801210).addOnTalkEvent(questId); //Technist.
		qe.registerQuestNpc(801211).addOnTalkEvent(questId); //Muse.
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		if (!lvlCheck)
			return false;
		if (qs != null)
			return false;
		env.setQuestId(questId);
		if (QuestService.startQuest(env)) {
			qs = player.getQuestStateList().getQuestState(questId);
			qs.setStatus(QuestStatus.REWARD);
			PlayerClass playerClass = player.getPlayerClass();
			if (!playerClass.isStartingClass())
				playerClass = PlayerClass.getStartingClassFor(playerClass);
			switch (playerClass) {
				case WARRIOR:
					qs.setQuestVar(1);
				break;
				case SCOUT:
					qs.setQuestVar(2);
				break;
				case MAGE:
					qs.setQuestVar(3);
				break;
				case PRIEST:
					qs.setQuestVar(4);
				break;
				case TECHNIST:
					qs.setQuestVar(5);
				break;
				case MUSE:
					qs.setQuestVar(6);
				break;
			}
			updateQuestStatus(env);
		}
		return true;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.REWARD)
			return false;
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		PlayerClass playerClass = PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass());
		switch (targetId) {
			case 203087:
				if (playerClass == PlayerClass.WARRIOR) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 1011);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 5);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203088:
				if (playerClass == PlayerClass.SCOUT) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 1352);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 6);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203089:
				if (playerClass == PlayerClass.MAGE) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 1693);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 7);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203090:
				if (playerClass == PlayerClass.PRIEST) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 2034);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 8);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 801210:
				if (playerClass == PlayerClass.TECHNIST) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 2375);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 45);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 801211:
				if (playerClass == PlayerClass.MUSE) {
					if (env.getDialog() == QuestDialog.USE_OBJECT)
						return sendQuestDialog(env, 2716);
					else if (env.getDialogId() == 1009)
						return sendQuestDialog(env, 46);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
		}
		return false;
	}
}