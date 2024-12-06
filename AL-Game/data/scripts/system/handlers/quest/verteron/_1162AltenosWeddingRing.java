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

import com.aionemu.gameserver.model.gameobjects.Creature;
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
public class _1162AltenosWeddingRing extends QuestHandler {

	private final static int questId = 1162;
	public _1162AltenosWeddingRing() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203095).addOnQuestStart(questId);
		qe.registerQuestNpc(203095).addOnTalkEvent(questId);
		qe.registerQuestNpc(203093).addOnTalkEvent(questId);
		qe.registerQuestNpc(700005).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203095) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
			return false;
		if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0); 
			switch (targetId) {
				case 700005: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (var == 0) {
							giveQuestItem(env, 182200563, 1); 
							qs = player.getQuestStateList().getQuestState(questId);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							return true;
                            }
						}
					}
				}
				case 203093:
			    case 203095: {
                switch (env.getDialog()) {
				    case START_DIALOG: {
                        return sendQuestDialog(env, 1352);
                    }
                    case CHECK_COLLECTED_ITEMS: {
                        return checkQuestItems(env, 1, 2, true, 0, 0);
					}
				}
				return false;
			}
	    }
    } 
    else if (qs.getStatus() == QuestStatus.REWARD) {
		if (targetId == 203095) {
            if (env.getDialog() == QuestDialog.START_DIALOG) {   
                    return sendQuestDialog(env, 2375); 
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
                }
			}
		}
		return false;
	}
}