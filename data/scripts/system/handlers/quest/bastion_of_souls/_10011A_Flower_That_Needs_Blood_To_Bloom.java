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
package quest.bastion_of_souls;

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
public class _10011A_Flower_That_Needs_Blood_To_Bloom extends QuestHandler {

	public static final int questId = 10011;
	private final static int[] npcs = {806075, 806585, 806589, 731785, 731786, 731787};
	private final static int[] bossWitchAh = {246506, 246507, 246508, 246509};
	public _10011A_Flower_That_Needs_Blood_To_Bloom() {
		super(questId);
	}
	
    @Override
    public void register() {
        for (int npc: npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        } 
		qe.registerQuestNpc(246810).addOnKillEvent(questId);
		for (int mob: bossWitchAh) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q10011_A_302340000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q10011_B_302340000"), questId);
    }
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10010, true);
    }
	
    @Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806585) { //IDAb1_Ere_Manager_Room_Bastiel_E.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					} case SELECT_ACTION_1012: {
						if (var == 0) {
							return sendQuestDialog(env, 1012);
						}
					} case SELECT_ACTION_1013: {
						if (var == 0) {
							return sendQuestDialog(env, 1013);
						}
					} case SELECT_ACTION_1014: {
						if (var == 0) {
							return sendQuestDialog(env, 1014);
						}
					} case STEP_TO_1: {
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 731785) { //IDAb1_Ere_Dead_Deva_L_E.
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					} case SELECT_ACTION_1353: {
						if (var == 1) {
							return sendQuestDialog(env, 1353);
						}
					} case STEP_TO_2: {
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 731786) { //IDAb1_Ere_Dying_Deva_L_E.
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_3: {
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 731787) { //IDAb1_Ere_Pain_Deva_L_E.
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					} case SELECT_ACTION_2035: {
						if (var == 3) {
							return sendQuestDialog(env, 2035);
						}
					} case STEP_TO_4: {
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			} if (targetId == 806589) { //IDAb1_Ere_Terrarium_End_Bastiel_E.
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 8) {
							return sendQuestDialog(env, 3739);
						}
					} case SELECT_ACTION_3740: {
						if (var == 8) {
							return sendQuestDialog(env, 3740);
						}
					} case SELECT_ACTION_3741: {
						if (var == 8) {
							return sendQuestDialog(env, 3741);
						}
					} case SET_REWARD: {
						qs.setQuestVar(9);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806075) { //Weatha.
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
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDAB1_ERE_Q10011_A_302340000")) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDAB1_ERE_Q10011_B_302340000")) {
				if (var == 7) {
					qs.setQuestVar(8);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onKillEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 5) {
				int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, 246810, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(6);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 6) {
				switch (targetId) {
					case 246506: { //IDAb1_Ere_Boss_Witch_01_75_Ah.
						changeQuestStep(env, 0, 1, false, 1);
						return true;
					} case 246507: { //IDAb1_Ere_Boss_Witch_02_75_Ah.
						changeQuestStep(env, 0, 1, false, 2);
						return true;
					} case 246508: { //IDAb1_Ere_Boss_Witch_03_75_Ah.
						changeQuestStep(env, 0, 1, false, 3);
						return true;
					} case 246509: { //IDAb1_Ere_Boss_Witch_04_75_Ah.
						return defaultOnKillEvent(env, 246509, 6, 7);
					}
				}
			}
        }
        return false;
    }
}