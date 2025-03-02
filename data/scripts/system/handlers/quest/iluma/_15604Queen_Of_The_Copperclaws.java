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
package quest.iluma;

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
public class _15604Queen_Of_The_Copperclaws extends QuestHandler {

    public static final int questId = 15604;
    public _15604Queen_Of_The_Copperclaws() {
        super(questId);
    }
	
    @Override
    public void register() {
        qe.registerQuestNpc(806161).addOnQuestStart(questId); //Cyclon.
		qe.registerQuestNpc(806161).addOnTalkEvent(questId); //Cyclon.
        qe.registerQuestNpc(241161).addOnKillEvent(questId); //Copperclaw Arachna.
		qe.registerQuestNpc(241160).addOnKillEvent(questId); //Olthrax.
		qe.registerOnEnterZone(ZoneName.get("ERASMID_HOLLOW_210100000"), questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 806161) { //Cyclon.
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
					} case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST_SIMPLE: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.START) {
           	int var = qs.getQuestVarById(0);
			if (targetId == 806161) { //Cyclon.
				switch (env.getDialog()) {
                    case START_DIALOG: {
                        if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        }
					} case SELECT_ACTION_1694: {
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					} case STEP_TO_3: {
						playQuestMovie(env, 1000);
                        changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
		} else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806161) { //Cyclon.
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
        int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 1) {
                int var1 = qs.getQuestVarById(1);
                if (var1 >= 0 && var1 < 4) {
                    return defaultOnKillEvent(env, 241161, var1, var1 + 1, 1);
                } else if (var1 == 4) {
					qs.setQuestVar(2);
					updateQuestStatus(env);
                    return true;
                }
            } else if (var == 4) {
				switch (targetId) {
                    case 241160: { //Olthrax.
						qs.setStatus(QuestStatus.REWARD);
					    updateQuestStatus(env);
						return true;
					}
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
			if (zoneName == ZoneName.get("ERASMID_HOLLOW_210100000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				} else if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			}
		}
		return false;
	}
}