/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/
public class _1336Scouting_For_Demokritos extends QuestHandler {

	private final static int questId = 1336;
	public _1336Scouting_For_Demokritos() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(204006).addOnQuestStart(questId); //Demokritos.
		qe.registerQuestNpc(204006).addOnTalkEvent(questId); //Demokritos.
		qe.registerOnEnterZone(ZoneName.get("LF2_SENSORY_AREA_Q1336_1_210020000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF2_SENSORY_AREA_Q1336_2_210020000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF2_SENSORY_AREA_Q1336_3_210020000"), questId);
		
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		if(qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204006){ //Demokritos.
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				} else
					return sendQuestStartDialog(env);
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204006) { //Demokritos.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null)
			return false;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
  		    int var = qs.getQuestVarById(0);
				if (zoneName == ZoneName.get("LF2_SENSORY_AREA_Q1336_1_210020000")) {
					if (var == 0 ) { 
                        playQuestMovie(env, 43);
						changeQuestStep(env, 0, 16, false);
						return true;
					}
				} else if (zoneName == ZoneName.get("LF2_SENSORY_AREA_Q1336_2_210020000")) {
					if (var == 16 ) { 
                        playQuestMovie(env, 44);
						changeQuestStep(env, 16, 48, false);
						return true;
					}
				} else if (zoneName == ZoneName.get("LF2_SENSORY_AREA_Q1336_3_210020000")) {
					if (var == 48 ) { 
                        playQuestMovie(env, 45);
						changeQuestStep(env, 48, 48, true);
						return true;
				}
			}
		}
		return false;
	}
}