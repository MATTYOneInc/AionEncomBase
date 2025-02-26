/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author VladimirZ
 */
public class _1559WhatsintheBox extends QuestHandler {

	private final static int questId = 1559;
	public _1559WhatsintheBox() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(700513).addOnTalkEvent(questId);
		qe.registerQuestNpc(798072).addOnTalkEvent(questId);
		qe.registerQuestNpc(204571).addOnTalkEvent(questId);
		qe.registerQuestNpc(798013).addOnTalkEvent(questId);
		qe.registerQuestItem(182201823, questId);
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
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
		if (targetId == 0) {
			if (env.getDialog() == QuestDialog.ACCEPT_QUEST) {
				return sendQuestStartDialog(env);
			    }
			if (env.getDialog() == QuestDialog.REFUSE_QUEST) {
				return closeDialogWindow(env);
			    }
		    }
		}	
        if (targetId == 700513) {
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (player.getInventory().getItemCountByItemId(182201823) == 0) {
							return giveQuestItem(env, 182201823, 1);
						}
					}
				}
			}
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() != QuestStatus.START) {
		switch (targetId) {
		case 798072: {
			switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 0)
						return sendQuestDialog(env, 1352);
				case STEP_TO_1: 
					if (var == 0)
					qs.setQuestVarById(0, var + 1);
				    updateQuestStatus(env);
                    return closeDialogWindow(env);
			}
		}
		case 204571: {
			switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 1)
						return sendQuestDialog(env, 1693);
				case STEP_TO_2:
					if (var == 1) 
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
                    return closeDialogWindow(env);
			}
		}
		case 798013: {
			switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 2)
						return sendQuestDialog(env, 2034);
				case STEP_TO_3:
					if (var == 2)
					qs.setQuestVarById(0, var + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					giveQuestItem(env, 182201824, 1);
                    return closeDialogWindow(env);
                    }
                }
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798072) {
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 2375);
				else if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}