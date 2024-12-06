/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.crafting;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ritsu
 */
public class _29057MasterConstructorsPotential extends QuestHandler {

	private final static int questId = 29057;
	private final static int[]	recipesItemIds = {152208541, 152208542};
	private final static int[]	recipesIds = {155008541, 155008542};

	public _29057MasterConstructorsPotential() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798452).addOnQuestStart(questId);
		qe.registerQuestNpc(798452).addOnTalkEvent(questId);
		qe.registerQuestNpc(798453).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) 
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 798452)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs == null)
			return false;

		if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 798453:
					long kinah = player.getInventory().getKinah();
					switch (dialog)
					{
						case START_DIALOG: 
						{
							switch (var)
							{
								case 0: 
									return sendQuestDialog(env, 1011);
								case 2: 
									return sendQuestDialog(env, 4080);
							}
						}
						case STEP_TO_10:
							if (kinah >= 167500) //Need check how many kinah decrased
							{
								if (!giveQuestItem(env, 152208541, 1))
									return true;
								player.getInventory().decreaseKinah(167500);
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}							
							else
								return sendQuestDialog(env, 4400);		
						case STEP_TO_20:
							if (kinah >= 223000)
							{
								if (!giveQuestItem(env, 152208542, 1))
									return true;
								player.getInventory().decreaseKinah(223000);
								qs.setQuestVarById(0, 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
							else
								return sendQuestDialog(env, 4400);								
					}
				case 798452:
					switch (env.getDialog())
					{
						case START_DIALOG: 
						{
							return sendQuestDialog(env, 1352);
						}
						case CHECK_COLLECTED_ITEMS:
							if(QuestService.collectItemCheck(env, true))
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							else
							{
								int checkFailId = 3398;
								if(player.getRecipeList().isRecipePresent(recipesIds[0]) || player.getRecipeList().isRecipePresent(recipesIds[1]))
									checkFailId = 2716;
								else if(player.getInventory().getItemCountByItemId(recipesItemIds[0]) > 0 || player.getInventory().getItemCountByItemId(recipesItemIds[1]) > 0)
									checkFailId = 3057;

								if(checkFailId == 3398)
								{
									qs.setQuestVar(2);
									updateQuestStatus(env);
								}
								return sendQuestDialog(env, checkFailId);
							}
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) 
		{
			if (targetId == 798452)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}
