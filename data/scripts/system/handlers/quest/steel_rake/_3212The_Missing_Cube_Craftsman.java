/*

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

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.questEngine.task.QuestTasks;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _3212The_Missing_Cube_Craftsman extends QuestHandler {

	private final static int questId = 3212;
	public _3212The_Missing_Cube_Craftsman() {
		super(questId);
	}
	
	public void register() {
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerAddOnLostTargetEvent(questId);
		qe.registerAddOnReachTargetEvent(questId);
		qe.registerQuestNpc(798321).addOnQuestStart(questId);
		qe.registerQuestNpc(203838).addOnTalkEvent(questId);
		qe.registerQuestNpc(798011).addOnTalkEvent(questId);
		qe.registerQuestNpc(798337).addOnTalkEvent(questId);
		qe.registerQuestNpc(730208).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798321) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203838) {
				if (dialog == QuestDialog.START_DIALOG) {
					if (qs.getQuestVarById(0) == 0) {
						return sendQuestDialog(env, 1011);
					}
				} else if (dialog == QuestDialog.STEP_TO_1) {
					return defaultCloseDialog(env, 0, 1);
				}
			} else if (targetId == 798011) {
				if (dialog == QuestDialog.START_DIALOG) {
					if (qs.getQuestVarById(0) == 1) {
						return sendQuestDialog(env, 1352);
					}
				} else if (dialog == QuestDialog.STEP_TO_2) {
					return defaultCloseDialog(env, 1, 2);
				}
			} else if (targetId == 798337) {
				if (dialog == QuestDialog.START_DIALOG) {
					if (qs.getQuestVarById(0) == 2) {
						return sendQuestDialog(env, 1693);
					}
				} else if (dialog == QuestDialog.STEP_TO_3) {
					Npc npc = (Npc) env.getVisibleObject();
					npc.getSpawn().setWalkerId("4212");
					WalkManager.startWalking((NpcAI2) npc.getAi2());
					PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
					player.getController().addTask(TaskId.QUEST_FOLLOW, QuestTasks.newFollowingToTargetCheckTask(env, npc, 505.69427f, 437.69382f, 885.1844f));
					return defaultCloseDialog(env, 2, 3);
				}
			} else if (targetId == 730208) {
				Npc npc = (Npc) env.getVisibleObject();
				npc.getController().delete();
				return true;
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798011) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				qs.setQuestVar(2);
				updateQuestStatus(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onNpcReachTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 3, 4, true);
	}
	
	@Override
	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 3, 2, false); 
	}
}