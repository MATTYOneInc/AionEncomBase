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
package quest.haramel;

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

public class _28500The_Secret_Of_The_Odella extends QuestHandler {

	private final static int questId = 28500;
	public _28500The_Secret_Of_The_Odella() {
		super(questId);
	}
	
	@Override
	public void register() {
		int[] npcs = {203560, 203649, 730306, 730307, 799522};
		qe.registerQuestNpc(203560).addOnQuestStart(questId);
		for (int npc: npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("HARAMEL_300200000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF1A_SENSORY_AREA_Q28500_220030000"), questId);
		qe.registerOnMovieEndQuest(217, questId);
		qe.registerOnMovieEndQuest(456, questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
	final Player player = env.getPlayer();
	final QuestState qs = player.getQuestStateList().getQuestState(questId);
	if (zoneName == ZoneName.get("DF1A_SENSORY_AREA_Q28500_220030000")) {
		if (qs.getQuestVars().getQuestVars() == 3) {
			playQuestMovie(env, 217);
			return true;
			}
		}
		if (zoneName == ZoneName.get("HARAMEL_300200000")) {
			if (qs.getQuestVars().getQuestVars() == 4) {
				playQuestMovie(env, 456);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
	    if (qs == null)
		return false;
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203560) {
				if (env.getDialog() == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 203649: {
					switch (env.getDialog()) {
						case START_DIALOG:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
                        case SELECT_ACTION_1012: {
							if (var == 0) {
							return sendQuestDialog(env, 1012);
							}
						}
						case STEP_TO_1: {
							if (var == 0) {
				            changeQuestStep(env, 0, 1, false);
                            return closeDialogWindow(env);
							}
						}
					}
					break;
				} case 730306: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						} case STEP_TO_2: {
				            changeQuestStep(env, 1, 2, false);
                            return closeDialogWindow(env);
						}
					}
					break;
				} case 730307: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						} case STEP_TO_3: {
				            changeQuestStep(env, 2, 3, false);
                            return closeDialogWindow(env);
						}
					}
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799522) {
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

    @Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 217) {
		    changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		}
		if (movieId == 456) {
		    changeQuestStep(env, 4, 4, true);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}