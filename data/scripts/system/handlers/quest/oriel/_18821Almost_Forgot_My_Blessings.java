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
package quest.oriel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _18821Almost_Forgot_My_Blessings extends QuestHandler
{
	private static final int questId = 18821;
	private static final Set<Integer> butlersElyos;
	
	public _18821Almost_Forgot_My_Blessings() {
		super(questId);
	}
	
	static {
		butlersElyos = new HashSet<Integer>();
		butlersElyos.add(810017);
		butlersElyos.add(810018);
		butlersElyos.add(810019);
		butlersElyos.add(810020);
		butlersElyos.add(810021);
	}
	
	@Override
	public void register() {
		Iterator<Integer> iter = butlersElyos.iterator();
		while (iter.hasNext()) {
			int butlerId = iter.next();
			qe.registerQuestNpc(butlerId).addOnQuestStart(questId);
			qe.registerQuestNpc(butlerId).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		if (!butlersElyos.contains(targetId))
			return false;
		House house = player.getActiveHouse();
		if (house == null || house.getButler() == null || house.getButler().getNpcId() != targetId)
			return false;
		QuestDialog dialog = env.getDialog();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			switch (dialog) {
				case START_DIALOG:
					return sendQuestDialog(env, 1011);
				case ACCEPT_QUEST:
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			switch (dialog) {
				case START_DIALOG:
					return sendQuestDialog(env, 2375);
				case SELECT_REWARD:
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				case SELECT_NO_REWARD:
					return sendQuestEndDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			switch (dialog) {
				case USE_OBJECT:
					return sendQuestDialog(env, 5);
				case SELECT_NO_REWARD:
					sendQuestEndDialog(env);
					return true;
			}
		}
		return false;
	}
}