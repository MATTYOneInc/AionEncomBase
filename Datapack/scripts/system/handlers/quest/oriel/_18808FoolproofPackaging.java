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
 * 
 * @author Bobobear & Ritsu
 */
public class _18808FoolproofPackaging extends QuestHandler 
{

	private static final int questId = 18808;

	public _18808FoolproofPackaging()
	{
		super(questId);
	}

	@Override
	public void register() 
	{
		qe.registerQuestNpc(830193).addOnQuestStart(questId);
		qe.registerQuestNpc(830193).addOnTalkEvent(questId);
		qe.registerQuestNpc(730534).addOnTalkEvent(questId);
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
			if (targetId == 830193) 
			{
				if (dialog == QuestDialog.START_DIALOG) 
					return sendQuestDialog(env, 1011); 
				if (dialog == QuestDialog.ACCEPT_QUEST_SIMPLE) 
				{
					if (giveQuestItem(env, 188051194, 1))
						return sendQuestStartDialog(env);
					else
						return true;
				}
				else 
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 730534: 
				{
					switch (dialog)
					{
						case USE_OBJECT: 
						{
							if (var == 0) 
								return sendQuestDialog(env, 2375);
						}
						case SELECT_REWARD:
						{
							changeQuestStep(env, 0, 0, true);
							return sendQuestDialog(env, 5);
						}
					}
				}

			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) 
		{
			if (targetId == 730534) 
				return sendQuestEndDialog(env);
		}
		return false;
	}
}
