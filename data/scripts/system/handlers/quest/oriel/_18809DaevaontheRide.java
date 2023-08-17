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
package quest.oriel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;


/**
 * @author zhkchi
 *
 */
public class _18809DaevaontheRide extends QuestHandler {

	private static final int questId = 18809;

	public _18809DaevaontheRide()
	{
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(830168).addOnQuestStart(questId);
		qe.registerQuestNpc(830168).addOnTalkEvent(questId);
		qe.registerQuestNpc(830263).addOnTalkEvent(questId);
		qe.registerQuestNpc(830201).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 830168) 
			{
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 830263: 
				{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1352);
						}
						case STEP_TO_1:{
							return defaultCloseDialog(env, 0, 1);
						}
					}
				}
				case 830201:{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_2:{
							return defaultCloseDialog(env, 1, 2);
						}
					}
				}
				case 830168:{
					switch (dialog) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_REWARD: {
							changeQuestStep(env, 2, 2, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) 
		{
			if (targetId == 830168)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
