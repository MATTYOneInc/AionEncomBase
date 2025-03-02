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
package quest.high_daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _25322Through_The_Rift extends QuestHandler {

    public static final int questId = 25322;
	private final static int[] LF5_B = {235801, 235802, 235803, 235804, 235805, 235806, 235807, 235808, 235809, 235810, 235811, 235812};
	private final static int[] LF5_D = {235852, 235853, 235854, 235855, 235856, 235857, 235858, 235859, 235860, 235861};
	private final static int[] LF5_F = {235876, 235877, 235878, 235879, 235882, 235883, 235884, 235885, 235886};
	private final static int[] LF5_H = {235888, 235889, 235890, 235891, 235892, 235893, 235894, 235895, 235896};
    public _25322Through_The_Rift() {
        super(questId);
    }
	
    @Override
    public void register() {
		qe.registerQuestNpc(805342).addOnQuestStart(questId); //Hikait.
        qe.registerQuestNpc(805342).addOnTalkEvent(questId); //Hikait.
		for (int mob: LF5_B) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
        } for (int mob2: LF5_D) {
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
        } for (int mob3: LF5_F) {
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
        } for (int mob4: LF5_H) {
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
        }
		qe.registerQuestNpc(805342).addOnAtDistanceEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("CRIMSON_HILLS_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("TWILIGHT_TEMPLE_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("PERENNIAL_MOSSWOOD_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DRAGON_LORDS_CENTERPIECE_210070000"), questId);
    }
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			QuestService.startQuest(env);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 805342) { //Hikait.
                if (env.getDialog() == QuestDialog.START_DIALOG) {
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
    public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }  
        int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
        if (var == 1 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, LF5_B, var1, var1 + 1, 1);
		} else if (var == 1 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		} if (var == 3 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, LF5_D, var1, var1 + 1, 1);
		} else if (var == 3 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		} if (var == 5 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, LF5_F, var1, var1 + 1, 1);
		} else if (var == 5 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		} if (var == 7 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, LF5_H, var1, var1 + 1, 1);
		} else if (var == 7 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(10);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
	
    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("CRIMSON_HILLS_210070000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
            } else if (zoneName == ZoneName.get("DRAGON_LORDS_CENTERPIECE_210070000")) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("TWILIGHT_TEMPLE_210070000")) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("PERENNIAL_MOSSWOOD_210070000")) {
				if (var == 6) {
					changeQuestStep(env, 6, 7, false);
					return true;
				}
			}
		}
		return false;
    }
}