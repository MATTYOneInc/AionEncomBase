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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ritsu
 * 
 */

public class _2513TheStrangeCottage extends QuestHandler {

	private final static int questId = 2513;
	public _2513TheStrangeCottage() {
		super(questId);
	}
	
    @Override
	public void register() {
		qe.registerQuestNpc(204732).addOnQuestStart(questId); //Gnalin
		qe.registerQuestNpc(204732).addOnTalkEvent(questId);
		qe.registerQuestNpc(204827).addOnTalkEvent(questId); //Hild
		qe.registerQuestNpc(204826).addOnTalkEvent(questId); //Freki
		qe.registerQuestNpc(790022).addOnTalkEvent(questId); //Byggvir
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204732) {
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204826){//Freki
				switch (dialog) {
					case START_DIALOG:
						if (var == 0)
							return sendQuestDialog(env, 1011);
					case STEP_TO_1:
						if (var == 0) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
				}
			}
			if (targetId == 204827) {//Hild
				switch (dialog){
					case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					case STEP_TO_2:
						if (var == 0) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
				}
			}
			if (targetId == 790022) {
				switch (dialog){
					case START_DIALOG:
						if (var == 0) {
							return sendQuestDialog(env, 1693);	
						}
					case STEP_TO_3:
						if (var == 0) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
                            return closeDialogWindow(env);
						}
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204732) {
				switch (dialog){
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_REWARD:
						return sendQuestDialog(env, 5);
					default: return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}