/*

 *
 *  Encom is free software: you can redistribute it and/or modify
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
package quest.fatebound_abbey;

import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.questEngine.handlers.*;
import com.aionemu.gameserver.questEngine.model.*;
import com.aionemu.gameserver.utils.*;

/****/
/** Author Rinzler (Encom)
/****/

public class _29618Recovering_From_Betrayal extends QuestHandler
{
	private final static int questId = 29618;
	
	public _29618Recovering_From_Betrayal() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(804665).addOnQuestStart(questId);
		qe.registerQuestNpc(804665).addOnTalkEvent(questId);
		qe.registerQuestNpc(805156).addOnTalkEvent(questId); //DF5 Ekios.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		} if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 804665) {
				switch (dialog) {
					case START_DIALOG: {
						if (player.getInventory().getItemCountByItemId(164000336) >= 1) { //Abbey Return Stone.
						    return sendQuestDialog(env, 4762);
						} else {
							PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
							"You must have <Abbey Return Stone>", ChatType.BRIGHT_YELLOW_CENTER), true);
							return true;
						}
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
						return sendQuestStartDialog(env);
					case REFUSE_QUEST_SIMPLE:
				        return closeDialogWindow(env);
				}
			}
		} if (qs == null) {
			return false;
		} if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 805156: { //DF5 Ekios.
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 10002);
						} case SELECT_REWARD: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
		    if (targetId == 805156) { //DF5 Ekios.
			    switch (dialog) {
					case SELECT_REWARD: {
						return sendQuestDialog(env, 5);
					} default:
						return sendQuestEndDialog(env);
				}
		    }
		}
		return false;
	}
}