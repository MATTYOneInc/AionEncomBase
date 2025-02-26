/*
 * This file is part of Encom.
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
package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Ritsu
 * 
 */
public class _2252ChasingtheLegend extends QuestHandler {

	private final static int questId = 2252;
	private final static int[] mob_ids = { 210634 }; //Minusha's Spirit
	public _2252ChasingtheLegend() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203646).addOnQuestStart(questId); //Sinood
		qe.registerQuestNpc(203646).addOnTalkEvent(questId);
		qe.registerQuestNpc(700060).addOnTalkEvent(questId); //Bone of Minusha
		for(int mob_id : mob_ids)
        qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch(targetId) {
		case 210634: //Minusha's Spirit
			if(var == 0) {
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}	

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		@SuppressWarnings("unused")
		Npc npc = null;
		if (env.getVisibleObject() instanceof Npc)
		npc = (Npc) env.getVisibleObject();
		targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE){
			if (targetId == 203646){//Sinood
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 203646) {
				switch (env.getDialog()) {
				case START_DIALOG:
					if (var == 1) {
						qs.setQuestVar(2);
						return sendQuestDialog(env, 1352);
					}
				case SELECT_REWARD:
					if (var == 2) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestEndDialog(env);
					}
				}
			}
			if (targetId == 700060) {
				switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 0) {
						final int targetObjectId = env.getVisibleObject().getObjectId();	
							if (npc == null || npc.getObjectId() != targetObjectId) {
		                        return false;
							}
							QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 210634, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Minusha's Spirit
							if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId) {
		                        return false;
						}
					}
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203646)
		    return sendQuestEndDialog(env);
		}
		return false;
	}
}