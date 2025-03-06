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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

public class _29000ExpertEssencetappersTest extends QuestHandler {

	private final static int questId = 29000;
	public _29000ExpertEssencetappersTest() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(204096).addOnQuestStart(questId);
		qe.registerQuestNpc(204096).addOnTalkEvent(questId);
		qe.registerQuestNpc(204097).addOnTalkEvent(questId);
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        if (player.getWorldId() == 120010000) { //Pandaemonium.
            QuestState qs = player.getQuestStateList().getQuestState(questId);
            if (qs == null) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
                    return true;
                }
            }
        }
        return false;
    }
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204096) { //Latatusk.
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204097: { //Relir.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						} case STEP_TO_1: {
							if (!player.getInventory().isFullSpecialCube()) {
								return defaultCloseDialog(env, 0, 1, 122001250, 1, 0, 0);
							}
						}
					}
					break;
				} case 204096: { //Latatusk.
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						} case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 1, true, 5, 10001);
						}
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204096) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}