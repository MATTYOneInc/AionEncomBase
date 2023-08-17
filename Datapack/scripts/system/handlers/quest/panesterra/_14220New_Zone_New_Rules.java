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
package quest.panesterra;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _14220New_Zone_New_Rules extends QuestHandler
{
    private final static int questId = 14220;
	private static final Set<Integer> GAB1PangaeaScout;
	
    public _14220New_Zone_New_Rules() {
        super(questId);
    }
	
	static {
		GAB1PangaeaScout = new HashSet<Integer>();
		GAB1PangaeaScout.add(802540);
		GAB1PangaeaScout.add(802541);
		GAB1PangaeaScout.add(802544);
		GAB1PangaeaScout.add(802545);
		GAB1PangaeaScout.add(802546);
		GAB1PangaeaScout.add(802547);
		GAB1PangaeaScout.add(804080);
		GAB1PangaeaScout.add(804081);
		GAB1PangaeaScout.add(804082);
		GAB1PangaeaScout.add(804083);
		GAB1PangaeaScout.add(804084);
		GAB1PangaeaScout.add(804085);
		GAB1PangaeaScout.add(804086);
		GAB1PangaeaScout.add(804087);
		GAB1PangaeaScout.add(804088);
		GAB1PangaeaScout.add(804089);
		GAB1PangaeaScout.add(804090);
		GAB1PangaeaScout.add(804091);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
	
    @Override
    public void register() {
		Iterator<Integer> iter = GAB1PangaeaScout.iterator();
		while (iter.hasNext()) {
		    int GAB1Id = iter.next();
		    qe.registerOnLevelUp(questId);
		    qe.registerQuestNpc(GAB1Id).addOnTalkEvent(questId);
		}
	}
	
	@Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (!GAB1PangaeaScout.contains(targetId)) {
			return false;
		} if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 802541) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case SELECT_ACTION_1012: {
						return sendQuestDialog(env, 1012);
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 802544 ||
			      targetId == 804080 ||
				  targetId == 804081 ||
				  targetId == 804082) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						return sendQuestDialog(env, 1353);
					} case STEP_TO_2: {
						playQuestMovie(env, 904);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 802545 ||
			      targetId == 804083 ||
				  targetId == 804084 ||
				  targetId == 804085) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						return sendQuestDialog(env, 1694);
					} case STEP_TO_2: {
						playQuestMovie(env, 905);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 802546 ||
			      targetId == 804086 ||
				  targetId == 804087 ||
				  targetId == 804088) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 2034);
						}
					} case SELECT_ACTION_2035: {
						return sendQuestDialog(env, 2035);
					} case STEP_TO_2: {
						playQuestMovie(env, 906);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 802547 ||
			      targetId == 804089 ||
				  targetId == 804090 ||
				  targetId == 804091) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					} case SELECT_ACTION_2376: {
						return sendQuestDialog(env, 2376);
					} case STEP_TO_2: {
						playQuestMovie(env, 907);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 802540) {
                return sendQuestEndDialog(env);
			}
		}
        return false;
    }
}