/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modif (y
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.steel_rake;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _3210Rescue_Haorunerk extends QuestHandler
{
	private final static int questId = 3210;
	
	public _3210Rescue_Haorunerk() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(798318).addOnQuestStart(questId);
		qe.registerQuestNpc(798318).addOnTalkEvent(questId);
		qe.registerQuestNpc(798331).addOnTalkEvent(questId);
		qe.registerQuestNpc(798332).addOnTalkEvent(questId);
		qe.registerQuestNpc(215056).addOnKillEvent(questId);
		qe.registerQuestNpc(215080).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798318) {
				switch (dialog) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case REFUSE_QUEST: {
						return sendQuestDialog(env, 1004);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					}
				}
			}
		} if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 798332 && qs.getQuestVarById(0) == 0) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 1011);
					} case SELECT_ACTION_1012: {
						return sendQuestDialog(env, 1012);
					} case STEP_TO_1: {
						qs.setQuestVarById(0, 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				}
			}
		} if (targetId == 798331) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 10002);
				} if (env.getDialog() == QuestDialog.SELECT_REWARD && qs.getQuestVarById(1) == 1 && qs.getQuestVarById(2) == 1) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			}
			return sendQuestEndDialog(env);
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		if (defaultOnKillEvent(env, 215056, 0, 1, 1) ||
		    defaultOnKillEvent(env, 215080, 0, 1, 2)) {
			return true;
		}
		return false;
	}
}