/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.verteron;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */
public class _1183SpiritOfNature extends QuestHandler {

	private final static int questId = 1183;

	public _1183SpiritOfNature() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730012).addOnQuestStart(questId);
		qe.registerQuestNpc(730012).addOnTalkEvent(questId);
		qe.registerQuestNpc(730013).addOnTalkEvent(questId);
		qe.registerQuestNpc(730014).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730012) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730013: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							return sendQuestDialog(env, 1352);
						}
						case STEP_TO_1: {
							if (player.getInventory().getItemCountByItemId(182200550) == 0)
								if (!giveQuestItem(env, 182200550, 1))
									return true;
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 730014: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_2: {
							if (player.getInventory().getItemCountByItemId(182200565) == 0)
								if (!giveQuestItem(env, 182200565, 1))
									return true;
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
						default:
							return sendQuestEndDialog(env);
					}
				}
				case 730012: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_REWARD: {
							qs.setQuestVar(3);
							removeQuestItem(env, 182200550, 1);
							removeQuestItem(env, 182200565, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730012) {
				if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
